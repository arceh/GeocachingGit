package hsnr.arcehfabencasob.www.geocaching.GlobaleKoordinaten;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hsnr.arcehfabencasob.www.geocaching.R;

import static android.content.Context.LOCATION_SERVICE;

public class My_GPS{

    MapView mapView;
    GoogleMap maps;
    Geocoder geo;
    String name;
    List<Address> kappa;
    private android.location.LocationListener locationListener,locationClient;
    private LocationManager service;
    private Double laenge, breite,breiteclient,laengeclient;
    private boolean triggergps = false;
    Context that;
    double timeout;
   private ArrayList<LatLng> superposition;
    private Location gg,ww;
    private String test;
    private static My_GPS Instance=null;
    public static My_GPS getInstance(Context text){
        if(Instance==null){
            Instance= new My_GPS(text);
        }
            return Instance;
    }



    private My_GPS(Context text) {
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
    public static boolean permissioncheck(Context text,int permiss){
        if (ActivityCompat.checkSelfPermission(text, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(text, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) text,new String[]{
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void gpsAn(){
        getReQuest(service,locationListener);
    }

    public void gpsAus(){
        if (ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        service.removeUpdates(locationListener);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public LatLng gpsPosAlt(){
        timeout = System.currentTimeMillis();
        double tmp=System.currentTimeMillis()-timeout;
        LatLng g =null;
        if(Build.MANUFACTURER.equals("unknown") && Build.BRAND.equals("Android")){
            if (ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            breite = service.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            laenge = service.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
            g= new LatLng(breite,laenge);
        }


        while(!triggergps && tmp<7000) {
            getReQuestWIfi(service,locationClient);
            getReQuest(service, locationListener);
            tmp=System.currentTimeMillis()-timeout;

            LatLng w=null;
            if(gg==null && ww== null){

            }else if (gg != null && ww==null){
                g = new LatLng(gg.getLatitude(), gg.getLongitude());
            }else if (ww != null && gg== null) {
                w = new LatLng(ww.getLatitude(), ww.getLongitude());
            }else if (ww != null && gg!= null) {
                g = new LatLng(ww.getLatitude(), ww.getLongitude());
                w = new LatLng(ww.getLatitude(), ww.getLongitude());
            }
            if (wifiIsBetter(gg, ww)) {
                if(w!=null)
                    superposition.add(w);
            } else {
                if(g!=null)
                    superposition.add(g);
            }
            ww = null;
            gg = null;
            triggergps = false;
        }
        LatLng l = sortinghaufen();
        if(l==null){
            Toast.makeText(that, R.string.Fehlergps, Toast.LENGTH_LONG).show();
            superposition.clear();
            return null;
        }else{
            superposition.clear();
            return l;
        }


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
                    if(gg!=null) {
                        g = new LatLng(gg.getLatitude(), gg.getLongitude());

                    }
                    LatLng w=null;
                    if(ww!=null)
                        w= new LatLng(ww.getLatitude(),ww.getLongitude());
                    if(wifiIsBetter(gg,ww)){
                        if(ww!=null)
                            superposition.add(w);
                    }
                    else {
                        superposition.add(g);
                    }

                }

            }
            LatLng l = sortinghaufen();
        if(l==null){
            superposition.clear();
            l = new LatLng(breite,laenge);
        }

            if (ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(that, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            service.removeUpdates(locationListener);
            triggergps = false;
            ww=null;
            gg=null;
            superposition.clear();
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

