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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hsnr.arcehfabencasob.www.geocaching.Aktivities.MainActivity;
import hsnr.arcehfabencasob.www.geocaching.Aktivities.MainPage;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;

public class My_GPS{

    MapView mapView;
    GoogleMap maps;
    Geocoder geo;
    String name;
    List<Address> kappa;
    private android.location.LocationListener locationListener,locationClient;
    private LocationManager service;
    private double laenge, breite,breiteclient,laengeclient;
    private boolean triggergps = false;
    Context that;
    double timeout;
   private ArrayList<LatLng> superposition;
    private Location gg,ww;



    public My_GPS(Context text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.onCreate(text);
        }
        that = text;

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Context context) {

        service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        superposition= new ArrayList<LatLng>();
        locationListener = new android.location.LocationListener() {


            @Override
            public void onLocationChanged(Location location) {
                Log.e("test", String.valueOf(location.getLongitude()));
                breite=location.getLatitude();
                laenge=location.getLongitude();
                gg=location;
                triggergps=false;
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
        locationClient= new android.location.LocationListener(){

            @Override
            public void onLocationChanged(Location location) {

                ww=location;
                breiteclient=location.getLatitude();
                laengeclient=location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //getReQuest(service, locationListener);
    }
    public boolean permissioncheck(int permiss){
        if (ActivityCompat.checkSelfPermission(that, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(that, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) that,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            },permiss);

            return false;


        }
        return true;
    }

    private LatLng sortinghaufen(){
        int median;
        Collections.sort(superposition, new Comparator<LatLng>() {
            @Override
            public int compare(LatLng o1, LatLng o2) {
                if(o1.latitude<=o2.latitude && o1.longitude<=o2.longitude){
                    return -1;
                }
                else if(o1.latitude<=o2.latitude && o1.longitude>o2.latitude){
                    return -1;
                }
                else if (o1.latitude>o2.latitude && o1.longitude<=o2.longitude){
                    return 1;
                }
                else if(o1.latitude>o2.latitude && o1.longitude>o2.longitude){
                    return 1;
                }
                else {
                    return 0;
                }
            }
        });
        median=superposition.size()-1;
        if(median>-1){
            if(median%2==0){
                median=median/2;
            }
            else{
                median=(median+1)/2;
            }
            return superposition.get(median);

        }
        return null;
    }
    private boolean wifiIsBetter(Location g,Location w){
        float t=0;
        if(g!=null && w!=null) {
            t = g.getAccuracy() - w.getAccuracy();
        }
        if(g!=null && w==null)
            return false;
        if(w!=null && g==null)
            return true;

        if(t<0){
            return true;
        }
        return false;
    }

    /** Wichtig für die Verwendung**/
    @RequiresApi(api = Build.VERSION_CODES.M)
    public LatLng getReQuestLatLng() {
            getReQuest(service, locationListener);
            breite = service.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            laenge = service.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
            getReQuest(service, locationListener);
            if (!triggergps) {
                timeout = System.currentTimeMillis();
                double tmp=System.currentTimeMillis()-timeout;
                while(!triggergps && tmp<8000) {
                    while(tmp<1000){
                        tmp=System.currentTimeMillis()-timeout;
                    }
                    getReQuestWIfi(service,locationClient);
                    getReQuest(service, locationListener);
                    tmp=System.currentTimeMillis()-timeout;
                    LatLng g= new LatLng(breite,laenge);
                    if(gg!=null)
                        g=new LatLng(gg.getLatitude(),gg.getLongitude());
                    LatLng w=new LatLng(breiteclient,laengeclient);
                    if(ww!=null)
                        w= new LatLng(ww.getLatitude(),ww.getLongitude());
                    if(wifiIsBetter(gg,ww)){
                        superposition.add(w);
                    }
                    else {
                        superposition.add(g);
                    }

                }

            }
            LatLng l = sortinghaufen();
        if(l==null){
            l = new LatLng(breite,laenge);
        }

            if (ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            service.removeUpdates(locationListener);
            triggergps = false;
            return l;


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

        }
        else {
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

                }



        }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private  void getReQuestWIfi(LocationManager lm, android.location.LocationListener ll) {
        if (ActivityCompat.checkSelfPermission(that, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(that, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        else {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll);

        }



    }
}

