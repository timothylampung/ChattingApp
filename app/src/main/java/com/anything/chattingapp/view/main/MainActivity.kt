package com.anything.chattingapp.view.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.anything.chattingapp.R
import com.anything.chattingapp.view.auth.LoginActivity
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val _vm: MainActivityViewModel by viewModel()
    private var appBarConfiguration: AppBarConfiguration? = null
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_chat, R.id.navigation_stories, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController!!, appBarConfiguration!!)
        navView.setupWithNavController(navController!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        val findNavController = findNavController((R.id.nav_host_fragment))
        return navController!!.navigateUp(appBarConfiguration!!) || super.onSupportNavigateUp()
    }

    fun initAuth() {
        if (_vm.getCurrentUser() == null) {
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
    }

    override fun onStart() {
        super.onStart()
        initAuth()
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION),1)
    }


}
