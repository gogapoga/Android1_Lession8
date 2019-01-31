package ru.gb.android1_lession8;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CityWeather[] arrayCity = ((MyApplication) getApplication()).getCities().getArrayVisibleCity();
        LinearLayout cities = (LinearLayout) findViewById(R.id.cities);
        for (int i = 0; i < arrayCity.length; i++) {
            CardView cv = (CardView) LayoutInflater.from(cities.getContext())
                    .inflate(R.layout.item_city, cities, false);
            TextView tv = (TextView) cv.findViewById(R.id.text);
            tv.setText(arrayCity[i].getName());
            cv.setTag(i);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MyApplication) getApplication()).setSelectedCity((int)v.getTag());
                }
            });
            cities.addView(cv);
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MyApplication myApplication = (MyApplication) getApplication();
        if (myApplication.getSelectedStyle() == 0) menu.getItem(0).setChecked(false);
        else menu.getItem(1).setChecked(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MyApplication myApplication = (MyApplication) getApplication();
        switch (item.getItemId()) {
            case R.id.action_day:
                if (myApplication.getSelectedStyle() != 0) {
                    myApplication.setSelectedStyle(0);
                    myApplication.setStyle(new Day());
                    recreate();
                }
                return true;
            case R.id.action_night:
                if (myApplication.getSelectedStyle() != 1) {
                    myApplication.setSelectedStyle(1);
                    myApplication.setStyle(new Night());
                    recreate();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
