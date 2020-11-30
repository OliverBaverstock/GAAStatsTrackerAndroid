package oliver.gaa_stats_tracker

import android.content.Intent
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
import kotlinx.android.synthetic.main.welcome_page.*
import org.jetbrains.anko.AnkoLogger

class RegisterPage: AppCompatActivity(), AnkoLogger {

    private lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Profile")
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

            signUpUser()
        }
    }

    fun signUpUser(){
            if(usernameField.text.toString().isEmpty()){
                usernameField.error = "Email cannot be empty"
                usernameField.requestFocus()
                return
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(usernameField.text.toString()).matches()){
                usernameField.error = "Please enter a valid email"
                usernameField.requestFocus()
                return
            }

            if(registerPassword.text.toString().isEmpty()){
                registerPassword.error = "Please enter a password"
                registerPassword.requestFocus()
                return
            }

        auth.createUserWithEmailAndPassword(usernameField.text.toString(), registerPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    val currentUserDB = databaseReference?.child(currentUser?.uid!!)
                    currentUserDB?.child("team_name")?.setValue(teamNameField.text.toString())
                    currentUserDB?.child("manager_name")?.setValue(managerNameField.text.toString())
                    currentUserDB?.child("captain_name")?.setValue(captainNameField.text.toString())

                    registerConstraint.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out))
                    Handler(Looper.getMainLooper()).postDelayed({
                        run {
                            startActivity(Intent(this, LoginPage::class.java))
                            finish()
                        }
                    }, 250)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Sign Up Failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }


}