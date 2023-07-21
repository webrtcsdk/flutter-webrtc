#include <rnnoise.h>
#include <jni.h>

static DenoiseState *rnnoise;

JNIEXPORT Void JNICALL
Java_com_cloudwebrtc_webrtc_utils_RNNoiseWrapper_init(JNIEnv *env, jobject instance) {
    rnnoise = rnnoise_create();
}

JNIEXPORT jshortArray JNICALL
Java_com_cloudwebrtc_webrtc_utils_RNNoiseWrapper_processAudio(JNIEnv *env, jobject instance,
                                                     jshortArray audioData_) {
    jshort *audioData = (*env)->GetShortArrayElements(env, audioData_, NULL);
    jsize audioDataLen = (*env)->GetArrayLength(env, audioData_);
    jshortArray processedData = (*env)->NewShortArray(env, audioDataLen);

    for (int i = 0; i < audioDataLen; i += 480) {
        if (i + 480 > audioDataLen) {
            break;
        }
        rnnoise_process_frame(rnnoise, audioData + i);
    }

    (*env)->SetShortArrayRegion(env, processedData, 0, audioDataLen, audioData);

    (*env)->ReleaseShortArrayElements(env, audioData_, audioData, 0);

    return processedData;
}
