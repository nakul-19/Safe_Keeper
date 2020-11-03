package com.myapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.myapp.R
import com.myapp.activity.MainActivity
import com.myapp.database.User
import com.myapp.database.UserDatabase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SignUpFragment : BaseFragment() {

    lateinit var name: EditText
    lateinit var address: EditText
    lateinit var email: EditText
    lateinit var mobile: EditText
    lateinit var pswrd: EditText
    lateinit var cnfPswrd: EditText
    lateinit var signup: TextView
    lateinit var login: TextView
    lateinit var loading: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
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
                }
            }
            if (user == null) {
                MainScope().launch {
                    context?.let {
                        UserDatabase(it).getUserDao().addUser(
                            User(
                                name.text.toString(),
                                email.text.toString(),
                                mobile.text.toString(),
                                address.text.toString(),
                                pswrd.text.toString()
                            )
                        )
                    }
                }
                loading.visibility = View.GONE
                launchMainActivity()
                return@setOnClickListener
            }
            email.error = "Email id already in use!"
        }

        login.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.login_frame, SignInFragment()).commit()
        }
        return view
    }

    private fun launchMainActivity() {
        Toast.makeText(requireContext(), "Welcome ${name.text}!", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.putExtra("Name", name.text.toString())
        intent.putExtra("Address", address.text.toString())
        intent.putExtra("Email", email.text.toString())
        intent.putExtra("Mobile", mobile.text.toString())
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
        if (address.text.isBlank()) {
            address.error = "Address can't be blank!"
            isValid = false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).find()) {
            email.error = "Invalid Email!"
            isValid = false
        }
        if (!Patterns.PHONE.matcher(mobile.text.toString()).find()) {
            mobile.error = "Invalid Mobile!"
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
        address = view.findViewById(R.id.etAddress)
        email = view.findViewById(R.id.etEmail)
        mobile = view.findViewById(R.id.etMobile)
        pswrd = view.findViewById(R.id.etPassword)
        cnfPswrd = view.findViewById(R.id.etCnfPassword)
        signup = view.findViewById(R.id.tvSignUpButton)
        login = view.findViewById(R.id.tvLogin)
        loading = view.findViewById(R.id.loginLoading)
    }

}