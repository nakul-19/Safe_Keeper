package com.myapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myapp.R
import com.myapp.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val arg = Bundle()
        val name = intent.getStringExtra("Name")
        arg.putString("Name", name)
        val address = intent.getStringExtra("Address")
        arg.putString("Address", address)
        val mobile = intent.getStringExtra("Mobile")
        arg.putString("Mobile", mobile)
        val email = intent.getStringExtra("Email")
        arg.putString("Email", email)
        val frag = HomeFragment()
        frag.arguments = arg
        supportFragmentManager.beginTransaction().add(R.id.main_frame, frag).commit()
    }
}