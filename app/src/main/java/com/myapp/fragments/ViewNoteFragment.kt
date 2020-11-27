package com.myapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.myapp.R
import com.myapp.database.Note
import com.myapp.util.hideKeyboard

class ViewNoteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var heading: String? = null
    private var body: String? = null
    private var uid: Int = 0
    var note: Note?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = (it.getSerializable("Note") as Note?)
            heading = (it.getSerializable("Note") as Note).heading
            body = (it.getSerializable("Note") as Note).body
            uid = it.getInt("Uid")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_note, container, false)
        hideKeyboard()
        init(view)
        return view
    }

    private fun init(view: View) {
        val vnHeading = view.findViewById<EditText>(R.id.vnHeading)
        val vnBody = view.findViewById<EditText>(R.id.vnContent)
        Log.d("heading",heading!!)
        vnHeading.setText(heading)
        vnBody.setText(body)
        val b_edit = view.findViewById<FloatingActionButton>(R.id.bEdit)
        b_edit.setOnClickListener {
            val arg = Bundle()
            arg.putSerializable("Note",note!!)
            val frag = AddNoteFragment()
            frag.arguments=arg
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame,frag).commit()
        }
    }
}