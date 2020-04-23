package com.example.petland.user_profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petland.HomeActivity
import com.example.petland.R
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_view_invitations.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.recyclerView

class ViewInvitationsActivity : AppCompatActivity() {

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
    private fun updateInvitations(){
        val currentUser = ParseUser.getCurrentUser()
        val query = ParseQuery.getQuery<ParseObject>("Invitation")
        query.whereEqualTo("receiver", currentUser)
        invitationsList = query.find()
        if (invitationsList != null) {
            adapter = InvitationAdapter(invitationsList.toList())
            recyclerView.adapter = adapter
        }

    }

    fun goBack(view: View) {
        val intent = Intent(this, HomeActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

}
