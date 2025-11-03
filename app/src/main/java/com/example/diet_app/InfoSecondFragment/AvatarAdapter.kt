package com.example.diet_app.InfoSecondFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.diet_app.R

class AvatarAdapter(
    private val avatarList: List<Int>,
    private val listener: OnAvatarClickListener
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    interface OnAvatarClickListener {
        fun onAvatarClick(avatarResId: Int)
    }

    inner class AvatarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarImage: ImageView = itemView.findViewById(R.id.image_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_avatar, parent, false)
        return AvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatarRes = avatarList[position]
        holder.avatarImage.setImageResource(avatarRes)
        holder.itemView.setOnClickListener {
            listener.onAvatarClick(avatarRes)
        }
    }

    override fun getItemCount(): Int = avatarList.size
}
