package com.cloudwebrtc.webrtc.utils;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.cloudwebrtc.webrtc.record.AudioSamplesInterceptor;
import com.cloudwebrtc.webrtc.record.AudioTrackInterceptor;

import org.webrtc.audio.JavaAudioDeviceModule;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NoiseReductionProcessor extends AudioSamplesInterceptor {
    private static final int SAMPLE_RATE = 44100; // Set the sample rate according to your requirement
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = 480; // Set the buffer size according to your requirement
    private static final int NOISE_REDUCTION_INTERVAL_MS = 10; // Set the noise reduction interval in milliseconds

    private final AudioRecord audioRecord;
    private final RNNoiseWrapper rnNoiseWrapper;
    private volatile boolean isRunning;

    private final JavaAudioDeviceModule audioDeviceModule;

    private AudioTrackInterceptor audioTrackInterceptor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public NoiseReductionProcessor(RNNoiseWrapper rnNoiseWrapper, JavaAudioDeviceModule audioDeviceModule) {
        this.rnNoiseWrapper = rnNoiseWrapper;
        this.audioDeviceModule = audioDeviceModule;
        audioRecord = new AudioRecord.Builder()
                .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AUDIO_FORMAT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(CHANNEL_CONFIG)
                        .build())
                .setBufferSizeInBytes(BUFFER_SIZE)
                .build();
    }

    public void start() {
        if (isRunning) return;

        isRunning = true;
        audioRecord.startRecording();

        byte[] audioBuffer = new byte[BUFFER_SIZE];
        while (isRunning) {
            int bytesRead = audioRecord.read(audioBuffer, 0, BUFFER_SIZE);
            if (bytesRead > 0) {
                // Process audio data with RNNoise
                short[] audioDataShorts = bytesToShorts(audioBuffer);
                short[] processedAudioData = rnNoiseWrapper.processAudio(audioDataShorts);
                byte[] processedAudioBytes = shortsToBytes(processedAudioData);

                // Write the processed audio data to the audio track
                audioTrackInterceptor.write(processedAudioBytes, 0, processedAudioBytes.length);
            }

            // Sleep for the noise reduction interval
            try {
                Thread.sleep(NOISE_REDUCTION_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        audioRecord.stop();
        audioRecord.release();
    }

    public void stop() {
        isRunning = false;
    }

    private short[] bytesToShorts(byte[] bytes) {
        short[] shorts = new short[bytes.length / 2];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
        return shorts;
    }

    private byte[] shortsToBytes(short[] shorts) {
        ByteBuffer buffer = ByteBuffer.allocate(shorts.length * 2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (short s : shorts) {
            buffer.putShort(s);
        }
        return buffer.array();
    }
}
