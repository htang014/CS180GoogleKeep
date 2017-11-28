package com.chickendinner.keep;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chickendinner.keep.tools.NoteIdGenerator;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteActivity extends AppCompatActivity {
    protected FirebaseDatabase mDatabase;
    protected DatabaseReference mReference;
    protected FirebaseAuth mAuth;
    protected GoogleSignInClient mGoogleSignInClient;
    protected NoteIdGenerator mNoteIdGenerator = new NoteIdGenerator();

    protected EditText mNoteTitleView;
    protected View mNoteFragmentView;
    protected TextView mEditTimeView;

    protected Calendar cal;
    protected String uid;
    protected String noteId;

    public void onClick(View view){}
    protected void saveDataToFirebase(){}
    protected void saveDataToFirebase(View view){}

    public void updateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
        String aux = String.format("Edited %s at %s", sdf.format(cal.getTime()), stf.format(cal.getTime()));
        mEditTimeView.setText(aux);
    }
}
