package oliver.gaa_stats_tracker.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.account_fragment.*
import oliver.gaa_stats_tracker.R
import oliver.gaa_stats_tracker.WelcomePage

// TODO: Rename parameter arguments, choose names that match

class AccountFragment : Fragment() {

    lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Profile")

        loadProfile()
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.account_fragment, container, false)

    }

    fun loadProfile(){
        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)

        userReference?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                teamNameText.text = snapshot.child("team_name").value.toString()
                managerNameText.text = snapshot.child("manager_name").value.toString()
                captainNameText.text = snapshot.child("captain_name").value.toString()
                emailAddressText.text = user?.email

            }

            override fun onCancelled(error: DatabaseError) {
                teamNameText.text = "Could not retrieve"
                managerNameText.text = "Could not retrieve"
                captainNameText.text = "Could not retrieve"
                emailAddressText.text = "Could not retrieve"
            }
            
        })
    }


}
