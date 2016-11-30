package hsnr.arcehfabencasob.www.geocaching.GlobaleCordinaten;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import hsnr.arcehfabencasob.www.geocaching.Aktivities.MainActivity;
import hsnr.arcehfabencasob.www.geocaching.Aktivities.MainPage;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;

public class Map implements ActivityCompat.OnRequestPermissionsResultCallback{

    MapView mapView;
    GoogleMap maps;
    Geocoder geo;
    String name;
    List<Address> kappa;
    private android.location.LocationListener locationListener;
    private LocationManager service;
    private double laenge, breite,sended;
    private boolean triggergps = false;
    Context that;
    double timeout;
    Thread t,pre;



    public Map(Context text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.onCreate(text);
        }
        that = text;

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Context context) {

        service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        locationListener = new android.location.LocationListener() {


            @Override
            public void onLocationChanged(Location location) {
                Log.e("test", String.valueOf(location.getLongitude()));
                laenge = location.getLongitude();
                breite = location.getLatitude();
                triggergps=true;
                //LatLng mypos = new LatLng(breite, laenge);


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider)
            {
                Log.e("provider", provider);
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //getReQuest(service, locationListener);
    }
    private void permissioncheck(){
        if (ActivityCompat.checkSelfPermission(that, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(that, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) that,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            },10);
            Log.e("test","bin raus");


        }
    }


    /** Wichtig für die Verwendung**/
    @RequiresApi(api = Build.VERSION_CODES.M)
    public LatLng getReQuestLatLng() {
        t = new Thread(){

            @RequiresApi(api = Build.VERSION_CODES.M)
            public void run(){
                double tmp=System.currentTimeMillis()-timeout;
                while(!triggergps && tmp<3000) {
                    getReQuest(service, locationListener);
                    tmp=System.currentTimeMillis()-timeout;
                }

            }
        };
        pre= new Thread(){
          public void run(){
            if(ActivityCompat.checkSelfPermission(that, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(that, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissioncheck();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
          }
        };
        pre.start();
        try {
            pre.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ActivityCompat.checkSelfPermission(that, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(that, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getReQuest(service, locationListener);
            breite = service.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            laenge = service.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
            getReQuest(service, locationListener);
            if (!triggergps) {
                timeout = System.currentTimeMillis();
                t.start();
            } else {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
           while(t.isAlive()){

           }
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LatLng l = new LatLng(breite, laenge);
            if (ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            service.removeUpdates(locationListener);
            triggergps = false;
            return l;
        }
        else{
            return new LatLng(0,0);
        }

    }
    /** Wichtig für die Verwendung**/
    public  float getDistanz(LatLng x,LatLng y){
        float[] m=new float[1];
        Location.distanceBetween(x.latitude,x.longitude,y.latitude,y.longitude,m);

        return m[0];
    }
    /** Wichtig für die Verwendung**/
    public boolean compareCoords(LatLng x,LatLng y,int z){

        if(getDistanz(x,y)<=z){
            return true;
        }else {

            return false;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
   private  void getReQuest(LocationManager lm, android.location.LocationListener ll) {
        if (ActivityCompat.checkSelfPermission(that, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(that, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) that,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET
            },10);
            try {

            } catch(Exception ex) {

            }
            Log.e("test","bin raus");
            return;
        }
        else {


                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

                }



        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if(requestCode==10){
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && (ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                }
                else{
                    permissioncheck();
                }
            }
    }
}

