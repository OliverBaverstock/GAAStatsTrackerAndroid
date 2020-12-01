package oliver.gaa_stats_tracker.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.new_match_fragment.*
import kotlinx.android.synthetic.main.register_page.*
import oliver.gaa_stats_tracker.LoginPage
import oliver.gaa_stats_tracker.R
import com.airbnb.lottie.LottieAnimationView

class NewMatchFragment : Fragment() {


    lateinit var auth: FirebaseAuth
    var profileReference: DatabaseReference? = null
    var matchesReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    lateinit var homeName: String
    lateinit var lottieAnimationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        matchesReference = database?.reference!!.child("Matches")
        profileReference = database?.reference!!.child("Profile")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.new_match_fragment, container, false)
        var addMatchButton: View = view.findViewById(R.id.addMatchButton)
        lottieAnimationView = view.findViewById(R.id.celebration)
        loadProfile()
        addMatchButton.setOnClickListener {
            addMatch()
        }
        return view
    }

    fun addMatch() {

        if (homeGoalsField.text.toString().isEmpty()) {
            homeGoalsField.error = "Please enter a valid score"
            homeGoalsField.requestFocus()
            return
        }

        if (homePointsField.text.toString().isEmpty()) {
            homePointsField.error = "Please enter a valid score"
            homePointsField.requestFocus()
            return
        }

        if (oppNameField.text.toString().isEmpty()) {
            oppNameField.error = "Please enter an opposition name"
            oppNameField.requestFocus()
            return
        }

        if (oppGoalsField.text.toString().isEmpty()) {
            oppGoalsField.error = "Please enter a valid score"
            oppGoalsField.requestFocus()
            return
        }

        if (oppPointsField.text.toString().isEmpty()) {
            oppPointsField.error = "Please enter a score"
            oppPointsField.requestFocus()
            return
        }

            val currentUser = auth.currentUser
            val currentUserDB = matchesReference?.child(currentUser?.uid!!)

            //Home Team
            currentUserDB?.child("team_name")?.setValue(teamNameFieldNew.text.toString())
            currentUserDB?.child("team_goals")?.setValue(Integer.parseInt(homeGoalsField.text.toString()))
            currentUserDB?.child("team_points")?.setValue(Integer.parseInt(homePointsField.text.toString()))

            //Opp Team
            currentUserDB?.child("opp_name")?.setValue(oppNameField.text.toString())
            currentUserDB?.child("opp_goals")?.setValue(Integer.parseInt(oppGoalsField.text.toString()))
            currentUserDB?.child("opp_points")?.setValue(Integer.parseInt(oppPointsField.text.toString()))

                ?.addOnCompleteListener {
                    Toast.makeText(context, "Saved Succesfully", Toast.LENGTH_LONG).show()

                    homeGoalsField.setHint("Team Goals")
                    homePointsField.setHint("Team Points")
                    oppNameField.setHint("Opposition Name")
                    oppGoalsField.setHint("Opposition Goals")
                    oppPointsField.setHint("Opposition Points")

                    lottieAnimationView.playAnimation()
                }

        }

    fun loadProfile(){
        val user = auth.currentUser
        val userReference = profileReference?.child(user?.uid!!)

        userReference?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                teamNameFieldNew.text = snapshot.child("team_name").value.toString()
                homeName = snapshot.child("team_name").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                teamNameText.text = "Could not retrieve"
            }
        })
    }
}
