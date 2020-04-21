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
import kotlinx.android.synthetic.main.fragment_user_profile.view.*

class ViewInvitationsActivity : AppCompatActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: InvitationAdapter
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(this) //Is this ok? maybe not :o
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.activity_view_invitations, container, false)


        rootView.recyclerView.isNestedScrollingEnabled = false
        rootView.recyclerView.layoutManager = layoutManager

        return rootView
    }

    override fun onResume() {
        super.onResume()
        updateInvitations()
    }
    private fun updateInvitations(){
    /*val invitations = Pets()
    val petlist = pets.getPets()
    if (petlist != null) {
        adapter = InvitationAdapter(petlist.toList())
        rootView.recyclerView.adapter = adapter
    }
     */
}



    fun goBack(view: View) {
        val intent = Intent(this, HomeActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

}
