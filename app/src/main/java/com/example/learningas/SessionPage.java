package com.example.learningas;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SessionPage extends AppCompatActivity {

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        setContentView(R.layout.activity_session_page);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbarlayout);
        Bundle b = getIntent().getExtras();
        musicIds = new ArrayList<>();
        musicIds.add(R.raw.drums1);
        musicIds.add(R.raw.drums2);
        musicIds.add(R.raw.drums3);
        musicIds.add(R.raw.drums4);
        pause=false;
        countDownIds = R.raw.countdown;
        setsCount = b.getInt("sets");
        exercises = b.getInt("exercises");
        work = b.getInt("work");
        rest = b.getInt("rest");
        mediaPlayer = new MediaPlayer();
        thread = new Thread(){
            public void run() {
                try {
                    if(SessionPage.isRunning == false)
                    {
                        SessionPage.isRunning = true;
                        StartSession();
                        SessionPage.isRunning = false;
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
        }
        };
        thread.start();
    }


    private Thread thread;
    private static boolean isRunning = false;
    private int setsCount;
    private int exercises;
    private int work;
    private int rest;
    private MediaPlayer mediaPlayer;
    private ArrayList<Integer> musicIds;
    private int countDownIds;
    private boolean pause;

    //region Workout and Rest
    private void WorkOut() throws InterruptedException, IOException {
        final TextView timer = (TextView) findViewById(R.id.timer);
        Random rand = new Random();

        int idx = rand.nextInt(4);
        mediaPlayer = MediaPlayer.create(this, musicIds.get(idx));
        mediaPlayer.start();
        for(int time = work; time >= 0 ; time--)
        {
            final int ftime = time;
            while(pause == true);
            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    timer.setText(String.valueOf(ftime));
                    timer.invalidate();
                }
            });
            Thread.sleep(1000);
        }
        mediaPlayer.pause();
    }

    private void Rest() throws InterruptedException, IOException {
        final TextView timer = (TextView) findViewById(R.id.timer);

        for(int time = rest ; time > 0; time--)
        {
            while(pause == true);
            if(time == 5)
            {
                mediaPlayer = MediaPlayer.create(this, countDownIds);
                mediaPlayer.start();
            }
            final int finaltime = time;
            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    timer.setText(String.valueOf(finaltime));
                    timer.invalidate();
                }
            });
            Thread.sleep(1000);
        }
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
        }
    }
    //endregion

    //region UI Modifiers
    private void SetTimerGreen() {
        final TextView timer = (TextView) findViewById(R.id.timer);

        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                timer.setBackgroundResource(R.drawable.circle);
                timer.invalidate();
            }
        });
    }

    private void SetTimerYellow() {
        final TextView timer = (TextView) findViewById(R.id.timer);
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                timer.setBackgroundResource(R.drawable.yellowcircle);
                timer.invalidate();
            }
        });
    }

    private void UpdateExerciseCount(final int count) {
        final TextView exercisesViewer = findViewById(R.id.exercisesViewer);
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                exercisesViewer.setText("Exercise : " + String.valueOf(count +1));
                exercisesViewer.invalidate();
            }
        });
    }

    private void UpdateSetCount(final int count) {
        final TextView setsViewer = findViewById(R.id.setsViewer);
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                setsViewer.setText("Set : "+String.valueOf(count +1));
                setsViewer.invalidate();
            }
        });
    }
    //endregion

    private void StartSession() throws InterruptedException, IOException {
//        Thread thread = new Thread(){
//        public void run(){
            for(int set = 0 ; set < setsCount; set++)
            {
                UpdateSetCount(set);
                for(int exercise = 0 ; exercise < exercises ; exercise++)
                {
                    UpdateExerciseCount(exercise);

                    SetTimerGreen();
                    try {
                        WorkOut();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    SetTimerYellow();
                    try {
                        Rest();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                         e.printStackTrace();
                    }
                }
            }
//        }
//        };
//        thread.start();
        finish();
        return;
    }

    public void Session_Paused(View view) {
        pause = !pause;
    }
}