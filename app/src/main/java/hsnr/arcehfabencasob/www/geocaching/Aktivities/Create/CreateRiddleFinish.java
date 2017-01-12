package hsnr.arcehfabencasob.www.geocaching.Aktivities.Create;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 05.01.2017.
 */

public class CreateRiddleFinish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_riddle_finish);
    }

    protected void createRiddle (View view){
        finish();
    }

}
