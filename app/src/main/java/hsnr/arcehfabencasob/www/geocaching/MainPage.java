package hsnr.arcehfabencasob.www.geocaching;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by Assares on 03.11.2016.
 */

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        setContentView(R.layout.main_page);

        TextView testen = null;
        Context context = findViewById(R.id.main_page_header).getContext();
        XmlPullParser parser = getResources().getXml(R.xml.element);
        AttributeSet attr = Xml.asAttributeSet(parser);


        testen = (TextView) onCreateView("1.Rätsel",context,attr);
        ListView container;
        container = (ListView) findViewById(R.id.contentList);
        container.addFooterView(testen);
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
}


