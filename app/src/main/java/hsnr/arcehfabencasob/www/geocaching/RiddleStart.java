package hsnr.arcehfabencasob.www.geocaching;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Assares on 22.11.2016.
 */

//Unterordner für klassen?
//Listen layout mit mehreren Elementen?

public class RiddleStart extends AppCompatActivity {
    protected Map map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_start);
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("riddleName");
        TextView nameView = (TextView) findViewById(R.id.riddle_start_name);
        nameView.setText(name);
        map = new Map(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void kappa(View view){
        Map map = new Map(view);
        LatLng test = map.getReQuestLatLng();

        TextView testview = (TextView) findViewById(R.id.riddle_start_author);
        testview.setText(test.toString());
    }

}
