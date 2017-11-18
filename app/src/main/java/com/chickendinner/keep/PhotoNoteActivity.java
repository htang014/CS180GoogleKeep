package com.chickendinner.keep;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PhotoNoteActivity extends AppCompatActivity implements View.OnClickListener
{

    private ImageView mImageView;
    private ImageButton StartCameraBtn;
    private File photoFile;

    //requestCode
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private static final String albumName = "Camera";

    int targetW;
    int targetH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_note);

        mImageView = (ImageView) findViewById(R.id.imageView);
        targetW = mImageView.getWidth();
        targetH = mImageView.getHeight();

        StartCameraBtn = (ImageButton) findViewById(R.id.StartCamera);

        StartCameraBtn.setOnClickListener(this);
        try
        {
            photoFile = createFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.StartCamera:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
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
                    galleryAddPic();
                }
                break;
        }
    }

    private File getPhotoDir() {
        File storDirPublic = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storDirPublic = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    albumName);

            if (storDirPublic != null) {
                if (!storDirPublic.mkdirs()) {
                    if (!storDirPublic.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storDirPublic;

    }

    private File createFile() throws IOException {
        photoFile = null;

        String fileName;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fileName = JPEG_FILE_PREFIX + timeStamp + "_";

        photoFile = File.createTempFile(fileName, JPEG_FILE_SUFFIX, getPhotoDir());

        return photoFile;
    }

    private void setPic() {


        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
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

        mImageView.setImageBitmap(bitmap);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photoFile);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}
