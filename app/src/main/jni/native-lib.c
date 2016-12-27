#include <jni.h>

JNIEXPORT jstring JNICALL
Java_felix_gpsbusgeohashdemo_CUtil_getGeohash(JNIEnv *env, jobject instance, jdouble lat,
                                              jdouble lon) {

    // TODO


    return (*env)->NewStringUTF(env, returnValue);
}