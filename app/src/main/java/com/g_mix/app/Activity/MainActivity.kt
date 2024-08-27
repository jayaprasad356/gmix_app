package com.g_mix.app.Activity


import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import com.g_mix.app.Fragment.HomeFragment
import com.g_mix.app.Fragment.MyOrderFragment
import com.g_mix.app.R
import com.g_mix.app.helper.Session
import com.g_mix.app.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : BaseActivity(), NavigationBarView.OnItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    lateinit var activity: Activity
    lateinit var session: Session

    private lateinit var fm: FragmentManager

    private lateinit var bottomNavigationView: BottomNavigationView

    private val homeFragment = HomeFragment()
    private val myOrderFragment = MyOrderFragment()

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeComponents()

        if (savedInstanceState != null) {
            val selectedItemId = savedInstanceState.getInt("selectedItemId", R.id.navHome)
            bottomNavigationView.selectedItemId = selectedItemId

            when (selectedItemId) {
                R.id.navHome -> fm.beginTransaction().replace(R.id.fragment_container, homeFragment).commit()
                R.id.navMyOrder -> fm.beginTransaction().replace(R.id.fragment_container, myOrderFragment).commit()
            }
        } else {
            fm.beginTransaction().replace(R.id.fragment_container, homeFragment).commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedItemId", bottomNavigationView.selectedItemId)
    }

    private fun initializeComponents() {
        activity = this
        session = Session(activity)
        fm = supportFragmentManager
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(this)

        // Set default fragment
        fm.beginTransaction().replace(R.id.fragment_container, homeFragment).commit()
        // Set default selected icon
        bottomNavigationView.menu.findItem(R.id.navHome).setIcon(R.drawable.nav_select_home)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction = fm.beginTransaction()

        // Reset all icons to default
        bottomNavigationView.menu.findItem(R.id.navHome).setIcon(R.drawable.nav_home)
        bottomNavigationView.menu.findItem(R.id.navMyOrder).setIcon(R.drawable.nav_my_product)

        // Handle navigation and set selected icon
        when (item.itemId) {
            R.id.navHome -> {
                transaction.replace(R.id.fragment_container, homeFragment)
                item.setIcon(R.drawable.nav_select_home)
            }
            R.id.navMyOrder -> {
                transaction.replace(R.id.fragment_container, myOrderFragment)
                item.setIcon(R.drawable.nav_select_my_oreder)
            }
        }
        transaction.commit()
        return true
    }
}

