import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CanaryTest {
  @Test fun `This would fail if test environment if wrongly setup`() {
    assertThat(true)
        .isTrue()
  }
}
