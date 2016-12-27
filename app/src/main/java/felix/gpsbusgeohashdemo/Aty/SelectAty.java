package felix.gpsbusgeohashdemo.Aty;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import felix.gpsbusgeohashdemo.Bean.Station;
import felix.gpsbusgeohashdemo.DB.StationHelper;
import felix.gpsbusgeohashdemo.R;
import felix.gpsbusgeohashdemo.Util.ToolUtil;

/**
 * Created by felix on 12/24/2016.
 */


public class SelectAty extends Activity {
    private StationHelper mStationHelper;
    private List<Station> mStations;
    private double mLat;
    private double mLon;
    private TextView mTextView;
    private ListView mLvStation;
    private StationAdp mStationAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_select);
        mLat = ToolUtil.nextLatitude();
        mLon = ToolUtil.nextLongitude();
        mTextView = ((TextView) findViewById(R.id.tv_result));
        mLvStation = ((ListView) findViewById(R.id.lv_station));
        setClickListenner(R.id.btn_del, R.id.btn_select_nomal, R.id.btn_select_no_sqrt, R.id.btn_select_gps_index, R.id.btn_select_geo_c, R.id.btn_select_geo_java);
        mStationHelper = StationHelper.getHelper(this);
        mStations = new ArrayList<>();
        mStationAdp = new StationAdp(this, mStations, mLat, mLon);
        mLvStation.setAdapter(mStationAdp);
    }

    private void setClickListenner(int... ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(mOnClickListener);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final long time = System.currentTimeMillis();
            List<Station> stations = new ArrayList<>();
            switch (v.getId()) {
                case R.id.btn_del:
                    mStationHelper.delAll();
                    break;
                case R.id.btn_select_nomal:
                    stations = mStationHelper.getStations(mLat, mLon);
                    break;
                case R.id.btn_select_no_sqrt:
                    stations = mStationHelper.getStationsWithOutSQRT(mLat, mLon);
                    break;
                case R.id.btn_select_gps_index:
                    stations = mStationHelper.getStationWithIndex(mLat, mLon);
                    break;
                case R.id.btn_select_geo_java:
                    stations = mStationHelper.getStationWithJavaGeo(mLat, mLon);
                    break;
                case R.id.btn_select_geo_c:
                    stations = mStationHelper.getStationWithCGeo(mLat, mLon);
                    break;
                default:
                    break;
            }
            mStations.clear();
            mStations.addAll(stations);
            mStationAdp.notifyDataSetChanged();
            final long now = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();

            if (v instanceof TextView) {
                sb.append(((TextView) v).getText().toString());
                sb.append(":");
            }
            sb.append("耗时:").append(now - time).append("ms");
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");
            sb.append("更新时间:").append(sdf.format(new Date(now)));
            sb.append("总数量：").append(stations.size());
            mTextView.setText(sb.toString());
        }
    };

}
