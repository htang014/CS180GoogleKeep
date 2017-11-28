package com.chickendinner.keep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.chickendinner.keep.fragments.DrawingFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class DrawingActivity extends NoteActivity implements View.OnClickListener,View.OnFocusChangeListener, DrawingFragment.Callback,Handler.Callback {
    private static final String TAG = "DrawingActivity";

    private View mUndoView;
    private View mRedoView;
    private View mPenView;
    private View mEraserView;
    private View mClearView;
    private DrawingFragment mDrawingFragment;
    private ProgressDialog mSaveProgressDlg;
    private static final int MSG_SAVE_SUCCESS = 1;
    private static final int MSG_SAVE_FAILED = 2;
    private Handler mHandler;
    private EditText mTextNoteTitle;
    private String noteId;
    private String savedFile;
    private FirebaseStorage storage;
    private StorageReference imagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        mEditTimeView = (TextView) findViewById(R.id.editTime);
        cal = Calendar.getInstance();
        updateTime();

        mDrawingFragment = (DrawingFragment) findViewById(R.id.palette);
        mDrawingFragment.setCallback(this);

        mUndoView = findViewById(R.id.undo);
        mRedoView = findViewById(R.id.redo);
        mPenView = findViewById(R.id.pen);
        mPenView.setSelected(true);
        mEraserView = findViewById(R.id.eraser);
        mClearView = findViewById(R.id.trashButton);

        mUndoView.setOnClickListener(this);
        mRedoView.setOnClickListener(this);
        mPenView.setOnClickListener(this);
        mEraserView.setOnClickListener(this);
        mClearView.setOnClickListener(this);


        mUndoView.setEnabled(false);
        mRedoView.setEnabled(false);

        mHandler = new Handler(this);

        mTextNoteTitle = (EditText) findViewById(R.id.textNoteTitle);
        mTextNoteTitle.setOnFocusChangeListener(this);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users").child(uid);

        storage = FirebaseStorage.getInstance();
        imagesRef = storage.getReference().child("images");

        Intent i = getIntent();
        String loadKey = i.getStringExtra(MainActivity.EXTRA_KEY);
        String loadTitle = i.getStringExtra(MainActivity.EXTRA_TITLE);

        if (!loadKey.equals("")) {
            mTextNoteTitle.setText(loadTitle);
            noteId = loadKey;
        } else {
            noteId = mNoteIdGenerator.generateNoteId();
        }
    }

    private void initSaveProgressDlg() {
        mSaveProgressDlg = new ProgressDialog(this);
        mSaveProgressDlg.setMessage("saving, please wait...");
        mSaveProgressDlg.setCancelable(false);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SAVE_FAILED:
                mSaveProgressDlg.dismiss();
                Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
                break;
            case MSG_SAVE_SUCCESS:
                mSaveProgressDlg.dismiss();
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onUndoRedoStatusChanged() {
        mUndoView.setEnabled(mDrawingFragment.canUndo());
        mRedoView.setEnabled(mDrawingFragment.canRedo());
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            String saveText;
            if (view == mTextNoteTitle) {
                saveText = mTextNoteTitle.getText().toString();
                mReference.child(noteId).child("title").setValue(saveText);
            }
        }
    }

    public void requestAllPower() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    protected void saveDataToDB() {
        EditText mNoteTitle = (EditText) findViewById(R.id.textNoteTitle);
        String data = savedFile;
        mReference.child(noteId).child("type").setValue("3");
        mReference.child(noteId).child("title").setValue(mNoteTitle.getText().toString());
        mReference.child(noteId).child("data").setValue(data);
    }

    @Override
    public void onClick(View v) {
        clearAllFocus();
        switch (v.getId()) {
            case R.id.undo:
                mDrawingFragment.undo();
                break;
            case R.id.redo:
                mDrawingFragment.redo();
                break;
            case R.id.pen:
                v.setSelected(true);
                mEraserView.setSelected(false);
                mDrawingFragment.setMode(DrawingFragment.Mode.DRAW);
                break;
            case R.id.eraser:
                v.setSelected(true);
                mPenView.setSelected(false);
                mDrawingFragment.setMode(DrawingFragment.Mode.ERASER);
                break;
            case R.id.clear:
                mDrawingFragment.clear();
                break;
            case R.id.backButton:
                saveDataToDB();
                finish();
                //break;
            //case R.id.saveButton:
                requestAllPower();
                if (mSaveProgressDlg == null) {
                    initSaveProgressDlg();
                }
                mSaveProgressDlg.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bm = mDrawingFragment.buildBitmap();
                        uploadPic(bm);
                    }
                }).start();
                break;
            case R.id.textNoteTitle:
                break;
            case R.id.trashButton:
                mReference.child(noteId).removeValue();
                finish();
                break;
        }
    }

    private void clearAllFocus() {
        mTextNoteTitle.clearFocus();
    }

    public void uploadPic(Bitmap bitmap) {
        StorageReference myImageRef = imagesRef.child(noteId + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = myImageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }
}