package com.myapp.activity

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.myapp.R
import com.myapp.fragments.SignInFragment
import com.myapp.fragments.SignUpFragment
import com.myapp.util.hideKeyboard

class LoginActivity : AppCompatActivity() {

    lateinit var ml: MotionLayout
    lateinit var sign_in: TextView
    lateinit var sign_up: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ml=findViewById(R.id.motionLayout)
        sign_in=findViewById(R.id.signIn)
        sign_up=findViewById(R.id.signUp)
        supportFragmentManager.beginTransaction().replace(R.id.login_frame, SignInFragment())
            .commit()
        sign_in.setOnClickListener {
            gotoSignIn()
        }
        sign_up.setOnClickListener {
            gotoSignUp()
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    fun gotoSignUp() {
        ml.transitionToEnd()
        supportFragmentManager.beginTransaction().replace(R.id.login_frame, SignUpFragment())
            .commit()
    }

    fun gotoSignIn()
    {
        ml.transitionToStart()
        supportFragmentManager.beginTransaction().replace(R.id.login_frame, SignInFragment())
            .commit()
    }

    override fun onPause() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onPause()
    }

    override fun onRestart() {
        hideKeyboard()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onRestart()
    }
}