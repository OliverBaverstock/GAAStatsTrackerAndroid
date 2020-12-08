package oliver.gaa_stats_tracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.match_details_activity.*
import kotlinx.android.synthetic.main.new_match_fragment.*
import oliver.gaa_stats_tracker.models.Match
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MatchDetails : AppCompatActivity(), AnkoLogger {

    lateinit var auth: FirebaseAuth
    var database: FirebaseDatabase? = null
    var matchesReference: DatabaseReference? = null
    var matchReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_details_activity)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        matchesReference = database?.getReference("Matches")?.child(auth.currentUser?.uid!!)
        val matchesKey = intent.getStringExtra("MatchesKey").toString()
        info {"Match Key: $matchesKey"}
        matchReference = matchesReference?.child(matchesKey)

        loadMatch()

        returnDetailsButton.setOnClickListener{
            startActivity(Intent(this, LoggedIn::class.java))
            finish()
        }

        deleteMatchButton.setOnClickListener{
            deleteMatch()
            startActivity(Intent(this, LoggedIn::class.java))
            finish()
        }

        updateMatchButton.setOnClickListener{
            updateMatch()
        }
    }

    fun deleteMatch(){
        matchReference?.removeValue()
    }

    fun loadMatch(){

        matchReference?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                teamNameDetails.setText(snapshot.child("teamName").value.toString())
                teamGoalsDetails.setText(snapshot.child("teamGoals").value.toString())
                teamPointsDetails.setText(snapshot.child("teamPoints").value.toString())

                oppNameDetails.setText(snapshot.child("oppName").value.toString())
                oppGoalsDetails.setText(snapshot.child("oppGoals").value.toString())
                oppPointsDetails.setText(snapshot.child("oppPoints").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                teamNameDetails.setText("Could not retrieve Match")
                teamGoalsDetails.setText("Could not retrieve Match")
                teamPointsDetails.setText("Could not retrieve Match")

                oppNameDetails.setText("Could not retrieve Match")
                oppGoalsDetails.setText("Could not retrieve Match")
                oppPointsDetails.setText("Could not retrieve Match")
            }

        })
    }

    fun updateMatch() {

        if (teamGoalsDetails.text.toString().isEmpty()) {
            teamGoalsDetails.error = "Please enter a valid score"
            teamGoalsDetails.requestFocus()
            return
        }

        if (teamPointsDetails.text.toString().isEmpty()) {
            teamPointsDetails.error = "Please enter a valid score"
            teamPointsDetails.requestFocus()
            return
        }

        if (oppNameDetails.text.toString().isEmpty()) {
            oppNameDetails.error = "Please enter an opposition name"
            oppNameDetails.requestFocus()
            return
        }

        if (oppGoalsDetails.text.toString().isEmpty()) {
            oppGoalsDetails.error = "Please enter a score"
            oppGoalsDetails.requestFocus()
            return
        }

        if (oppPointsDetails.text.toString().isEmpty()) {
            oppPointsDetails.error = "Please enter a valid score"
            oppPointsDetails.requestFocus()
            return
        }

        //UserID
        val userID = auth.currentUser?.uid.toString()

        //Gets home details and current userID
        var homeName = teamNameDetails.text.toString()
        var homeGoals = Integer.parseInt(teamGoalsDetails.text.toString())
        var homePoints = Integer.parseInt(teamPointsDetails.text.toString())

        //Gets Opposition details
        var oppName = oppNameDetails.text.toString()
        var oppGoals = Integer.parseInt(oppGoalsDetails.text.toString())
        var oppPoints = Integer.parseInt(oppPointsDetails.text.toString())

        //Creates match object
        val match = Match(oppGoals, oppName, oppPoints, homeGoals, homeName, homePoints, userID)

        //Adding match to the Matches table
        matchReference?.setValue(match)

            //On complete it gives a message depending on the result
            ?.addOnCompleteListener {
                    Toast.makeText(this, "Match Updated", Toast.LENGTH_LONG).show()
            }
    }
}