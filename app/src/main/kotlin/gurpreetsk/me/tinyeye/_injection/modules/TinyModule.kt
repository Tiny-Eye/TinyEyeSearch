package gurpreetsk.me.tinyeye._injection.modules

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import gurpreetsk.me.tinyeye._storage.FirebaseRepository
import gurpreetsk.me.tinyeye._storage.contracts.RemoteRepository

@Module class TinyModule {
  @Provides fun provideDatabaseReference(): DatabaseReference =
      FirebaseDatabase.getInstance().reference

  @Provides fun provideFirebaseRepository(databaseReference: DatabaseReference): RemoteRepository {
    return FirebaseRepository(databaseReference)
  }
}
