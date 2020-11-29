package oliver.gaa_stats_tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.welcome_page.*

class WelcomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_page)
        welcomeConstraint.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))

        //Called when login button is clicked
        loginButton.setOnClickListener {
            welcomeConstraint.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out))
            Handler(Looper.getMainLooper()).postDelayed({
                run {
                    startActivity(Intent(this, LoginPage::class.java))
                    finish()
                }
            }, 250)
        }

        //Called when register button is clicked
        registerButton.setOnClickListener {
            welcomeConstraint.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out))
            Handler(Looper.getMainLooper()).postDelayed({
                run {
                    startActivity(Intent(this, RegisterPage::class.java))
                    finish()
                }
            }, 250)
        }
        }
    }
