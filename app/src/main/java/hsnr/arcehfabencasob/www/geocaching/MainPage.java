package hsnr.arcehfabencasob.www.geocaching;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Assares on 03.11.2016.
 */

public class MainPage extends AppCompatActivity {

    private RiddleDataSource database = new RiddleDataSource(this);


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
                fillDummy();
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

    private void fillDummy() {
        database.open();
        HashMap<Integer, Question> q1 = new HashMap<>();
        q1.put(0, new Question("Wie sieht ein Hund aus?", new Coordinate(5.4, 7.8)));
        q1.put(1, new Question("Wie sieht eine Katze aus?", new Coordinate(10.5, 0.8)));
        q1.put(2, new Question("Wie sieht eine Giraffe aus?", new Coordinate(4.2, 10.6)));
        Riddle r1 = new Riddle("Rätsel der Erste", q1, "Wambomann", 5.0f);
        HashMap<Integer, Question> q2 = new HashMap<>();
        q2.put(0, new Question("Was ist die Hauptstadt von Georgien?", new Coordinate(2.3, 5)));
        q2.put(1, new Question("Was ist die Hauptstadt von Dark Souls?", new Coordinate(9.2, 9.76)));
        q2.put(2, new Question("Was ist die Hauptstadt von Bielefeld?", new Coordinate(0, 0)));
        q2.put(3, new Question("Was ist die Hauptstadt von Armin?", new Coordinate(69, 69.69)));
        Riddle r2 = new Riddle("Rätsel der Erste", q2, "Wambofrau", 5.0f);
        HashMap<Integer, Question> q3 = new HashMap<>();
        q3.put(0, new Question("Wer kam am 23.11.2016 zu spät zu EZS?", new Coordinate(69, 69.69)));
        q3.put(1, new Question("Wer wird 2017 Präsident der Vereinigten Staaten?", new Coordinate(666, 666.666)));
        q3.put(2, new Question("Wer trägt ein Bein von einem Vogel als Ohrring?", new Coordinate(78, 65.432)));
        q3.put(3, new Question("Wer spielt gerne mit Bällen?", new Coordinate(42.32, 42.42)));
        q3.put(4, new Question("BEAAAARDS?", new Coordinate(32, 245.23)));
        Riddle r3 = new Riddle("Rätsel Bart", q3, "Bartfrau", 5.0f);
        database.setRiddleInDatabase(r1);
        database.setRiddleInDatabase(r2);
        database.setRiddleInDatabase(r3);
    }

}


