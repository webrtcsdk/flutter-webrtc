package com.cloudwebrtc.webrtc.audio;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.cloudwebrtc.webrtc.FlutterWebRTCPlugin;
import com.cloudwebrtc.webrtc.utils.RNNoiseWrapper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RNNoiseProcessor {

    static final String TAG = FlutterWebRTCPlugin.TAG;
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AUDIO_FORMAT);

    private boolean isProcessing = false;
    private HandlerThread processingThread;
    private Handler processingHandler;

    public void startProcessing() {
        isProcessing = true;

        processingThread = new HandlerThread("RNNoiseProcessorThread");
        processingThread.start();
        processingHandler = new Handler(processingThread.getLooper());

        processingHandler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                // Initialize AudioRecord to capture data from the microphone
                AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);

                // Initialize AudioTrack to play back the processed audio
                AudioTrack audioTrack = new AudioTrack.Builder()
                        .setAudioAttributes(new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build())
                        .setAudioFormat(new AudioFormat.Builder()
                                .setSampleRate(SAMPLE_RATE)
                                .setEncoding(AUDIO_FORMAT)
                                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                .build())
                        .setTransferMode(AudioTrack.MODE_STREAM)
                        .build();

                byte[] audioBuffer = new byte[BUFFER_SIZE];
                short[] processedAudioData = new short[BUFFER_SIZE / 2];

                // Start recording from the microphone
                audioRecord.startRecording();

                // Start audio playback
                audioTrack.play();

                // Initialize the RNNoiseWrapper for noise processing
                RNNoiseWrapper rnNoiseWrapper = new RNNoiseWrapper();
                rnNoiseWrapper.init();

                while (isProcessing) {
                    // Read data from the microphone into the audioBuffer array
                    int bytesRead = audioRecord.read(audioBuffer, 0, BUFFER_SIZE);

                    // Convert the byte array to short array (16-bit PCM samples)
                    ByteBuffer.wrap(audioBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(processedAudioData);

                    // Process the audio data using RNNoiseWrapper for noise reduction
                    processedAudioData = rnNoiseWrapper.processAudio(processedAudioData);

                    // Convert the processed short array back to byte array
                    ByteBuffer.wrap(audioBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(processedAudioData);

                    Log.d(TAG, "RNNoise here..Okay?");
                    // Play back the processed audio
                    audioTrack.write(audioBuffer, 0, bytesRead);
                }

                // Stop and release AudioRecord and AudioTrack
                audioRecord.stop();
                audioRecord.release();

                audioTrack.stop();
                audioTrack.release();
            }
        });
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
