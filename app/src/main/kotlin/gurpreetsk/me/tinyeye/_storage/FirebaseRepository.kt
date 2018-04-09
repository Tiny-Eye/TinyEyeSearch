package gurpreetsk.me.tinyeye._storage

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import gurpreetsk.me.tinyeye._storage.contracts.RemoteRepository
import gurpreetsk.me.tinyeye.feedback.UserFeedback
import io.reactivex.Observable

class FirebaseRepository : RemoteRepository {
  private val databaseReference: DatabaseReference by lazy { FirebaseDatabase.getInstance().reference }

  override fun submitFeedback(userFeedback: UserFeedback): Observable<Boolean> {
    return Observable
        .fromCallable { databaseReference.child("feedback").push().setValue(userFeedback).isComplete }
  }
}
