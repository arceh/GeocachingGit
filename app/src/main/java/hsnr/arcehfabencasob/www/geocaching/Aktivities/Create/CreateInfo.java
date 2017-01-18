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
import android.view.View;

import hsnr.arcehfabencasob.www.geocaching.GlobaleKoordinaten.My_GPS;
import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 05.01.2017.
 */

public class CreateInfo extends AppCompatActivity {

    protected String user;
    protected My_GPS gp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Aufbau der Aktivität
         * Übergebene Informationen abspeichern  */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_info);
        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");
        gp = My_GPS.getInstance(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void accept(View view) {
        /* Starten der Aktivität zum Erstellen eines Rätsels */
        Intent intent = new Intent(this, CreateRiddleCps.class);
        //gp.gpsAn();
        gp.permissioncheck(this, 3);
        intent = new Intent(this, CreateRiddleCps.class);
        intent.putExtra("user",user);
        gp.gpsAn();
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 3) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {


            } else {
                gp.permissioncheck(this,3);
            }
        }
    }
}