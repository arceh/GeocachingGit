package hsnr.arcehfabencasob.www.geocaching.Aktivities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import hsnr.arcehfabencasob.www.geocaching.R;

public class MainActivity extends AppCompatActivity {

    boolean i = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void registry(View view) {
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
                error.setText("Bitte geben sie einen Namen an");
                user.setBackgroundColor(Color.argb(100,255,0,0));
                break;
            case 1: error.setTextColor(Color.RED);
                error.setText(user.getText().toString() + " bereits registriert");
                user.setBackgroundColor(Color.argb(100,255,0,0));
                break;
            case 2: error.setTextColor(Color.RED);
                error.setText("Bitte geben sie ein gültiges Passwort an");
                pwd.setBackgroundColor(Color.argb(100,255,0,0));
                break;
            case 3: error.setTextColor(Color.GREEN);
                error.setText(user.getText().toString() + " wurde registriert");
                break;
            default: error.setTextColor(Color.RED);
                error.setText("Unbekannter Fehler bei Registrierung");
        }

    }

    public void login(View view) {
        TextView error;
        error = (TextView) findViewById(R.id.errorField);
        EditText user;
        user = (EditText) findViewById(R.id.user);
        EditText pwd;
        pwd = (EditText) findViewById(R.id.password);
        user.setBackgroundColor(Color.argb(0,0,0,0));
        pwd.setBackgroundColor(Color.argb(0,0,0,0));
        switch (checkLogin(user.getText().toString(),pwd.getText().toString())){
            case 0: error.setTextColor(Color.RED);
                error.setText("Anmeldung fehlgeschlagen \n Name oder Passwort falsch");
                break;
            case 1: error.setTextColor(Color.RED);
                error.setText("Anmeldung fehlgeschlagen \n Name oder Passwort falsch");
                break;
            case 2: Intent intent = new Intent(this,MainPage.class);
                startActivity(intent);
                break;
            default: error.setTextColor(Color.RED);
                error.setText("Unbekannter Fehler bei Login");
        }
    }

    protected int checkLogin(String Username,String password){
        /* Datenbank:
            suche nach name => falls nicht vorhanden return 0
            fordere hash-pwd für name an => stimmen hash nicht über ein return 1
                                         => stimmt hash über ein return 2
         */
        if(!Username.equals("Assares")){
            return 0;
        }
        if(password.equals("1234")){
            return 2;
        } else {
            return 1;
        }
    }

    protected int checkRegistry(String Username,String password){
        /* Datenbank:
            suche name in db => ist name leer return 0
                             => name schon vorhanden return 1
            lege neuen user in db an => passwort nicht aktzeptabel return 2
                                     => alles ok return 3
         */
        if(Username.isEmpty()){
            return 0;
        }
        if(Username.equals("Assares")){
            return 1;
        }
        if(password.isEmpty()){
            return 2;
        }
        return 3;
    }
}
