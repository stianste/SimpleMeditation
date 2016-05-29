package com.stian.simplemeditation;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class timerActivity extends AppCompatActivity {

    private int minutes;
    private int seconds;
    TextView minutesView;
    TextView secondsView;
    CountDownTimer cdt;
    Database db;

    //TODO: Implement database. Just follow this video: https://www.youtube.com/watch?v=cp2rL3sAFmI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new Database(this);

        setContentView(R.layout.activity_timer);
        Intent intent = getIntent();

        minutes = intent.getIntExtra("minutes", 0);
        //No sessions longer than an hour (for now)
        if (minutes > 60) minutes = 60;
        seconds = intent.getIntExtra("seconds", 0);
        if (seconds > 60) seconds = 60;
        minutesView = (TextView) findViewById(R.id.minutes);
        minutesView.setText(corretTimeFormat(minutes));
        secondsView = (TextView) findViewById(R.id.seconds);
        secondsView.setText(corretTimeFormat(seconds));
        startTimer();

    }
    public void onBackPressed(){return;}

    private void startTimer() {
        //https://developer.android.com/reference/android/os/CountDownTimer.html
        cdt = new CountDownTimer(minutes * 60000 + seconds * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                minutesView.setText(corretTimeFormat((int) millisUntilFinished / 60000));
                secondsView.setText(corretTimeFormat(((int) millisUntilFinished / 1000) % 60));
            }

            public void onFinish() {
                playGong();
                insertSession(minutes, seconds);
            }
        }.start();

    }
    private void cancelTimer(){
        cdt.cancel();
    }

    private void playGong() {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.gong);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }

        });
    }

    public void stopTimer(View v){
        cancelTimer();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private String corretTimeFormat(int n){
        if (n > 60) {
            //We do not allow sessions longer than 1 hour.
            return "60";
        }
        if(String.valueOf(n).length() < 2){
            return "0" + String.valueOf(n);
        }
        return String.valueOf(n);
    }
    public boolean insertSession(int minutes, int seconds){
        return db.insertData(minutes, seconds);
    }
}
