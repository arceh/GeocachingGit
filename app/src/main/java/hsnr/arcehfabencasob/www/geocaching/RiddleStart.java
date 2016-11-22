package hsnr.arcehfabencasob.www.geocaching;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Assares on 22.11.2016.
 */

//Unterordner für klassen?
//Listen layout mit mehreren Elementen?

public class RiddleStart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_start);
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("riddleName");
        TextView nameView = (TextView) findViewById(R.id.riddle_start_name);
        nameView.setText(name);
        String kappa = "kappa";
        Toast.makeText(this, kappa, Toast.LENGTH_LONG).show();
    }

}
