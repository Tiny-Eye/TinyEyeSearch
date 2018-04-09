package gurpreetsk.me.tinyeye.feedback

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import gurpreetsk.me.tinyeye._storage.contracts.RemoteRepository
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class FeedbackPresenterTests {
  private lateinit var view: FeedbackView
  private lateinit var presenter: FeedbackPresenter
  private lateinit var remoteRepository: RemoteRepository

  @Before fun setup() {
    view = mock(FeedbackView::class.java)
    remoteRepository = mock(RemoteRepository::class.java)

    presenter = FeedbackPresenterImpl(view, remoteRepository)
  }

  @Test fun `username under 2 characters is invalid`() {
    val invalidFormWithShortUsername = UserFeedback.create("A", "@imGurpreetSK", "Nice app!")

    // Act
    presenter.validateForm(invalidFormWithShortUsername)

    // Verify
    verify(view, times(1)).showFailure()
    verify(remoteRepository, never()).submitFeedback(any())
  }

  @Test fun `username over 2 characters is valid`() {
    val validFormWithUsername = UserFeedback.create("Aman", "imGurpreetSK@twitter.me", "Nice app right there!")
    `when`(remoteRepository.submitFeedback(validFormWithUsername))
        .thenReturn(Observable.just(true))

    // Act
    presenter.validateForm(validFormWithUsername)

    // Verify
    verify(view, times(1)).showSuccess()
    verify(remoteRepository, times(1)).submitFeedback(validFormWithUsername)
  }

  @Test fun `username cannot start with special character`() {
    val invalidFormWithInvalidUsername = UserFeedback.create("!@#W", "", "")

    // Act
    presenter.validateForm(invalidFormWithInvalidUsername)

    // Verify
    verify(view, times(1)).showFailure()
    verify(remoteRepository, never()).submitFeedback(any())
  }

  @Test fun `only valid emails are accepted`() {
    val validFormWithValidContact = UserFeedback.create("Gurpreet", "gurpreetsk@hotmail.com", "Nice app right there!")
    `when`(remoteRepository.submitFeedback(validFormWithValidContact))
        .thenReturn(Observable.just(true))

    // Act
    presenter.validateForm(validFormWithValidContact)

    // Verify
    verify(view, times(1)).showSuccess()
    verify(remoteRepository, times(1)).submitFeedback(validFormWithValidContact)
  }

  @Test fun `invalid emails are not accepted`() {
    val invalidFormWithInValidContact = UserFeedback.create("Gurpreet", "@imurpreetSK", "")

    // Act
    presenter.validateForm(invalidFormWithInValidContact)

    // Verify
    verify(view, times(1)).showFailure()
    verify(remoteRepository, never()).submitFeedback(any())
  }

  @Test fun `feedback less than 20 characters are invalid`() {
    val invalidFormWithInValidFeedback = UserFeedback.create("Gurpreet", "gurpreetsk@hotmail", "")

    // Act
    presenter.validateForm(invalidFormWithInValidFeedback)

    // Verify
    verify(view, times(1)).showFailure()
    verify(remoteRepository, never()).submitFeedback(any())
  }
}
