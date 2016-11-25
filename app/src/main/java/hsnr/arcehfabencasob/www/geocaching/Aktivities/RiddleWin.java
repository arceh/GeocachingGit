package hsnr.arcehfabencasob.www.geocaching.Aktivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 25.11.2016.
 */

public class RiddleWin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_win);
        Bundle extras = getIntent().getExtras();

    }

}
