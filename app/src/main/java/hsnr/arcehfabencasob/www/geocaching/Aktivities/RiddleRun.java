package hsnr.arcehfabencasob.www.geocaching.Aktivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 24.11.2016.
 */

public class RiddleRun extends AppCompatActivity {
    int cpAkt = 1;
    int cpAnz = 1;
    String name;
    String Answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_run);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        cpAnz = extras.getInt("cp");
        cpAkt = extras.getInt("cpOld", 1);
        TextView nameView = (TextView) findViewById(R.id.riddle_run_name);
        nameView.setText(name);
        TextView cpView = (TextView) findViewById(R.id.riddle_run_cp);
        cpView.setText("Checkpoint: " + cpAkt + "/" + cpAnz);
    }

    protected void nextCp(View view){
        //antwort kontrollieren
        if(cpAkt>=cpAnz){
            //Gewonnen
        } else {
            cpAkt++;
            Intent intent = new Intent(this,RiddleRun.class);
            intent.putExtra("name", name);
            intent.putExtra("cp", cpAnz);
            intent.putExtra("cpOld", cpAkt);
            startActivity(intent);
        }
    }

}
