package oliver.gaa_stats_tracker

import android.content.Intent
import android.database.SQLException
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.login_page.*
import kotlinx.android.synthetic.main.register_page.*
import org.jetbrains.anko.AnkoLogger


class LoginPage : AppCompatActivity(), AnkoLogger {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

//        val currentuser = auth.currentUser
//        if(currentuser != null){
//            startActivity(Intent(this, LoggedIn::class.java))
//            finish()
//        }

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
            loginUser()
        }

    }


    public override fun onStart() {
        super.onStart()
    }

    fun loginUser() {
        if (usernameLoginField.text.toString().isEmpty()) {
            usernameLoginField.error = "Email cannot be empty"
            usernameLoginField.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(usernameLoginField.text.toString()).matches()) {
            usernameLoginField.error = "Please enter a valid email"
            usernameLoginField.requestFocus()
            return
        }

        if (passwordLoginField.text.toString().isEmpty()) {
            passwordLoginField.error = "Please enter a password"
            passwordLoginField.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(
            usernameLoginField.text.toString(),
            passwordLoginField.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    loginConstraint.startAnimation(
                        AnimationUtils.loadAnimation(
                            this,
                            R.anim.fade_out
                        )
                    )
                    Handler(Looper.getMainLooper()).postDelayed({
                        run {
                            startActivity(Intent(this, LoggedIn::class.java))
                            finish()
                        }
                    }, 250)
                } else {
                    passwordLoginField.error = "Username Or Password Incorrect"
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Sign Up Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}