package com.chickendinner.keep;


import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chickendinner.keep.fragments.NoteFragment;
import com.chickendinner.keep.fragments.TextNoteFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TextNoteActivity extends NoteActivity {
    private static final String TAG = "TextNoteActivity";

    private TextNoteFragment mTextNoteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_note);

        // Variable initialization
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users/" + uid);

        mTextNoteFragment = (TextNoteFragment) getFragmentManager().
                findFragmentById(R.id.textNoteFragment);

        mNoteTitleView = (EditText) findViewById(R.id.noteTitle);
        mNoteFragmentView = findViewById(R.id.textNoteFragment);

        // Get extra data from MainActivity
        Intent i = getIntent();
        String loadKey = i.getStringExtra(MainActivity.EXTRA_KEY);
        String loadTitle = i.getStringExtra(MainActivity.EXTRA_TITLE);

        if (!loadKey.equals("")) {
            mNoteTitleView.setText(loadTitle);
            noteId = loadKey;
            mReference.child(noteId).child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mTextNoteFragment.setText((String) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            noteId = mNoteIdGenerator.generateNoteId();
        }

        // Listen for changes to data
        mNoteTitleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveDataToFirebase(mNoteTitleView);
            }
        });
        mTextNoteFragment.setOnFragmentChangeListener(new NoteFragment.OnFragmentChangeListener() {
            @Override
            public void onFragmentChanged() {
                saveDataToFirebase(mNoteFragmentView);
            }
        });

        // Time and date intialization
        mEditTimeView = (TextView) findViewById(R.id.editTime);
        cal = Calendar.getInstance();
        updateTime();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.trashButton:
                mReference.child(noteId).removeValue();
                finish();
                break;
        }
    }

    @Override
    protected void saveDataToFirebase(View view){
        if (view == mNoteTitleView) {
            String title = mNoteTitleView.getText().toString();
            mReference.child(noteId).child("title").setValue(title.trim());
        } else if (view == mNoteFragmentView) {
            String body = mTextNoteFragment.getText();
            mReference.child(noteId).child("data").setValue(body.trim());
        }
        mReference.child(noteId).child("type").setValue("0");
    }
}
