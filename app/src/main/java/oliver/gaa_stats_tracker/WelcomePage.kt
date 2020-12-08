package oliver.gaa_stats_tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.list_view_fragment.*
import kotlinx.android.synthetic.main.welcome_page.*
import oliver.gaa_stats_tracker.fragments.DataAdapter
import oliver.gaa_stats_tracker.models.Match
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class WelcomePage : AppCompatActivity(), AnkoLogger {

    var database: FirebaseDatabase? = null
    var ratingReference: DatabaseReference? = null
    var ratingList = ArrayList<Float>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_page)
        welcomeConstraint.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
        database = FirebaseDatabase.getInstance()
        ratingReference = database?.reference!!.child("Rating")
        ratingReference?.keepSynced(true)
        showRating()

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
        fun showRating() {
            info { "Hello World" }
           // ratingReference = database?.getReference("Rating")
            ratingReference?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.e("cancel", error.toString())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    ratingList.clear()
                    for (data in snapshot.children) {
                        var ratings = data.child("rating").value.toString().toFloat()
                        ratingList.add(ratings)
                        info{ratingList}
                    }
                    ratingBar.rating = ratingList.sum() / ratingList.size
                }
            })
        }

    }
