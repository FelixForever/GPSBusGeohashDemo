package felix.gpsbusgeohashdemo.Bean;

/**
 * Created by felix on 12/23/2016.
 */


public class Station {
    private long mId;
    private String mName;
    private double mLatitude;
    private double mLongitude;
    private String mGeohash;

    private double mDistance = -1;

    public Station() {

    }


    public Station(long id, PointLoc pointLoc) {
        this(id, pointLoc.getLatitude(), pointLoc.getLongitude(), "");
    }

    public Station(long id, PointLoc pointLoc, String geohash) {
        this(id, pointLoc.getLatitude(), pointLoc.getLongitude(), geohash);
    }

    public Station(long id, double latitude, double longitude, String geohash) {
        this(id, "编号" + id, latitude, longitude, geohash);
    }

    public Station(long id, String name, double latitude, double longitude, String geohash) {
        mId = id;
        mName = name;
        mLatitude = latitude;
        mLongitude = longitude;
        mGeohash = geohash;
    }

    public PointLoc getPointLoc() {
        return new PointLoc(mLatitude, mLongitude);
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        mDistance = distance;
    }

    public String getGeohash() {
        return mGeohash;
    }

    public void setGeohash(String geohash) {
        mGeohash = geohash;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
}
