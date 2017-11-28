package com.chickendinner.keep;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chickendinner.keep.beans.CheckListBean;
import com.chickendinner.keep.fragments.ChecklistFragment;
import com.chickendinner.keep.fragments.NoteFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ChecklistActivity extends NoteActivity {
    ChecklistFragment mChecklistFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        mChecklistFragment = (ChecklistFragment) getFragmentManager().
                findFragmentById(R.id.checklistFragment);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users").child(uid);

        mNoteTitleView = (EditText) findViewById(R.id.noteTitle);
        mNoteFragmentView = findViewById(R.id.checklistFragment);

        Intent i = getIntent();
        String loadKey = i.getStringExtra(MainActivity.EXTRA_KEY);
        String loadTitle = i.getStringExtra(MainActivity.EXTRA_TITLE);

        if (!loadKey.equals("")) {
            mNoteTitleView.setText(loadTitle);
            noteId = loadKey;
            mReference.child(noteId).child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    setSavedData((List<Object>) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            noteId = mNoteIdGenerator.generateNoteId();
            mChecklistFragment.addItem();
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
        mChecklistFragment.setOnFragmentChangeListener(new NoteFragment.OnFragmentChangeListener() {
            @Override
            public void onFragmentChanged() {
                saveDataToFirebase(mNoteFragmentView);
            }
        });

        mEditTimeView = (TextView) findViewById(R.id.editTime);
        cal = Calendar.getInstance();
        updateTime();
    }

    protected void setSavedData(List<Object> dataSet) {
        for (Object tp : dataSet) {
            Map<String, Object> m = (Map<String, Object>) tp;
            mChecklistFragment.addItem((String)m.get("text"), (boolean)m.get("check"));
        }
    }

    @Override
    protected void saveDataToFirebase(View view){
        if (view == mNoteTitleView) {
            mReference.child(noteId).child("title").setValue(mNoteTitleView.getText().toString());
        } else if (view == mNoteFragmentView) {
            List<CheckListBean> data = mChecklistFragment.getDataset();
            mReference.child(noteId).child("data").setValue(data);
        }
        mReference.child(noteId).child("type").setValue("1");
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.textNoteTitle:
            case R.id.checklistFragment:
                updateTime();
                break;
            case R.id.backButton:
                saveDataToFirebase();
                finish();
                break;
            case R.id.addItemButton:
                mChecklistFragment.addItem();
                break;
            case R.id.trashButton:
                mReference.child(noteId).removeValue();
                finish();
                break;
        }

    }
}