package com.myapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.myapp.R
import com.myapp.database.User
import com.myapp.database.UserDatabase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                var user: User? = null
                MainScope().launch {
                    applicationContext.let {
                        user = UserDatabase(it).getUserDao().getLoggedInUser()
                    }
                }
                if (user == null) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                } else {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.putExtra("Name", user!!.name)
                    intent.putExtra("Email", user!!.email)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                }
                finish()
            },
            2000
        )
    }
}