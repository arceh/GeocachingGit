package hsnr.arcehfabencasob.www.geocaching.Aktivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import hsnr.arcehfabencasob.www.geocaching.DBS.Riddle;
import hsnr.arcehfabencasob.www.geocaching.DBS.RiddleDataSource;
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
    String answer1 = "";
    protected RiddleDataSource database = new RiddleDataSource(this);
    Riddle riddle;


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
        question = riddle.getQuestions().get(cpAkt).getQuestion().toString().toString().toString();
        //lade antwort zu cpAnz
        answer = riddle.getQuestions().get(cpAkt).getAnswer().toString();
        TextView nameView = (TextView) findViewById(R.id.riddle_run_name);
        nameView.setText(name);
        TextView cpView = (TextView) findViewById(R.id.riddle_run_cp);
        cpView.setText("Checkpoint: " + cpAkt + "/" + cpAnz);
        TextView questionView = (TextView) findViewById(R.id.riddle_run_riddle);
        questionView.setText(question);
    }

    protected void nextCp(View view){
        if(answer == answer1) {//kontrolliere position
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
                question = riddle.getQuestions().get(cpAkt).getQuestion().toString().toString().toString();
                answer = riddle.getQuestions().get(cpAkt).getAnswer().toString();
                answer = answer1;
                answer1.replace("1","2");
                TextView questionView = (TextView) findViewById(R.id.riddle_run_riddle);
                questionView.setText(question);
            }
        } else {
            Toast.makeText(this, "ALDA DU BIS FALSCH HIA", Toast.LENGTH_LONG).show();
        }
    }

}
