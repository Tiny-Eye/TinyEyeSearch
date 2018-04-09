package gurpreetsk.me.tinyeye.feedback

import gurpreetsk.me.tinyeye._common.isValidEmail
import gurpreetsk.me.tinyeye._storage.contracts.RemoteRepository
import io.reactivex.Observable

class FeedbackPresenterImpl(
    private val view: FeedbackView,
    private val remoteRepository: RemoteRepository
) : FeedbackPresenter {
  override fun validateForm(userFeedback: UserFeedback) {
    val username = userFeedback.username()
    val contact  = userFeedback.contact()
    val feedback = userFeedback.feedback()

    val isValidUsername = username.length > 2 && username.first().isLetter()
    val isValidContact  = contact.isValidEmail()
    val isValidFeedback = feedback.length >= 20

    if (isValidUsername && isValidContact && isValidFeedback) {
      val remoteCall = remoteRepository.submitFeedback(userFeedback)
      Observable.merge(
          remoteCall.filter { it } .map { view.showSuccess() },
          remoteCall.filter { !it }.map { view.showFailure() }
      ).subscribe() // TODO(gs) 9/Apr/18 - Dispose this.
    } else {
      view.showFailure()
    }
  }
}
