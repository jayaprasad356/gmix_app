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

        val spinner: Spinner = findViewById(R.id.spinner_states)

        val statesArray = resources.getStringArray(R.array.indian_states)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statesArray)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        var productId = intent.getStringExtra("PRODUCT_ID")
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

            if (name.isEmpty()) {
                Toast.makeText(activity, "Please enter Name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (mobileNumber.isEmpty()) {
                Toast.makeText(activity, "Please enter mobile number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (mobileNumber.length != 10) {
                Toast.makeText(activity, "Please enter valid mobile number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (alternateMobile.isEmpty()) {
                Toast.makeText(activity, "Please enter alternate mobile number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (alternateMobile.length != 10) {
                Toast.makeText(activity, "Please enter valid alternate mobile number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (doorNumber.isEmpty()) {
                Toast.makeText(activity, "Please enter door number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (street.isEmpty()) {
                Toast.makeText(activity, "Please enter street", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (city.isEmpty()) {
                Toast.makeText(activity, "Please enter city", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (state == "Select State" || state.isEmpty()) {
                Toast.makeText(activity, "Please select a state", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (pincode.isEmpty()) {
                Toast.makeText(activity, "Please enter pin code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (pincode.length != 6) {
                Toast.makeText(activity, "Please enter valid pin code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                addAddress(
                    productId ?: "",
                    itemName ?: "",
                    itemPrice ?: "",
                    itemImage ?: "",
                    itemWeight ?: "",
                    name ?: "",
                    mobileNumber ?: "",
                    alternateMobile ?: "",
                    street ?: "",
                    doorNumber ?: "",
                    landmark ?: "",
                    pincode ?: "",
                    city ?: "",
                    state ?: ""
                )
            }
        }
    }


    private fun addAddress(
        productId: String,
        itemName: String,
        itemPrice: String,
        itemImage: String,
        itemWeight: String,
        name: String,
        mobileNumber: String,
        alternateMobile: String,
        street: String,
        doorNumber: String,
        landmark: String,
        pincode: String,
        city: String,
        state: String,
        ) {
        if (isLoading) return
        isLoading = true

        val params = buildProfileParams(
            name,
            mobileNumber,
            alternateMobile,
            street,
            doorNumber,
            landmark,
            pincode,
            city,
            state,
            )
        ApiConfig.RequestToVolley({ result, response ->
            handleProfileResponse(
                result,
                response,
                productId,
                itemName,
                itemPrice,
                itemImage,
                itemWeight,
                )
        }, activity, Constant.ADD_ADDRESS, params, true, 1)

        Log.d("ADD_ADDRESS","ADD_ADDRESS: " + Constant.ADD_ADDRESS)
        Log.d("ADD_ADDRESS","ADD_ADDRESSparams: " + params)
    }

    private fun buildProfileParams(
        name: String,
        mobileNumber: String,
        alternateMobile: String,
        street: String,
        doorNumber: String,
        landmark: String,
        pincode: String,
        city: String,
        state: String,
        ): HashMap<String, String> {

        return hashMapOf(
            Constant.USER_ID to session.getData(Constant.USER_ID),
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

    private fun handleProfileResponse(
        result: Boolean,
        response: String,
        productId: String,
        itemName: String,
        itemPrice: String,
        itemImage: String,
        itemWeight: String,
    ) {
        isLoading = false

        if (result) {
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.getBoolean(Constant.SUCCESS)) {
                    // Get the data array and then the first object within it
                    val dataArray = jsonObject.getJSONArray(Constant.DATA)
                    val dataObject = dataArray.getJSONObject(0)

                    // Extract the values from the data object
                    val addressId = dataObject.getString(Constant.ID)
                    val name = dataObject.getString(Constant.NAME)
                    val mobile = dataObject.getString(Constant.MOBILE)
                    val alternateMobile = dataObject.getString(Constant.ALTERNATE_MOBILE)
                    val doorNumber = dataObject.getString(Constant.DOOR_NUMBER)
                    val streetName = dataObject.getString(Constant.STREET_NAME)
                    val city = dataObject.getString(Constant.CITY)
                    val pincode = dataObject.getString(Constant.PINCODE)
                    val state = dataObject.getString(Constant.STATE)

                    // Construct the full address string
                    val address = "$doorNumber, $streetName, $city, $state - $pincode"

                    // Show success message
                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()

                    // Prepare the intent with all necessary data
                    val intent = Intent(activity, PaymentActivity::class.java)
                    intent.putExtra("PRODUCT_ID", productId)
                    intent.putExtra("ITEM_NAME", itemName)
                    intent.putExtra("ITEM_PRICE", itemPrice)
                    intent.putExtra("ITEM_IMAGE", itemImage)
                    intent.putExtra("ITEM_WEIGHT", itemWeight)
                    intent.putExtra("NAME", name)
                    intent.putExtra("MOBILE_NUMBER", mobile)
                    intent.putExtra("ADDRESS", address)
                    intent.putExtra("ADDRESS_ID", addressId)

                    // Start the PaymentActivity
                    startActivity(intent)
                } else {
                    // Handle the failure case
                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

}