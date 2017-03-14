package hsnr.arcehfabencasob.www.geocaching.Aktivities;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import hsnr.arcehfabencasob.www.geocaching.Aktivities.Create.CreateInfo;
import hsnr.arcehfabencasob.www.geocaching.Aktivities.Create.CreateRiddleCps;
import hsnr.arcehfabencasob.www.geocaching.Aktivities.Run.RiddleStart;
import hsnr.arcehfabencasob.www.geocaching.DBS.Question;
import hsnr.arcehfabencasob.www.geocaching.DBS.Riddle;
import hsnr.arcehfabencasob.www.geocaching.DBS.RiddleDataSource;
import hsnr.arcehfabencasob.www.geocaching.GlobaleKoordinaten.Coordinate;
import hsnr.arcehfabencasob.www.geocaching.GlobaleKoordinaten.My_GPS;
import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 03.11.2016.
 */

public class MainPage extends AppCompatActivity {

    protected RiddleDataSource database = new RiddleDataSource(this);
    protected String user;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Aufbau der Aktivität
         * Übergebene Informationen abspeichern */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void init(){
        /* Dummydaten setzen und temporäre Rätsel aus der Datenbank löschen */
        fillDummy();
        database.open();
        ArrayList<Riddle> r = database.getRiddlesByName("temporär");
        if(r != null) {
            for (int i = 0; i < r.size(); i++) {
                database.deleteRiddle(r.get(i));
            }
        }
        database.close();
        refreshRiddles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Aufbau des Options-Menü */
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Auslösen von Optionen im Menü */
        int id = item.getItemId();
        switch(id) {
            case R.id.menuCreate:
                createRiddle();
                return true;
            case R.id.menuLogout:
                finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initOnClick() {
        /* Hinzufügen der Auslöser
         * für die Elemente der Liste */
        ListView ContactList = (ListView) findViewById(R.id.main_page_contentList);
        ContactList.setOnItemClickListener(     new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView viewName = (TextView) view;
                String name = viewName.getText().toString();
                lookRiddle(name);
            }
        });
    }

    protected void refreshRiddles(){
        /* Füllen der Liste mit allen Rätseln */
        ArrayAdapter<String> adapter;
        ArrayList<String> listItem = new ArrayList<>();
        database.open();
        listItem = database.getAllRiddleNames();
        database.close();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItem);
        ListView container;
        container = (ListView) findViewById(R.id.main_page_contentList);
        container.setAdapter(adapter);
        initOnClick();
    }

    protected void lookRiddle(String name){
        /* Starten der Aktivität zum Spielen eines Rätsels */
        Intent intent = new Intent(this,RiddleStart.class);
        intent.putExtra("riddleName", name);
        startActivity(intent);
    }

    protected void createRiddle(){
        /* Starten der Aktivität zum erstellen eines Rätsels */
        Intent intent = new Intent(this, CreateInfo.class);
        intent.putExtra("user", user);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        /* Erneuert die Liste beim Abschluss der Create-Aktivität */
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            refreshRiddles();
        }
    }
    //----------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fillDummy() {
        /* Fügt der Datenbank ein Standart Rätsel hinzu */
        database.open();
        ArrayList<Riddle> r = database.getRiddlesByName("Hochschule Führung");
        if(r == null) {
            HashMap<Integer, Question> q3 = new HashMap<>();
            q3.put(1, new Question("Wo liegt der größte Hörsaal?", new Coordinate(51.3165, 6.5715)));
            q3.put(2, new Question("Wird zur Übertragung von Drehung genutzt. ", new Coordinate(51.3165, 6.5708)));
            q3.put(3, new Question("Wo sind wir grade?", new Coordinate(51.3163, 6.5697)));
            q3.put(4, new Question("Wo befindet sich die nächste Werkstatt?", new Coordinate(51.3169, 6.5697)));
            q3.put(5, new Question("Wo ist der nächste Parkplatzeingang?", new Coordinate(51.3175, 6.5694)));
            Riddle r3 = new Riddle("Hochschule Führung", q3, "Der Admin", 5.0f, 42);
            database.setRiddleInDatabase(r3);
        }
        database.close();
    }



}