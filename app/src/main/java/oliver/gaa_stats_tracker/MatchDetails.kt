package oliver.gaa_stats_tracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.match_details_activity.*
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
}