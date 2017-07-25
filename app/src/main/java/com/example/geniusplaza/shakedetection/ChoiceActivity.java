package com.example.geniusplaza.shakedetection;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChoiceActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    final float[] mValuesMagnet      = new float[3];
    final float[] mValuesAccel       = new float[3];
    final float[] mValuesOrientation = new float[3];
    final float[] mRotationMatrix    = new float[9];
    TextView ques, choice1, choice2, choice3, choice4,score, life;
    int responseNumber;
    GestureDetector gestureDetector;
    int pointerCount;
    Questions questions, quesObject;
    List<Questions> questionArray = new ArrayList<>();
    public static final String mPath = "data.txt";
    public int counter = 0,lives=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        quesObject = new Questions(this);
        questionArray = quesObject.readLine(mPath);


        SensorManager sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        ques = (TextView)findViewById(R.id.question);
        choice1 = (TextView)findViewById(R.id.textViewLeft);
        choice2 = (TextView)findViewById(R.id.textViewRight);
        choice3 = (TextView)findViewById(R.id.textViewUp);
        choice4 = (TextView)findViewById(R.id.textViewDown);
        score = (TextView)findViewById(R.id.textViewScore);
        life =(TextView)findViewById(R.id.textViewLives);
        questions = loadQuestion();
        gestureDetector = new GestureDetector(ChoiceActivity.this, ChoiceActivity.this);

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
            };
        };

        // You have set the event lisetner up, now just need to register this with the
        // sensor manager along with the sensor wanted.
        setListners(sensorManager, mEventListener);
    }
    public void setListners(SensorManager sensorManager, SensorEventListener mEventListener)
    {
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
                if (pointerCount == 3) {
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
        if (questions.getRightAnswer().equals(questions.getAnswer1())) {
            if (responseNumber == 2) {
                handleCorrect();
            } else {
                handleInCorrect();
                return;
            }
        }
        else if (questions.getRightAnswer().equals(questions.getAnswer2())) {
            if (responseNumber == 1) {
                handleCorrect();
            } else {
                handleInCorrect();
                return;
            }
        }
        else if (questions.getRightAnswer().equals(questions.getAnswer3())) {
            if (responseNumber == 3) {
              handleCorrect();
            } else {
                handleInCorrect();
                return;
            }
        }
        else if (questions.getRightAnswer().equals(questions.getAnswer4())) {
            if (responseNumber == 4) {
                handleCorrect();
            } else {
                handleInCorrect();
                return;
            }
        }
    }
    public Questions loadQuestion(){
        ques.setText(questionArray.get(counter).getQuestion());
        choice1.setText(questionArray.get(counter).getAnswer1());
        choice2.setText(questionArray.get(counter).getAnswer2());
        choice3.setText(questionArray.get(counter).getAnswer3());
        choice4.setText(questionArray.get(counter).getAnswer4());
        return questionArray.get(counter);
    }
    public  void handleCorrect(){
        ++counter;
        score.setText("Score: "+counter);
        if(counter==questionArray.size()){
            Toast.makeText(getApplicationContext(),"You Beat the Game!",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ChoiceActivity.this, GameDashboard.class);
            startActivity(i);
        }
        else{
            Toast.makeText(getApplicationContext(),"Correct!",Toast.LENGTH_SHORT).show();
            questions=loadQuestion();
        }
    }
    public  void handleInCorrect(){
        if(lives>0){
            lives--;
            life.setText("Lives: "+lives);
            ++counter;
            if(counter==questionArray.size()){
                Toast.makeText(getApplicationContext(),"Game Over! Correct answer is:"+questions.getRightAnswer(),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ChoiceActivity.this, GameDashboard.class);
                startActivity(i);
            }
            else {
                score.setText("Score: "+ String.valueOf(counter-1));
                Toast.makeText(getApplicationContext(),"InCorrect! Correct answer is:"+questions.getRightAnswer(),Toast.LENGTH_SHORT).show();
                questions=loadQuestion();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Game Over! Correct answer is:"+questions.getRightAnswer(),Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ChoiceActivity.this, GameDashboard.class);
            startActivity(i);
        }

    }
}
