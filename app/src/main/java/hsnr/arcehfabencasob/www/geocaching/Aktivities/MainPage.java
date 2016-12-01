package hsnr.arcehfabencasob.www.geocaching.Aktivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import hsnr.arcehfabencasob.www.geocaching.DBS.Question;
import hsnr.arcehfabencasob.www.geocaching.DBS.Riddle;
import hsnr.arcehfabencasob.www.geocaching.DBS.RiddleDataSource;
import hsnr.arcehfabencasob.www.geocaching.GlobaleCordinaten.Coordinate;
import hsnr.arcehfabencasob.www.geocaching.GlobaleCordinaten.Map;
import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 03.11.2016.
 */

public class MainPage extends AppCompatActivity {

    protected RiddleDataSource database = new RiddleDataSource(this);

    //----------------------------------------------------
    protected Map map;
    public int bla = 1;
    HashMap<Integer, Question> q1 = new HashMap<>();
    //----------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        ArrayAdapter<String> adapter;
        ArrayList<String> listItem = new ArrayList<String>();

        database.open();
        listItem = database.getAllRiddleNames();
        database.close();

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItem);
        ListView container;
        container = (ListView) findViewById(R.id.main_page_contentList);
        container.setAdapter(adapter);
        initOnClick();

        //-----------------------
        map = new Map(this);
        //-----------------------
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.menuCreate:
                Toast.makeText(this, "Gibt es noch nicht", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuLogout:
                finish();
                return true;
            case R.id.dummy:
                fillDummy();
                return true;
            case R.id.cords:
                gpsTestenHeißtJetztDieFunktionWeilCarstenKappaKomischFandUndTrotzdemMochte();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initOnClick() {
        ListView ContactList = (ListView) findViewById(R.id.main_page_contentList);
        ContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView viewName = (TextView) view;
                String name = viewName.getText().toString();
                lookRiddle(name);

            }
        });
    }

    protected void lookRiddle(String name){
        Intent intent = new Intent(this,RiddleStart.class);
        intent.putExtra("riddleName", name);
        startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fillDummy() {

        database.open();
        LatLng test = map.getReQuestLatLng();
        if(bla == 1) {
            q1.put(1, new Question("Wie sieht ein Hund aus?", new Coordinate(test.latitude, test.longitude)));
        }
        if(bla == 2) {
            q1.put(2, new Question("Wie sieht eine Katze aus?", new Coordinate(test.latitude, test.longitude)));
        }
        if(bla == 3) {
            q1.put(3, new Question("Wie sieht eine Giraffe aus?", new Coordinate(test.latitude, test.longitude)));
            Riddle r1 = new Riddle("Rätsel der Erste", q1, "Wambomann", 5.0f);
            HashMap<Integer, Question> q2 = new HashMap<>();
            q2.put(1, new Question("Wer kam am 23.11.2016 zu spät zu EZS?", new Coordinate(69, 69.69)));
            q2.put(2, new Question("Wer wird 2017 Präsident der Vereinigten Staaten?", new Coordinate(666, 666.666)));
            q2.put(3, new Question("Wer trägt ein Bein von einem Vogel als Ohrring?", new Coordinate(78, 65.432)));
            q2.put(4, new Question("Wer spielt gerne mit Bällen?", new Coordinate(42.32, 42.42)));
            Riddle r2 = new Riddle("Rätsel der Zwei", q2, "Wambofrau", 5.0f);
            HashMap<Integer, Question> q3 = new HashMap<>();
            q3.put(1, new Question("Wo liegt der größte Hörsaal?", new Coordinate(51.3165,6.5715)));
            q3.put(2, new Question("Wird zur Übertragung von Drehung genutzt. ", new Coordinate(51.3165,6.5708)));
            q3.put(3, new Question("Wo sind wir grade?", new Coordinate(51.3163,6.5697)));
            q3.put(4, new Question("Wo befindet sich die nächste Werkstatt?", new Coordinate(51.3169,6.5697)));
            q3.put(5, new Question("Wo ist der nächste Parkplatzeingang?", new Coordinate(51.3175,6.5694)));
            Riddle r3 = new Riddle("Hochschule Führung", q3, "Armin Cehajic", 5.0f);
            database.setRiddleInDatabase(r1);
            database.setRiddleInDatabase(r2);
            database.setRiddleInDatabase(r3);
        }
        bla++;
        database.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void gpsTestenHeißtJetztDieFunktionWeilCarstenKappaKomischFandUndTrotzdemMochte(){
        LatLng test = map.getReQuestLatLng();
        Toast.makeText(this, test.toString(), Toast.LENGTH_LONG).show();
    }



}


