package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.firstlibrary.TextBackImage;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView targetImage;
    TextBackImage textBackImage;
    String str[]={"@drawable/milkyway2","@drawable/back4","@drawable/glitter"};
    final Random randomfour=new Random();
    int randomFour;
    RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         randomFour=(randomfour.nextInt())%4;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout=findViewById(R.id.layout);
        final Random random=new Random(40000000);
        targetImage=findViewById(R.id.AttackImage);
        textBackImage=findViewById(R.id.AttackText);
        int imageResource = getResources().getIdentifier(str[Math.abs(randomFour)], null, getPackageName());
        Log.i("Random",String.valueOf(Math.abs(randomFour)));
        layout.setBackgroundResource(imageResource);
        targetImage.setImageResource(imageResource);
        textBackImage.setOrientation(targetImage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        randomFour=(randomfour.nextInt()+1)%3;
    }
}
