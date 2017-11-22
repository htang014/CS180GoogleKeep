package com.chickendinner.keep;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;

import com.chickendinner.keep.prevew.PreviewListBean;
import com.chickendinner.keep.prevew.PreviewRecyclerViewAdapter;
import com.chickendinner.keep.recycler.CheckListBean;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends NoteActivity {
    public static final String EXTRA_INFO = "com.chickendinner.keep.MESSAGE";

//    private TextView mTextMessage;
    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;

    List<PreviewListBean> mDataset = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        signInWithGoogle();

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        if(uid!=null) {
            mRecyclerView = (RecyclerView) findViewById(R.id.preview_list);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            mDatabase = FirebaseDatabase.getInstance();

            mReference = mDatabase.getReference("users").child(uid);

            mReference.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            setPreviewData((Map<String, Object>) dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    }
            );
        }
            Log.e("data size => ", mDataset.size() + "");
    }

    public void setPreviewData(Map<String, Object> notes) {
        for (String noteId : notes.keySet()) {
            Log.e("noteId =>", noteId);
            Map<String, Object> dataSet = (Map<String, Object>) notes.get(noteId);
            String type = (String) dataSet.get("type");
            String title = (String) dataSet.get("title");
            Object data = dataSet.get("data");
            mDataset.add(new PreviewListBean(noteId, title, type, data));
        }

        mRecyclerView.setAdapter(new PreviewRecyclerViewAdapter(mDataset, this));
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
//                mTextMessage.setText("text note");
                intent = new Intent(this, TextNoteActivity.class);
                break;
            case R.id.checklistButton:
//                mTextMessage.setText("checklist");
                intent = new Intent(this, ChecklistActivity.class);
                break;
            case R.id.drawingButton:
//                mTextMessage.setText("drawing");
                intent = new Intent(this, DrawingActivity.class);
                break;
            case R.id.photoButton:
//                mTextMessage.setText("photo");
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
