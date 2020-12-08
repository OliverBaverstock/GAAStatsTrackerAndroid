package oliver.gaa_stats_tracker.fragments

import android.content.Intent
import android.graphics.ColorSpace
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.oAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.cardview_layout.view.*
import kotlinx.android.synthetic.main.list_view_fragment.*
import kotlinx.android.synthetic.main.new_match_fragment.*
import oliver.gaa_stats_tracker.MatchDetails
import oliver.gaa_stats_tracker.R
import oliver.gaa_stats_tracker.WelcomePage
import oliver.gaa_stats_tracker.models.Match
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class ListViewFragment : Fragment(), AnkoLogger {

    lateinit var auth: FirebaseAuth
    var database: FirebaseDatabase? = null
    var matchesReference: DatabaseReference? = null
    var matchList = ArrayList<Match>()
    var model: Match? = null
    var keyList = ArrayList<String>()
    var key: String? = null

    lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        matchesReference = database?.getReference("Matches")?.child(auth.currentUser?.uid!!)
        matchesReference?.keepSynced(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.list_view_fragment, container, false)
        showMatches()
        return view
    }

    fun showMatches() {
        matchesReference?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                matchList.clear()
                    for (data in snapshot.children) {
                        model = data.getValue(Match::class.java)
                        key = data.key
                        matchList.add(model as Match)
                        keyList.add(key as String)
                }
                if(matchList.size > 0){
                    layoutManager = LinearLayoutManager(context)
                    recyclerView.layoutManager = layoutManager
                    val adapter = DataAdapter(matchList, keyList)
                    recyclerView.adapter = adapter
                }
                if(matchList.size == 0) {
                    listTextSize.text = "You should add some matches!!"
                }
                if(matchList.size == 1){
                    listTextSize.text = "You have " + matchList.size + " match saved!!"
                }
                if(matchList.size > 1){
                    listTextSize.text = "Great you have " + matchList.size + " matches saved!!"
                }
            }
        })
    }
}

class DataAdapter constructor(var list: ArrayList<Match>, var keyList: ArrayList<String>) :
    RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var homeTeam = itemView.homeTeamLabel
        var awayTeam = itemView.awayTeamLabel
        var matchID = itemView.matchID
        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MatchDetails::class.java)
                intent.putExtra("MatchesKey", this.matchID.text)
                print("Adapter Position: $adapterPosition")
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cardview_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DataAdapter.ViewHolder, position: Int) {
        holder.homeTeam.text = list[position].teamName
        holder.awayTeam.text = list[position].oppName
        holder.matchID.text =  keyList[position]
    }
}