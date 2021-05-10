package com.electrocoder.grader

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.electrocoder.grader.databinding.ActivityMainBinding
import com.electrocoder.grader.util.Constants
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()

        MobileAds.initialize(this) {

        }

        createNotificationChannel()
        setSupportActionBar(binding.mainToolbar)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {

            var options: NavOptions = NavOptions.Builder()
                .setPopUpTo(it.itemId, true)
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .build()

            if(it.itemId != R.id.predmetiFragment)
                Navigation.findNavController(this, R.id.navigation_host).navigate(it.itemId, null, options)
            else
                Navigation.findNavController(this, R.id.navigation_host).navigate(it.itemId)

            //NavigationUI.onNavDestinationSelected(it, Navigation.findNavController(this, R.id.navigation_host))

            /*if(!isAtCurrentDestination(it.itemId))
                Navigation.findNavController(this, R.id.navigation_host).navigate(it.itemId) */
            true
        }

        //setupActionBarWithNavController(NavController(this), binding.mainToolbar)

        val navController = findNavController(R.id.navigation_host)
        val appBarConfiguration = AppBarConfiguration(navController.graph)


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id == R.id.addPredmetFragment || destination.id == R.id.predmetFragment) {
                binding.mainToolbar.visibility = View.VISIBLE
                binding.mainToolbar.setupWithNavController(navController, appBarConfiguration)
                //setupActionBarWithNavController(navController, appBarConfiguration)

                //binding.bottomNavigationView.visibility = View.GONE

                binding.bottomNavigationView.animateAndHide(1)

            }
            // Sakrivanje toolbara i bottomnavigationa ako navigira na fragmente
            else if(destination.id == R.id.aboutAppFragment || destination.id == R.id.prijaviGreskuFragment) {
                if(binding.mainToolbar.isVisible) {
                    binding.mainToolbar.visibility = View.GONE
                }
                binding.bottomNavigationView.animateAndHide(1)

            } else {
                binding.mainToolbar.visibility = View.GONE
                binding.bottomNavigationView.animateAndHide(0)
            }
            hideKeyboard()
        }

    }

    fun setupBottomNavigation() {
        NavigationUI.setupWithNavController(binding.bottomNavigationView, Navigation.findNavController(this, R.id.navigation_host))
    }

    fun isAtCurrentDestination(destination: Int): Boolean {
        return Navigation.findNavController(this, R.id.navigation_host).currentDestination?.id == destination
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Podsjetnik za testove"
            val descriptionText = "Služi za prikaz notifikacija o testovima"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.PODSJETNIK_TESTA_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notificationChannels.removeAll { true }
            notificationManager.createNotificationChannel(channel)
        }
    }
}
