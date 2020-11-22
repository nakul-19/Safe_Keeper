package com.myapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myapp.R
import com.myapp.database.User
import kotlinx.android.synthetic.main.avatar_layout.view.*

class AvatarAdapter(val list: ArrayList<Int>) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    class AvatarViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private var selectedPos: Int =0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AvatarViewHolder {
        return AvatarViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.avatar_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AvatarAdapter.AvatarViewHolder, position: Int) {
        holder.view.av_image.setImageResource(list[position])

        if(position==selectedPos)
            holder.view.selected_bg.setBackgroundResource(R.color.colorPrimary)
        else
            holder.view.selected_bg.setBackgroundResource(R.color.white)

        holder.view.setOnClickListener {
            selectedPos=position
            holder.view.selected_bg.setBackgroundResource(R.color.colorPrimary)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getSelected() : Int {
        return list[selectedPos]
    }
}