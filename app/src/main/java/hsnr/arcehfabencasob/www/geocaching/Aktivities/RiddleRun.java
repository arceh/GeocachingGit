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
    String name = "";
    String question = "";
    String answer = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_run);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        cpAnz = extras.getInt("cp");
        cpAkt = extras.getInt("cpOld", 1);
        //lade frage zu cpAnz
        question = "Wer kam am 23.11.2016 zu spät zu EZS?";
        //lade antwort zu cpAnz
        answer = "123";
        TextView nameView = (TextView) findViewById(R.id.riddle_run_name);
        nameView.setText(name);
        TextView cpView = (TextView) findViewById(R.id.riddle_run_cp);
        cpView.setText("Checkpoint: " + cpAkt + "/" + cpAnz);
        TextView questionView = (TextView) findViewById(R.id.riddle_run_riddle);
        questionView.setText(question);
    }

    protected void nextCp(View view){
        if(answer == "123") {//kontrolliere position
            if (cpAkt >= cpAnz) {
                Intent intent = new Intent(this, RiddleWin.class);
                intent.putExtra("name", name);
                startActivity(intent);
                //Gewonnen
            } else {
                cpAkt++;
                //lade neue frage
                //lade neue antwort
                TextView cpView = (TextView) findViewById(R.id.riddle_run_cp);
                cpView.setText("Checkpoint: " + cpAkt + "/" + cpAnz);
                /*/
                Intent intent = new Intent(this, RiddleRun.class);
                intent.putExtra("name", name);
                intent.putExtra("cp", cpAnz);
                intent.putExtra("cpOld", cpAkt);
                startActivity(intent);
                //*/
            }
        } else {
            //nicht in der nähe
        }
    }

}
