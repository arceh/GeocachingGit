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

    protected String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_info);
        Bundle extras = getIntent().getExtras();
        String user = extras.getString("user");
    }

    protected void accept(View view){
        Intent intent = new Intent(this, CreateRiddleCps.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }


}
