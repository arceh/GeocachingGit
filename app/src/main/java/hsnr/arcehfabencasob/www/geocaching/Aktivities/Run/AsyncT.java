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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected LatLng doInBackground(RiddleRun... rrs) {
        Looper.prepare();
        rr = rrs[0];
        LatLng result = rr.map.getReQuestLatLng();
        return result;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(LatLng result) {
        rr.nextCpPlus(result);
    }


}