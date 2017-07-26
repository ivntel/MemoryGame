package com.example.geniusplaza.shakedetection;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by geniusplaza on 7/25/17.
 */

public class Questions {
    String question;
    String answer1;
    String answer2;
    String answer3;
    String answer4;
    String rightAnswer;
    private Context mContext;

    public Questions(Context context) {
        this.mContext = context;
    }

    public List<Questions> readLine(String path) {
        List<String> mLines = new ArrayList<>();
        List<Questions> quesArray = new ArrayList<>();
        AssetManager am = mContext.getAssets();

        try {
            InputStream is = am.open(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null){
                mLines.add(line);

                String[] split = line.split(",");
                int[] ar = {1,2,3,4};
                Random rnd = ThreadLocalRandom.current();
                for (int i = ar.length - 1; i > 0; i--)
                {
                    int index = rnd.nextInt(i + 1);
                    // Simple swap
                    int a = ar[index];
                    ar[index] = ar[i];
                    ar[i] = a;
                }
                question = split[0];
                answer1 = split[ar[0]];
                answer2 = split[ar[1]];
                answer3 = split[ar[2]];
                answer4 = split[ar[3]];
                rightAnswer = split[5];
                quesArray.add(new Questions(question,answer1,answer2,answer3,answer4,rightAnswer));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return quesArray;
    }
    public Questions(String question, String answer1, String answer2, String answer3, String answer4, String rightAnswer) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.rightAnswer = rightAnswer;
    }

    public Questions(String question, String answer1, String answer2, String rightAnswer) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.rightAnswer = rightAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
}
