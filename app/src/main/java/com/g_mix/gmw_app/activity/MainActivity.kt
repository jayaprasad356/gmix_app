package com.g_mix.gmw_app.activity


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.g_mix.gmw_app.Fargment.MyProfileFragment
import com.g_mix.gmw_app.Fragment.HomeFragment
import com.g_mix.gmw_app.Fragment.MyOrderFragment
import com.g_mix.gmw_app.Fragment.RewardFragment
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.helper.Session
import com.g_mix.gmw_app.databinding.ActivityMainBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class MainActivity : BaseActivity(), NavigationBarView.OnItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    lateinit var activity: Activity
    lateinit var session: Session

    private lateinit var fm: FragmentManager

    private lateinit var bottomNavigationView: BottomNavigationView

    private val homeFragment = HomeFragment()
    private val myOrderFragment = MyOrderFragment()
    private val rewardFragment = RewardFragment()
    private val myProfileFragment = MyProfileFragment()

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


        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()

        }
        binding.tvMobileNumber.text = session.getData(Constant.MOBILE)

        val fabWhatsapp: FloatingActionButton = findViewById(R.id.fabWhatsapp)
        fabWhatsapp.setOnClickListener {
            val whatsappIntent = Intent(Intent.ACTION_VIEW)
            val number = "91" + session.getData(Constant.CUSTOMER_SUPPORT_NUMBER)
            whatsappIntent.data = Uri.parse("https://api.whatsapp.com/send?phone=$number")
            try {
                startActivity(whatsappIntent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "WhatsApp not installed.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun showLogoutConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(activity)
            .setMessage("Are you sure you want to logout?")
            .setCancelable(true)
            .setPositiveButton("Logout") { dialog, _ ->
                // Perform logout action
                session.logoutUser(activity)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialogBuilder.show()

        // Change button text colors
        dialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity, R.color.primary))
        dialogBuilder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activity, R.color.text_grey))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedItemId", bottomNavigationView.selectedItemId)
    }

    private fun initializeComponents() {
        activity = this
        session = Session(activity)
        userdetails()
        setting()
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
        bottomNavigationView.menu.findItem(R.id.navReward).setIcon(R.drawable.outline_reward_ic)
        bottomNavigationView.menu.findItem(R.id.navMyProfile).setIcon(R.drawable.outline_profile)

        // Handle navigation and set selected icon
        when (item.itemId) {
            R.id.navHome -> {
                transaction.replace(R.id.fragment_container, homeFragment)
                item.setIcon(R.drawable.nav_select_home)
            }
            R.id.navReward -> {
                transaction.replace(R.id.fragment_container, rewardFragment)
                item.setIcon(R.drawable.reward_ic)
            }
            R.id.navMyOrder -> {
                transaction.replace(R.id.fragment_container, myOrderFragment)
                item.setIcon(R.drawable.nav_select_my_oreder)
            }
            R.id.navMyProfile -> {
                transaction.replace(R.id.fragment_container, myProfileFragment)
                item.setIcon(R.drawable.filled_profile)
            }
        }
        transaction.commit()
        return true
    }

    private fun userdetails() {
        val params = HashMap<String, String>()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(Constant.DATA)
                        val userObject = jsonArray.getJSONObject(0)
                        val points = userObject.getInt("points").toString()

                        // Log the points to verify they are correct
                        Log.d("POINTS", "Points retrieved: $points")

                        // Set points to session
                        session.setData(Constant.POINTS, points.toString())

                        // Log session data to verify it is set correctly
                        Log.d("SESSION", "Points in session: ${session.getData(Constant.POINTS)}")


                    } else {
                        Toast.makeText(
                            this,
                            jsonObject.getString(Constant.MESSAGE),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                    this,
                    "Failed to retrieve data.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, this, Constant.USERDETAILS, params, true)
    }



    private fun setting() {
        val params = HashMap<String, String>()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)

        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(Constant.DATA)

                        // Assuming you want to display the first item's delivery charge
                        if (jsonArray.length() > 0) {
                            val dataObject = jsonArray.getJSONObject(0)
                            val deliveryCharges = dataObject.getString("delivery_charges")
                            val customer_support_number = dataObject.getString("customer_support_number")

                            session.setData(Constant.DELIVERY_CHARGE, deliveryCharges)
                            session.setData(Constant.CUSTOMER_SUPPORT_NUMBER, customer_support_number)

                            // Display the delivery charges in a toast
                        }
                    } else {
                        Toast.makeText(
                            this,
                            jsonObject.getString(Constant.MESSAGE),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, "Failed to retrieve addresses", Toast.LENGTH_SHORT).show()
            }
        }, this, Constant.SETTING, params, true)
    }
}

