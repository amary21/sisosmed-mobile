package com.amary.sisosmed.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.amary.sisosmed.R
import com.amary.sisosmed.constant.KeyValue
import com.amary.sisosmed.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        setupNavigationDestination()
    }

    private fun setupNavigationDestination() {
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when (destination.id) {
                R.id.navigation_splash, R.id.navigation_login, R.id.navigation_register ->
                    binding.cvNavView.isVisible = false
                else -> binding.cvNavView.isVisible = true
            }
        }
    }

    override fun onBackPressed() {
        when(navController.currentDestination?.id){
            R.id.navigation_login, R.id.navigation_home, R.id.navigation_favorite, R.id.navigation_setting -> finish()
            R.id.navigation_register -> {
                navController.previousBackStackEntry?.savedStateHandle?.set(KeyValue.IS_FROM_REGISTER, true)
                navController.popBackStack()
            }
            else -> navController.popBackStack()
        }
    }
}