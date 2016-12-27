package felix.gpsbusgeohashdemo.Aty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import felix.gpsbusgeohashdemo.Bean.Station;
import felix.gpsbusgeohashdemo.R;

/**
 * Created by felix on 12/24/2016.
 */


public class StationAdp extends BaseAdapter {
    public static final double distancePerDegress = 111000.0;
    private List<Station> mCell;
    private Context mContext;
    private double mLat;
    private double mLon;
    private LayoutInflater mLayoutInflater;

    public StationAdp(Context context, List<Station> cell, double lat, double lon) {
        mContext = context;
        mCell = cell;
        mLayoutInflater = LayoutInflater.from(mContext);
        mLat = lat;
        mLon = lon;
    }

    @Override
    public Object getItem(int position) {
        return mCell.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mCell.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.station_itm, parent, false);
            viewHodler = new ViewHodler(convertView);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = ((ViewHodler) convertView.getTag());
        }
        viewHodler.setData(mCell.get(position));
        return convertView;
    }

    class ViewHodler {
        private TextView mName;
        private TextView mDistance;

        ViewHodler(View view) {
            mName = ((TextView) view.findViewById(R.id.tv_name));
            mDistance = ((TextView) view.findViewById(R.id.tv_distance));
        }

        public void setData(Station station) {
            mName.setText(station.getName());
            double distance = getDistance(station.getLatitude(), station.getLongitude());
            mDistance.setText(((int) (distance * 1000)) / 1000.0     + "");
        }

        private double getDistance(double lat, double lon) {
            double dlat = Math.abs(mLat - lat) * distancePerDegress;
            double dlon = Math.abs(mLon - lon) * distancePerDegress * Math.cos(lat);
            return Math.sqrt(dlat * dlat + dlon * dlon);
        }

    }
}
