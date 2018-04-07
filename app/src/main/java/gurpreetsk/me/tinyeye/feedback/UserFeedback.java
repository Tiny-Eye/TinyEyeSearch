package gurpreetsk.me.tinyeye.feedback;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue public abstract class UserFeedback {
  @NonNull public abstract String username();
  @NonNull public abstract String contact();
  @NonNull public abstract String feedback();

  public static UserFeedback create(String username, String contact, String feedback) {
    return new AutoValue_UserFeedback(username, contact, feedback);
  }

  public static JsonAdapter<UserFeedback> jsonAdapter(Moshi moshi) {
    return new AutoValue_UserFeedback.MoshiJsonAdapter(moshi);
  }
}
