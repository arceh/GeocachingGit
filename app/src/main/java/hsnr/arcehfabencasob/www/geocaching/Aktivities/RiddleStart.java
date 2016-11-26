package hsnr.arcehfabencasob.www.geocaching.Aktivities;

import android.content.Intent;
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

public class RiddleStart extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_start);
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("riddleName");
        TextView nameView = (TextView) findViewById(R.id.riddle_start_name);
        nameView.setText(name);
        //Anzahl cp holen und Anzeigen
        //Autor holen und Anzeigen
        //Bewertung
        //Durchmesser
    }

    protected void startRiddle(View view){
        int cp = 0;
        TextView nameView = (TextView) findViewById(R.id.riddle_start_name);
        String name = nameView.getText().toString();
        TextView cpView = (TextView) findViewById(R.id.riddle_start_cp);
        try {
            cp = Integer.parseInt(cpView.getText().toString());
        } catch(NumberFormatException nfe) {
            return;
        }
        Intent intent = new Intent(this,RiddleRun.class);
        intent.putExtra("name", name);
        intent.putExtra("cp", cp);
        startActivity(intent);
    }

}
