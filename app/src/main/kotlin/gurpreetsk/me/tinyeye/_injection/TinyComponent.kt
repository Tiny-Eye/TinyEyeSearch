package gurpreetsk.me.tinyeye._injection

import android.content.Context
import dagger.Component
import gurpreetsk.me.tinyeye.TinyApplication
import gurpreetsk.me.tinyeye._injection.modules.PresentersModule
import gurpreetsk.me.tinyeye._injection.modules.TinyModule
import gurpreetsk.me.tinyeye._storage.contracts.RemoteRepository

@Component(modules = [
  TinyModule::class,
  PresentersModule::class
])
interface TinyComponent {
  fun remoteRepository(): RemoteRepository

  companion object {
    fun obtain(context: Context): TinyComponent {
      return (context.applicationContext as TinyApplication).component()
    }
  }
}
