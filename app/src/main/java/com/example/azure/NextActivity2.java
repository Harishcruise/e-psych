package com.example.azure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NextActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.happy_activity);

        Button btnHappy = findViewById(R.id.button3);

        btnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent ( NextActivity2.this, MainActivity2.class);
                startActivity(i);
            }
        });

    }



    public void Happy(View view) {
        Intent i = new Intent ( NextActivity2.this, MainActivity2.class);
        startActivity(i);


    }
}
