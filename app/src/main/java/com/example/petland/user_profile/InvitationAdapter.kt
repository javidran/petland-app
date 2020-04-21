package com.example.petland.user_profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageUtils
import com.parse.ParseObject
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
            this.invitation = invitation
            view.name.text = "Nombre del pet"//invitation.get("name") as String
            view.ownerName.text = "Nombre del owner"

            val imageUtils = ImageUtils()
            imageUtils.retrieveImage(invitation, view.petImageInvitation)

        }
    }


}