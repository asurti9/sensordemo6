package com.example.sensordemo6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.hardware.Sensor.TYPE_ORIENTATION;

public class MainActivity extends AppCompatActivity {


    private final String TAG = "EIC";

    private Button btn_accel;
    private TextView textView_sensorValues;
    private TextView textView_sensorMockValues;
    private TextView textView_adjustedvalues;
    private  TextView textView_calculatedvalues;
    private Button btn_lockMockValues;
    private  int theta = 0, phi = 90, si = 0;

    private boolean isMockValueButtonPressed = false;

    private SensorManager sensorManager;
    private Sensor sensor;
    private float x, y, z;

    private double x_mock = 0, y_mock = 0, z_mock = 0;
    private double x_cal[] = new double[3] , y_cal[] = new double[3], z_cal[] = new double[3];
    private Matrix matrix_x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        btn_accel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

                if (sensorManager == null) {
                    Log.d(TAG, "getSystemService(Context.SENSOR_SERVICE) is null - SensorManager service does not exist");
                } else {
                    if (sensor == null) {
                        Log.d(TAG, "getDefaultSensor(Sensor.TYPE_ACCELEROMETER) is null - Accelerometer sensor does not exists");
                    } else {
                        Log.d(TAG, "SensorManager service exists");
                        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                        Log.d(TAG, "Accelerometer Listener Registered");
                    }
                }


            }
        });

        btn_lockMockValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMockValueButtonPressed = true;
            }
        });
    }

    private void initUI() {
        btn_accel = (Button) findViewById(R.id.btn_accel);
        textView_sensorValues = (TextView) findViewById(R.id.tv_sensor_values);
        textView_sensorMockValues = (TextView) findViewById(R.id.tv_sensor_mock_values);
        btn_lockMockValues = (Button) findViewById(R.id.btn_mock_values);
        textView_adjustedvalues = (TextView)findViewById(R.id.tv_sensor_adjustedvalues);
        textView_calculatedvalues = (TextView)findViewById(R.id.tv_sensor_calculatedvalues);
    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];

            textView_sensorValues.setText("X: " + String.format("%.1f", x) + " Y: " + String.format("%.1f", y) + " Z: " + String.format("%.1f", z));

            //x_cal[0] = x*(Math.cos(theta)*Math.cos(si)) + y*(Math.cos(theta)*Math.sin(si)) - z*Math.sin(theta);
            //y_cal[0] = x*(Math.cos(si)*Math.sin(theta)*Math.sin(phi) + y*((Math.cos(phi)*Math.cos(si)) + Math.sin(theta)*Math.sin(phi)*Math.sin(si)) + z*(Math.cos(theta)*Math.sin(phi)));
            //z_cal[0] = x*(Math.cos(phi)*Math.cos(si)*Math.sin(theta) + Math.sin(phi)*Math.sin(si)) + y*(Math.cos(phi)*Math.sin(theta)*Math.sin(si)-Math.cos(si)*Math.sin(phi) + z*(Math.cos(theta)*Math.cos(phi)));
            /*x_cal[0] = (x );
            x_cal[1] = (y*Math.cos(90) - z*Math.sin(90)) ;
            x_cal[2] = (y*Math.sin(90) + z*Math.cos(90)) ;*/

            x_mock = (x*Math.cos(Math.toRadians(90)) + z*Math.sin(Math.toRadians(90)));
            y_mock = (y);
            z_mock = (-(x * Math.sin(Math.toRadians(90))) + (z * Math.cos(Math.toRadians(90))));

            /*z_cal[0] = (x*Math.cos(90) - y*Math.sin(90));
            z_cal[1] = (x*Math.sin(90) + y*Math.cos(90));
            z_cal[2] = (z) ;*/

            //x_mock = (x_cal[0]*(y_cal[0]*z_cal[0] + y_cal[0]*z_cal[1] + y_cal[0]*z_cal[2]) + x_cal[0]*(y_cal[1]*z_cal[0] + y_cal[1]*z_cal[1] + y_cal[1]*z_cal[2]) + x_cal[0]*(y_cal[2]*z_cal[0] + y_cal[2]*z_cal[1] + y_cal[2]*z_cal[2]));
            //y_mock = (x_cal[1]*(y_cal[0]*z_cal[0] + y_cal[0]*z_cal[1] + y_cal[0]*z_cal[2]) + x_cal[1]*(y_cal[1]*z_cal[0] + y_cal[1]*z_cal[1] + y_cal[1]*z_cal[2]) + x_cal[1]*(y_cal[2]*z_cal[0] + y_cal[2]*z_cal[1] + y_cal[2]*z_cal[2]));
            //z_mock = (x_cal[2]*(y_cal[0]*z_cal[0] + y_cal[0]*z_cal[1] + y_cal[0]*z_cal[2]) + x_cal[2]*(y_cal[1]*z_cal[0] + y_cal[1]*z_cal[1] + y_cal[1]*z_cal[2]) + x_cal[2]*(y_cal[2]*z_cal[0] + y_cal[2]*z_cal[1] + y_cal[2]*z_cal[2]));
            textView_calculatedvalues.setText("x_mock: " + String.format("%.2f",x_mock) + "y_mock: " + String.format("%.2f",y_mock) + "z_mock: " + String.format("%.2f",z_mock));

            /*if(isMockValueButtonPressed) {
                btn_lockMockValues.setClickable(false);
                isMockValueButtonPressed = false;
                x_mock = x;
                y_mock = y;
                z_mock = z;

                textView_sensorMockValues.setText("X: " + String.format("%.1f", x_mock) + " Y: " + String.format("%.1f", y_mock) + " Z: " + String.format("%.1f", z_mock));
            }*/
            //Rx = x + ycos@ - zsin@ + ysin@ + zcos@
            //Ry = xcos@ + zsin@ + y - xsin@ + zcos@
            //Rz = xcos@ - ysin@ + xsin@ + ycos@ + z
            //textView_adjustedvalues.setText(" X : " +String.format("%.1f", (x-x_mock))+ " Y : " +String.format("%.1f", (y-y_mock)) + " Z : " +String.format("%.1f", (z-z_mock)));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        try {

            if (sensorEventListener != null)
                sensorManager.unregisterListener(sensorEventListener);
            Log.d(TAG, "currentAccelListener unRegistered");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        try {
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "currentAccelListener Registered");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
