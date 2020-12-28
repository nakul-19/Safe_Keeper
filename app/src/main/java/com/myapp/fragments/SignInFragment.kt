package com.myapp.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.myapp.R
import com.myapp.activity.MainActivity
import com.myapp.database.User
import com.myapp.database.UserDatabase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SignInFragment : Fragment() {

    lateinit var email: EditText
    lateinit var pswrd: EditText
    lateinit var signup: TextView
    lateinit var signup2: TextView
    lateinit var login: TextView
    lateinit var signin: TextView
    lateinit var loading: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        init(view)
        login.setOnClickListener {

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

                        loading.visibility = View.GONE
                        email.error = "Username doesn't exist!"
                    } else {

                        loading.visibility = View.GONE

                        if (user?.password != pswrd.text.toString()) {
                            pswrd.error = "Incorrect Password!"
                        } else
                            launchMainActivity(user!!)
                    }
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({

            }, 500)
        }

        signin.setOnClickListener {
            return@setOnClickListener
        }

        signup.setOnClickListener {
            openSignUp()
        }

        signup2.setOnClickListener {
            if (activity==null)
                return@setOnClickListener
            openSignUp()
        }
        return view
    }

    private fun openSignUp() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.login_frame, SignUpFragment()).commit()
        requireActivity().findViewById<TextView>(R.id.signIn).setTextColor(resources.getColor(R.color.colorInactive))
        requireActivity().findViewById<TextView>(R.id.signUp).setTextColor(resources.getColor(R.color.white))
        val ml = requireActivity().findViewById<MotionLayout>(R.id.motionLayout)
        ml.transitionToEnd()
    }

    private fun launchMainActivity(user: User) {
        Toast.makeText(requireContext(), "Welcome ${user.name}!", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.putExtra("Name", user.name)
        intent.putExtra("Email", user.email)
        intent.putExtra("Uid",user.userId)
        intent.putExtra("Avatar",user.avatar)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_in,R.anim.slide_out)
        requireActivity().finish()
    }

    private fun check(): Boolean {
        var isValid = true
        if (email.text.isBlank()) {
            email.error = "Invalid Username!"
            isValid = false
        }
        if (pswrd.length() < 6) {
            pswrd.error = "Password too short!"
            isValid = false
        }
        return isValid
    }

    private fun init(view: View) {
        email = view.findViewById(R.id.etEmailLogin)
        pswrd = view.findViewById(R.id.etPasswordLogin)
        loading = requireActivity().findViewById(R.id.lLoading)
        signup = view.findViewById(R.id.tvSignUpButton)
        signup2 = requireActivity().findViewById(R.id.signUp)
        signin = requireActivity().findViewById(R.id.signIn)
        login = view.findViewById(R.id.tvLoginButton)
    }
}