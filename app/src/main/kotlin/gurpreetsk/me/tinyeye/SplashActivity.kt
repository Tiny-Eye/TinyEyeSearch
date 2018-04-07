package gurpreetsk.me.tinyeye

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.clicks
import gurpreetsk.me.tinyeye.cloudvision.activities.MainActivity
import gurpreetsk.me.tinyeye.ocr.OcrCaptureActivity
import kotlinx.android.synthetic.main.activity_splash.recogniseOcrButton
import kotlinx.android.synthetic.main.activity_splash.recogniseImageButton

class SplashActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    bind()
  }

  private fun bind() {
    recogniseOcrButton.clicks()
        .doOnNext { startActivity(Intent(this@SplashActivity, OcrCaptureActivity::class.java)) }
    recogniseImageButton.clicks()
        .doOnNext { startActivity(Intent(this@SplashActivity, MainActivity::class.java)) }
  }
}
