package com.myapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.myapp.R
import com.myapp.database.User
import com.myapp.database.UserDatabase
import com.myapp.fragments.AddNoteFragment
import com.myapp.fragments.HomeFragment
import com.myapp.fragments.ViewNoteFragment
import com.myapp.util.hideKeyboard
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var user: User
    var uid: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideKeyboard()
        val drawer = findViewById<DrawerLayout>(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)

        this.supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar!!.setDisplayShowCustomEnabled(true);
        supportActionBar!!.setCustomView(R.layout.custom_action_bar);

        drawer.addDrawerListener(toggle)
        supportActionBar!!.setCustomView(R.layout.custom_action_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        toggle.syncState()
        val arg = Bundle()
        val name = intent.getStringExtra("Name")
        uid = intent.getIntExtra("Uid", 1)
        arg.putInt("Uid", uid)
        val email = intent.getStringExtra("Email")
        val av = intent.getIntExtra("Avatar", R.drawable.avatar1)
        val nHeader = findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)
        setHeader(nHeader, name!!, email!!, av)
        login(email)
        val frag = HomeFragment()
        frag.arguments=arg
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fragment_open_enter,
                R.anim.fragment_close_exit,
                R.anim.fragment_fade_enter,
                R.anim.fragment_fade_exit
            )
            .replace(R.id.main_frame, frag).commit()
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
            hideKeyboard()

            return true
        }
        hideKeyboard()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.main_frame)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        if(f is AddNoteFragment || f is ViewNoteFragment){
            val frag = HomeFragment()
            val arg = Bundle()
            arg.putInt("Uid", uid)
            frag.arguments = arg
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fragment_open_enter,
                    R.anim.fragment_close_exit,
                    R.anim.fragment_fade_enter,
                    R.anim.fragment_fade_exit
                ).replace(R.id.main_frame,frag).commit()
            return
        }
            super.onBackPressed()
    }

}