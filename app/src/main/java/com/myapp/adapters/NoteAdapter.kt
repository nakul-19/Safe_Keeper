package com.myapp.adapters

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.myapp.R
import com.myapp.database.Note
import com.myapp.database.NoteDatabase
import com.myapp.fragments.ViewNoteFragment
import kotlinx.android.synthetic.main.note_layout.view.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NoteAdapter(var list: ArrayList<Note>, val uid: Int) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteAdapter.NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        var heading=list[position].heading
        if(heading.length>16){
            heading=heading.substring(0,15)+"..."
        }
        holder.view.itemHeading.text=heading

        holder.view.bDelete.setOnClickListener {
            MainScope().launch {
                holder.view.context?.let{
                    NoteDatabase(it).getNoteDao().deleteNote(list[position])
                    list.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }

        holder.view.itemHeading.setOnClickListener {
            val arg=Bundle()
            arg.putInt("Uid",uid)
            arg.putSerializable("Note",list[position])
            Log.d("heading",list[position].heading)
            val frag = ViewNoteFragment()
            frag.arguments=arg
            (it.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fragment_open_enter,
                    R.anim.fragment_close_exit,
                    R.anim.fragment_fade_enter,
                    R.anim.fragment_fade_exit
                ).replace(R.id.main_frame,frag).commit()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}