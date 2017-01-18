package hsnr.arcehfabencasob.www.geocaching.Aktivities.Create;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import hsnr.arcehfabencasob.www.geocaching.Aktivities.Run.RiddleWin;
import hsnr.arcehfabencasob.www.geocaching.GlobaleKoordinaten.My_GPS;
import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 05.01.2017.
 */

public class CreateInfo extends AppCompatActivity {

    protected String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Aufbau der Aktivität
         * Übergebene Informationen abspeichern  */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_info);
        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void accept(View view){
        /* Starten der Aktivität zum Erstellen eines Rätsels */
        Intent intent = new Intent(this, CreateRiddleCps.class);
        My_GPS gp= My_GPS.getInstance(this);
        //gp.gpsAn();
        gp.permissioncheck(this,1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        gp.gpsAn();
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }
}
