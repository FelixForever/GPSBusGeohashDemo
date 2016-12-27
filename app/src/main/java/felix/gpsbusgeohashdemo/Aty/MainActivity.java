package felix.gpsbusgeohashdemo.Aty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import felix.gpsbusgeohashdemo.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        findViewById(R.id.btn_quarity).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_check).setOnClickListener(mOnClickListener);
//        Log.i(TAG, "onCreate: " + CUtil.getRadomDouble());
//        ;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_quarity:
                    startActivity(new Intent(mContext, InsertAty.class));
                    break;
                case R.id.btn_check:
                    startActivity(new Intent(mContext, SelectAty.class));
                    break;
                default:
                    break;
            }
        }
    };
}


