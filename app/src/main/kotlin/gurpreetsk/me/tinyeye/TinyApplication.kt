package gurpreetsk.me.tinyeye

import android.app.Application
import gurpreetsk.me.tinyeye._injection.DaggerTinyComponent
import gurpreetsk.me.tinyeye._injection.TinyComponent
import gurpreetsk.me.tinyeye._injection.modules.TinyModule

class TinyApplication : Application() {
  private lateinit var component: TinyComponent

  override fun onCreate() {
    super.onCreate()
    component = setupDaggerComponent()
  }

  private fun setupDaggerComponent(): TinyComponent =
    DaggerTinyComponent
        .builder()
        .tinyModule(TinyModule())
        .build()

  fun component(): TinyComponent =
      component
}
