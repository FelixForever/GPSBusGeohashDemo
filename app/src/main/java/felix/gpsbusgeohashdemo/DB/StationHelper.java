package felix.gpsbusgeohashdemo.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import felix.gpsbusgeohashdemo.Util.JavaUtil;
import felix.gpsbusgeohashdemo.Bean.Station;
import felix.gpsbusgeohashdemo.Util.CUtil;

/**
 * Created by felix on 12/23/2016.
 */


public class StationHelper extends SQLiteOpenHelper {
    private static final String createLatIndex = "CREATE INDEX IF NOT EXISTS LatitudeIndex on STATION(latitude);";
    private static final String delLatIndex = "DROP INDEX IF EXISTS LatitudeIndex;";
    private static final String createLonIndex = "CREATE INDEX IF NOT EXISTS LongitudeIndex on STATION(longitude);";
    private static final String delLonIndex = "DROP INDEX IF EXISTS LongitudeIndex ;";
    private static final String createGeohashIndex = "CREATE INDEX IF NOT EXISTS GeohashIndex on STATION(geohascode);";
    private static final String delGeohashIndex = "DROP INDEX IF EXISTS GeohashIndex ;";
    private static final double minDistance = 610;
    private static final double distancePerDegress = 111000;
    private static final double minDeress = minDistance * minDistance / (distancePerDegress * distancePerDegress);

    private static StationHelper sStationHelper;

    /**
     * 获得该单例
     *
     * @param context
     * @return
     */
    public static StationHelper getHelper(Context context) {
        if (sStationHelper == null) {
            sStationHelper = new StationHelper(context);
        }
        return sStationHelper;
    }

    public StationHelper(Context context) {
        this(context, "Station", null, 1);
    }

    public StationHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 创建数据库
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS STATION(_id long primary key," +
                "name varchar(50) not null," +
                "latitude double not null," +
                "longitude double not null," +
                "geohascode varchar(10));";
        sqLiteDatabase.execSQL(sql);
        createIndex(sqLiteDatabase);
    }

    /**
     * 数据库升级，不做操作
     *
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * 插入单条数据，提供geohash
     *
     * @param db
     * @param station
     * @param geohash
     */
    private void insert(SQLiteDatabase db, Station station, String geohash) {
        String sql = String.format("INSERT INTO STATION VALUES(%d,'%s',%f,%f,'%s');", station.getId(), station.getName(), station.getLatitude(), station.getLongitude(), geohash);
        db.execSQL(sql);
    }

    /**
     * 插入单条数据，geohash使用c计算
     *
     * @param db
     * @param station
     */
    private void insertWithCGeo(SQLiteDatabase db, Station station) {
        insert(db, station, CUtil.getGeohash(station.getLatitude(), station.getLongitude()));
    }

    /**
     * 插入多条数据，geohash使用java计算
     *
     * @param db
     * @param station
     */
    private void insertWithJavaGeo(SQLiteDatabase db, Station station) {
        insert(db, station, JavaUtil.getGeohash(station.getLatitude(), station.getLongitude()));
    }


