package gurpreetsk.me.tinyeye._base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class TinyActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(inflateLayout())

    bind()
  }

  abstract fun bind()

  abstract fun inflateLayout(): Int
}
