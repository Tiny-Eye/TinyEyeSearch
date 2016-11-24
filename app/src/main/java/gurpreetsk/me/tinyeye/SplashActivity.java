package gurpreetsk.me.tinyeye;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.File;

import gurpreetsk.me.tinyeye.cloudvision.activities.MainActivity;
import gurpreetsk.me.tinyeye.ocr.OcrCaptureActivity;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Fabric.with(this, new Crashlytics());

        TextView Button_OCR_search = (TextView) findViewById(R.id.button_ocr_search);
        TextView Button_other_search = (TextView) findViewById(R.id.button_other_search);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }

        Button_OCR_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, OcrCaptureActivity.class));
            }
        });

        Button_other_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/gurpreetsk.me.tinyeye/cache/");
                    if (!dir.exists()) {
                        if (dir.mkdirs())       //TODO: make this work
                            Log.i(TAG, "onCreate: Made directories");
                    }
                } else {
                    Toast.makeText(this, "Image input through camera won't work properly.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
