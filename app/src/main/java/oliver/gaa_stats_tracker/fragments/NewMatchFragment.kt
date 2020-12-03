package oliver.gaa_stats_tracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.new_match_fragment.*
import oliver.gaa_stats_tracker.R
import com.airbnb.lottie.LottieAnimationView
import oliver.gaa_stats_tracker.models.Match

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
        matchesReference = database?.getReference("Matches")?.child(auth.currentUser?.uid!!)
        profileReference = database?.reference!!.child("Profile")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.new_match_fragment, container, false)
        var addMatchButton: View = view.findViewById(R.id.addMatchButton)
        var resetFieldsButton: View = view.findViewById(R.id.resetFieldsButton)
        lottieAnimationView = view.findViewById(R.id.celebration)
        loadProfile()
        addMatchButton.setOnClickListener {
            addMatch()
        }
        resetFieldsButton.setOnClickListener {
            resetFields()
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
            oppGoalsField.error = "Please enter a score"
            oppGoalsField.requestFocus()
            return
        }

        if (oppPointsField.text.toString().isEmpty()) {
            oppPointsField.error = "Please enter a valid score"
            oppPointsField.requestFocus()
            return
        }

        //Gets surent user and creates a unique key
        val currentUser = auth.currentUser
        val key = matchesReference?.push()?.key

        //Gets home details and current userID
        var userID = currentUser?.uid!!
        var homeName = teamNameFieldNew.text.toString()
        var homeGoals = Integer.parseInt(homeGoalsField.text.toString())
        var homePoints = Integer.parseInt(homePointsField.text.toString())

        //Gets Opposition details
        var oppName = oppNameField.text.toString()
        var oppGoals = Integer.parseInt(oppGoalsField.text.toString())
        var oppPoints = Integer.parseInt(oppPointsField.text.toString())

        //Creates match object
        val match = Match(oppGoals, oppName, oppPoints, homeGoals, homeName, homePoints, userID)

        //Adding match to the Matches table
        matchesReference?.child(key!!)?.setValue(match)

                //On complete it gives a message depending on the result
            ?.addOnCompleteListener {

                //Celebration Animation and message if the users team wins
                if (homeGoals * 3 + homePoints > oppGoals * 3 + oppPoints) {
                    resetFields()
                    lottieAnimationView.playAnimation()
                    Toast.makeText(context, "Congratulations", Toast.LENGTH_LONG).show()
                }
                //Print message if a draw
                if (homeGoals * 3 + homePoints == oppGoals * 3 + oppPoints) {
                    resetFields()
                    Toast.makeText(context, "Ohh draw game", Toast.LENGTH_LONG).show()
                }
                //Print a losing message if users team loses
                if (homeGoals * 3 + homePoints < oppGoals * 3 + oppPoints) {
                    resetFields()
                    Toast.makeText(context, "Better Luck Next Time", Toast.LENGTH_LONG).show()
                }
            }
    }

    //Looads current user details to set the team name
    fun loadProfile() {
        val user = auth.currentUser
        val userReference = profileReference?.child(user?.uid!!)

        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                teamNameFieldNew.text = snapshot.child("team_name").value.toString()
                homeName = snapshot.child("team_name").value.toString()
            }

            //Use if details cannot be retrieved
            override fun onCancelled(error: DatabaseError) {
                teamNameText.text = "Could not retrieve"
            }
        })
    }


    fun resetFields() {
        homeGoalsField.setText("")
        homePointsField.setText("")
        oppNameField.setText("")
        oppPointsField.setText("")
        oppGoalsField.setText("")
    }
}
