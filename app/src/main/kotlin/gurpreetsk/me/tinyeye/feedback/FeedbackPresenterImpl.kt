package gurpreetsk.me.tinyeye.feedback

object FeedbackPresenterImpl : FeedbackPresenter {
  override fun validateForm(userFeedback: UserFeedback): Boolean {
    val username = userFeedback.username()
    return (username.length > 2 && username.first().isLetter())
  }
}
