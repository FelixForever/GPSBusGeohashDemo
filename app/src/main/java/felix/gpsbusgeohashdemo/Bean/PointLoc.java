package felix.gpsbusgeohashdemo.Bean;

/**
 * Created by felix on 12/23/2016.
 */


public class PointLoc {
    private double mLatitude;
    private double mLongitude;

    public PointLoc() {

    }

    public PointLoc(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
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
