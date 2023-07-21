package com.cloudwebrtc.webrtc.utils;

public class RNNoiseWrapper {
    static {
        System.loadLibrary("rnnoise");
    }

    public native void init();
    public native short[] processAudio(short[] audioData);
}
