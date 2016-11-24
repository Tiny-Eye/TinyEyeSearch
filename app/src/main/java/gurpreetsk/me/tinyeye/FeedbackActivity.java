package gurpreetsk.me.tinyeye;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {

    EditText name, contact, feedback;
    Button sendFeedback;

    String userName, userContact, userFeedback;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getHandles();

        try {
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            sendFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userName = name.getText().toString();
                    userContact = contact.getText().toString();
                    userFeedback = feedback.getText().toString();
                    if (name.getText().length() > 5 && contact.getText().length() > 8 && feedback.getText().length() > 20) {
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        sendToDatabase(userName, userContact, userFeedback);
                        Toast.makeText(FeedbackActivity.this, "Thanks for your feedback", Toast.LENGTH_SHORT).show();
                        setEditTextsEmpty();
                    } else {
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_settings),"Please input something", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction(getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            FirebaseCrash.report(e);
        }

    }

    private void sendToDatabase(String userName, String userContact, String userFeedback) {

        FirebaseModel user = new FirebaseModel(userName, userContact, userFeedback);
        mDatabase.child("feedback").push().setValue(user);

    }

    private void setEditTextsEmpty() {

        name.setText("");
        contact.setText("");
        feedback.setText("");
        userName = "";
        userContact = "";
        userFeedback = "";

    }

    private void getHandles() {

        name = (EditText) findViewById(R.id.username_feedback_edittext);
        contact = (EditText) findViewById(R.id.contact_feedback_edittext);
        feedback = (EditText) findViewById(R.id.feedback_feedback_edittext);
        sendFeedback = (Button) findViewById(R.id.submit_feedback_button);

    }
}
