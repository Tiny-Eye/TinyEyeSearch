package gurpreetsk.me.tinyeye._storage.contracts

import gurpreetsk.me.tinyeye.feedback.UserFeedback
import io.reactivex.Observable

interface RemoteRepository {
  fun submitFeedback(userFeedback: UserFeedback): Observable<Boolean>
}
