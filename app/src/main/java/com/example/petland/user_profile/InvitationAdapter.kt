package com.example.petland.user_profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageUtils
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.user_invitation_element.view.*

class InvitationAdapter(private val invitations: List<ParseObject>) :
    RecyclerView.Adapter<InvitationAdapter.InvitationHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): InvitationHolder {
        return InvitationHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.user_invitation_element, p0, false)

        )
    }

    override fun getItemCount(): Int {
        return invitations.count()
    }

    override fun onBindViewHolder(holder: InvitationHolder, position: Int) {
        holder.bindInvitationInfo(invitations[position])
    }

    class InvitationHolder(v: View) : RecyclerView.ViewHolder(v) {
        var view: View = v

        private lateinit var invitation: ParseObject

        fun bindInvitationInfo(invitation: ParseObject){
            val pet = invitation.get("petO") as ParseObject
            val creator = invitation.get("creator") as ParseObject
            this.invitation = invitation
            val user = ParseUser.getCurrentUser()

            val listUsers = ParseQuery.getQuery<ParseObject>("_User")
            listUsers.whereEqualTo("objectId", creator.objectId)
            val listPets = ParseQuery.getQuery<ParseObject>("Pet")
            listPets.whereEqualTo("objectId", pet.objectId)
            view.buttonAccept.setOnClickListener {
                this.invitation.put("answer", true)
                this.invitation.saveInBackground()
                val relation = pet.getRelation<ParseUser>("caregivers")
                relation.add(user)
                user.save()
                pet.saveInBackground()
                this.invitation.deleteInBackground()
            }
            view.buttonDeny.setOnClickListener { this.invitation.deleteInBackground() }


            view.ownerName.text = listUsers.first.get("name") as String
            view.name.text = listPets.first.get("name") as String
            val imageUtils = ImageUtils()
            imageUtils.retrieveImage(listPets.first, view.petImageInvitation)
        }
    }


}