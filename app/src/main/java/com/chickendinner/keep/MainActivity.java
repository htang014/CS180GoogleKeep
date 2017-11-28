package com.chickendinner.keep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import com.chickendinner.keep.beans.NoteItemBean;
import com.chickendinner.keep.beans.PreviewListBean;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NoteActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_KEY = "com.chickendinner.keep.KEY";
    public static final String EXTRA_TITLE = "com.chickendinner.keep.TITLE";

    //private TextView mTextMessage;
    private Toolbar mToolbar;
    private ListView mListView;
    private FirebaseListAdapter mAdapter;

    // Items in Firebase-stored note
    List<PreviewListBean> mDataset = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        if(uid!=null) {
            mDatabase = FirebaseDatabase.getInstance();
            mReference = mDatabase.getReference("users/" + uid);
        }
        Log.e("data size => ", mDataset.size() + "");
    }

    @Override
    public void onStart(){
        super.onStart();

        mListView = (ListView) findViewById(R.id.preview);
        mAdapter =  new FirebaseListAdapter<NoteItemBean>(this,
                NoteItemBean.class,android.R.layout.simple_list_item_1, mReference) {
            @Override
            protected void populateView(View view, NoteItemBean mContainer, int position) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(mContainer.getTitle());
                ((TextView)view.findViewById(android.R.id.text1)).setTag(mContainer.getType());
            }
        };
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long id){

                Intent intent;
                String type = (String) v.getTag();
                switch(type) {
                    case "0":
                        intent = new Intent(v.getContext(), TextNoteActivity.class);
                        break;
                    case "1":
                        intent = new Intent(v.getContext(), ChecklistActivity.class);
                        break;
                    case "2":
                        intent = new Intent(v.getContext(), DrawingActivity.class);
                        break;
                    case "3":
                        intent = new Intent(v.getContext(), PhotoNoteActivity.class);
                        break;
                    default:
                        intent = null;
                        break;
                }

                intent.putExtra(EXTRA_KEY, mAdapter.getRef(position).getKey());
                intent.putExtra(EXTRA_TITLE, (String) ((TextView)v).getText());
                startActivity(intent);
            }
        });
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

        intent.putExtra(EXTRA_KEY, "");
        intent.putExtra(EXTRA_TITLE, "");
        startActivity(intent);
    }

    private void signOutWithGoogle() {
        uid = null;
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
    }
}
