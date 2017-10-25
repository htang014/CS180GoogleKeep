package com.chickendinner.keep;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void makeNote(View view) {
        switch (view.getId()) {
            case R.id.textNoteButton:
                break;
            case R.id.checklistButton:
                break;
            case R.id.drawingButton:
                break;
            case R.id.recordingButton:
                break;
            case R.id.photoButton:
                break;

        }
    }
}
