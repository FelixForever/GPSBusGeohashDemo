package felix.gpsbusgeohashdemo.Aty;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import felix.gpsbusgeohashdemo.Bean.Station;
import felix.gpsbusgeohashdemo.DB.StationHelper;
import felix.gpsbusgeohashdemo.R;
import felix.gpsbusgeohashdemo.Util.ToolUtil;

/**
 * Created by felix on 12/24/2016.
 */


public class InsertAty extends Activity {

    private StationHelper mStationHelper;
    private List<Station> mStations;
    private double mLat;
    private double mLon;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_insert);
        mStationHelper = StationHelper.getHelper(this);
        mStations = ToolUtil.getRadom();
        mLat = ToolUtil.nextLatitude();
        mLon = ToolUtil.nextLongitude();
        mTextView = ((TextView) findViewById(R.id.tv_result));
        setClickListenner(R.id.btn_del, R.id.btn_insert_c, R.id.btn_insert_java, R.id.btn_insert_c_index, R.id.btn_insert_java_index,
                R.id.btn_select, R.id.btn_select_no_sqrt, R.id.btn_select_gps_index, R.id.btn_select_geo_c, R.id.btn_select_geo_java);
    }


    private void setClickListenner(int... ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(mOnClickListener);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            clickView(v);
        }
    };
    private Handler sHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                mTextView.setText(((String) msg.obj));
                return true;
            }
            return false;
        }
    });

    private void clickView(View view) {
        final int id = view.getId();
        final String str;
        if (view instanceof TextView) {
            str = ((TextView) view).getText().toString();
        } else {
            str = "";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result = clickView(id, str);
                Message msg = sHandler.obtainMessage();
                msg.what = 1;
                msg.obj = result;
                sHandler.sendMessage(msg);
            }
        }).start();
    }

    private String clickView(int id, String text) {
        final long time = System.currentTimeMillis();
        switch (id) {
            case R.id.btn_del:
                mStationHelper.delAll();
                break;
            case R.id.btn_insert_c:
                mStationHelper.insertWithC(mStations);
                break;
            case R.id.btn_insert_java:
                mStationHelper.insertWithJava(mStations);
                break;
            case R.id.btn_insert_c_index:
                // ToolUtil.getRadomC();
                mStationHelper.insertIndexWithC(mStations);
                break;
            case R.id.btn_insert_java_index:
//                ToolUtil.getRadomJava();
                mStationHelper.insertWithIndexJava(mStations);
                break;
            case R.id.btn_select:
                mStationHelper.getStations(mLat, mLon);
                break;
            case R.id.btn_select_no_sqrt:
                mStationHelper.getStationsWithOutSQRT(mLat, mLon);
                break;
            case R.id.btn_select_gps_index:
                mStationHelper.getStationWithIndex(mLat, mLon);
                break;
            case R.id.btn_select_geo_java:
                mStationHelper.getStationWithJavaGeo(mLat, mLon);
                break;
            case R.id.btn_select_geo_c:
                mStationHelper.getStationWithCGeo(mLat, mLon);
                break;
            default:
                break;
        }
        final long now = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();

        if (!TextUtils.isEmpty(text)) {
            sb.append(text);
            sb.append(":");
        }
        sb.append("耗时:").append(now - time).append("ms");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss.SSS");
        sb.append("更新时间:").append(sdf.format(new Date(now)));
        return sb.toString();
    }

}
