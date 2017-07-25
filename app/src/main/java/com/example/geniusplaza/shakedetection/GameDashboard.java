package com.example.geniusplaza.shakedetection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_dashboard);
    }

    public void buttonMemory(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void buttonChoice(View v){
        Intent i = new Intent(this, ChoiceActivity.class);
        startActivity(i);
    }
}
