package oliver.gaa_stats_tracker

import android.content.Intent
import android.database.SQLException
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login_page.*
import org.jetbrains.anko.AnkoLogger


class LoginPage : AppCompatActivity(), AnkoLogger {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
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
        signinButton.setOnClickListener {
            var uname = usernameLoginField.text.toString()
            var pword = passwordLoginField.text.toString()
            System.out.print(uname)
            System.out.print(pword)
        }

    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser : FirebaseUser?){

    }

}