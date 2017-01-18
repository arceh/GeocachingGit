package hsnr.arcehfabencasob.www.geocaching.Aktivities.Run;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import hsnr.arcehfabencasob.www.geocaching.Aktivities.MyThread;
import hsnr.arcehfabencasob.www.geocaching.DBS.Riddle;
import hsnr.arcehfabencasob.www.geocaching.DBS.RiddleDataSource;
import hsnr.arcehfabencasob.www.geocaching.GlobaleKoordinaten.My_GPS;
import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 24.11.2016.
 */

public class RiddleRun extends AppCompatActivity {
    int distanzToTarget = 20;
    int cpAkt = 1;
    int cpAnz = 1;
    String name = "";
    String question = "";
    LatLng answer;
    protected RiddleDataSource database = new RiddleDataSource(this);
    Riddle riddle;
    int id;
    protected My_GPS map;
    private Thread gpsThread = new Thread() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void run() {
            /* Abfragen der Aktuellen Position */
            final LatLng res = map.getReQuestLatLng();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    nextCpPlus(res);
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
        setContentView(R.layout.riddle_run);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        cpAnz = extras.getInt("cp");
        init();
    }

    protected void init(){
        /* Initialisieren der Variabeln */
        database.open();
        riddle = database.getRiddlesByName(name).get(0);
        database.close();
        id = riddle.getId();
        question = riddle.getQuestions().get(cpAkt).getQuestion().toString();
        LatLng tmp = new LatLng(riddle.getQuestions().get(cpAkt).getAnswer().x, riddle.getQuestions().get(cpAkt).getAnswer().y);
        answer = tmp;
        TextView nameView = (TextView) findViewById(R.id.riddle_run_name);
        nameView.setText(name);
        TextView cpView = (TextView) findViewById(R.id.riddle_run_cp);
        cpView.setText(getString(R.string.checkpoint) + " " + cpAkt + "/" + cpAnz);
        TextView questionView = (TextView) findViewById(R.id.riddle_run_riddle);
        questionView.setText(question);
        map = My_GPS.getInstance(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void nextCp(View view) {
        /* Abfragen der Rechte und Position beziehen */
        boolean rights;
        rights = map.permissioncheck(1);
        if (rights) {
            Button btn = (Button) findViewById(R.id.riddle_run_next);
            btn.setVisibility(View.GONE);
            new MyThread(gpsThread).start();
        }
    }

    protected void nextCpPlus(LatLng temp) {
        /* Vergleichen der Aktuellen Position mit der Antwort */
        Button btn = (Button) findViewById(R.id.riddle_run_next);
        btn.setVisibility(View.VISIBLE);
        if (map.compareCoords(answer, temp, distanzToTarget)) {
            if (cpAkt >= cpAnz) {
                Intent intent = new Intent(this, RiddleWin.class);
                intent.putExtra("name", name);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
                return;

            } else {
                cpAkt++;
                TextView cpView = (TextView) findViewById(R.id.riddle_run_cp);
                cpView.setText(getString(R.string.checkpoint) + " " + cpAkt + "/" + cpAnz);
                question = riddle.getQuestions().get(cpAkt).getQuestion().toString();
                LatLng tmp = new LatLng(riddle.getQuestions().get(cpAkt).getAnswer().x, riddle.getQuestions().get(cpAkt).getAnswer().y);
                answer = tmp;
                TextView questionView = (TextView) findViewById(R.id.riddle_run_riddle);
                questionView.setText(question);
            }
        } else {
            Toast.makeText(this, R.string.wrongPos, Toast.LENGTH_LONG).show();
            //Toast.makeText(this, String.valueOf(map.getDistanz(answer, temp)), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /* Kontrollieren ob Rechte zugelassen wurden */
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                Button btn = (Button) findViewById(R.id.riddle_run_next);
                btn.setVisibility(View.GONE);
                new MyThread(gpsThread).start();
            } else {
                map.permissioncheck(1);
            }
        }
    }
}