package hsnr.arcehfabencasob.www.geocaching.Aktivities.Create;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 05.01.2017.
 */

public class CreateRiddleCps extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_riddle_cps);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_riddle_menu, menu);
        return true;
    }
}
