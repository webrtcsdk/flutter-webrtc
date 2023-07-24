#include <rnnoise.h>
#include <jni.h>

static DenoiseState *rnnoise;

#define FRAME_SIZE 720

JNIEXPORT void JNICALL
Java_com_cloudwebrtc_webrtc_utils_RNNoiseWrapper_init(JNIEnv *env, jobject instance) {
    rnnoise = rnnoise_create(NULL);
}

JNIEXPORT void JNICALL
Java_com_cloudwebrtc_webrtc_utils_RNNoiseWrapper_dispose(JNIEnv *env, jobject instance) {
    rnnoise_destroy(rnnoise);
}

JNIEXPORT jshortArray JNICALL
Java_com_cloudwebrtc_webrtc_utils_RNNoiseWrapper_processAudio(JNIEnv *env, jobject instance,
                                                              jshortArray audioData_) {
    jshort *audioData = (*env)->GetShortArrayElements(env, audioData_, NULL);
    jsize audioDataLen = (*env)->GetArrayLength(env, audioData_);
    jshortArray processedData = (*env)->NewShortArray(env, audioDataLen);

    float x[FRAME_SIZE];

    for (int i = 0; i < audioDataLen; i += FRAME_SIZE) {
        if (i + FRAME_SIZE > audioDataLen) {
            break;
        }

        // Copy audio data from Java array to float array x
        for (int j = 0; j < FRAME_SIZE; j++) {
            x[j] = (float) audioData[i + j];
        }

        // Process the frame with RNNoise
        rnnoise_process_frame(rnnoise, x, x);

        // Copy processed audio data from float array x back to Java array
        for (int j = 0; j < FRAME_SIZE; j++) {
            audioData[i + j] = (jshort) x[j];
        }
    }

    // Release the Java array elements
    (*env)->ReleaseShortArrayElements(env, audioData_, audioData, 0);

    // Return the processed audio data
    return processedData;
}
