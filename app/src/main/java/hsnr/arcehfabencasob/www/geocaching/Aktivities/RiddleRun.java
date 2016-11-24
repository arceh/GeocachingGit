package hsnr.arcehfabencasob.www.geocaching.Aktivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 24.11.2016.
 */

public class RiddleRun extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_run);
        /*
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        String cp = extras.getString("cp");
        TextView nameView = (TextView) findViewById(R.id.riddle_run_name);
        nameView.setText(name);
        cp = "Checkpoint: " + "/" + cp;
        */
    }

}
