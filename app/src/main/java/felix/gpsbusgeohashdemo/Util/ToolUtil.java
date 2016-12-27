package felix.gpsbusgeohashdemo.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import felix.gpsbusgeohashdemo.Bean.PointLoc;
import felix.gpsbusgeohashdemo.Bean.Station;

/**
 * Created by felix on 12/23/2016.
 */


public class ToolUtil {
    private final static int SIZE = 500000;

    /**
     * 获得十万个随机车站
     *
     * @return
     */
    public static List<Station> getRadom() {
        List<Station> pointLocs = new ArrayList<>();
        for (int i = 0; i < SIZE ; i++) {
            pointLocs.add(new Station(sRandom.nextLong(), getMyPoint()));
        }
        return pointLocs;
    }

    public static List<Station> getRadomC() {

        for (int i = 0; i < SIZE; i++) {
            CUtil.getGeohashs(nextLatitude(), nextLongitude());
        }
        if (true)
            return null;
        List<Station> pointLocs = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            final double lat = nextLatitude();
            final double lon = nextLongitude();
            pointLocs.add(new Station(sRandom.nextLong(), lat, lon, CUtil.getGeohash(lat, lon)));
        }
        return pointLocs;
    }

    public static List<Station> getRadomJava() {
        for (int i = 0; i < SIZE; i++) {
            JavaUtil.getGeohashList(nextLatitude(), nextLongitude());
        }
        if (true)
            return null;
        List<Station> pointLocs = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            final double lat = nextLatitude();
            final double lon = nextLongitude();
            pointLocs.add(new Station(sRandom.nextLong(), lat, lon, JavaUtil.getGeohash(lat, lon)));
        }
        return pointLocs;
    }

    /**
     * 获得一个随机位置
     *
     * @return
     */
    public static PointLoc getMyPoint() {
        return new PointLoc(nextLatitude(), nextLongitude());
    }

    /**
     * 获得一个随机纬度
     *
     * @return
     */
    public static double nextLatitude() {
        return nextDouble(24.432667, 25.052248);
    }

    /**
     * 获得一个随机经度
     *
     * @return
     */
    public static double nextLongitude() {
        return nextDouble(117.806733, 118.43454);
    }

    private static Random sRandom = new Random();

    /**
     * 获得一个介于min和max之间的数
     *
     * @param min
     * @param max
     * @return
     */
    public static double nextDouble(double min, double max) {
        return sRandom.nextDouble() * (max - min) + min;
    }
}
