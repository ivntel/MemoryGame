package com.example.geniusplaza.shakedetection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    Button btn_listen, btn_play, btn_next;
    TextView txtl, textViewTop, score;
    TextToSpeech t1;
    static int counter = 2;
    static int iterator = 0;
    String arrayDirection[] = {"Right", "Left", "Double Tap", "2 Finger Tap"};
    ArrayList<String> display = new ArrayList<>();
    float[] mValuesMagnet = new float[3];
    float[] mValuesAccel = new float[3];
    float[] mValuesOrientation = new float[3];
    float[] mRotationMatrix = new float[9];
    GestureDetector gestureDetector;
    int pointerCount;
    int scoreCount;
    int responseNumber;
    ConstraintLayout notClickLayout;
    Vibrator v;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display.clear();
        SensorManager sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        gestureDetector = new GestureDetector(MainActivity.this, MainActivity.this);
        Random rand = new Random();
        int n = rand.nextInt(arrayDirection.length);
        display.add(arrayDirection[n]);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);

                }
            }
        });

        btn_next = (Button) findViewById(R.id.buttonNext);
        btn_play = (Button) findViewById(R.id.buttonPlay);
        btn_listen = (Button) findViewById(R.id.buttonListen);
        txtl = (TextView) findViewById(R.id.textView1);
        textViewTop = (TextView) findViewById(R.id.textViewTop);
        score = (TextView) findViewById(R.id.scoreCount);
        notClickLayout = (ConstraintLayout) findViewById(R.id.cLayout);
        v = (Vibrator)MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        notClickLayout.setBackgroundColor(getResources().getColor(R.color.blue));

        notClickLayout.setClickable(true);
        final SensorEventListener mEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            public void onSensorChanged(SensorEvent event) {
                // Handle the events for which we registered
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
                        break;

                    case Sensor.TYPE_MAGNETIC_FIELD:
                        System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
                        break;
                }
            }

            ;
        };

        // You have set the event lisetner up, now just need to register this with the
        // sensor manager along with the sensor wanted.
        setListners(sensorManager, mEventListener);

        btn_listen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                notClickLayout.setClickable(true);

                txtl.setText("Follow the directions in order!");
                btn_listen.setVisibility(View.GONE);
                btn_play.setVisibility(View.VISIBLE);
                notClickLayout.setBackgroundColor(getResources().getColor(R.color.blue));
                textViewTop.setVisibility(View.VISIBLE);

                if(counter == 1){
                    display.clear();
                    Random rand = new Random();

                    int n = rand.nextInt(arrayDirection.length);
                    display.add(arrayDirection[n]);
                }

                Random rand = new Random();

                int n = rand.nextInt(arrayDirection.length);
                display.add(arrayDirection[n]);

                textViewTop.setText(display.toString());
                t1.setSpeechRate((float) 0.7);
                t1.speak(display.toString(), TextToSpeech.QUEUE_FLUSH, null);

            }

        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iterator = 0;
                btn_play.setVisibility(View.GONE);
                notClickLayout.setClickable(false);
                textViewTop.setVisibility(View.GONE);
            }
        });
    }


    // Register the event listener and sensor type.
    public void setListners(SensorManager sensorManager, SensorEventListener mEventListener) {
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        int action = (event.getAction() & MotionEvent.ACTION_MASK) % 5;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pointerCount = event.getPointerCount();
                if (pointerCount == 2) {
                    responseNumber = 3;
                    handleResponse(responseNumber);
                    //Toast.makeText(MainActivity.this, "2 Finger Tap on Screen is Working.", Toast.LENGTH_LONG).show();
                }
                break;
        }
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
        SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);

        if (mValuesOrientation[2] > 0)
            responseNumber = 1;
        if (mValuesOrientation[2] < 0)
            responseNumber = 2;

        handleResponse(responseNumber);

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {

        responseNumber = 4;
        handleResponse(responseNumber);

        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void handleResponse(int responseNumber) {
        if (iterator == display.size() - 1) {
            if (display.get(iterator).equals("Double Tap")) {
                if (responseNumber == 4) {
                    txtl.setText("Level Completed!");
                    handleAnswer();
                } else {
                    txtl.setText("InCorrect");
                    handleAnswer();
                    return;
                }
            } else if (display.get(iterator).equals("Right")) {
                if (responseNumber == 1) {
                    txtl.setText("Level Completed!");
                    handleAnswer();
                    return;
                } else {
                    txtl.setText("InCorrect");
                    handleAnswer();
                    return;
                }
            } else if (display.get(iterator).equals("Left")) {
                if (responseNumber == 2) {
                    txtl.setText("Level Completed!");
                    handleAnswer();
                    return;
                } else {
                    txtl.setText("InCorrect");
                    handleAnswer();
                    return;
                }
            }
            else if (display.get(iterator).equals("2 Finger Tap")) {
                if (responseNumber == 3) {
                    txtl.setText("Level Completed!");
                    handleAnswer();
                } else {
                    txtl.setText("InCorrect");
                    handleAnswer();
                    return;
                }
            }
        } else {
            if (display.get(iterator).equals("Double Tap")) {
                if (responseNumber == 4) {
                    txtl.setText("Correct");
                    handleAnswer();
                } else {
                    txtl.setText("InCorrect");
                    handleAnswer();
                    return;
                }
            }

            else if (display.get(iterator).equals("Right")) {
                if (responseNumber == 1) {
                    txtl.setText("Correct");
                    handleAnswer();
                } else {
                    txtl.setText("InCorrect");
                    handleAnswer();
                    return;
                }
            }
            else if (display.get(iterator).equals("Left")) {
                if (responseNumber == 2) {
                    txtl.setText("Correct");
                    handleAnswer();
                } else {
                    txtl.setText("InCorrect");
                    handleAnswer();
                    return;
                }
            }
            else if (display.get(iterator).equals("2 Finger Tap")) {
                if (responseNumber == 4) {
                    txtl.setText("Correct");
                    handleAnswer();
                } else {
                    txtl.setText("InCorrect");
                    handleAnswer();
                }
            }

        }
    }
    public void handleAnswer(){
        if(txtl.getText().equals("InCorrect")){
            counter = 1;
            iterator = 0;
            v.vibrate(1000);
            scoreCount = 0;
            score.setText("Score: " + scoreCount);
            notClickLayout.setBackgroundColor(getResources().getColor(R.color.red));
            notClickLayout.setClickable(true);
            btn_listen.setText("New Game");
            btn_listen.setVisibility(View.VISIBLE);
        }
        if(txtl.getText().equals("Correct")){
            iterator++;
            scoreCount++;
            v.vibrate(400);
            score.setText("Score: " + scoreCount);
            notClickLayout.setBackgroundColor(getResources().getColor(R.color.green));
            notClickLayout.setBackgroundColor(getResources().getColor(R.color.blue));
            notClickLayout.setBackgroundColor(getResources().getColor(R.color.green));
        }
        if(txtl.getText().equals("Level Completed!")){
            ++counter;
            iterator = 0;
            scoreCount++;
            v.vibrate(1400);
            score.setText("Score: " + scoreCount);
            notClickLayout.setBackgroundColor(getResources().getColor(R.color.blue));
            btn_listen.setText("Listen");
            btn_listen.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_menu:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("HOW TO USE")
                        .setMessage("Remember the directions and the order of them and input the directions in the specified order by tapping on the screen to submit\n\nTo submit left or right directions tilt phone in the appropriate direction and tap screen the screen.")
                        .setNegativeButton("Done",null)
                        .create()
                        .show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
