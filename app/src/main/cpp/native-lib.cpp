#include <jni.h>
#include <math.h>
#include <stdlib.h>
#include <time.h>
#include <android/log.h>

#define DECODE_SIZE 15

#define minDistance  610.0
#define distancePerDegress  111000.0
#define divider '_'
#define MIN_LAT 24.432667
#define MAX_LAT 25.052248
#define MIN_LON 117.806733
#define MAX_LON 118.43454
#define SIZE 10000
const char base32[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e', 'f',
                       'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                       'y', 'z'};


/**
 * 获得介于minValue和maxValue的15位01编码
 */
bool *getCode(double value, const double minValue, const double maxValue) {
    bool *byte = new bool[30];
    double min = minValue;
    double max = maxValue;
    for (int i = 15 - 1; i >= 0; i--) {
        const double mid = (min + max) / 2;
        if (value >= mid) {
            byte[i] = 1;
            min = mid;
        } else {
            byte[i] = 0;
            max = mid;
        }
    }
    return byte;
}


/**
 * 获得纬度的01编码
 */
bool *getLatCode(double lat) {
    return getCode(lat, -90, 90);
}

/**
 * 获得经度的01编码
 */
bool *getLonCode(double lon) {
    return getCode(lon, -180, 180);
}

/**
 * 获得固定经纬度的geohash的base32编码
 */
char *getGeoHashCode(double lat, double lon) {
    __android_log_print(1, "tag", "heheh");
    bool code[30];
    bool *latCode = getLatCode(lat);
    bool *lonCode = getLonCode(lon);
    for (int i = 0; i < 30; i++) {
        if (i % 2 == 0) {
            code[i] = latCode[i / 2];
        } else {
            code[i] = lonCode[(i - 1) / 2];
        }
    }
    delete[]latCode;
    delete[]lonCode;
    char *hash = new char[7];
    for (int i = 0; i < 6; i++) {
        int count = 0;
        int add = 16;
        for (int j = 0; j < 5; j++) {
            if (code[i * 5 + j]) {
                count += add;
            }
            add = add >> 1;
        }
        hash[i] = base32[count];
    }
    hash[6] = 0;
    return hash;
}

extern "C" {

/*
* Class:     com_ulang_AudioLib
* Method:    sayHelloEx
* Signature: ()Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL
        Java_felix_gpsbusgeohashdemo_Util_CUtil_getGeohash(JNIEnv *env, jobject type, jdouble lat,
                                                           jdouble lon);
JNIEXPORT jstring JNICALL
        Java_felix_gpsbusgeohashdemo_Util_CUtil_getGeohashs(JNIEnv *env, jobject type, jdouble lat,
                                                            jdouble lon);
JNIEXPORT jdouble JNICALL
        Java_felix_gpsbusgeohashdemo_Util_CUtil_getRadomDouble(JNIEnv *env, jclass type);
JNIEXPORT jobject JNICALL
        Java_felix_gpsbusgeohashdemo_Util_CUtil_getRadomStation(JNIEnv *env, jclass type,
                                                                jint size);
}


/**
 * 获得固定位置的单个geohash的base32编码，用于插入时编码
 */

JNIEXPORT jstring JNICALL
Java_felix_gpsbusgeohashdemo_Util_CUtil_getGeohash(JNIEnv *env, jobject type, jdouble lat,
                                                   jdouble lon) {

    char *jniStr = getGeoHashCode(lat, lon);
    jstring str = env->NewStringUTF(jniStr);
    delete[]jniStr;
    return str;
}

//extern "C"


/**
 * 获得固定位置及其四方的geohash的base32编码，得到多个字符串用"|"隔开，用于读取时得到及其边缘位置
 */
JNIEXPORT jstring JNICALL
Java_felix_gpsbusgeohashdemo_Util_CUtil_getGeohashs(JNIEnv *env, jobject type, jdouble lat,
                                                    jdouble lon) {

    double dlat = minDistance / distancePerDegress;
    double dLon = minDistance / (distancePerDegress * cos(lat));
    const double minLat = lat - dlat;
    const double maxLat = lat + dlat;
    const double minLon = lon - dLon;
    const double maxLon = lon + dLon;
    const double lats[] = {minLat, lat, maxLat};
    const double lons[] = {minLon, lon, maxLon};
    char result[63];
    for (int i = 0; i < 9; i++) {
        const char *top = getGeoHashCode(lats[i / 3], lons[i % 3]);
        for (int j = 0; j < 6; j++) {
            result[j + i * 7] = top[j];
        }
        delete[]top;
        result[6 + i * 7] = divider;
    }
    result[62] = 0;
    jstring str = env->NewStringUTF(result);
    return str;
}


JNIEXPORT jdouble JNICALL
Java_felix_gpsbusgeohashdemo_Util_CUtil_getRadomDouble(JNIEnv *env, jclass type) {

    // TODO
    srand((unsigned) time(NULL));
    rand() / double(RAND_MAX);
    double a = rand();
    double b = rand();
    return a - b;
}

JNIEXPORT jobject JNICALL
Java_felix_gpsbusgeohashdemo_Util_CUtil_getRadomStation(JNIEnv *env, jclass type, jint size) {

//    srand((unsigned) time(NULL));
//    const double dlat = MAX_LAT - MIN_LAT;
//    const double dlon = MAX_LON - MIN_LON;
//    rand() / double(RAND_MAX);
//    jclass cls = env->FindClass("Lpackagename/classname;");  //创建一个class的引用
//    jmethodID id = env->GetMethodID(cls, "", "(D)V");  //注意这里方法的名称是""，它表示这是一个构造函数，而且构造参数是double型的
//    //jobject obj = env->NewObjectA(cls, id,);  //获得一实例，args是构造函数的参数，它是一个jvalue*类型。
//    for (int i = 0; i < size; ++i) {
//        const long time = rand();
//        const double lat = rand() / double(RAND_MAX) * dlat + MIN_LAT;
//        const double lon = rand() / double(RAND_MAX) * dlon + MIN_LON;
//
//        jvalue sid(time);
//        jvalue name(time);
//        jvalue latitude(lat);
//        jvalue longitude(lon);
//        jvalue geohash(getGeoHashCode(lat, lon));
//        jvalue *jvalue1 = new jvalue[]{sid, name, latitude, longitude, geohash};
//        jobject obj = env->NewObjectA(cls, id, jvalue1);
//    }
    return NULL;

}