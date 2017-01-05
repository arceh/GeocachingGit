package hsnr.arcehfabencasob.www.geocaching.Aktivities.Run;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by uni on 22.12.16.
 */

public class AsyncT extends AsyncTask<RiddleRun, Integer, LatLng> {

    private final String LOG_TAG = this.getClass().getSimpleName();
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
        Log.d(LOG_TAG, "Async Task Update...");
    }

    protected void onPostExecute(LatLng result) {
        //Looper.loop();
        Log.d(LOG_TAG, "Koordinaten: " + result.toString());
        rr.nextCpPlus(result);
        Looper.myLooper().getThread().interrupt();
        Log.d(LOG_TAG, this.getStatus().toString());
        Looper.loop();
    }
}