    /**
     * 普通插入多条数据,用C创建geohash
     *
     * @param stations
     */
    private void insert(List<Station> stations, boolean dropIndex, boolean isC) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        if (dropIndex) {
            dropIndex(db);
        }
        delAll(db);
        try {
            if (isC) {
                for (Station station : stations) {
                    insertWithCGeo(db, station);
                }
            } else {
                for (Station station : stations) {
                    insertWithJavaGeo(db, station);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dropIndex) {
            createIndex(db);
        }
        db.endTransaction();
    }

    public void insertWithC(List<Station> stations) {
        insert(stations, false, true);
    }

    public void insertWithJava(List<Station> stations) {
        insert(stations, false, true);
    }

    public void insertIndexWithC(List<Station> stations) {
        insert(stations, true, true);
    }

    public void insertWithIndexJava(List<Station> stations) {
        insert(stations, true, true);
    }

    /**
     * 删除所有数据
     */
    public void delAll() {
        delAll(getWritableDatabase());
    }

    private void delAll(SQLiteDatabase db) {
        db.execSQL("delete FROM STATION");
    }

    /**
     * 给定四周固定hash值，获取station列表
     *
     * @param lat
     * @param lon
     * @param geocode
     * @return
     */
    private List<Station> getStationWithGeo(double lat, double lon, String[] geocode) {
        final double dlat = minDistance / distancePerDegress;
        final double dLon = minDistance / (distancePerDegress * Math.cos(lat));
        final double minLat = lat - dlat;
        final double maxLat = lat + dlat;
        final double minLon = lon - dLon;
        final double maxLon = lon + dLon;
        final String geoSql = String.format("select * from STATION WHERE " +
                "geohascode='%s' OR " +
                "geohascode='%s' OR " +
                "geohascode='%s' OR " +
                "geohascode='%s' OR " +
                "geohascode='%s' OR " +
                "geohascode='%s' OR " +
                "geohascode='%s' OR " +
                "geohascode='%s' OR " +
                "geohascode='%s' ", geocode);

        final String childSql = String.format("select * from (%s)geoTable WHERE latitude>=%f AND " +
                "latitude<=%f AND longitude>=%f AND longitude<=%f ", geoSql, minLat, maxLat, minLon, maxLon);
        final String sql = String.format("select * from (%s)stationTable where " +
                        "((latitude-%f)*(latitude-%f)+(longitude-%f)*(longitude-%f))<=%f",
                childSql, lat, lat, lon, lon, minDeress);
        return getStations(sql);
    }

    /**
     * 通过c获得的geohash得到station列表
     *
     * @param lat
     * @param lon
     * @return
     */
    public List<Station> getStationWithCGeo(double lat, double lon) {
        return getStationWithGeo(lat, lon, CUtil.getGeohashList(lat, lon));
    }

    /**
     * 通过java获得的geohash得到station列表
     *
     * @param lat
     * @param lon
     * @return
     */
    public List<Station> getStationWithJavaGeo(double lat, double lon) {
        return getStationWithGeo(lat, lon, JavaUtil.getGeohashList(lat, lon));
    }

    /**
     * 使用经纬度索引
     *
     * @param lat
     * @param lon
     * @return
     */
    public List<Station> getStationWithIndex(double lat, double lon) {
        final double dlat = minDistance / distancePerDegress;
        final double dLon = minDistance / (distancePerDegress * Math.cos(lat));
        final double minLat = lat - dlat;
        final double maxLat = lat + dlat;
        final double minLon = lon - dLon;
        final double maxLon = lon + dLon;

        final String childSql = String.format("select * from STATION WHERE latitude>=%f AND " +
                "latitude<=%f AND longitude>=%f AND longitude<=%f ", minLat, maxLat, minLon, maxLon);
        final String sql = String.format("select * from (%s)stationTable where ((latitude-%f)*(latitude-%f)+(longitude-%f)*(longitude-%f))<=%f;", childSql, lat, lat, lon, lon, minDeress);

        return getStations(sql);
    }

    /**
     * 获取最近站点，减少取平方根耗的时间
     *
     * @param lat
     * @param lon
     * @return
     */
    public List<Station> getStationsWithOutSQRT(double lat, double lon) {
        final String sql = String.format("select * from STATION where ((latitude-%f)*(latitude-%f)+(longitude-%f)*(longitude-%f))<=%f", lat, lat, lon, lon, minDeress);
        return getStations(sql);
    }

    /**
     * 暴力获取站点信息，效率最低
     *
     * @param lat
     * @param lon
     * @return
     */
    public List<Station> getStations(double lat, double lon) {
        String sql = String.format("select * from STATION WHERE SQRT((latitude-%f)*(latitude-%f)+(longitude-%f)*(longitude-%f)) <=%f;",
                lat, lat, lon, lon, minDistance);
        return getStations(sql);
    }

    /**
     * 根据sql语句获取station
     *
     * @param sql
     * @return
     */
    private List<Station> getStations(final String sql) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor == null) {
            return null;
        }
        final int idIndex = cursor.getColumnIndex("_id");
        final int latitudeIndex = cursor.getColumnIndex("latitude");
        final int longitudeIndex = cursor.getColumnIndex("longitude");
        final int nameIndex = cursor.getColumnIndex("name");
        final int geohascodeIndex = cursor.getColumnIndex("geohascode");
        //  final int distanceIndex = cursor.getColumnIndex("distance");
        List<Station> stations = new ArrayList<>();
        while (cursor.moveToNext()) {
            Station station = new Station();
            station.setId(cursor.getLong(idIndex));
            station.setLatitude(cursor.getDouble(latitudeIndex));
            station.setLongitude(cursor.getDouble(longitudeIndex));
            station.setName(cursor.getString(nameIndex));
            station.setGeohash(cursor.getString(geohascodeIndex));
//            if (distanceIndex != -1) {
//                station.setDistance(cursor.getDouble(distanceIndex));
//            }
            stations.add(station);
        }
        return stations;
    }

    /**
     * 删除所有索引
     *
     * @param sqLiteDatabase
     */
    private void dropIndex(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(delLatIndex);
        sqLiteDatabase.execSQL(delLonIndex);
        sqLiteDatabase.execSQL(delGeohashIndex);
    }

    /**
     * 添加所有索引
     *
     * @param sqLiteDatabase
     */
    private void createIndex(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createLatIndex);
        sqLiteDatabase.execSQL(createLonIndex);
        sqLiteDatabase.execSQL(createGeohashIndex);
    }

}

