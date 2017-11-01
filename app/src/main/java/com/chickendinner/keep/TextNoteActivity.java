package com.chickendinner.keep;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class TextNoteActivity extends AppCompatActivity {

    private TextView mEditTime;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_note);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mEditTime = (TextView) findViewById(R.id.editTime);
        cal = Calendar.getInstance();
        updateTime();
    }

    public void reactToClick(View view){
        switch (view.getId()) {
            case R.id.textNoteTitle:
            case R.id.textNoteBody:
                updateTime();
                break;
            case R.id.backButton:
                finish();
        }
    }

    public void updateTime(){
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String aux = String.format("Edited %s", sdf.format(cal.getTime()));
        mEditTime.setText(aux);
    }



}