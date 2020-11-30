package oliver.gaa_stats_tracker

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.login_page.*
import kotlinx.android.synthetic.main.register_page.*
import kotlinx.android.synthetic.main.welcome_page.*
import org.jetbrains.anko.AnkoLogger

class RegisterPage: AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)
        registerConstraint.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))

        returnButton.setOnClickListener {
            registerConstraint.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out))
            Handler(Looper.getMainLooper()).postDelayed({
                run {
                    startActivity(Intent(this, WelcomePage::class.java))
                    finish()
                }
            }, 250)
        }

        registerButton2.setOnClickListener {
            registerConstraint.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out))
            Handler(Looper.getMainLooper()).postDelayed({
                run {
                    startActivity(Intent(this, LoginPage::class.java))
                    finish()
                }
            }, 250)
        }
    }
}