package hsnr.arcehfabencasob.www.geocaching.Aktivities.Create;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.regex.Pattern;

import hsnr.arcehfabencasob.www.geocaching.DBS.Question;
import hsnr.arcehfabencasob.www.geocaching.DBS.Riddle;
import hsnr.arcehfabencasob.www.geocaching.GlobaleKoordinaten.Coordinate;
import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 05.01.2017.
 */

public class CreateRiddleCps extends AppCompatActivity {

    protected int anzCp;
    protected TextView cpField;
    protected EditText questionField;
    protected Button nextButton;
    protected HashMap<Integer, Question> newQuest = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_riddle_cps);
        init();
    }

    protected void init(){
        anzCp = 0;
        cpField = (TextView) findViewById(R.id.create_riddle_cps_anz);
        questionField = (EditText) findViewById(R.id.create_riddle_cps_question);
        nextButton = (Button) findViewById(R.id.create_riddle_cps_button);
        cpField.setText(getString(R.string.createRiddleCpsCP) + anzCp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_riddle_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.create_riddle_cps_finish:
                finishRiddle();
                break;
            case R.id.create_riddle_cps_back:
                lastQuestion();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void nextQuestion(View view){
        String question = questionField.getText().toString();
        if(Pattern.matches(" *",question)){
            return;
        }
        anzCp++;
        newQuest.put(anzCp, new Question(question, new Coordinate(51.3165,6.5715))); //hier Koordinaten benötigt
        cpField.setText(getString(R.string.createRiddleCpsCP) + anzCp);
        if(anzCp >= 10){
            questionField.setText(R.string.maxCp);
            questionField.setEnabled(false);
            nextButton.setEnabled(false);
        } else {
            questionField.setText("");
        }
    }

    protected void lastQuestion(){
        if(anzCp <= 0){
            return;
        }
        if(anzCp >= 10){
            questionField.setEnabled(true);
            nextButton.setEnabled(true);
        }
        questionField.setText(newQuest.get(anzCp).getQuestion());
        newQuest.remove(anzCp);
        anzCp--;
        cpField.setText(getString(R.string.createRiddleCpsCP) + anzCp);
    }

    protected void finishRiddle(){
        if(anzCp <= 0){

        } else {
            Riddle riddle;
            Intent intent = new Intent(this, CreateRiddleFinish.class);
            startActivity(intent);
            finish();
        }
    }
}
