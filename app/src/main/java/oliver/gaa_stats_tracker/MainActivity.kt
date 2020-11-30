package oliver.gaa_stats_tracker

import android.content.Intent
import android.database.SQLException
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import oliver.gaa_stats_tracker.models.Match
import org.jetbrains.anko.AnkoLogger

class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        welcomeLabel2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.top_animation))
        animationView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_animation))

        Handler(Looper.getMainLooper()).postDelayed({
            run {
                mainConstraint.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out))
            }
        }, 5000)
        Handler(Looper.getMainLooper()).postDelayed({
            run {
                startActivity(Intent(this, WelcomePage::class.java))
                finish()
            }
        }, 5000)
    }
}