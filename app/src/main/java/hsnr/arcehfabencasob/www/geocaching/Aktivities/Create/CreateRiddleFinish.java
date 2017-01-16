package hsnr.arcehfabencasob.www.geocaching.Aktivities.Create;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import hsnr.arcehfabencasob.www.geocaching.DBS.Question;
import hsnr.arcehfabencasob.www.geocaching.DBS.Riddle;
import hsnr.arcehfabencasob.www.geocaching.DBS.RiddleDataSource;
import hsnr.arcehfabencasob.www.geocaching.GlobaleKoordinaten.My_GPS;
import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 05.01.2017.
 */

public class CreateRiddleFinish extends AppCompatActivity {
    protected RiddleDataSource database = new RiddleDataSource(this);
    protected Riddle riddle;
    protected ArrayList<Riddle> riddleArray;
    protected EditText nameField;
    protected TextView cpField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Speichern der Aktuellen Frage und die da zugehörige Antwort
        *  Zurücksetzten der Felder */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_riddle_finish);
        Bundle extras = getIntent().getExtras();
        int anzCp = extras.getInt("anzCp",0);
        init(anzCp);
    }

    protected void init(int anzCp){
        /* Initialisieren der Variablen
        *  erstelltes Rätsel aus der Datenbank holen*/
        database.open();
        riddleArray = database.getRiddlesByName("temporär");
        if(riddleArray != null) {
            riddle = riddleArray.get(0);
            database.deleteRiddle(riddleArray.get(0));
        }
        database.close();
        nameField = (EditText) findViewById(R.id.create_riddle_finish_name);
        cpField  = (TextView) findViewById(R.id.create_riddle_finish_cp);
        cpField.setText(String.valueOf(anzCp));
    }

    protected void createRiddle (View view){
        /* Abschließen des Rätsels
        *  Speichern in Datenbank*/
        String name = nameField.getText().toString();
        riddle.setRiddleName(name);
        database.open();
        database.setRiddleInDatabase(riddle);
        database.close();
        finish();
    }

}
