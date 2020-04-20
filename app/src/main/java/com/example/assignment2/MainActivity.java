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
    String str[]={"@drawable/milkyway2","@drawable/back4","@drawable/glitter","@drawable/bcak5"};
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
        SensorManager sensorManager =
                (SensorManager) getSystemService(SENSOR_SERVICE);
        targetImage=findViewById(R.id.AttackImage);
        textBackImage=findViewById(R.id.AttackText);
        int imageResource = getResources().getIdentifier(str[Math.abs(randomFour)], null, getPackageName());
        Log.i("Random",String.valueOf(Math.abs(randomFour)));
        layout.setBackgroundResource(imageResource);
        targetImage.setImageResource(imageResource);
        Sensor gyroscopeSensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        // Create a listener
       SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                // More code goes here
                float axisX = sensorEvent.values[0];
                float axisY = sensorEvent.values[1];
                float axisZ = sensorEvent.values[2];
               double max= Math.max(Math.max(axisX,axisY),axisZ);
                double omegaMagnitude = Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);
                double maximum=Math.max(max,omegaMagnitude);
                double paddingno=(maximum*random.nextFloat())%100;
                if(paddingno<0){
                    paddingno=paddingno*(-1);
                }
                int paddingfinal=Math.abs(((int)paddingno*74)%100);
                Log.i("tag",axisX + "   "+axisY+"   "+paddingfinal);
                targetImage.setPadding(paddingfinal,paddingfinal,paddingfinal,paddingfinal);
                targetImage.setVisibility(paddingfinal);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

// Register the listener
        sensorManager.registerListener(gyroscopeSensorListener,
                gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        randomFour=(randomfour.nextInt()+1)%4;
    }
}
