package gurpreetsk.me.tinyeye.feedback

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class FeedbackPresenterTests {
  private lateinit var presenter: FeedbackPresenter

  @Before fun setup() {
    val view = mock(FeedbackView::class.java)
    presenter = FeedbackPresenterImpl(view)
  }

  @Test fun `username under 2 characters is invalid`() {
    val invalidFormWithShortUsername = UserFeedback.create("A", "@imGurpreetSK", "Nice app!")

    // Assert
    assertThat(presenter.validateForm(invalidFormWithShortUsername))
        .isFalse()
  }

  @Test fun `username over 2 characters is valid`() {
    val validFormWithUsername = UserFeedback.create("Aman", "imGurpreetSK@twitter.me", "Nice app right there!")

    // Assert
    assertThat(presenter.validateForm(validFormWithUsername))
        .isTrue()
  }

  @Test fun `username cannot start with special character`() {
    val invalidFormWithInvalidUsername = UserFeedback.create("!@#W", "", "")

    // Assert
    assertThat(presenter.validateForm(invalidFormWithInvalidUsername))
        .isFalse()
  }

  @Test fun `only valid emails are accepted`() {
    val validFormWithValidContact = UserFeedback.create("Gurpreet", "gurpreetsk@hotmail.com", "Nice app right there!")

    // Assert
    assertThat(presenter.validateForm(validFormWithValidContact))
        .isTrue()
  }

  @Test fun `invalid emails are not accepted`() {
    val invalidFormWithInValidContact = UserFeedback.create("Gurpreet", "@imGurpreetSK", "")

    // Assert
    assertThat(presenter.validateForm(invalidFormWithInValidContact))
        .isFalse()
  }

  @Test fun `feedback less than 20 characters are invalid`() {
    val invalidFormWithInValidFeedback = UserFeedback.create("Gurpreet", "gurpreetsk@hotmail", "")

    // Assert
    assertThat(presenter.validateForm(invalidFormWithInValidFeedback))
        .isFalse()
  }
}
