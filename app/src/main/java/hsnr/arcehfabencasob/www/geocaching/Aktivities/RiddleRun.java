package hsnr.arcehfabencasob.www.geocaching.Aktivities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import hsnr.arcehfabencasob.www.geocaching.DBS.Riddle;
import hsnr.arcehfabencasob.www.geocaching.DBS.RiddleDataSource;
import hsnr.arcehfabencasob.www.geocaching.GlobaleCordinaten.My_GPS;
import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 24.11.2016.
 */

public class RiddleRun extends AppCompatActivity {
    int cpAkt = 1;
    int cpAnz = 1;
    String name = "";
    String question = "";
    LatLng answer;
    protected RiddleDataSource database = new RiddleDataSource(this);
    Riddle riddle;
    protected My_GPS map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_run);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        cpAnz = extras.getInt("cp");
        database.open();
        riddle = database.getRiddlesByName(name).get(0);
        database.close();
        question = riddle.getQuestions().get(cpAkt).getQuestion().toString();
        //lade antwort zu cpAnz
        LatLng tmp = new LatLng(riddle.getQuestions().get(cpAkt).getAnswer().x, riddle.getQuestions().get(cpAkt).getAnswer().y);
        answer = tmp;
        TextView nameView = (TextView) findViewById(R.id.riddle_run_name);
        nameView.setText(name);
        TextView cpView = (TextView) findViewById(R.id.riddle_run_cp);
        cpView.setText("Checkpoint: " + cpAkt + "/" + cpAnz);
        TextView questionView = (TextView) findViewById(R.id.riddle_run_riddle);
        questionView.setText(question);
        map = new My_GPS(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void nextCp(View view){
        LatLng temp = map.getReQuestLatLng();
        if(map.compareCoords(answer,temp,40)) {//kontrolliere position
            if (cpAkt >= cpAnz) {
                Intent intent = new Intent(this, RiddleWin.class);
                intent.putExtra("name", name);
                startActivity(intent);
                finish();
                return;
            } else {
                cpAkt++;
                TextView cpView = (TextView) findViewById(R.id.riddle_run_cp);
                cpView.setText("Checkpoint: " + cpAkt + "/" + cpAnz);
                question = riddle.getQuestions().get(cpAkt).getQuestion().toString();
                LatLng tmp = new LatLng(riddle.getQuestions().get(cpAkt).getAnswer().x, riddle.getQuestions().get(cpAkt).getAnswer().y);
                answer = tmp;
                TextView questionView = (TextView) findViewById(R.id.riddle_run_riddle);
                questionView.setText(question);
            }
        } else {
            Toast.makeText(this, "Sie sind an der falschen Position.", Toast.LENGTH_LONG).show();
            Toast.makeText(this, String.valueOf(map.getDistanz(answer,temp)), Toast.LENGTH_LONG).show();
        }
    }

}
