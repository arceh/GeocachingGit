package hsnr.arcehfabencasob.www.geocaching.Aktivities;

import android.os.Looper;

/**
 * Created by uni on 12.01.17.
 */

public class MyThread extends Thread {


    public MyThread(Thread t) {
        super(t);
    }

    @Override
    public void run() {
        Looper.prepare();
        super.run();
    }
}
