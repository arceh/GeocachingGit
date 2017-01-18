package hsnr.arcehfabencasob.www.geocaching.Aktivities.Run;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import hsnr.arcehfabencasob.www.geocaching.Aktivities.MainPage;
import hsnr.arcehfabencasob.www.geocaching.DBS.Riddle;
import hsnr.arcehfabencasob.www.geocaching.DBS.RiddleDataSource;
import hsnr.arcehfabencasob.www.geocaching.DBS.RiddleDbHelper;
import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 25.11.2016.
 */

public class RiddleWin extends AppCompatActivity {


    private String nameofRiddle,superuser;
    private int id,anzahl;
    private float superrating;
    private Riddle ridp;
    private RatingBar bar;
    protected RiddleDataSource database = new RiddleDataSource(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Aufbau der Aktivität
         * Übergebene Informationen abspeichern */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_win);
        nameofRiddle=getIntent().getExtras().getString("name");
        id=getIntent().getExtras().getInt("id");
        init();
    }

    protected void init(){
        /* Initialisieren der Variabeln */
        bar = (RatingBar) findViewById(R.id.ratingBar);
        database.open();
        ridp=database.getRiddleById(id);
        superrating=ridp.getRating();
        superuser=ridp.getCreatorName();
        anzahl=ridp.getRatingCount();
        database.close();
    }

    protected void rating(View view){
        //berechne rating und setzte neues rating
        float tmp=0;
        tmp=((anzahl*superrating)+bar.getRating())/(anzahl+1);
        ridp.setRating(tmp);
        //update wieder
        database.open();
        database.updateRiddle(ridp);
        database.close();
        finish(); //schließen der Activity
        return;
    }


}
