package com.one.fruitmanbuyer.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.one.fruitmanbuyer.R
import com.one.fruitmanbuyer.ui.login.LoginActivity
import com.one.fruitmanbuyer.ui.main.order.OrderFragment
import com.one.fruitmanbuyer.ui.main.profile.ProfileFragment
import com.one.fruitmanbuyer.ui.main.timeline.TimelineFragment
import com.one.fruitmanbuyer.ui.report.ReportActivity
import com.one.fruitmanbuyer.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        var navStatus = -1
        const val CHANNEL_ID = "FruitManBuyer"
        private const val CHANNEL_NAME= "FruitMan"
        private const val CHANNEL_DESC = "Android FCM"
    }
    private var fragment : Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if(savedInstanceState == null){ nav_view.selectedItemId = R.id.navigation_home }
        isLoggedIn()
        setupNotificationManager()

        action_info_app.setOnClickListener {
            AlertDialog.Builder(this@MainActivity).apply {
                setTitle("FruitmanPengepul versi 1.0")
                setMessage("Aplikasi untuk membantu pengepul dalam mencari hasil panen buah dengan sistem tebasan by Ikhwanudin")
                setPositiveButton("ok"){dialog,_ ->
                    dialog.dismiss()
                }
            }.show()
        }

        action_report.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }

    }



    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.navigation_home -> {
                if(navStatus != 0){
                    fragment = TimelineFragment()
                    navStatus = 0
                }
            }
            R.id.navigation_order -> {
                if(navStatus != 1){
                    fragment = OrderFragment()
                    navStatus = 1
                }
            }
//
            R.id.navigation_profile -> {
                if(navStatus != 2){
                    fragment = ProfileFragment()
                    navStatus = 2
                }
            }
        }
        if(fragment == null){
            navStatus = 0
            fragment = TimelineFragment()
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.screen_container, fragment!!)
        fragmentTransaction.commit()
        true
    }

    private fun isLoggedIn(){
        if(Constants.getToken(this@MainActivity).equals("UNDEFINED")){
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }).also { finish() }
        }
    }

    private fun setupNotificationManager(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESC
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
