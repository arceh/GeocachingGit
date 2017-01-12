package hsnr.arcehfabencasob.www.geocaching.Aktivities.Run;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by uni on 22.12.16.
 */

public class AsyncT extends AsyncTask<RiddleRun, Integer, LatLng> {

    private RiddleRun rr;
    private Thread t;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected LatLng doInBackground(RiddleRun... rrs) {
        Looper.prepare();
        System.gc();
        rr = rrs[0];
        LatLng result = rr.map.getReQuestLatLng();
        t = Looper.myLooper().getThread();
        return result;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(LatLng result) {
        rr.nextCpPlus(result);
        System.gc();
    }


}
