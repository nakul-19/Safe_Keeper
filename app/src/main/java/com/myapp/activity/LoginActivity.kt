package com.myapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myapp.R
import com.myapp.fragments.SignInFragment

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportFragmentManager.beginTransaction().replace(R.id.login_frame, SignInFragment())
            .commit()
    }
}