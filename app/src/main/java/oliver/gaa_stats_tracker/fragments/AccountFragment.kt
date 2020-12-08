package oliver.gaa_stats_tracker.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.new_match_fragment.*
import kotlinx.android.synthetic.main.welcome_page.view.*
import oliver.gaa_stats_tracker.LoginPage
import oliver.gaa_stats_tracker.R
import oliver.gaa_stats_tracker.WelcomePage
import oliver.gaa_stats_tracker.models.Match


class AccountFragment : Fragment() {

    lateinit var auth: FirebaseAuth
    var database: FirebaseDatabase? = null
    var profileReference: DatabaseReference? = null
    var matchesReference: DatabaseReference? = null
    var ratingReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        profileReference = database?.reference!!.child("Profile")
        matchesReference = database?.reference!!.child("Matches")?.child(auth.currentUser?.uid!!)
        ratingReference = database?.reference!!.child("Rating")
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.account_fragment, container, false)
        var logoutButton: View = view.findViewById(R.id.logoutButton)
        var deleteButton: View = view.findViewById(R.id.deleteAccountButton)
        var ratingBarInfo: RatingBar = view.findViewById(R.id.ratingBarInfo)
        loadProfile()
        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(context, WelcomePage::class.java))
            activity?.finish()
        }
        deleteButton.setOnClickListener{

            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to delete your account?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    startActivity(Intent(context, WelcomePage::class.java))
                    deleteAccount()
                    Toast.makeText(context,"Account Deleted",Toast.LENGTH_SHORT).show()
                    activity?.finish()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
        ratingBarInfo.setOnRatingBarChangeListener{ RatingBar, rating, fromUser ->

            //Gets home details and current userID
            var key = auth.currentUser?.uid
            var rating = ratingBarInfo.rating

            //Adding match to the Matches table
            ratingReference?.child(key!!)?.child("rating")?.setValue(rating)

                //On complete it gives a message depending on the result
                ?.addOnCompleteListener {
                    //Celebration Animation and message if the users team wins
                    Toast.makeText(context, "Thanks for rating", Toast.LENGTH_LONG).show()
                }
        }

        return view
    }

    fun loadProfile(){
        val user = auth.currentUser
        val userReference = profileReference?.child(user?.uid!!)

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

    fun deleteAccount(){
        matchesReference?.removeValue()
        database?.reference!!.child("Profile").child(auth.currentUser?.uid!!).removeValue()
        val user = auth.currentUser
        user?.delete()
        auth.signOut()
    }
}
