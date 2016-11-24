package hsnr.arcehfabencasob.www.geocaching.Aktivities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import hsnr.arcehfabencasob.www.geocaching.GlobaleCordinaten.Map;
import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 22.11.2016.
 */

//Unterordner für klassen?
//Listen layout mit mehreren Elementen?

public class RiddleStart extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.riddle_start);
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("riddleName");
        TextView nameView = (TextView) findViewById(R.id.riddle_start_name);
        nameView.setText(name);

    }

}
