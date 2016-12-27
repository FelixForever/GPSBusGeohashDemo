package felix.gpsbusgeohashdemo.Util;

/**
 * Created by felix on 12/23/2016.
 */


public class JavaUtil {
    public static final double minDistance = 610.0;
    public static final double distancePerDegress = 111000.0;
    public static final char divider = '|';
    public static char base32[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z'};

    /**
     * 获得介于minValue和maxValue的15位01编码
     *
     * @param value
     * @param minValue
     * @param maxValue
     * @return
     */
    private static boolean[] getCode(double value, final double minValue, final double maxValue) {
        boolean[] code = new boolean[15];
        double min = minValue;
        double max = maxValue;
        for (int i = 15 - 1; i >= 0; i--) {
            final double mid = (min + max) / 2;
            if (value >= mid) {
                code[i] = true;
                min = mid;
            } else {
                code[i] = false;
                max = mid;
            }
        }
        return code;
    }

    /**
     * 获得纬度的01编码
     *
     * @param lat
     * @return
     */
    private static boolean[] getLatCode(double lat) {
        return getCode(lat, -90, 90);
    }

    /**
     * 获得经度的01编码
     *
     * @param lon
     * @return
     */
    private static boolean[] getLonCode(double lon) {
        return getCode(lon, -180, 180);
    }

    /**
     * 获得固定经纬度的geohash的base32编码
     *
     * @param lat
     * @param lon
     * @return
     */
    private static char[] getGeoHashCode(double lat, double lon) {
        boolean[] code = new boolean[30];
        boolean[] latCode = getLatCode(lat);
        boolean[] lonCode = getLonCode(lon);
        for (int i = 0; i < 30; i++) {
            if (i % 2 == 0) {
                code[i] = latCode[i / 2];
            } else {
                code[i] = lonCode[(i - 1) / 2];
            }
        }
        char[] hash = new char[7];
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

    /**
     * 获得四周的9个hash编码
     *
     * @param lat
     * @param lon
     * @return
     */
    public static String[] getGeohashList(double lat, double lon) {
        double dlat = minDistance / distancePerDegress;
        double dLon = minDistance / (distancePerDegress * Math.cos(lat));
        final double minLat = lat - dlat;
        final double maxLat = lat + dlat;
        final double minLon = lon - dLon;
        final double maxLon = lon + dLon;
        final double lats[] = {minLat, lat, maxLat};
        final double lons[] = {minLon, lon, maxLon};
        char[] result = new char[6];
        String[] strs = new String[9];
        for (int i = 0; i < 9; i++) {
            final char[] top = getGeoHashCode(lats[i / 3], lons[i % 3]);
            for (int j = 0; j < 6; j++) {
                result[j] = top[j];
            }
            //result[6] = ((char) 0);
            strs[i] = new String(result);
        }
        return strs;
    }

    /**
     * 获得单个hash算法
     *
     * @param lat
     * @param lon
     * @return
     */
    public static String getGeohash(double lat, double lon) {
        return getGeoHashCode(lat, lon).toString();
    }
}
