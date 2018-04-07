package gurpreetsk.me.tinyeye.feedback

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FeedbackPresenterTests {
  @Test fun `username under 2 characters is invalid`() {
    val invalidFormWithShortUsername = UserFeedback.create("A", "@imGurpreetSK", "Nice app!")

    // Assert
    assertThat(FeedbackPresenterImpl.validateForm(invalidFormWithShortUsername))
        .isFalse()
  }

  @Test fun `username over 2 characters is valid`() {
    val validFormWithShortUsername = UserFeedback.create("Aman", "@imGurpreetSK", "Nice app!")

    // Assert
    assertThat(FeedbackPresenterImpl.validateForm(validFormWithShortUsername))
        .isTrue()
  }

  @Test fun `username cannot start with special character`() {
    val invalidFormWithInvalidUsername = UserFeedback.create("!@#W", "", "")

    // Assert
    assertThat(FeedbackPresenterImpl.validateForm(invalidFormWithInvalidUsername))
        .isFalse()
  }
}
