package com.chickendinner.keep;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteActivity extends AppCompatActivity {
    protected FirebaseDatabase mDatabase;
    protected DatabaseReference mReference;
    protected FirebaseAuth mAuth;
    protected String uid;

    protected TextView mEditTime;
    protected Calendar cal;

    public void onClick(View view){}

    public void updateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
        String aux = String.format("Edited %s at %s", sdf.format(cal.getTime()), stf.format(cal.getTime()));
        mEditTime.setText(aux);
    }
}
