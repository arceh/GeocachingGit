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
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

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

    //----------------------------------------------------
    protected My_GPS map;
    //----------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        View t = findViewById(R.id.main_page_header);
        ((TextView) t).setText(R.string.dTitleMP);

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
        map = new My_GPS(this);
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

        if(!map.permissioncheck(2)){
            return;
        }
        database.open();
        HashMap<Integer, Question> q3 = new HashMap<>();
        q3.put(1, new Question("Wo liegt der größte Hörsaal?", new Coordinate(51.3165,6.5715)));
        q3.put(2, new Question("Wird zur Übertragung von Drehung genutzt. ", new Coordinate(51.3165,6.5708)));
        q3.put(3, new Question("Wo sind wir grade?", new Coordinate(51.3163,6.5697)));
        q3.put(4, new Question("Wo befindet sich die nächste Werkstatt?", new Coordinate(51.3169,6.5697)));
        q3.put(5, new Question("Wo ist der nächste Parkplatzeingang?", new Coordinate(51.3175,6.5694)));
        Riddle r3 = new Riddle("Hochschule Führung", q3, "Armin Cehajic", 5.0f);
        database.setRiddleInDatabase(r3);
        database.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void gpsTestenHeißtJetztDieFunktionWeilCarstenKappaKomischFandUndTrotzdemMochte(){
        boolean testen;
        testen = map.permissioncheck(1);
        if(testen){
            LatLng test = map.getReQuestLatLng();
            Toast.makeText(this, test.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                LatLng test = map.getReQuestLatLng();
                Toast.makeText(this, test.toString(), Toast.LENGTH_LONG).show();
            }
            else{
                map.permissioncheck(1);
            }
        }
    }



}


