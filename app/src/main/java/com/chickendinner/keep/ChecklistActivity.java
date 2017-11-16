package com.chickendinner.keep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chickendinner.keep.recycler.RecyclerListFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChecklistActivity extends NoteActivity {
    RecyclerListFragment mRecyclerListFragment;

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
    }

    //Todo add database part here
    protected void saveDataToDB() {
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