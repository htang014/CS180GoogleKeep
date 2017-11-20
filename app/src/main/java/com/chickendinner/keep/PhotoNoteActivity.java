package com.chickendinner.keep;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PhotoNoteActivity extends NoteActivity implements View.OnClickListener, View.OnFocusChangeListener
{

    private EditText mTextNoteTitle;
    private String noteId;

    private ImageView mImageView;
    private ImageButton StartCameraBtn;
    private File photoFile = null;

    private String mCurrentPhotoPath;

    //requestCode
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private static final String albumName = "KeepAlbum";

    //int targetW;
    //int targetH;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_note);

        mTextNoteTitle = (EditText) findViewById(R.id.textNoteTitle);
        mImageView = (ImageView) findViewById(R.id.imageView);

        mEditTime = (TextView) findViewById(R.id.editTime);
        cal = Calendar.getInstance();
        updateTime();

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("users").child(uid);
        noteId = mNoteIdGenerator.generateNoteId();

        //targetW = mImageView.getWidth();
        //targetH = mImageView.getHeight();

        StartCameraBtn = (ImageButton) findViewById(R.id.StartCamera);

        StartCameraBtn.setOnClickListener(this);
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

    @Override
    public void onClick(View view)
    {
        clearAllFocus();
        switch (view.getId())
        {
            case R.id.StartCamera:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                try
//                {
//                    photoFile = createFile();
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
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
                    //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                break;

            case R.id.textNoteTitle:

            case R.id.clear:
                mImageView.setImageBitmap(null);
                break;

            case R.id.save:
                finish();
                break;

            case R.id.backButton:
                finish();
                break;
        }
    }

    private void clearAllFocus()
    {
        mTextNoteTitle.clearFocus();
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

                    /*BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);

                    int photoW = bmOptions.outWidth;
                    int photoH = bmOptions.outHeight;


                    int scaleFactor = 1;
                    if ((targetW > 0) || (targetH > 0)) {
                        scaleFactor = Math.min(photoW / targetW, photoH / targetH);
                    }

                    bmOptions.inJustDecodeBounds = false;
                    bmOptions.inSampleSize = scaleFactor;
                    bmOptions.inPurgeable = true;

                    Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);

                    mImageView.setImageBitmap(bitmap);*/

//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    mImageView.setImageBitmap(photo);
                    setPic();
                    galleryAddPic();
                }
                break;
        }
    }

//    private File getPhotoDir()
//    {
//        File storDirPublic = null;
//
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            storDirPublic = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                    albumName);
//
//            if (storDirPublic != null) {
//                if (!storDirPublic.mkdirs()) {
//                    if (!storDirPublic.exists()) {
//                        Log.d("CameraSample", "failed to create directory");
//                        return null;
//                    }
//                }
//            }
//        } else {
//            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
//        }
//
//        return storDirPublic;
//
//    }

    private File createFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
//        photoFile = null;
//
//        String fileName;
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        fileName = JPEG_FILE_PREFIX + timeStamp + "_";
//
//        photoFile = File.createTempFile(fileName, JPEG_FILE_SUFFIX, getPhotoDir());
//
//        return photoFile;
    }

    private void galleryAddPic()
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri contentUri = Uri.fromFile(photoFile);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
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
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }
}
