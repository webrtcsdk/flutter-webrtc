package com.cloudwebrtc.webrtc

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import android.opengl.GLES20
import android.opengl.GLUtils
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import com.cloudwebrtc.webrtc.utils.ImageSegmenterHelper
import com.google.android.gms.tflite.client.TfLiteInitializationOptions
import com.google.android.gms.tflite.gpu.support.TfLiteGpu
import com.google.android.gms.tflite.java.TfLite
import com.google.mediapipe.tasks.vision.core.RunningMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.webrtc.EglBase
import org.webrtc.SurfaceTextureHelper
import org.webrtc.TextureBufferImpl
import org.webrtc.VideoFrame
import org.webrtc.VideoProcessor
import org.webrtc.VideoSink
import org.webrtc.VideoSource
import org.webrtc.YuvConverter
import org.webrtc.YuvHelper
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.util.Arrays
import kotlin.math.max

class FlutterRTCVirtualBackground {
    val TAG = FlutterWebRTCPlugin.TAG

    private val scale = 0.25f
    private val frameSizeProcessing = 225
    private var videoSource: VideoSource? = null
    private var textureHelper: SurfaceTextureHelper? = null
    private var backgroundBitmap: Bitmap? = null
    private var expectConfidence = 0.7
    private var imageSegmentationHelper: ImageSegmenterHelper? = null
    private var sink: VideoSink? = null
    private var bitmapMap = HashMap<Long, CacheFrame>()

    // MARK: Public functions

