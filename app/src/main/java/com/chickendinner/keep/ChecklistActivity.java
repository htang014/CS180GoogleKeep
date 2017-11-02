package com.chickendinner.keep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chickendinner.keep.recycler.RecyclerListFragment;

import java.util.Calendar;

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

    public void reactToClick(View view){
        mRecyclerListFragment.addItem();
    }
}