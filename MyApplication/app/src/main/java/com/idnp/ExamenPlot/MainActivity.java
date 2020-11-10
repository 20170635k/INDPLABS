package com.idnp.ExamenPlot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Plot plot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.plot = (Plot) this.findViewById(R.id.plot);
        Random random = new Random();
        this.plot.setData(new float[] {
                random.nextInt(50), random.nextInt(50), random.nextInt(50),
                random.nextInt(50),random.nextInt(50), random.nextInt(50), random.nextInt(50)
        });
    }
}