package gurpreetsk.me.tinyeye.ocr.UI.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import gurpreetsk.me.tinyeye.CreditsActivity;
import gurpreetsk.me.tinyeye.FeedbackActivity;
import gurpreetsk.me.tinyeye.R;
import gurpreetsk.me.tinyeye.cloudvision.utils.Constants;
import gurpreetsk.me.tinyeye.ocr.UI.fragments.WebViewFragment;

public class OCRMainActivity extends AppCompatActivity {

    String OCRresponse;
    private static final String TAG = "OCRMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrmain);

        OCRresponse = getIntent().getStringExtra(Constants.OCR_CAPTURE_ACTIVITY_INTENT_KEY);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.OCR_MAIN_ACTIVITY_INTENT_KEY, OCRresponse);

        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.lm_fragment, fragment);
        ft.commit();

        Log.i(TAG, "onCreate: "+ OCRresponse);
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

}