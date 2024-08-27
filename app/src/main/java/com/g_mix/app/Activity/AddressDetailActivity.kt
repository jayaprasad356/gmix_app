package com.g_mix.app.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.g_mix.app.Model.HomeProduct
import com.g_mix.app.R
import com.g_mix.app.databinding.ActivityAddressDetailBinding
import com.g_mix.app.helper.ApiConfig
import com.g_mix.app.helper.Constant
import com.g_mix.app.helper.Session
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class AddressDetailActivity : BaseActivity() {

    lateinit var binding: ActivityAddressDetailBinding
    lateinit var activity: Activity
    lateinit var session: Session
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddressDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        session = Session(activity)

        var id = intent.getStringExtra("ID")
        var itemName = intent.getStringExtra("ITEM_NAME")
        var itemPrice = intent.getStringExtra("ITEM_PRICE")
        var itemImage = intent.getStringExtra("ITEM_IMAGE")
        var itemWeight = intent.getStringExtra("ITEM_WEIGHT")

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }


        // Find views and set data
        findViewById<TextView>(R.id.tvItemName).text = itemName
        findViewById<TextView>(R.id.tvItemWeight).text = itemWeight

        Glide.with(this)
            .load(itemImage)
            .placeholder(R.drawable.demo_image)
            .error(R.drawable.demo_image)
            .into(findViewById(R.id.ivItemImage))

        // Set OnClickListener on btnGenerateOtp button
        binding.btnAddAddrsss.setOnClickListener {
            addAddress(id ?: "",
                itemName ?: "",
                itemPrice ?: "",
                itemImage ?: "",
                itemWeight ?: "")
        }

        val spinner: Spinner = findViewById(R.id.spinner_states)

        val statesArray = resources.getStringArray(R.array.indian_states)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statesArray)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }


    private fun addAddress( id: String, itemName: String, itemPrice: String, itemImage: String, itemWeight: String) {
        if (isLoading) return
        isLoading = true

        val params = buildProfileParams()
        ApiConfig.RequestToVolley({ result, response ->
            handleProfileResponse(result, response, id, itemName, itemPrice, itemImage, itemWeight)
        }, activity, Constant.ADD_ADDRESS, params, true, 1)
    }

    private fun buildProfileParams(): HashMap<String, String> {
        // Get reference to the TextInputEditText view
        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etMobileNumber = findViewById<TextInputEditText>(R.id.etMobileNumber)
        val etAlternateMobile = findViewById<TextInputEditText>(R.id.etAlternateMobile)
        val etStreet = findViewById<TextInputEditText>(R.id.etStreet)
        val etDoorNumber = findViewById<TextInputEditText>(R.id.etDoorNumber)
        val etLandmark = findViewById<TextInputEditText>(R.id.etLandmark)
        val etPincode = findViewById<TextInputEditText>(R.id.etPincode)
        val etCity = findViewById<TextInputEditText>(R.id.etCity)
        val spinnerStates = findViewById<Spinner>(R.id.spinner_states)

        // Retrieve the value and print it
        val name = etName.text.toString()
        val mobileNumber = etMobileNumber.text.toString()
        val alternateMobile = etAlternateMobile.text.toString()
        val street = etStreet.text.toString()
        val doorNumber = etDoorNumber.text.toString()
        val landmark = etLandmark.text.toString()
        val pincode = etPincode.text.toString()
        val city = etCity.text.toString()
        val state = spinnerStates.selectedItem.toString()

        return hashMapOf(
            Constant.USER_ID to "1",
//            Constant.USER_ID to session.getData(Constant.USER_ID),
            Constant.NAME to name,
            Constant.MOBILE to mobileNumber,
            Constant.ALTERNATE_MOBILE to alternateMobile,
            Constant.STREET_NAME to street,
            Constant.DOOR_NUMBER to doorNumber,
            Constant.LANDMARK to landmark,
            Constant.PINCODE to pincode,
            Constant.CITY to city,
            Constant.STATE to state,
        )
    }

    private fun handleProfileResponse(result: Boolean, response: String, id: String, itemName: String, itemPrice: String, itemImage: String, itemWeight: String) {
        isLoading = false

        if (result) {
            try {
                val jsonObject: JSONObject = JSONObject(response)
                if (jsonObject.getBoolean(Constant.SUCCESS)) {
                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()

                    val intent = Intent(activity, PaymentActivity::class.java)
                    intent.putExtra("ID", id)
                    intent.putExtra("ITEM_NAME", itemName)
                    intent.putExtra("ITEM_PRICE", itemPrice)
                    intent.putExtra("ITEM_IMAGE", itemImage)
                    intent.putExtra("ITEM_WEIGHT", itemWeight)
                    startActivity(intent)
                } else {
                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}