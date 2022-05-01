package com.amary.sisosmed

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.amary.sisosmed.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        setupNavigationDestination(navController)
    }

    private fun setupNavigationDestination(navController: NavController) {
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when (destination.id) {
                R.id.navigation_splash, R.id.navigation_login, R.id.navigation_register ->
                    binding.cvNavView.isVisible = false
            }
        }
    }
}