package com.myapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapp.R
import com.myapp.activity.MainActivity
import com.myapp.adapters.AvatarAdapter
import com.myapp.database.User
import com.myapp.database.UserDatabase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SignUpFragment : BaseFragment() {

    lateinit var name: EditText
    lateinit var email: EditText
    lateinit var pswrd: EditText
    lateinit var cnfPswrd: EditText
    lateinit var signup: TextView
    lateinit var login: TextView
    lateinit var adapter: AvatarAdapter
    lateinit var loading: ProgressBar
    val list: ArrayList<Int> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val recycler = view.findViewById<RecyclerView>(R.id.avatarRecycler)
        recycler.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        list.add(R.drawable.avatar1)
        list.add(R.drawable.avatar3)
        list.add(R.drawable.avatar2)
        list.add(R.drawable.avatar4)
        list.add(R.drawable.avatar5)
        list.add(R.drawable.avatar6)
        list.add(R.drawable.avatar7)
        list.add(R.drawable.avatar8)
        adapter= AvatarAdapter(list)
        recycler.adapter=adapter
        init(view)
        signup.setOnClickListener {

            loading.visibility = View.VISIBLE

            if (!check()) {
                loading.visibility = View.GONE
                return@setOnClickListener
            }

            var user: User? = null

            MainScope().launch {
                context?.let {
                    user = UserDatabase(it).getUserDao().getUser(email.text.toString())
                    if (user == null) {
                        UserDatabase(it).getUserDao().addUser(
                            User(
                                name.text.toString(),
                                email.text.toString(),
                                pswrd.text.toString(),
                                adapter.getSelected()
                            )
                        )
                        user = UserDatabase(it).getUserDao().getUser(email.text.toString())
                        loading.visibility = View.GONE
                        launchMainActivity(user!!)
                    } else
                        email.error = "Username already in use!"
                    loading.visibility = View.GONE
                }
            }
            return@setOnClickListener
        }

        login.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.login_frame, SignInFragment()).commit()
        }
        return view
    }

    private fun launchMainActivity(user: User) {
        Toast.makeText(requireContext(), "Welcome ${email.text}!", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.putExtra("Name", name.text.toString())
        intent.putExtra("Email", email.text.toString())
        intent.putExtra("Uid", user.userId)
        intent.putExtra("Avatar", adapter.getSelected())
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        requireActivity().finish()
    }

    private fun check(): Boolean {
        var isValid = true

        if (name.text.isBlank()) {
            name.error = "Name can't be blank!"
            isValid = false
        }
        if (email.text.isBlank()) {
            email.error = "Username can't be blank!"
            isValid = false
        }
        if (pswrd.length() < 6) {
            pswrd.error = "Password should have at least 6 characters!"
            isValid = false
        } else {
            if (pswrd.text.toString() != cnfPswrd.text.toString()) {
                cnfPswrd.error = "Passwords don't match!"
                isValid = false
            }
        }

        return isValid
    }

    private fun init(view: View) {
        name = view.findViewById(R.id.etName)
        email = view.findViewById(R.id.etEmail)
        pswrd = view.findViewById(R.id.etPassword)
        cnfPswrd = view.findViewById(R.id.etCnfPassword)
        signup = view.findViewById(R.id.tvSignUpButton)
        login = view.findViewById(R.id.tvLogin)
        loading = view.findViewById(R.id.loginLoading)
    }

}