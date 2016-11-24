package gurpreetsk.me.tinyeye.cloudvision.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import gurpreetsk.me.tinyeye.CreditsActivity;
import gurpreetsk.me.tinyeye.FeedbackActivity;
import gurpreetsk.me.tinyeye.R;
import gurpreetsk.me.tinyeye.cloudvision.fragments.WikiFragment;
import gurpreetsk.me.tinyeye.cloudvision.utils.Constants;

import static gurpreetsk.me.tinyeye.cloudvision.utils.Constants.WIKI_ACTIVITY_INTENT_KEY;
import static gurpreetsk.me.tinyeye.cloudvision.utils.Constants.WIKI_DATA_INTENT_KEY;

public class DetailActivity extends AppCompatActivity {

    String APIresponse;
    ArrayList<String> location;

    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        APIresponse = getIntent().getStringExtra(Constants.DATA_ACTIVITY_INTENT_KEY);
        location = getIntent().getStringArrayListExtra(Constants.MAP_FRAGMENT_INTENT_KEY);

        Toast.makeText(this, APIresponse, Toast.LENGTH_LONG).show();
        Log.i(TAG, "onCreate: " + APIresponse);

        Bundle bundle = new Bundle();
        bundle.putString(WIKI_DATA_INTENT_KEY, APIresponse);
        bundle.putStringArrayList(WIKI_ACTIVITY_INTENT_KEY, location);

        WikiFragment fragment = new WikiFragment();
        fragment.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.vision_fragment_framelayout, fragment);
        ft.commit();

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

