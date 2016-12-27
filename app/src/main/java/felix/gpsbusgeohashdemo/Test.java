package felix.gpsbusgeohashdemo;

import android.util.Log;

import felix.gpsbusgeohashdemo.Util.ToolUtil;

/**
 * Created by felix on 12/23/2016.
 */


public class Test {
    private static final String TAG = Test.class.getSimpleName();

    public static void test() {
        for (int i = 0; i < 1000; i++) {
            double radom = ToolUtil.nextDouble(1, 2);
            Log.i(TAG, "test: " + radom);
        }
    }
}
