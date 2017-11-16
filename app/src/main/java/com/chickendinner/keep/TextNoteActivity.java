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
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class TextNoteActivity extends NoteActivity implements View.OnFocusChangeListener {
    private EditText mTextNoteTitle;
    private EditText mTextNoteBody;
    private String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_note);
        mEditTime = (TextView) findViewById(R.id.editTime);
        cal = Calendar.getInstance();
        updateTime();

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users").child(uid);

        mTextNoteTitle = (EditText) findViewById(R.id.textNoteTitle);
        mTextNoteBody = (EditText) findViewById(R.id.textNoteBody);
        mTextNoteTitle.setOnFocusChangeListener(this);
        mTextNoteBody.setOnFocusChangeListener(this);
        noteId = "000000000000000000";
    }

    @Override
    public void onClick(View view){
        clearAllFocus();
        switch (view.getId()) {
            case R.id.textNoteTitle:
            case R.id.checklistFragment:
                updateTime();
                break;
            case R.id.backButton:
                finish();
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            String saveText;
            if (view == mTextNoteTitle) {
                saveText = mTextNoteTitle.getText().toString();
                mReference.child(noteId).child("title").setValue(saveText);
            }
            if (view == mTextNoteBody) {
                saveText = mTextNoteBody.getText().toString();
                mReference.child(noteId).child("body").setValue(saveText);
            }
        }
    }

    private void clearAllFocus(){
        mTextNoteTitle.clearFocus();
        mTextNoteBody.clearFocus();
    }
}
