package oliver.gaa_stats_tracker

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.login_page.*
import kotlinx.android.synthetic.main.welcome_page.*

class LoginPage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        loginConstraint.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))

        homeButton.setOnClickListener {
                loginConstraint.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out))
                Handler(Looper.getMainLooper()).postDelayed({
                    run {
                        startActivity(Intent(this, WelcomePage::class.java))
                        finish()
                    }
                }, 250)
        }
    }
}