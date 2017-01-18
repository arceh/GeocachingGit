package hsnr.arcehfabencasob.www.geocaching.Aktivities.Create;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
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
            /* Abfragen der Aktuellen Position */
            final LatLng res = map.gpsPosAlt();   /*Todo : Hier Koordinaten anfragen*/
            final Coordinate coord;
            if(res==null){
                        /*Fehler behandlung Hier
                        * Ein Toast wird schon geworfen
                        * Wird nie aufgerufen*/
               coord=null;
            }
            else {
                 coord = new Coordinate(res.latitude, res.longitude);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(coord!=null)
                        nextQuestion(coord);
                    Button btn = (Button) findViewById(R.id.create_riddle_cps_button);
                    btn.setVisibility(View.VISIBLE);


                }
            });
        }
    };
    public MyThread getGps = new MyThread(gpsThread);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Aufbau der Aktivität
         * Übergebene Informationen abspeichern */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_riddle_cps);
        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");
        init();
    }

    protected void init(){
        /* Initialisieren der Variabeln */
        anzCp = 0;
        cpField = (TextView) findViewById(R.id.create_riddle_cps_anz);
        questionField = (EditText) findViewById(R.id.create_riddle_cps_question);
        nextButton = (Button) findViewById(R.id.create_riddle_cps_button);
        cpField.setText(getString(R.string.createRiddleCpsCP) + anzCp);
        map = My_GPS.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Erstellen des Options-Menü */
        getMenuInflater().inflate(R.menu.create_riddle_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Auslösen von Optionen im Menü */
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void nextQuestionStart(View view){
        /* Abfragen der Rechte für GPS und ermitteln der Position */
        boolean rights;
        Button btn = (Button) findViewById(R.id.create_riddle_cps_button);
        btn.setVisibility(View.GONE);
        rights = map.permissioncheck(2);
        if (rights) {
            new MyThread(gpsThread).start();
        }
        return;
    }

    protected void nextQuestion(Coordinate coords){
        /* Speichern der Aktuellen Frage und die da zugehörige Antwort
        *  Zurücksetzten der Felder */

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
        /* Laden der letzten Frage */
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
        /* Starten der Aktivität zum Abschließen des erstell Vorgang
        *  speichert Rätsel in Datenbank mit temporären Namen*/
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
            intent.putExtra("anzCp",anzCp);
            map.gpsAus();
            startActivity(intent);
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /* Kontrollieren ob Rechte zugelassen wurden */
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                new MyThread(gpsThread).start();
            } else {
                map.permissioncheck(2);
            }
        }
    }
}
