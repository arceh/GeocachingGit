package hsnr.arcehfabencasob.www.geocaching.Aktivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import hsnr.arcehfabencasob.www.geocaching.DBS.RiddleDataSource;
import hsnr.arcehfabencasob.www.geocaching.DBS.User;
import hsnr.arcehfabencasob.www.geocaching.R;
import hsnr.arcehfabencasob.www.geocaching.GlobaleCordinaten.My_GPS;

public class MainActivity extends AppCompatActivity {

    boolean i = false;
    protected RiddleDataSource database = new RiddleDataSource(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View t = findViewById(R.id.main_Activity_Label);
        ((TextView) t).setText(R.string.dTitleMain);
        t = findViewById(R.id.user);
        ((EditText) t).setHint(R.string.dt1Main);
        t = findViewById(R.id.password);
        ((EditText) t).setHint(R.string.dt2Main);
        t = findViewById(R.id.loginButton);
        ((Button) t).setText(R.string.dLogin);
        t = findViewById(R.id.registryButton);
        ((Button) t).setText(R.string.dRgistry);
    }


    protected void registry(View view) {

        TextView error;
        error = (TextView) findViewById(R.id.errorField);
        EditText user;
        user = (EditText) findViewById(R.id.user);
        EditText pwd;
        pwd = (EditText) findViewById(R.id.password);
        user.setBackgroundColor(Color.argb(0,0,0,0));
        pwd.setBackgroundColor(Color.argb(0,0,0,0));

        switch (checkRegistry(user.getText().toString(),pwd.getText().toString())){
            case 0: error.setTextColor(Color.RED);
                error.setText(R.string.emptyName);
                user.setBackgroundColor(Color.argb(100,255,0,0));
                break;
            case 1: error.setTextColor(Color.RED);
                error.setText(user.getText().toString() + getString(R.string.alreadyExists));
                user.setBackgroundColor(Color.argb(100,255,0,0));
                break;
            case 2: error.setTextColor(Color.RED);
                error.setText(R.string.emptyPassword);
                pwd.setBackgroundColor(Color.argb(100,255,0,0));
                break;
            case 3: error.setTextColor(Color.GREEN);
                error.setText(user.getText().toString() + getString(R.string.registrySuccess));
                break;
            default: error.setTextColor(Color.RED);
                error.setText(R.string.unknowError);
        }

    }

    protected void login(View view) {
        TextView error;
        error = (TextView) findViewById(R.id.errorField);
        EditText user;
        user = (EditText) findViewById(R.id.user);
        EditText pwd;
        pwd = (EditText) findViewById(R.id.password);
        user.setBackgroundColor(Color.argb(0,0,0,0));
        pwd.setBackgroundColor(Color.argb(0,0,0,0));
        database.open();
        if(User.compareLoginCredentials(user.getText().toString(),pwd.getText().toString(),database)){
            Intent intent = new Intent(this,MainPage.class);
            startActivity(intent);
        } else {
            error.setTextColor(Color.RED);
            error.setText(R.string.wrongLogin);
        }
        database.close();
    }

    protected int checkRegistry(String username,String password){
        /* Datenbank:
            suche name in db => ist name leer return 0
                             => name schon vorhanden return 1
            lege neuen user in db an => passwort nicht aktzeptabel return 2
                                     => alles ok return 3
         */
        if(username.isEmpty()){
            return 0;
        }
        if(password.isEmpty()){
            return 2;
        }
        database.open();
        User user = new User(username, password);
        if(database.setUserInDatabase(user) == null){
            database.close();
            return 1;
        }
        database.close();
        return 3;
    }
}
