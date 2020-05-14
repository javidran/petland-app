package com.example.petland.user_profile.invitations

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petland.R
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_view_invitations.*

class ViewInvitationsActivity : AppCompatActivity(),
    ViewInvitationsCallback {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: InvitationAdapter
    private lateinit var rootView: View
    var invitationsList = listOf<ParseObject>() //Empty list of parse objects

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(this) //Is this ok? maybe not :o
        setContentView(R.layout.activity_view_invitations)
        recyclerView.isNestedScrollingEnabled = false //evitar scrolling
        recyclerView.layoutManager = layoutManager

    }

    override fun onResume() {
        super.onResume()
        updateInvitations()
    }
    override fun updateInvitations(){
        val currentUser = ParseUser.getCurrentUser()
        val query = ParseQuery.getQuery<ParseObject>("Invitation")
        query.whereEqualTo("receiver", currentUser)
        query.whereEqualTo("answer", false)
        invitationsList = query.find()
        if (invitationsList != null) {
            if(invitationsList.toList().isEmpty()){
                val noInvites: TextView = findViewById(R.id.noInvitations)
                noInvites.visibility = View.VISIBLE
                val yesInvites: TextView = findViewById(R.id.instructionsInvitations)
                yesInvites.visibility = View.INVISIBLE
            }
            else{
                val noInvites: TextView = findViewById(R.id.noInvitations)
                noInvites.visibility = View.INVISIBLE
                val yesInvites: TextView = findViewById(R.id.instructionsInvitations)
                yesInvites.visibility = View.VISIBLE
            }


            adapter =
                InvitationAdapter(
                    invitationsList.toList(),
                    this
                )
            recyclerView.adapter = adapter
        }


    }

    fun goBack(view: View) {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}
