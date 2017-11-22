package com.chickendinner.keep;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chickendinner.keep.recycler.CheckListBean;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        mReference = mDatabase.getReference("users/" + uid);

        mTextNoteTitle = (EditText) findViewById(R.id.textNoteTitle);
        mTextNoteBody = (EditText) findViewById(R.id.textNoteBody);
        mTextNoteTitle.setOnFocusChangeListener(this);
        mTextNoteBody.setOnFocusChangeListener(this);

        // Get extra data from MainActivity
        Intent i = getIntent();
        String loadKey = i.getStringExtra(MainActivity.EXTRA_KEY);
        String loadTitle = i.getStringExtra(MainActivity.EXTRA_TITLE);

        if (!loadKey.equals("")) {
            mTextNoteTitle.setText(loadTitle);
            noteId = loadKey;
            mReference.child(noteId).child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mTextNoteBody.setText((String) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            noteId = mNoteIdGenerator.generateNoteId();
        }
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
                mReference.child(noteId).child("title").setValue(saveText.trim());
            }
            if (view == mTextNoteBody) {
                saveText = mTextNoteBody.getText().toString();
                mReference.child(noteId).child("data").setValue(saveText.trim());
            }
            mReference.child(noteId).child("type").setValue("0");
        }
    }

    private void clearAllFocus(){
        mTextNoteTitle.clearFocus();
        mTextNoteBody.clearFocus();
    }
}
