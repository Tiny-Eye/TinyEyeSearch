package gurpreetsk.me.tinyeye;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue abstract class User {
  public abstract String name();
  public abstract String contact();
  public abstract String feedback();

  public static User create(String name, String contact, String feedback) {
    return new AutoValue_User(name, contact, feedback);
  }

  public static JsonAdapter<User> jsonAdapter(Moshi moshi) {
    return new AutoValue_User.MoshiJsonAdapter(moshi);
  }
}
