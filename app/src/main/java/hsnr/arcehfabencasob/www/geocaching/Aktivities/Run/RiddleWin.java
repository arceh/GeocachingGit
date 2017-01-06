package hsnr.arcehfabencasob.www.geocaching.Aktivities.Run;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hsnr.arcehfabencasob.www.geocaching.R;

/**
 * Created by Assares on 25.11.2016.
 */

public class RiddleWin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riddle_win);
        Bundle extras = getIntent().getExtras();
    }

    protected void rating(View view){
        //berechne rating und setzte neues rating
        //Intent intent = new Intent(this,MainPage.class);
        //startActivity(intent);
        finish(); //schließen der Activity
        return;
    }

}
