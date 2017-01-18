package hsnr.arcehfabencasob.www.geocaching.Aktivities.Run;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import hsnr.arcehfabencasob.www.geocaching.DBS.Question;
import hsnr.arcehfabencasob.www.geocaching.DBS.Riddle;
import hsnr.arcehfabencasob.www.geocaching.DBS.RiddleDataSource;
import hsnr.arcehfabencasob.www.geocaching.GlobaleKoordinaten.Coordinate;
import hsnr.arcehfabencasob.www.geocaching.GlobaleKoordinaten.My_GPS;
import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 22.11.2016.
 */

public class RiddleStart extends AppCompatActivity {

    protected RiddleDataSource database = new RiddleDataSource(this);
    protected My_GPS myGps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Aufbau der Aktivität
         * Übergebene Informationen abspeichern */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_start);
        Bundle extras = getIntent().getExtras();
        init(extras.getString("riddleName"));
    }

    protected void init(String name){
        /* Initialisieren der Variabeln */
        TextView nameView = (TextView) findViewById(R.id.riddle_start_name);
        nameView.setText(name);
        TextView cpView = (TextView) findViewById(R.id.riddle_start_cp);
        TextView authorView = (TextView) findViewById(R.id.riddle_start_author);
        TextView rateView = (TextView) findViewById(R.id.riddle_start_rating);
        TextView diameterView = (TextView) findViewById(R.id.riddle_start_diameter);
        database.open();
        ArrayList<Riddle> allRiddle = database.getRiddlesByName(name);
        Riddle r = allRiddle.get(0);
        cpView.setText(String.valueOf(database.getRiddleCheckpointCountByName(name)));
        authorView.setText(database.getRiddleCreatorByName(name));
        rateView.setText(Float.toString(database.getRiddleRatingByName(name)));
        database.close();
        myGps =My_GPS.getInstance(this);
        diameterView.setText(Float.toString(diameter(r)) + " km");
    }

    protected void startRiddle(View view){
        /* Starten der Aktivität zum Spielen eines Rätsels */
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
        finish();
        return;
    }

    protected float diameter(Riddle r){
        /* Berechnung des maximalen Abstands von 2 Antworten*/
        float result = 0;
        HashMap<Integer,Question> q = r.getQuestions();
        for(int i = 0; i< q.size();i++){
            Coordinate a = q.get(i+1).getAnswer();
            for (int j = i; j < q.size();j++){
                Coordinate b = q.get(j+1).getAnswer();
                LatLng x = new LatLng(a.x,a.y);
                LatLng y = new LatLng(b.x,b.y);
                float z = myGps.getDistanz(x,y);
                if (z > result){
                    result = z;
                }
            }
        }
        return (result/1000);
    }

}