    /**
     * Initialize the VirtualBackgroundManager with the given VideoSource.
     *
     * @param videoSource The VideoSource to be used for video capturing.
     */
    fun initialize(context: Context, videoSource: VideoSource) {
        // Enable GPU
        val useGpuTask = TfLiteGpu.isGpuDelegateAvailable(context)

        val interpreterTask = useGpuTask.continueWith { useGpuTask ->
            TfLite.initialize(context,
                TfLiteInitializationOptions.builder()
                    .setEnableGpuDelegateSupport(useGpuTask.result)
                    .build())
        }

        this.videoSource = videoSource
        this.imageSegmentationHelper = ImageSegmenterHelper(
            context = context,
            runningMode = RunningMode.LIVE_STREAM,
            imageSegmenterListener = object :
                ImageSegmenterHelper.SegmenterListener {
                override fun onError(error: String, errorCode: Int) {
                    // no-op
                }

                override fun onResults(resultBundle: ImageSegmenterHelper.ResultBundle) {
                    val timestampNS = resultBundle.frameTime
                    val cacheFrame: CacheFrame = bitmapMap[timestampNS] ?: return
                    bitmapMap.remove(timestampNS)

                    val maskFloat = resultBundle.results
                    val maskWidth = resultBundle.width
                    val maskHeight = resultBundle.height

                    val bitmap = cacheFrame.originalBitmap
                    val mask = convertFloatBufferToByteBuffer(maskFloat)

                    // Convert the buffer to an array of colors
                    val colors = maskColorsFromByteBuffer(
                        mask,
                        maskWidth,
                        maskHeight,
                        bitmap,
                        bitmap.width,
                        bitmap.height
                    )

                    // Create the segmented bitmap from the color array
                    val segmentedBitmap = createBitmapFromColors(colors, bitmap.width, bitmap.height)

                    if (backgroundBitmap == null) {
                        // If the background bitmap is null, return without further processing
                        return
                    }

                    // Draw the segmented bitmap on top of the background for human segments
                    val outputBitmap = drawSegmentedBackground(segmentedBitmap, backgroundBitmap)

                    // Apply a filter to reduce noise (if applicable)
                    if (outputBitmap != null) {
                        // Create a new VideoFrame from the processed bitmap
                        val yuvConverter = YuvConverter()
                        if (textureHelper != null && textureHelper!!.handler != null) {
                            textureHelper!!.handler.post {
                                val textures = IntArray(1)
                                GLES20.glGenTextures(1, textures, 0)
                                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
                                GLES20.glTexParameteri(
                                    GLES20.GL_TEXTURE_2D,
                                    GLES20.GL_TEXTURE_MIN_FILTER,
                                    GLES20.GL_NEAREST
                                )
                                GLES20.glTexParameteri(
                                    GLES20.GL_TEXTURE_2D,
                                    GLES20.GL_TEXTURE_MAG_FILTER,
                                    GLES20.GL_NEAREST
                                )
                                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, outputBitmap, 0)
                                val buffer = TextureBufferImpl(
                                    outputBitmap.width,
                                    outputBitmap.height,
                                    VideoFrame.TextureBuffer.Type.RGB,
                                    textures[0],
                                    Matrix(),
                                    textureHelper!!.handler,
                                    yuvConverter,
                                    null
                                )
                                val i420Buf = yuvConverter.convert(buffer)
                                if (i420Buf != null) {
                                    // Create the output VideoFrame and send it to the sink
                                    val outputVideoFrame =
                                        VideoFrame(i420Buf, cacheFrame.originalFrame.rotation, cacheFrame.originalFrame.timestampNs)
                                    sink?.onFrame(outputVideoFrame)
                                } else {
                                    // If the conversion fails, send the original frame to the sink
                                    sink?.onFrame(cacheFrame.originalFrame)
                                }
                            }
                        }
                    }
                }
            })
        setVirtualBackground()
    }

    /**
     * Dispose of the VirtualBackgroundManager, clearing its references and configurations.
     */
    fun dispose() {
        this.videoSource = null
        this.expectConfidence = 0.7
        setBackgroundIsNull()
    }

    fun setBackgroundIsNull() {
        this.backgroundBitmap = null
    }

    /**
     * Configure the virtual background by setting the background bitmap and the desired confidence level.
     *
     * @param bgBitmap The background bitmap to be used for virtual background replacement.
     * @param confidence The confidence level (0 to 1) for selecting the foreground in the segmentation mask.
     */
    fun configurationVirtualBackground(bgBitmap: Bitmap, confidence: Double) {
        backgroundBitmap = bgBitmap
        expectConfidence = confidence
    }

    /**
     * Set up the virtual background processing by attaching a VideoProcessor to the VideoSource.
     * The VideoProcessor will handle capturing video frames, performing segmentation, and replacing the background.
     */
    private fun setVirtualBackground() {
        // Create an instance of EglBase
        val eglBase = EglBase.create()
        textureHelper = SurfaceTextureHelper.create("SurfaceTextureThread", eglBase.eglBaseContext)

        // Attach a VideoProcessor to the VideoSource to process captured video frames
        videoSource!!.setVideoProcessor(object : VideoProcessor {
            override fun onCapturerStarted(success: Boolean) {
                // Handle video capture start event
            }

            override fun onCapturerStopped() {
                // Handle video capture stop event
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onFrameCaptured(frame: VideoFrame) {
                if (sink != null) {
                    if (backgroundBitmap == null) {
                        // If no background is set, pass the original frame to the sink
                        sink!!.onFrame(frame)
                    } else {
                        // Otherwise, perform segmentation on the captured frame and replace the background
                        val inputFrameBitmap: Bitmap? = videoFrameToBitmap(frame)
                        if (inputFrameBitmap != null) {
                            runSegmentationInBackground(inputFrameBitmap, frame, sink!!)
                        } else {
                            Log.d(TAG, "Convert video frame to bitmap failure")
                        }
                    }
                }
            }

            override fun setSink(videoSink: VideoSink?) {
                // Store the VideoSink to send the processed frame back to WebRTC
                // The sink will be used after segmentation processing
                sink = videoSink
            }
        })
    }

    /**
     * Perform segmentation on the input bitmap in the background thread.
     * After segmentation, the background is replaced with the configured virtual background.
     *
     * @param inputFrameBitmap The input frame bitmap to be segmented.
     * @param frame The original VideoFrame metadata for the input bitmap.
     * @param sink The VideoSink to send the processed frame back to WebRTC.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun runSegmentationInBackground(
        inputFrameBitmap: Bitmap,
        frame: VideoFrame,
        sink: VideoSink
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            processSegmentation(inputFrameBitmap, frame, sink)
        }
    }

    /**
     * Convert a VideoFrame to a Bitmap for further processing.
     *
     * @param videoFrame The input VideoFrame to be converted.
     * @return The corresponding Bitmap representation of the VideoFrame.
     */
    private fun videoFrameToBitmap(videoFrame: VideoFrame): Bitmap? {
        // Retain the VideoFrame to prevent it from being garbage collected
        videoFrame.retain()

        // Convert the VideoFrame to I420 format
        val buffer = videoFrame.buffer
        val i420Buffer = buffer.toI420()
        val y = i420Buffer!!.dataY
        val u = i420Buffer.dataU
        val v = i420Buffer.dataV
        val width = i420Buffer.width
        val height = i420Buffer.height
        val strides = intArrayOf(
            i420Buffer.strideY,
            i420Buffer.strideU,
            i420Buffer.strideV
        )
        // Convert I420 format to NV12 format as required by YuvImage
        val chromaWidth = (width + 1) / 2
        val chromaHeight = (height + 1) / 2
        val minSize = width * height + chromaWidth * chromaHeight * 2
        val yuvBuffer = ByteBuffer.allocateDirect(minSize)
        YuvHelper.I420ToNV12(
            y,
            strides[0],
            v,
            strides[2],
            u,
            strides[1],
            yuvBuffer,
            width,
            height
        )
        // Remove leading 0 from the ByteBuffer
        val cleanedArray =
            Arrays.copyOfRange(yuvBuffer.array(), yuvBuffer.arrayOffset(), minSize)
        val yuvImage = YuvImage(
            cleanedArray,
            ImageFormat.NV21,
            width,
            height,
            null
        )
        i420Buffer.release()
        videoFrame.release()

        // Convert YuvImage to byte array
        val outputStream = ByteArrayOutputStream()
        yuvImage.compressToJpeg(
            Rect(0, 0, yuvImage.width, yuvImage.height),
            90,
            outputStream
        )
        val jpegData = outputStream.toByteArray()

        // Convert byte array to Bitmap
        return BitmapFactory.decodeByteArray(jpegData, 0, jpegData.size)
    }

    /**
     * Process the segmentation of the input bitmap using the AI segmenter.
     * The resulting segmented bitmap is then combined with the provided background bitmap,
     * and the final output frame is sent to the video sink.
     *
     * @param bitmap The input bitmap to be segmented.
     * @param original The original video frame for metadata reference (rotation, timestamp, etc.).
     * @param sink The VideoSink to receive the processed video frame.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun processSegmentation(bitmap: Bitmap, original: VideoFrame, sink: VideoSink) {
        val resizeBitmap = resizeBitmapKeepAspectRatio(bitmap, frameSizeProcessing)

        // Segment the input bitmap using the ImageSegmentationHelper
        val frameTime: Long = SystemClock.uptimeMillis()
        bitmapMap[frameTime] = CacheFrame(originalBitmap = resizeBitmap, originalFrame = original)

        imageSegmentationHelper?.segmentLiveStreamFrame(resizeBitmap, frameTime)
    }

    private fun resizeBitmap(bitmap: Bitmap, scaleFactory: Float): Bitmap {
        val newWidth = (bitmap.width * scaleFactory).toInt()
        val newHeight = (bitmap.height * scaleFactory).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)
    }

    /**
     * Resize the given bitmap while maintaining its original aspect ratio.
     *
     * @param bitmap The bitmap to be resized.
     * @param maxSize The maximum size (width or height) of the resized bitmap.
     * @return The resized bitmap while keeping its original aspect ratio.
     */
    private fun resizeBitmapKeepAspectRatio(bitmap: Bitmap, maxSize: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height

        // Check the current size of the image and return if it doesn't exceed the maxSize
        if (originalWidth <= maxSize && originalHeight <= maxSize) {
            return bitmap
        }

        // Determine whether to maintain width or height to keep the original aspect ratio
        val scaleFactor: Float = if (originalWidth >= originalHeight) {
            maxSize.toFloat() / originalWidth
        } else {
            maxSize.toFloat() / originalHeight
        }

        // Calculate the new size of the image while maintaining the original aspect ratio
        val newWidth = (originalWidth * scaleFactor).toInt()
        val newHeight = (originalHeight * scaleFactor).toInt()

        // Create a new bitmap with the scaled size while preserving the aspect ratio
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun convertFloatBufferToByteBuffer(floatBuffer: FloatBuffer): ByteBuffer {
        // Calculate the number of bytes needed for the ByteBuffer
        val bufferSize = floatBuffer.remaining() * 4 // 4 bytes per float

        // Create a new ByteBuffer with the calculated size
        val byteBuffer = ByteBuffer.allocateDirect(bufferSize)

        // Transfer the data from the FloatBuffer to the ByteBuffer
        byteBuffer.asFloatBuffer().put(floatBuffer)

        // Reset the position of the ByteBuffer to 0
        byteBuffer.position(0)

        return byteBuffer
    }

    /**
     * Convert the mask buffer to an array of colors representing the segmented regions.
     *
     * @param mask The mask buffer obtained from the AI segmenter.
     * @param maskWidth The width of the mask.
     * @param maskHeight The height of the mask.
     * @param originalBitmap The original input bitmap used for color extraction.
     * @param scaledWidth The width of the scaled bitmap.
     * @param scaledHeight The height of the scaled bitmap.
     * @return An array of colors representing the segmented regions.
     */
    private fun maskColorsFromByteBuffer(
        mask: ByteBuffer,
        maskWidth: Int,
        maskHeight: Int,
        originalBitmap: Bitmap,
        scaledWidth: Int,
        scaledHeight: Int
    ): IntArray {
        val colors = IntArray(scaledWidth * scaledHeight)
        var count = 0
        val scaleX = scaledWidth.toFloat() / maskWidth
        val scaleY = scaledHeight.toFloat() / maskHeight
        for (y in 0 until scaledHeight) {
            for (x in 0 until scaledWidth) {
                val maskX: Int = (x / scaleX).toInt()
                val maskY: Int = (y / scaleY).toInt()
                if (maskX in 0 until maskWidth && maskY >= 0 && maskY < maskHeight) {
                    val position = (maskY * maskWidth + maskX) * 4
                    mask.position(position)

                    // Get the confidence of the (x,y) pixel in the mask being in the foreground.
                    val foregroundConfidence = mask.float
                    val pixelColor = originalBitmap.getPixel(x, y)

                    // Extract the color channels from the original pixel
                    val alpha = Color.alpha(pixelColor)
                    val red = Color.red(pixelColor)
                    val green = Color.green(pixelColor)
                    val blue = Color.blue(pixelColor)

                    // Calculate the new alpha and color for the foreground and background
                    var newAlpha: Int
                    var newRed: Int
                    var newGreen: Int
                    var newBlue: Int
                    if (foregroundConfidence >= expectConfidence) {
                        // Foreground uses color from the original bitmap
                        newAlpha = alpha
                        newRed = red
                        newGreen = green
                        newBlue = blue
                    } else {
                        // Background is black with alpha 0
                        newAlpha = 0
                        newRed = 0
                        newGreen = 0
                        newBlue = 0
                    }

                    // Create a new color with the adjusted alpha and RGB channels
                    val newColor = Color.argb(newAlpha, newRed, newGreen, newBlue)
                    colors[count] = newColor
                } else {
                    // Pixels outside the original mask size are considered background (black with alpha 0)
                    colors[count] = Color.argb(0, 0, 0, 0)
                }
                count++
            }
        }
        return colors
    }

    /**
     * Draws the segmentedBitmap on top of the backgroundBitmap with the background resized and centered
     * to fit the dimensions of the segmentedBitmap. The output is a new bitmap containing the combined
     * result.
     *
     * @param segmentedBitmap The bitmap representing the segmented foreground with transparency.
     * @param backgroundBitmap The bitmap representing the background image to be used as the base.
     * @return The resulting bitmap with the segmented foreground overlaid on the background.
     *         Returns null if either of the input bitmaps is null.
     */
    private fun drawSegmentedBackground(
        segmentedBitmap: Bitmap?,
        backgroundBitmap: Bitmap?
    ): Bitmap? {
        if (segmentedBitmap == null || backgroundBitmap == null) {
            // Handle invalid bitmaps
            return null
        }

        val segmentedWidth = segmentedBitmap.width
        val segmentedHeight = segmentedBitmap.height

        // Create a new bitmap with dimensions matching the segmentedBitmap
        val outputBitmap =
            Bitmap.createBitmap(segmentedWidth, segmentedHeight, Bitmap.Config.ARGB_8888)

        // Create a canvas to draw on the outputBitmap
        val canvas = Canvas(outputBitmap)

        // Calculate the scale factor for the backgroundBitmap to be larger or equal to the segmentedBitmap
        val scaleX = segmentedWidth.toFloat() / backgroundBitmap.width
        val scaleY = segmentedHeight.toFloat() / backgroundBitmap.height
        val scale = max(scaleX, scaleY)

        // Calculate the new dimensions of the backgroundBitmap after scaling
        val newBackgroundWidth = (backgroundBitmap.width * scale).toInt()
        val newBackgroundHeight = (backgroundBitmap.height * scale).toInt()

        // Calculate the offset to center the backgroundBitmap in the outputBitmap
        val offsetX = (segmentedWidth - newBackgroundWidth) / 2
        val offsetY = (segmentedHeight - newBackgroundHeight) / 2

        // Create a transformation matrix to scale and center the backgroundBitmap
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        matrix.postTranslate(offsetX.toFloat(), offsetY.toFloat())

        // Draw the backgroundBitmap on the canvas with the specified scale and centering
        canvas.drawBitmap(backgroundBitmap, matrix, null)

        // Draw the segmentedBitmap on the canvas
        canvas.drawBitmap(segmentedBitmap, 0f, 0f, null)

        return outputBitmap
    }

    /**
     * Apply Mean Filter to the given bitmap with the specified kernel size.
     * The Mean Filter is a simple image processing filter that smoothens the image by
     * replacing each pixel value with the average value of its neighboring pixels.
     *
     * @param bitmap The bitmap to be processed.
     * @param kernelSize The size of the square kernel (odd number) for the filter. For example, 3, 5, 7, etc.
     * @return The bitmap after applying the Mean Filter.
     */
    fun meanFilter(bitmap: Bitmap, kernelSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        // Create a new output bitmap with the same dimensions as the input bitmap
        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Loop through each pixel in the bitmap and apply the Mean Filter
        for (x in 0 until width) {
            for (y in 0 until height) {
                // Collect pixel values within the kernel
                val kernelPixels = mutableListOf<Int>()
                for (i in -kernelSize / 2..kernelSize / 2) {
                    for (j in -kernelSize / 2..kernelSize / 2) {
                        val pixelX = x + i
                        val pixelY = y + j
                        if (pixelX in 0 until width && pixelY in 0 until height) {
                            kernelPixels.add(bitmap.getPixel(pixelX, pixelY))
                        }
                    }
                }

                // Calculate the average value of the pixels in the kernel
                val avgColor = calculateAverageColor(kernelPixels)

                // Set the average color as the new color of the pixel in the output bitmap
                outputBitmap.setPixel(x, y, avgColor)
            }
        }

        return outputBitmap
    }

    /**
     * Calculate the average color from the given list of pixel colors.
     *
     * @param colors The list of pixel colors.
     * @return The average color calculated from the list of pixel colors.
     */
    private fun calculateAverageColor(colors: List<Int>): Int {
        var sumRed = 0
        var sumGreen = 0
        var sumBlue = 0

        for (color in colors) {
            sumRed += color shr 16 and 0xFF
            sumGreen += color shr 8 and 0xFF
            sumBlue += color and 0xFF
        }

        val avgRed = sumRed / colors.size
        val avgGreen = sumGreen / colors.size
        val avgBlue = sumBlue / colors.size

        return (avgRed shl 16) or (avgGreen shl 8) or avgBlue or (0xFF shl 24)
    }

    /**
     * Creates a bitmap from an array of colors with the specified width and height.
     *
     * @param colors The array of colors representing the pixel values of the bitmap.
     * @param width The width of the bitmap.
     * @param height The height of the bitmap.
     * @return The resulting bitmap created from the array of colors.
     */
    private fun createBitmapFromColors(colors: IntArray, width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888)
    }
}

data class CacheFrame(
    val originalBitmap: Bitmap,
    val originalFrame: VideoFrame,
)