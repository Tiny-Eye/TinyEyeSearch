package gurpreetsk.me.tinyeye.cloudvision.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.firebase.crash.FirebaseCrash;

import gurpreetsk.me.tinyeye.BuildConfig;
import gurpreetsk.me.tinyeye.CreditsActivity;
import gurpreetsk.me.tinyeye.FeedbackActivity;
import gurpreetsk.me.tinyeye.R;
import gurpreetsk.me.tinyeye.cloudvision.adapter.ResponseAdapter;
import gurpreetsk.me.tinyeye.cloudvision.utils.Constants;
import gurpreetsk.me.tinyeye.cloudvision.utils.PermissionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static gurpreetsk.me.tinyeye.cloudvision.utils.VisionKey.CLOUD_VISION_API_KEY;

public class MainActivity extends AppCompatActivity {
    public static final String FILE_NAME = "tiny_eye.jpg";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    private TextView mImageDetails;
    private ImageView mMainImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder
                        .setMessage(R.string.dialog_select_prompt)
                        .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startGalleryChooser();
                            }
                        })
                        .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startCamera();
                            }
                        });
                builder.create().show();
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FirebaseCrash.report(new RuntimeException("LongPress"));
                return true;
            }
        });

        mImageDetails = (TextView) findViewById(R.id.image_details);
        mMainImage = (ImageView) findViewById(R.id.main_image);

        mImageDetails.setText("Use the floating action button\nto select the image");

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_credits) {
            startActivity(new Intent(this, CreditsActivity.class));
            return true;
        }

        if (id == R.id.action_feedback) {
            startActivity(new Intent(this, FeedbackActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startGalleryChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a photo"), GALLERY_IMAGE_REQUEST);
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            Uri photoURI;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                photoURI = FileProvider.getUriForFile(MainActivity.this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            else
                photoURI = Uri.fromFile(getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/gurpreetsk.me.tinyeye/cache/");     //TODO: Change to external storage
        Log.i(TAG, "getCameraFile: DIRpath: " + dir);
        return new File(dir + "/" + FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: request-code " + requestCode + " result-code " + resultCode);
        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {        //TODO: resolve error
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                uploadImage(FileProvider.getUriForFile(MainActivity.this,  getApplicationContext().getPackageName() + ".provider", getCameraFile()));
//            else
            uploadImage(Uri.fromFile(getCameraFile()));
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.permissionGranted(
                requestCode,
                CAMERA_PERMISSIONS_REQUEST,
                grantResults)) {
            startCamera();
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                1200);

                callCloudVision(bitmap);
                mMainImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                FirebaseCrash.log("Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            FirebaseCrash.log("Image picker gave us a null image");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading
        mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(new
                            VisionRequestInitializer(CLOUD_VISION_API_KEY));
                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        List<Feature> featureList = new ArrayList<>();
                        Feature labelDetection = new Feature();
                        labelDetection.setType("LABEL_DETECTION");
                        labelDetection.setMaxResults(5);
                        featureList.add(labelDetection);

                        Feature textDetection = new Feature();
                        textDetection.setType("TEXT_DETECTION");
                        textDetection.setMaxResults(5);
                        featureList.add(textDetection);

                        Feature landmarkDetection = new Feature();
                        landmarkDetection.setType("LANDMARK_DETECTION");
                        landmarkDetection.setMaxResults(5);
                        featureList.add(landmarkDetection);

                        Feature logoDetection = new Feature();
                        logoDetection.setType("LOGO_DETECTION");
                        logoDetection.setMaxResults(5);
                        featureList.add(logoDetection);

//                        Feature faceDetection = new Feature();
//                        faceDetection.setType("FACE_DETECTION");
//                        faceDetection.setMaxResults(5);
//                        featureList.add(faceDetection);

                        // add the features we want
                        annotateImageRequest.setFeatures(featureList);

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);

                    //TODO: resolve bug
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    startDetailActivity(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                    FirebaseCrash.log(e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                    FirebaseCrash.log(e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                mImageDetails.setText(result);
            }
        }.execute();
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private void startDetailActivity(BatchAnnotateImagesResponse response) {
        ArrayList<String> responselist = new ArrayList<>();
        Log.i(TAG, "startDetailActivity: Response: " + response);
        ArrayList<String> location = new ArrayList<>();
        try {
            for (int i = 0; i < response.getResponses().get(0).getLandmarkAnnotations().size(); i++) {
                String landmark = response.getResponses().get(0).getLandmarkAnnotations().get(i).getDescription();
                String lat = response.getResponses().get(0).getLandmarkAnnotations().get(i).getLocations().get(0).getLatLng().getLatitude().toString();
                String lon = response.getResponses().get(0).getLandmarkAnnotations().get(i).getLocations().get(0).getLatLng().getLongitude().toString();
                responselist.add(landmark);
                location.add(lat);
                location.add(lon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < response.getResponses().get(0).getLabelAnnotations().size(); i++) {
                String label = response.getResponses().get(0).getLabelAnnotations().get(i).getDescription();
                responselist.add(label);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < response.getResponses().get(0).getLogoAnnotations().size(); i++) {
                String logo = response.getResponses().get(0).getLogoAnnotations().get(i).getDescription();
                responselist.add(logo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < response.getResponses().get(0).getTextAnnotations().size(); i++) {
                String text = response.getResponses().get(0).getTextAnnotations().get(i).getDescription();
                responselist.add(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(MainActivity.this, DataActivity.class);
        intent.putStringArrayListExtra(Constants.MAIN_ACTIVITY_INTENT_KEY, responselist);
        intent.putStringArrayListExtra(Constants.MAP_ACTIVITY_INTENT_KEY, location);
        startActivity(intent);

    }
}
