package com.stian.simplemeditation;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed(){return;}

    public void startTimer(View v) {
        EditText minutesText = (EditText) findViewById(R.id.minutes);
        int minutes = Integer.parseInt(String.valueOf(minutesText.getText().toString()));
        EditText secondsText = (EditText) findViewById(R.id.seconds);
        int seconds = Integer.parseInt(String.valueOf(secondsText.getText().toString()));
        Intent intent = new Intent(this, timerActivity.class);
        intent.putExtra("minutes", minutes);
        intent.putExtra("seconds", seconds);
        startActivity(intent);
    }
    public void viewAllEntries(View v){
        Database db = new Database(this);
        Cursor res = db.getAllData();
        if(res.getCount() == 0) {
            showMessage("Nothing to show", "Empty");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            buffer.append("ID: " + res.getString(0) + "\n");
            buffer.append("Minutes: " + res.getString(1)+ "\n");
            buffer.append("Seconds: " + res.getString(2)+ "\n\n");
        }
        showMessage("Data:", buffer.toString());
    }
    public void viewStats(View v){
        Database db = new Database(this);
        Cursor minuteRes = db.getTotalMinutes();
        Cursor secondRes = db.getTotalSeconds();

        if(! minuteRes.moveToFirst() || ! secondRes.moveToFirst()) {
            showMessage("Nothing to show", "Empty");
            return;
        }
        Log.w("Stian", "Moved to first");
        int minutes = minuteRes.getInt(0);
        int seconds = secondRes.getInt(0);

        String totalTime = getTotalTime(minutes, seconds);
        showMessage("Total time spent meditating:", totalTime);

    }

    private String getTotalTime(int minutes, int seconds) {
        minutes += Math.floor((double) seconds / 60);
        String hours = String.valueOf(Math.floor((double) minutes / 60));
        return hours + " hours : " + String.valueOf(minutes % 60) + " minutes";
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
