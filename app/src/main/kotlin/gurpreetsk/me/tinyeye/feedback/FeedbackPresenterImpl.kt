package gurpreetsk.me.tinyeye.feedback

import gurpreetsk.me.tinyeye.common.isValidEmail

class FeedbackPresenterImpl(view: FeedbackView) : FeedbackPresenter {
  override fun validateForm(userFeedback: UserFeedback): Boolean {
    val username = userFeedback.username()
    val contact  = userFeedback.contact()
    val feedback = userFeedback.feedback()

    return (
        (username.length > 2 && username.first().isLetter())
        && contact.isValidEmail()
        && feedback.length >= 20
        )
  }
}
