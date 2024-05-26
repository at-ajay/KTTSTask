package com.kttstask.presentation.screens.location_screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kttstask.R
import com.kttstask.data.models.User

class UserAdapter(
    var users: MutableList<User>,
    val logoutClickListener: (user: User) -> Unit,
    val onSwitchUserClicked: (user: User) -> Unit,
): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val profileChar = itemView.findViewById<TextView>(R.id.user_name_char)
        val name = itemView.findViewById<TextView>(R.id.user_name)
        val email = itemView.findViewById<TextView>(R.id.user_email)
        val user = itemView.findViewById<ConstraintLayout>(R.id.user)
        val logoutBtn = itemView.findViewById<ImageButton>(R.id.user_logout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.profileChar.text = users[position].fullName[0].toString().uppercase()
        holder.name.text = users[position].fullName
        holder.email.text = users[position].emailAddress

        holder.user.setOnClickListener {
           onSwitchUserClicked(users[position])
        }

        holder.logoutBtn.setOnClickListener {
            logoutClickListener(users[position])
        }
    }

}