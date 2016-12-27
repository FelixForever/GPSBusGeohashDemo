package felix.gpsbusgeohashdemo.Util;

import android.util.Log;

import java.util.List;

import felix.gpsbusgeohashdemo.Bean.Station;

/**
 * Created by felix on 12/23/2016.
 */


public class CUtil {
    private static final String TAG = CUtil.class.getSimpleName();

    static {
        System.loadLibrary("native-lib");
    }

    public static String[] getGeohashList(double lat, double lon) {
        final String str = getGeohashs(lat, lon);
        final String[] strs = str.trim().split("_");
        Log.i(TAG, "getGeohashList: " + str);
        Log.i(TAG, "getGeohashList: " + strs.length);
        for (int i = 0; i < strs.length; i++) {
            Log.i(TAG, "getGeohashList: " + strs[i]);
        }
        return strs;
    }

    public native static String getGeohash(double lat, double lon);

    //  public native List<String> getGeohashs(double lat, double lon);

    public native static String getGeohashs(double lat, double lon);

    public native static double getRadomDouble();

    public native static List<Station> getRadomStation(int size);
}
