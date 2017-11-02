package com.chickendinner.keep;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteActivity extends AppCompatActivity {

    protected TextView mEditTime;
    protected Calendar cal;

    public void reactToClick(View view){}

    public void updateTime(){
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String aux = String.format("Edited %s", sdf.format(cal.getTime()));
        mEditTime.setText(aux);
    }
}
