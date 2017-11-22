package com.chickendinner.keep;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.chickendinner.keep.prevew.PreviewListBean;
import com.chickendinner.keep.recycler.CheckListBean;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends NoteActivity {
    public static final String EXTRA_KEY = "com.chickendinner.keep.KEY";
    public static final String EXTRA_INFO = "com.chickendinner.keep.MESSAGE";
    public static final String EXTRA_TITLE = "com.chickendinner.keep.TITLE";
    /*public static final String EXTRA_DATA = "com.chickendinner.keep.DATA";
    public static final String EXTRA_LIST = "com.chickendinner.keep.LIST";*/
    private static final String TAG = "MainActivity";

    /*public Vector<String> title;
    public Vector<String> data;
    public Vector<List<CheckListBean>> listData;*/
    //public Vector<String> type;

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

        /*title = new Vector<String>(0);
        data = new Vector<String>(0);
        listData = new Vector<List<CheckListBean>>(0);*/
        //type = new Vector<String>(0);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        signInWithGoogle();

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
        mAdapter =  new FirebaseListAdapter<NoteItemContainer>(this,
                NoteItemContainer.class,android.R.layout.simple_list_item_1, mReference) {
            @Override
            protected void populateView(View view, NoteItemContainer mContainer, int position) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(mContainer.getTitle());
                /*title.addElement(mContainer.getTitle());
                data.addElement(mContainer.getData());
                listData.addElement(mContainer.getListData());*/
                ((TextView)view.findViewById(android.R.id.text1)).setTag(mContainer.getType());
                //type.addElement(mContainer.getType());
                //((TextView)view.findViewById(android.R.id.text2)).setText("" + receiptItem.getExpiration() + " days left");
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
                /*intent.putExtra(EXTRA_DATA, data.elementAt(position));
                intent.putExtra(EXTRA_LIST, (Serializable)listData.elementAt(position));*/
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
        //intent.putExtra(EXTRA_DATA, "");
        //intent.putExtra(EXTRA_LIST, "");
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
