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
        mTextMessage = (TextView) findViewById(R.id.textMessage);
        mTextMessage.setText("This is a placeholder.  Click a button to display text.");
    }

    public void makeNote(View view) {
        switch (view.getId()) {
            case R.id.textNoteButton:
                mTextMessage.setText("text note");
                break;
            case R.id.checklistButton:
                mTextMessage.setText("checklist");
                break;
            case R.id.drawingButton:
                mTextMessage.setText("drawing");
                break;
            case R.id.recordingButton:
                mTextMessage.setText("recording");
                break;
            case R.id.photoButton:
                mTextMessage.setText("photo");
                break;

        }
    }
}
