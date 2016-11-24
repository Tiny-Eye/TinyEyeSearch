package gurpreetsk.me.tinyeye.cloudvision.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gurpreetsk.me.tinyeye.CreditsActivity;
import gurpreetsk.me.tinyeye.FeedbackActivity;
import gurpreetsk.me.tinyeye.R;
import gurpreetsk.me.tinyeye.cloudvision.adapter.ResponseAdapter;
import gurpreetsk.me.tinyeye.cloudvision.utils.Constants;

public class DataActivity extends AppCompatActivity {

    private ArrayList<String> responselist = new ArrayList<>();
    private ArrayList<String> location = new ArrayList<>();
    private ListView listView;
    private ResponseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        TextView tv = (TextView) findViewById(R.id.no_result_tv);
        responselist =  getIntent().getStringArrayListExtra(Constants.MAIN_ACTIVITY_INTENT_KEY);
        location = getIntent().getStringArrayListExtra(Constants.MAP_ACTIVITY_INTENT_KEY);

        listView = (ListView) findViewById(R.id.response_list);
        adapter = new ResponseAdapter(this.getApplicationContext(), responselist, location);
        listView.setAdapter(adapter);

        if(adapter.isEmpty()) {
            tv.setVisibility(View.VISIBLE);
        }else{
            tv.setVisibility(View.GONE);
        }

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
