package com.chickendinner.keep;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chickendinner.keep.recycler.CheckListBean;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class PhotoNoteActivity extends NoteActivity implements View.OnClickListener, View.OnFocusChangeListener
{

    private EditText mTextNoteTitle;
    private String noteId;

    private ImageView mImageView;
    private ImageButton StartCameraBtn;
    private File photoFile = null;

    private String mCurrentPhotoPath;
    private String mResultPhotoPath;

    private ProgressDialog mSaveProgressDlg;
    private static final int MSG_SAVE_SUCCESS = 1;
    private static final int MSG_SAVE_FAILED = 2;
    private Handler mHandler;

    //requestCode
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_note);

        mTextNoteTitle = (EditText) findViewById(R.id.textNoteTitle);
        mImageView = (ImageView) findViewById(R.id.imageView);

        mEditTime = (TextView) findViewById(R.id.editTime);
        cal = Calendar.getInstance();
        //updateTime();

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users").child(uid);
        //noteId = mNoteIdGenerator.generateNoteId();

        Intent i = getIntent();
        if (i != null && i.getStringExtra("noteId") != null) {
            EditText mNoteTitle = (EditText) findViewById(R.id.textNoteTitle);
            mNoteTitle.setText(i.getStringExtra("title"));
            noteId = i.getStringExtra("noteId");
            mReference.child(noteId).child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    setSavedData((Map<String, Object>) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            noteId = mNoteIdGenerator.generateNoteId();
        }

        StartCameraBtn = (ImageButton) findViewById(R.id.StartCamera);

        StartCameraBtn.setOnClickListener(this);
    }

    protected void setSavedData(Map<String, Object> data)
    {
        String res = (String) data.get("data");
        setPic();
    }

    //Todo add database part here
    protected void saveDataToDB() {
        EditText mNoteTitle = (EditText) findViewById(R.id.textNoteTitle);
        String data = mResultPhotoPath;
        mReference.child(noteId).child("type").setValue("3");
        mReference.child(noteId).child("title").setValue(mNoteTitle.getText().toString());
        mReference.child(noteId).child("data").setValue(data);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            String saveText;
            if (view == mTextNoteTitle) {
                saveText = mTextNoteTitle.getText().toString();
                mReference.child("title").setValue(saveText);
            }
        }
    }

    private void clearAllFocus()
    {
        mTextNoteTitle.clearFocus();
    }

    @Override
    public void onClick(View view)
    {
        clearAllFocus();
        switch (view.getId())
        {
            case R.id.StartCamera:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                {
                    try
                    {
                        photoFile = createFile();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    if(photoFile != null){
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.chickendinner.keep",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
                break;

            case R.id.textNoteTitle:
                break;

            case R.id.clear:
                mImageView.setImageBitmap(null);
                break;

            case R.id.save:
                saveDataToDB();
                finish();
                break;

            case R.id.backButton:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        mImageView.setImageBitmap(null);

        switch (requestCode)
        {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK)
                {
                    setPic();
                    //galleryAddPic();
                }
                break;
        }
    }

    private File createFile() throws IOException
    {
        // Create an image file name
        String imageFileName = System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);


        String savedFile = saveImage(bitmap, 100);
        mResultPhotoPath = savedFile;

        scanFile(PhotoNoteActivity.this, savedFile);
        mImageView.setImageBitmap(bitmap);
    }

    private static String saveImage(Bitmap bmp, int quality) {
        if (bmp == null) {
            return null;
        }
        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (appDir == null) {
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timeStamp + ".jpg";
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

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

    private static void scanFile(Context context, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(scanIntent);
    }
}
