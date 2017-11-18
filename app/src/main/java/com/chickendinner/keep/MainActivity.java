package com.chickendinner.keep;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_INFO = "com.chickendinner.keep.MESSAGE";

    private TextView mTextMessage;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mTextMessage = (TextView) findViewById(R.id.textMessage);
        mTextMessage.setText("This is a placeholder.  Click a button to display text.");

        signInWithGoogle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    // Handle app bar actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                // User chose the "Sign Out" item.
                signOutWithGoogle();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void makeNote(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.textNoteButton:
                mTextMessage.setText("text note");
                intent = new Intent(this, TextNoteActivity.class);
                break;
            case R.id.checklistButton:
                mTextMessage.setText("checklist");
                intent = new Intent(this, ChecklistActivity.class);
                break;
            case R.id.drawingButton:
                mTextMessage.setText("drawing");
                intent = new Intent(this, DrawingActivity.class);
                break;
            case R.id.recordingButton:
                mTextMessage.setText("recording");
                break;
            case R.id.photoButton:
                mTextMessage.setText("photo");
                intent = new Intent(this, PhotoNoteActivity.class);
                break;

        }
        startActivity(intent);
    }

    private void signOutWithGoogle() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra(EXTRA_INFO, "sign_out");
        startActivity(intent);
    }

    private void signInWithGoogle(){
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra(EXTRA_INFO, "sign_in");
        startActivity(intent);
    }
}
