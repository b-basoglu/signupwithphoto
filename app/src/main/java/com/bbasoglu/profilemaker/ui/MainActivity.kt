package com.bbasoglu.profilemaker.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import com.bbasoglu.core.ui.BaseActivity
import com.bbasoglu.profilemaker.R
import com.bbasoglu.profilemaker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(), BaseActivity.ActivityListener {


    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    override val viewModel by viewModels<MainActivityViewModel>()

    override fun getNavController(): NavController = navController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHost = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHost.navController
        destinationController()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }


    private fun destinationController(){
        navController.addOnDestinationChangedListener { _, destination, _ ->

        }
    }

    override fun onNavigate(navDirections: NavDirections, extras: ActivityNavigator.Extras?) {
        navigateTo(navDirections,extras)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}