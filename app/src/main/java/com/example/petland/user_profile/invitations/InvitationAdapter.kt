package com.example.petland.user_profile.invitations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageUtils
import com.parse.ParseUser
import kotlinx.android.synthetic.main.user_invitation_element.view.*

class InvitationAdapter(private val invitations: List<Invitation>, private val viewInvitationsCallback: ViewInvitationsCallback) :
    RecyclerView.Adapter<InvitationAdapter.InvitationHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): InvitationHolder {
        return InvitationHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.user_invitation_element, p0, false),
            viewInvitationsCallback
        )
    }

    override fun getItemCount(): Int {
        return invitations.count()
    }

    override fun onBindViewHolder(holder: InvitationHolder, position: Int) {
        holder.bindInvitationInfo(invitations[position])
    }

    class InvitationHolder(v: View, viewInvitationsCallback: ViewInvitationsCallback) : RecyclerView.ViewHolder(v) {
        var view: View = v
        val listCallback: ViewInvitationsCallback = viewInvitationsCallback

        private lateinit var invitation: Invitation

        fun bindInvitationInfo(invitation: Invitation){
            val pet = invitation.getPet()
            val creator = invitation.getCreator()
            this.invitation = invitation
            val user = ParseUser.getCurrentUser()

            view.buttonAccept.setOnClickListener {
                pet.addCaregiver(user)
                pet.saveInBackground()
                this.invitation.setAnswer(true)
                this.invitation.saveInBackground()
                listCallback.updateInvitations()
            }
            view.buttonDeny.setOnClickListener {
                this.invitation.deleteInBackground()
                listCallback.updateInvitations()
            }

            view.ownerName.text = creator.getString("name")
            view.name.text = pet.getName()
            ImageUtils.retrieveImage(pet, view.petImageInvitation)
        }
    }


}