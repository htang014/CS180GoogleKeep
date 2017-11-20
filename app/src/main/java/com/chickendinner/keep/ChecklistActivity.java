package com.chickendinner.keep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.chickendinner.keep.recycler.CheckListBean;
import com.chickendinner.keep.recycler.RecyclerListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChecklistActivity extends NoteActivity {
    RecyclerListFragment mRecyclerListFragment;
    private String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        mRecyclerListFragment = (RecyclerListFragment) getFragmentManager().
                findFragmentById(R.id.checklistFragment);
        mRecyclerListFragment.addItem();
        mEditTime = (TextView) findViewById(R.id.editTime);
        cal = Calendar.getInstance();
        updateTime();

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users").child(uid);
        Intent i = getIntent();
        if (i != null && i.getStringExtra("noteId") != null) {
            EditText mNoteTitle = (EditText) findViewById(R.id.textNoteTitle);
            mNoteTitle.setText(i.getStringExtra("title"));
            noteId = i.getStringExtra("noteId");
        } else {
            noteId = mNoteIdGenerator.generateNoteId();
        }
    }

    //Todo add database part here
    protected void saveDataToDB() {
        EditText mNoteTitle = (EditText) findViewById(R.id.textNoteTitle);
        List<CheckListBean> data = mRecyclerListFragment.getStringAndCheckData();
        mReference.child(noteId).child("type").setValue("1");
        mReference.child(noteId).child("title").setValue(mNoteTitle.getText().toString());
        mReference.child(noteId).child("data").setValue(data);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.textNoteTitle:
            case R.id.checklistFragment:
                updateTime();
                break;
            case R.id.backButton:
                saveDataToDB();
                finish();
                break;
            case R.id.addItemButton:
                mRecyclerListFragment.addItem();
                break;
        }

    }
}