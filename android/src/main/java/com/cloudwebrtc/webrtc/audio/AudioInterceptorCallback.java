package com.cloudwebrtc.webrtc.audio;

import android.media.AudioTrack;
import android.util.Log;

import com.cloudwebrtc.webrtc.record.AudioTrackInterceptor;
import com.cloudwebrtc.webrtc.utils.RNNoiseWrapper;

import org.webrtc.audio.JavaAudioDeviceModule;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AudioInterceptorCallback implements JavaAudioDeviceModule.SamplesReadyCallback {

    static private final String TAG = "WebRtcAudioTrackUtils";
    private final AudioTrack audioTrack;
    private final RNNoiseWrapper rnNoiseWrapper;

    public AudioInterceptorCallback(AudioTrack audioTrack) {
        this.audioTrack = audioTrack;
        rnNoiseWrapper = new RNNoiseWrapper();
        rnNoiseWrapper.init();
    }

    @Override
    public void onWebRtcAudioRecordSamplesReady(JavaAudioDeviceModule.AudioSamples audioSamples) {
        byte[] audioBytes = audioSamples.getData();
        short[] audioShorts = bytesToShorts(audioBytes);

        short[] audioProcessed = rnNoiseWrapper.processAudio(audioShorts);

        // Write the processed audio data to the AudioTrackInterceptor for playback and WebRTC
        audioTrack.write(audioProcessed, 0, audioProcessed.length);

//        Log.w(TAG, "RNNoise by lambiengcode ** " + (audioProcessed != audioShorts));
    }

    private short[] bytesToShorts(byte[] bytes) {
        short[] shorts = new short[bytes.length / 2];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
        return shorts;
    }

    private byte[] shortsToBytes(short[] shorts) {
        ByteBuffer buffer = ByteBuffer.allocate(shorts.length * 2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.asShortBuffer().put(shorts);
        return buffer.array();
    }
}
