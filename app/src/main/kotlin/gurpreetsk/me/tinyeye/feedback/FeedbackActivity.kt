package gurpreetsk.me.tinyeye.feedback

import android.support.design.widget.Snackbar
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import gurpreetsk.me.tinyeye.R
import gurpreetsk.me.tinyeye._base.TinyActivity
import gurpreetsk.me.tinyeye._injection.TinyComponent
import gurpreetsk.me.tinyeye._storage.contracts.RemoteRepository
import kotlinx.android.synthetic.main.activity_feedback.*

class FeedbackActivity : TinyActivity(), FeedbackView {
  private val remoteRepository: RemoteRepository by lazy { TinyComponent.obtain(this).remoteRepository() }
  private val presenter: FeedbackPresenter by lazy { FeedbackPresenterImpl(this, remoteRepository) }

  override fun inflateLayout(): Int =
      R.layout.activity_feedback

  override fun bind() {
    submitFeedbackButton.clicks()
        .map {
          val username = usernameFeedbackEditText.text.toString()
          val contact  = contactFeedbackEditText.text.toString()
          val feedback = feedbackFeedbackEditText.text.toString()

          presenter.validateForm(UserFeedback.create(username, contact, feedback))
        }
  }

  override fun showSuccess() {
    Toast.makeText(this@FeedbackActivity, "Thanks for your feedback", Toast.LENGTH_SHORT).show()
    setEditTextsEmpty()
  }

  override fun showFailure() {
    val snackbar = Snackbar.make(findViewById(R.id.activity_settings), "Please input something", Snackbar.LENGTH_INDEFINITE)
    snackbar.setAction(getString(R.string.ok)) { snackbar.dismiss() }
    snackbar.show()
  }

  private fun setEditTextsEmpty() {
    usernameFeedbackEditText.setText("")
    contactFeedbackEditText.setText("")
    feedbackFeedbackEditText.setText("")
  }
}
