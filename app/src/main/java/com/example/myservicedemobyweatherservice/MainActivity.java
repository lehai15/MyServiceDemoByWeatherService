package com.example.myservicedemobyweatherservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean binded = false;
    private WeatherService weatherService;
    private TextView textViewWeather;
    private EditText editTextLocation;
    private Button buttonWeather;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WeatherService.LocalWeatherBinder binder = (WeatherService.LocalWeatherBinder) service;
            weatherService = binder.getService();
            binded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binded = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textViewWeather = findViewById(R.id.textView2);
        this.editTextLocation = findViewById(R.id.editTextWeather);
        this.buttonWeather = findViewById(R.id.button);

        this.buttonWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeather();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Create intent object of WeatherService
        Intent intent = new Intent(this, WeatherService.class);
        //Call bindService(...) method to bind service with UI
        this.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (binded) {
            //Unbind service
            this.unbindService(connection);
            binded = false;
        }
    }

    private void showWeather() {
        String location = this.editTextLocation.getText().toString();
        String weather = this.weatherService.getWeatherToday(location);
        this.textViewWeather.setText(weather);
    }
}