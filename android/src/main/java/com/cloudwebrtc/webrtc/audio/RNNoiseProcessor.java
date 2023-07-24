package com.cloudwebrtc.webrtc.audio;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.cloudwebrtc.webrtc.utils.RNNoiseWrapper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

public class RNNoiseProcessor {
    private static final String TAG = "RNNoiseProcessor";
    private static final int SAMPLE_RATE = 96000;
    private static final int NUM_CHANNELS = 2;
    private static final int CHANNEL_CONFIG = (NUM_CHANNELS == 1) ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);

    private boolean isProcessing = false;
    private HandlerThread processingThread;
    private Handler processingHandler;

    public void startProcessing() {
        isProcessing = true;

        processingThread = new HandlerThread("RNNoiseProcessorThread");
        processingThread.start();
        processingHandler = new Handler(processingThread.getLooper());

        processingHandler.post(new Runnable() {
            @Override
            public void run() {
                // Initialize AudioRecord to capture data from the microphone(s)
                @SuppressLint("MissingPermission") AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);

                // Initialize AudioTrack to play back the processed audio
                AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE, AudioTrack.MODE_STREAM);

                int audioBufferSize = BUFFER_SIZE * NUM_CHANNELS;
                byte[] audioBuffer = new byte[audioBufferSize];
                short[] processedAudioData = new short[audioBufferSize / 2];

                // Initialize the RNNoiseWrapper for noise processing
                RNNoiseWrapper rnNoiseWrapper = new RNNoiseWrapper();
                rnNoiseWrapper.init();

                // Start recording from the microphone(s)
                audioRecord.startRecording();

                // Start audio playback
                audioTrack.play();

                while (isProcessing) {
                    // Read data from the microphone(s) into the audioBuffer array
                    int bytesRead = audioRecord.read(audioBuffer, 0, audioBufferSize);

                    // Convert the byte array to short array (16-bit PCM samples)
                    ByteBuffer.wrap(audioBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(processedAudioData);

                    // Process the audio data using RNNoiseWrapper for noise reduction
                    processedAudioData = rnNoiseWrapper.processAudio(processedAudioData);

                    // Convert the processed short array back to byte array
                    ByteBuffer.wrap(audioBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(processedAudioData);

                    logProcessedAudioSize(processedAudioData);

                    // Play back the processed audio
                    audioTrack.write(audioBuffer, 0, bytesRead * 2);
                }

                // Stop and release AudioRecord and AudioTrack
                audioRecord.stop();
                audioRecord.release();

                audioTrack.stop();
                audioTrack.release();
            }
        });
    }

    /**
     * Log the size of processedAudioData as a string representing the common audio size units (KB, MB, B).
     *
     * @param processedAudioData The short array containing the processed audio data.
     */
    private void logProcessedAudioSize(short[] processedAudioData) {
        // Calculate the size of the processedAudioData after converting to a byte array
        int byteSize = processedAudioData.length * 2; // Each short element has a size of 2 bytes

        // Convert the size to common audio size units (KB, MB, B) using the default locale
        String sizeStr;
        if (byteSize >= 1024 * 1024) {
            sizeStr = String.format(Locale.getDefault(), "%.2f MB", byteSize / (1024f * 1024f));
        } else if (byteSize >= 1024) {
            sizeStr = String.format(Locale.getDefault(), "%.2f KB", byteSize / 1024f);
        } else {
            sizeStr = byteSize + " B";
        }

        Log.w(TAG, "RNNoise by lambiengcode ** " + sizeStr);
    }

    public void stopProcessing() {
        isProcessing = false;

        if (processingThread != null) {
            processingThread.quitSafely();
            processingThread = null;
            processingHandler = null;
        }
    }
}
