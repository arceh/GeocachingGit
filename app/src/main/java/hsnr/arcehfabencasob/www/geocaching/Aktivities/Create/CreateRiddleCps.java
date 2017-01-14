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
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import hsnr.arcehfabencasob.www.geocaching.Aktivities.MyThread;
import hsnr.arcehfabencasob.www.geocaching.DBS.Question;
import hsnr.arcehfabencasob.www.geocaching.DBS.Riddle;
import hsnr.arcehfabencasob.www.geocaching.DBS.RiddleDataSource;
import hsnr.arcehfabencasob.www.geocaching.GlobaleKoordinaten.Coordinate;
import hsnr.arcehfabencasob.www.geocaching.GlobaleKoordinaten.My_GPS;
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
    protected String user;
    protected My_GPS map;
    protected RiddleDataSource database = new RiddleDataSource(this);
    private Thread gpsThread = new Thread() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void run() {
            //final LatLng res = ;   /*Todo : Hier Koordinaten anfragen*/
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    nextQuestion(new Coordinate(51.3165,6.5715));
                }
            });
        }
    };
    public MyThread getGps = new MyThread(gpsThread);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_riddle_cps);
        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");
        init();
    }

    protected void init(){
        anzCp = 0;
        cpField = (TextView) findViewById(R.id.create_riddle_cps_anz);
        questionField = (EditText) findViewById(R.id.create_riddle_cps_question);
        nextButton = (Button) findViewById(R.id.create_riddle_cps_button);
        cpField.setText(getString(R.string.createRiddleCpsCP) + anzCp);
        map = new My_GPS(this);
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
                return true;
            case R.id.create_riddle_cps_back:
                lastQuestion();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void nextQuestionStart(View view){
        new MyThread(gpsThread).start();
        return;
    }

    protected void nextQuestion(Coordinate coords){
        String question = questionField.getText().toString();
        if(Pattern.matches(" *",question)){
            return;
        }
        anzCp++;
        newQuest.put(anzCp, new Question(question, coords));
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
        if(anzCp <= 2){
            Toast.makeText(this,R.string.minCp,Toast.LENGTH_LONG).show();
        } else {
            int id=0;
            Riddle riddle = new Riddle("temporär",newQuest,user,0f,0);
            database.open();
            ArrayList<Riddle> r = database.getRiddlesByName("temporär");
            if(r != null) {
                for (int i = 0; i < r.size(); i++) {
                    database.deleteRiddle(r.get(i));
                }
            }
            database.setRiddleInDatabase(riddle);
            database.close();
            Intent intent = new Intent(this, CreateRiddleFinish.class);
            startActivity(intent);
            finish();
        }
    }
}
