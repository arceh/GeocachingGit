package hsnr.arcehfabencasob.www.geocaching;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Assares on 03.11.2016.
 */

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        //*
        ArrayAdapter<String> adapter;
        ArrayList<String> listItem = new ArrayList<String>();
        listItem.add("1.Rätsel");
        listItem.add("2.Rätsel");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItem);
        ListView container;
        container = (ListView) findViewById(R.id.main_page_contentList);
        container.setAdapter(adapter);
        initOnClick();
        /*/
        GridView container = (GridView) findViewById(R.id.main_page_contentList);
        ArrayAdapter<String> adapter;
        ArrayList<String> listItem = new ArrayList<String>();
        listItem.add("1.Rätsel");
        listItem.add("2.Rätsel");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItem);
        container.setAdapter(adapter);
        initOnClick();
        //*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.menuCreate:
                Toast.makeText(this, "Gibt es noch nicht", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuLogout:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.dummy:
                //hier dummy daten hinzufügen
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void initOnClick() {
        ListView ContactList = (ListView) findViewById(R.id.main_page_contentList);
        ContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView viewName = (TextView) view;
                String name = viewName.getText().toString();
                startRiddle(name);
            }
        });
    }

    protected void startRiddle(String name){
        Intent intent = new Intent(this,RiddleStart.class);
        intent.putExtra("riddleName", name);
        startActivity(intent);
    }

}


