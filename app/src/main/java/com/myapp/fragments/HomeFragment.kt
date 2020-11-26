package com.myapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.myapp.R
import com.myapp.adapters.NoteAdapter
import com.myapp.database.Note
import com.myapp.database.NoteDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    var list: ArrayList<Note> = ArrayList()
    lateinit var adapter: NoteAdapter
    var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt("Uid")
        }
        Log.d("Uid",id.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recycler = view.findViewById<RecyclerView>(R.id.noteRecyclerView)
        recycler.layoutManager=LinearLayoutManager(requireContext())
        adapter= NoteAdapter(list,id!!)
        recycler.adapter=adapter
        val b_add: FloatingActionButton = view.findViewById(R.id.bAdd)
        b_add.setOnClickListener {
            val frag = AddNoteFragment()
            val arg = Bundle()
            arg.putInt("Uid",id!!)
            frag.arguments = arg
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fragment_open_enter,
                    R.anim.fragment_close_exit,
                    R.anim.fragment_fade_enter,
                    R.anim.fragment_fade_exit
                ).replace(R.id.main_frame,frag).commit()
        }
        getData(view)
        return view
    }

    private fun getData(view: View) {
        val loading = view.findViewById<ProgressBar>(R.id.noteLoading)
        loading.visibility=View.VISIBLE
        MainScope().launch {
            context?.let {
                list.clear()
                list.addAll(NoteDatabase(it).getNoteDao().getNotes(id!!))
                adapter.notifyDataSetChanged()
                loading.visibility=View.GONE
            }
        }
    }

}