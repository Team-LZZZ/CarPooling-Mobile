package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.io.File;
import java.util.Locale;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.IConnectionStatus;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.LogInHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.UpdateUserHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.User;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonUtils;

public class ProfileActivity extends AppCompatActivity{

    @Bind(R.id.profile_imageView)
    ImageView mImageView;
    @Bind(R.id.profile_imageButton)
    ImageButton mImageButton;
    @Bind(R.id.profile_name)
    TextView mNameTextView;
    @Bind(R.id.profile_email)
    TextView mEmailTextView;
    @Bind(R.id.profile_phone)
    TextView mPhoneTextView;
    @Bind(R.id.profile_back_btn)
    Button mProfileBackBtn;

    private TransferUtility transferUtility;
    private TransferObserver transferObserver;
    private static final int REQUEST_PHOTO= 2;
    private ProgressDialog progressDialog;
    private static final String TAG = "UploadActivity";
    private UpdateUserHandler updateUserHandler;
    private User user;
    long time;
    Random random = new Random();
    private File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        user = AppContainer.getInstance().getActiveUser();
        mPhotoFile = getPhotoFile(user);
        transferUtility = CommonUtils.getTransferUtility(this);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = this.getPackageManager();
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mImageButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });
        CommonUtils.getProfile(this, user.getPhoto(), mImageView);
        mNameTextView.setText(user.getUsername());
        mEmailTextView.setText(user.getEmail());
        mPhoneTextView.setText(user.getPhone());

        mProfileBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void beginUpload(String filePath) {
        if (filePath == null) {
            Toast.makeText(this, "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);
        TransferObserver observer = transferUtility.upload(CommonConstants.BUCKET_NAME, file.getName() + time,
                file);
        observer.setTransferListener(new UploadListener());
        user.setPhoto(CommonConstants.S3_PREFIX + mPhotoFile.getName() + time);
        updateUserHandler = new UpdateUserHandler(new IConnectionStatus() {
            @Override
            public void onComplete(Boolean isSuccess, String additionalInfos) {
                progressDialog.dismiss();
                if (isSuccess){
                }else{
                        Toast.makeText(getBaseContext(), additionalInfos, Toast.LENGTH_LONG).show();
                }
            }
        });
        Gson gson = new Gson();
        User userInfo = new User();
        userInfo.setPhoto(user.getPhoto());
        String userJson = gson.toJson(userInfo);
        updateUserHandler.connectForResponse(CommonUtils.createHttpRequestMessageWithToken(userJson, CommonConstants.userSetting, "PUT"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        time = System.currentTimeMillis();
        progressDialog = CommonUtils.createProgressDialog(this, "Loading...");
        progressDialog.show();
        if (requestCode == REQUEST_PHOTO) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            beginUpload(mPhotoFile.getPath());
                            updatePhotoView();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }
    }

    public File getPhotoFile(User user) {
        File externalFilesDir = this
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, getPhotoFilename(user));
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mImageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = getScaledBitmap(
                    mPhotoFile.getPath(), this);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            mImageView.setImageBitmap(bitmap);
        }
    }

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);
    }

    private class UploadListener implements TransferListener {

        @Override
        public void onError(int id, Exception e) {
            Log.e(TAG, "Error during upload: " + id, e);
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d(TAG, "onStateChanged: " + id + ", " + newState);
        }
    }

    private void deleOldPhoto(String keyName){
        try {
            CommonUtils.getS3Client(this).deleteObject(new DeleteObjectRequest(CommonConstants.BUCKET_NAME, keyName));
        } catch (AmazonServiceException ase) {
            Log.i(CommonConstants.LogPrefix,"Caught an AmazonServiceException.");
            Log.i(CommonConstants.LogPrefix,"Error Message:    " + ase.getMessage());
            Log.i(CommonConstants.LogPrefix,"HTTP Status Code: " + ase.getStatusCode());
            Log.i(CommonConstants.LogPrefix,"AWS Error Code:   " + ase.getErrorCode());
            Log.i(CommonConstants.LogPrefix,"Error Type:       " + ase.getErrorType());
            Log.i(CommonConstants.LogPrefix,"Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            Log.i(CommonConstants.LogPrefix,"Caught an AmazonClientException.");
            Log.i(CommonConstants.LogPrefix,"Error Message: " + ace.getMessage());
        }
    }

    public String getPhotoFilename(User user) {
        return "IMG_" + user.getUsername();
    }
}

