package com.myapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.myapp.R
import com.myapp.database.User
import com.myapp.database.UserDatabase
import com.myapp.fragments.HomeFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawer = findViewById<DrawerLayout>(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        toggle.syncState()
        val arg = Bundle()
        val name = intent.getStringExtra("Name")
        arg.putString("Name", name)
        val email = intent.getStringExtra("Email")
        arg.putString("Email", email)
        val av = intent.getIntExtra("Avatar",R.drawable.avatar1)
        val nHeader = findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)
        setHeader(nHeader,name!!,email!!,av)
//        val v:View = n.getHeaderView(0)
//        val tv_Name:TextView = v.findViewById(R.id.lName)
//        tv_Name.text=name
        login(email)
        val frag = HomeFragment()
        frag.arguments = arg
        supportFragmentManager.beginTransaction().add(R.id.main_frame, frag).commit()
    }

    private fun setHeader(view: View, name: String, email: String, av: Int) {
        val l_name: TextView = view.findViewById(R.id.lName)
        val l_username: TextView = view.findViewById(R.id.lUserName)
        val l_av: CircleImageView = view.findViewById(R.id.lAvatar)
        l_name.text=name
        l_username.text=email
        l_av.setImageResource(av)
    }


    private fun login(email: String?) {
        MainScope().launch {
            applicationContext.let {
                user=UserDatabase(it).getUserDao().getUser(email!!)
                user.loggedIn=1
                UserDatabase(it).getUserDao().logUser(user)
            }
        }
    }

    fun logout(view: View) {
        GlobalScope.launch {
            applicationContext.let{
                user.loggedIn=0
                UserDatabase(it).getUserDao().logUser(user)
            }
        }
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
    }

}