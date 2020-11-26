package com.myapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.myapp.R
import com.myapp.database.Note
import com.myapp.database.NoteDatabase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class AddNoteFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    var uid: Int = 0
    var note: Note? = null
    lateinit var b_save: FloatingActionButton
    lateinit var b_cancel: FloatingActionButton
    lateinit var etHeading: EditText
    lateinit var etContent: EditText
    lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getInt("Uid")
            note=it.getSerializable("Note") as Note?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_note, container, false)
        init(view)
        return view
    }

    fun init(view: View){
        b_save = view.findViewById(R.id.bSave)
        b_cancel = view.findViewById(R.id.bCancel)
        etHeading = view.findViewById(R.id.nHeading)
        etContent = view.findViewById(R.id.nContent)
        loading = view.findViewById(R.id.addNoteLoading)

        if(note!=null){
            etHeading.setText(note!!.heading)
            etContent.setText(note!!.body)
        }

        b_cancel.setOnClickListener {
            if (note==null)
            requireActivity().onBackPressed()
            else{
                val frag = ViewNoteFragment()
                val arg=Bundle()
                arg.putInt("Uid",uid)
                arg.putSerializable("Note",note!!)
                frag.arguments=arg
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame,frag).commit()
            }
            return@setOnClickListener
        }

        b_save.setOnClickListener {
            if(!check())
                return@setOnClickListener
            loading.visibility=View.VISIBLE
            val title = etHeading.text.toString()
            val body = etContent.text.toString()
            MainScope().launch {
                context?.let {
                    if(note==null) {
                        NoteDatabase(it).getNoteDao().addNote(Note(uid, title, body))
                        loading.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Note Saved!", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressed()
                    }
                    else {
                        note!!.heading=title
                        note!!.body=body
                        NoteDatabase(it).getNoteDao().updateNote(note!!)

                        val frag = ViewNoteFragment()
                        val arg=Bundle()
                        arg.putInt("Uid",uid)
                        arg.putSerializable("Note",note!!)
                        frag.arguments=arg
                        Toast.makeText(requireContext(), "Note Saved!", Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frame,frag).commit()
                    }
                }
            }
        }
    }

    private fun check():Boolean {
        if(etContent.text.isNotBlank() && etHeading.text.isNotBlank())
            return true
        Toast.makeText(requireContext(),"Heading or content cannot be left blank!",Toast.LENGTH_SHORT).show()
        return false
    }

}