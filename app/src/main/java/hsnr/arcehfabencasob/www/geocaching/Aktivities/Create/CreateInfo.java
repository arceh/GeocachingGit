package hsnr.arcehfabencasob.www.geocaching.Aktivities.Create;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import hsnr.arcehfabencasob.www.geocaching.Aktivities.Run.RiddleWin;
import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 05.01.2017.
 */

public class CreateInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_info);
    }

    protected void accept(View view){
        Intent intent = new Intent(this, CreateRiddleCps.class);
        startActivity(intent);
        finish();
    }


}
