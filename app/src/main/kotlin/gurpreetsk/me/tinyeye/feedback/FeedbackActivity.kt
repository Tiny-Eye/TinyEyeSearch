package gurpreetsk.me.tinyeye.feedback

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.crash.FirebaseCrash
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxbinding2.view.clicks
import gurpreetsk.me.tinyeye.R
import gurpreetsk.me.tinyeye.base.TinyActivity
import kotlinx.android.synthetic.main.activity_feedback.*

class FeedbackActivity : TinyActivity(), FeedbackView {
  private val databaseReference: DatabaseReference by lazy { FirebaseDatabase.getInstance().reference }
  private val presenter: FeedbackPresenter by lazy { FeedbackPresenterImpl(this) }

  override fun inflateLayout(): Int =
      R.layout.activity_feedback

  override fun bind() {
    try {
      val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      submitFeedbackButton.clicks()
          .map {
            val username = usernameFeedbackEditText.text.toString()
            val contact  = contactFeedbackEditText.text.toString()
            val feedback = feedbackFeedbackEditText.text.toString()
            if (username.length > 5 && contact.length > 8 && feedback.length > 20) { // TODO(gs) 8/Apr/18 - Get a better logic for this. Also, why handle softInput?
              imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
              sendToDatabase(username, contact, feedback)
              Toast.makeText(this@FeedbackActivity, "Thanks for your feedback", Toast.LENGTH_SHORT).show()
              setEditTextsEmpty()
            } else {
              imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
              val snackbar = Snackbar.make(findViewById(R.id.activity_settings), "Please input something", Snackbar.LENGTH_INDEFINITE)
              snackbar.setAction(getString(R.string.ok)) { snackbar.dismiss() }
              snackbar.show()
            }
          }
    } catch (e: Exception) {
      e.printStackTrace()
      FirebaseCrash.report(e)
    }
  }

  private fun sendToDatabase(userName: String, userContact: String, userFeedback: String) {
    val user = UserFeedback.create(userName, userContact, userFeedback)
    databaseReference.child("feedback").push().setValue(user)
  }

  private fun setEditTextsEmpty() {
    usernameFeedbackEditText.setText("")
    contactFeedbackEditText.setText("")
    feedbackFeedbackEditText.setText("")
  }
}
