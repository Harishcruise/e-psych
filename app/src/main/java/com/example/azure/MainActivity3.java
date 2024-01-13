package com.example.azure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.education_activity);
    }
    public void Mood(View view){
        Intent i = new Intent(this, MainActivity9.class);
        startActivity(i);
    }
    public void Anxiety(View view){
        Intent n = new Intent(this, MainActivity8.class);
        startActivity(n);
    }
    public void Autism(View view){
        Intent m = new Intent(this, MainActivity11.class);
        startActivity(m);
    }
}
