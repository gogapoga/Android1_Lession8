package ru.gb.android1_lession8;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication myApplication = (MyApplication)getApplication();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        switch(myApplication.getNumberView()){
            case 0:
                CitesView();
                break;
            case 1:
                WeatherView();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MyApplication myApplication = (MyApplication) getApplication();
        if (myApplication.getSelectedStyle() == 0) menu.getItem(0).setChecked(true);
        if (myApplication.getSelectedStyle() == 1) menu.getItem(1).setChecked(true);
        if (myApplication.isShowWind()) menu.getItem(2).setChecked(true);
        if(myApplication.isShowHumidity()) menu.getItem(3).setChecked(true);
        if(myApplication.isShowPressure()) menu.getItem(4).setChecked(true);
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
            case R.id.checkWind:
                item.setChecked(!item.isChecked());
                myApplication.setShowWind(item.isChecked());
                if(myApplication.getNumberView() == 1) WeatherView();
                return true;
            case R.id.checkHumidity:
                item.setChecked(!item.isChecked());
                myApplication.setShowHumidity(item.isChecked());
                if(myApplication.getNumberView() == 1) WeatherView();
                return true;
            case R.id.checkPressure:
                item.setChecked(!item.isChecked());
                myApplication.setShowPressure(item.isChecked());
                if(myApplication.getNumberView() == 1) WeatherView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void CitesView() {
        final MyApplication myApplication = (MyApplication)getApplication();
        setTitle(makeTitle(getString(R.string.wordWeather)));
        CityWeather[] arrayCity = myApplication.getCities().getArrayVisibleCity();
        LinearLayout layout = (LinearLayout) findViewById(R.id.cities);
        layout.removeAllViews();
        layout.setBackgroundResource(myApplication.getStyle().getBackground());
        for (int i = 0; i < arrayCity.length; i++) {
            CardView cv = (CardView) LayoutInflater.from(layout.getContext())
                    .inflate(R.layout.item_city, layout, false);
            TextView tv = (TextView) cv.findViewById(R.id.text);
            tv.setText(arrayCity[i].getName());
            cv.setTag(i);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myApplication.setSelectedCity((int)v.getTag());
                    myApplication.setNumberView(1);
                    WeatherView();
                }
            });
            layout.addView(cv);
        }
    }

    private void WeatherView() {
        MyApplication myApplication = (MyApplication)getApplication();
        setTitle(makeTitle(myApplication.getCities().getArrayVisibleCity()[myApplication.getSelectedCity()].getName()));
        CityWeather city = myApplication.getCities().getArrayVisibleCity()[myApplication.getSelectedCity()];
        LinearLayout layout = (LinearLayout) findViewById(R.id.cities);
        layout.removeAllViews();
        layout.setBackgroundResource(myApplication.getStyle().getBackground());
        city.loadWeather();
        for (int i = 0; i < 24; i++) {
            CardView cv = (CardView) LayoutInflater.from(layout.getContext())
                    .inflate(R.layout.item_weather, layout, false);
            TextView tv = (TextView) cv.findViewById(R.id.text);
            StringBuilder str = new StringBuilder(getString(R.string.wordTime));
            str.append(i);
            str.append(":00\n");
            str.append(getString(R.string.wordTemperature));
            int t = city.getTemp(i);
            if (t > 0) str.append("+");
            str.append(t);
            str.append(getString(R.string.wordC));
            t = city.getHumidity(i);
            if (myApplication.isShowHumidity()) {
                str.append("\n");
                str.append(getString(R.string.wordHumidity));
                str.append(": ");
                str.append(t);
                str.append("%");
            }
            if (myApplication.isShowWind()) {
                str.append("\n");
                str.append(getString(R.string.wordWind));
                str.append(": ");
                str.append(city.getWind(i));
                str.append(getString(R.string.wordMC));
            }
            if (myApplication.isShowPressure()) {
                str.append("\n");
                str.append(getString(R.string.wordPressure));
                str.append(": ");
                str.append(city.getPressure(i));
                str.append(getString(R.string.wordMMPTST));
            }
            tv.setText(str.toString());
            ImageView iv = (ImageView) cv.findViewById(R.id.icon);
            Drawable img;
            if (t < 40) img = getDrawable(R.drawable.sun);
            else if (t > 80) img = getDrawable(R.drawable.cloud_r);
            else img = getDrawable(R.drawable.cloud);
            iv.setImageDrawable(img);
            layout.addView(cv);
        }
    }

    @Override
    public void onBackPressed() { //обработка нажатия кнопки назад
        MyApplication myApplication = (MyApplication) getApplication();
        if (myApplication.getNumberView() == 1) {
            CitesView();
            myApplication.setNumberView(0);
        }
    }

    private String makeTitle(String cityName) { //создание заголовка
        StringBuilder str = new StringBuilder("");
        str.append(cityName);
        str.append("     ");
        SimpleDateFormat sdf = new SimpleDateFormat("E  d MMM");
        str.append(sdf.format(Calendar.getInstance().getTime()));
        return str.toString();
    }

}
