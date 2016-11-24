package hsnr.arcehfabencasob.www.geocaching;

        import android.Manifest;
        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.location.Address;
        import android.location.Geocoder;
        import android.location.Location;
        import android.location.LocationManager;
        import android.os.Build;
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

public class Map extends AppCompatActivity{

    MapView mapView;
    GoogleMap maps;
    Geocoder geo;
    String name;
    List<Address> kappa;
    private android.location.LocationListener locationListener;
    private LocationManager service;
    private double laenge,breite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        final Context that=this;
                service = (LocationManager) getSystemService(LOCATION_SERVICE);
                locationListener = new android.location.LocationListener() {


                    @Override
                    public void onLocationChanged(Location location) {
                        Log.e("test", String.valueOf(location.getLongitude()));
                        laenge = location.getLongitude();
                        breite = location.getLatitude();
                        //LatLng mypos = new LatLng(breite, laenge);


                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Log.e("provider",provider);
                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };

               // getReQuest(service, locationListener);
            }

    /** Wichtig für die Verwendung**/
    @RequiresApi(api = Build.VERSION_CODES.M)
     public  LatLng getReQuestLatLng(){
        getReQuest(service,locationListener);
        LatLng l=new LatLng(breite,laenge);

        return l;
    }
    /** Wichtig für die Verwendung**/
    public  float getDistanz(LatLng x,LatLng y){
        float[] m=new float[1];
        Location.distanceBetween(x.latitude,x.longitude,y.latitude,y.longitude,m);

        return m[0];
    }
    /** Wichtig für die Verwendung**/
    public boolean compareCoords(LatLng x,LatLng y){

        if(x.latitude==y.latitude && x.longitude==y.longitude){
            return true;
        }else {

            return false;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
   private  void getReQuest(LocationManager lm, android.location.LocationListener ll) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET
            },10);
            Log.e("test","bin raus");
            return;
        }
        else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);


        }

    }
    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }
}

