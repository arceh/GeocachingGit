package hsnr.arcehfabencasob.www.geocaching;

import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

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




        /*/
        GridView container = (GridView) findViewById(R.id.main_page_container);

        String [] header = {"_id", "Name", "Checkpoints", "Bewertung",};
        MatrixCursor cursor = new MatrixCursor(header);
        int [] layouts = {R.id.main_pasge_container};

        for (int i=0; i<5;i++){
            cursor.addRow(new Object[]{i, (i + "Rätsel"), i*4, i*7%4});
        }
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor,header,layouts);
        container.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void listClick(View view){
        Toast.makeText(this, view.toString(), Toast.LENGTH_LONG).show();
    }


}


