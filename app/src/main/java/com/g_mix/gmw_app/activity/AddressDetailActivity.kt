package com.g_mix.gmw_app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.databinding.ActivityAddressDetailBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
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

        userdetails()
        var productId = intent.getStringExtra("PRODUCT_ID")
        var itemName = intent.getStringExtra("ITEM_NAME")
        var itemPrice = intent.getStringExtra("ITEM_PRICE")
        var itemImage = intent.getStringExtra("ITEM_IMAGE")
        var itemWeight = intent.getStringExtra("ITEM_WEIGHT")

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.etPincode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }

            override fun afterTextChanged(s: Editable?) {
                // Check if the length is 6 digits
                if (s?.length == 6) {
                    pincode() // Call the API to get city and state based on the pincode
                }
            }
        })


        // Find views and set data
        findViewById<TextView>(R.id.tvItemName).text = itemName
        findViewById<TextView>(R.id.tvItemWeight).text = itemWeight


        binding.etFirstName.setOnFocusChangeListener { _, hasFocus ->
            val materialCardView = findViewById<MaterialCardView>(R.id.cvFirstname)
            if (hasFocus) {
                // On focus, show the stroke
                materialCardView.strokeColor = ContextCompat.getColor(this, R.color.primary)
            } else {
                // On focus lost, remove the stroke
                materialCardView.strokeColor = ContextCompat.getColor(this, android.R.color.transparent)
            }
        }

        binding.etLastName.setOnFocusChangeListener { _, hasFocus ->
            val materialCardView = findViewById<MaterialCardView>(R.id.cvLastname)
            if (hasFocus) {
                // On focus, show the stroke
                materialCardView.strokeColor = ContextCompat.getColor(this, R.color.primary)
            } else {
                // On focus lost, remove the stroke
                materialCardView.strokeColor = ContextCompat.getColor(this, android.R.color.transparent)
            }

        }

        binding.etMobileNumber.setOnFocusChangeListener { _, hasFocus ->
            val materialCardView = findViewById<MaterialCardView>(R.id.cvMobile)

            if (hasFocus) {
                // On focus, show the stroke
                materialCardView.strokeColor = ContextCompat.getColor(this, R.color.primary)
            } else {
                // On focus lost, remove the stroke
                materialCardView.strokeColor =
                    ContextCompat.getColor(this, android.R.color.transparent)
            }
        }
        binding.etAlternateMobile.setOnFocusChangeListener { _, hasFocus ->
            val materialCardView = findViewById<MaterialCardView>(R.id.cvAlternateMobile)

            if (hasFocus) {
                // On focus, show the stroke
                materialCardView.strokeColor = ContextCompat.getColor(this, R.color.primary)
            } else {
                // On focus lost, remove the stroke
                materialCardView.strokeColor =
                    ContextCompat.getColor(this, android.R.color.transparent)
            }
        }
        binding.etPincode.setOnFocusChangeListener { _, hasFocus ->
            val materialCardView = findViewById<MaterialCardView>(R.id.cvPincode)

            if (hasFocus) {
                // On focus, show the stroke
                materialCardView.strokeColor = ContextCompat.getColor(this, R.color.primary)
            } else {
                // On focus lost, remove the stroke
                materialCardView.strokeColor =
                    ContextCompat.getColor(this, android.R.color.transparent)
            }
        }
        binding.etDoorNumber.setOnFocusChangeListener { _, hasFocus ->
            val materialCardView = findViewById<MaterialCardView>(R.id.cvDoorNumber)

            if (hasFocus) {
                // On focus, show the stroke
                materialCardView.strokeColor = ContextCompat.getColor(this, R.color.primary)
            } else {
                // On focus lost, remove the stroke
                materialCardView.strokeColor =
                    ContextCompat.getColor(this, android.R.color.transparent)
            }
        }
        binding.etStreet.setOnFocusChangeListener { _, hasFocus ->
            val materialCardView = findViewById<MaterialCardView>(R.id.cvStreet)

            if (hasFocus) {
                // On focus, show the stroke
                materialCardView.strokeColor = ContextCompat.getColor(this, R.color.primary)
            } else {
                // On focus lost, remove the stroke
                materialCardView.strokeColor =
                    ContextCompat.getColor(this, android.R.color.transparent)
            }
        }
        binding.etCity.setOnFocusChangeListener { _, hasFocus ->
            val materialCardView = findViewById<MaterialCardView>(R.id.cvCity)

            if (hasFocus) {
                // On focus, show the stroke
                materialCardView.strokeColor = ContextCompat.getColor(this, R.color.primary)
            } else {
                // On focus lost, remove the stroke
                materialCardView.strokeColor =
                    ContextCompat.getColor(this, android.R.color.transparent)
            }
        }
        binding.etLandmark.setOnFocusChangeListener { _, hasFocus ->
            val materialCardView = findViewById<MaterialCardView>(R.id.cvLandmark)

            if (hasFocus) {
                // On focus, show the stroke
                materialCardView.strokeColor = ContextCompat.getColor(this, R.color.primary)
            } else {
                // On focus lost, remove the stroke
                materialCardView.strokeColor =
                    ContextCompat.getColor(this, android.R.color.transparent)
            }
        }






        Glide.with(this)
            .load(itemImage)
            .placeholder(R.drawable.demo_image)
            .error(R.drawable.demo_image)
            .into(findViewById(R.id.ivItemImage))

        // Set OnClickListener on btnGenerateOtp button
        binding.btnAddAddrsss.setOnClickListener {

            // Get reference to the TextInputEditText view
            val etName = findViewById<TextInputEditText>(R.id.etFirstName)
            val etLastName = findViewById<TextInputEditText>(R.id.etLastName)
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
            val lastName = etLastName.text.toString()
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
                    lastName ?: "",
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
        lastName: String,
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
            lastName,
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
        lastName: String,
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
            Constant.FIRST_NAME to name,
            Constant.LAST_NAME to lastName,
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
                    // Get the data object directly (no array)
                    val dataObject = jsonObject.getJSONObject(Constant.DATA)

                    // Extract the values from the data object
                    val addressId = dataObject.getString(Constant.ID)
                    val name = dataObject.getString(Constant.FIRST_NAME)
                    val lastName = dataObject.getString(Constant.LAST_NAME)
                    val mobile = dataObject.getString(Constant.MOBILE)
                    val alternateMobile = dataObject.getString(Constant.ALTERNATE_MOBILE)
                    val doorNumber = dataObject.getString(Constant.DOOR_NUMBER)
                    val streetName = dataObject.getString(Constant.STREET_NAME)
                    val city = dataObject.getString(Constant.CITY)
                    val pincode = dataObject.getString(Constant.PINCODE)
                    val state = dataObject.getString(Constant.STATE)
                    val landmark = dataObject.optString("landmark", "") // Optional, might be empty

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
                Toast.makeText(activity, "An error occurred while parsing data", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun userdetails() {
        val params = HashMap<String, String>()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)

        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(Constant.DATA)

                        // Assuming you want to set the first address in the binding
                        if (jsonArray.length() > 0) {
                            val addressObject = jsonArray.getJSONObject(0)

                            // Extract the data from the first address object
                            val name = addressObject.getString("first_name").toString()
                            val lastName = addressObject.getString("last_name").toString()
                            val mobile = addressObject.getString("mobile").toString()
                            val alternateMobile = addressObject.getString("alternate_mobile").toString()
                            val doorNumber = addressObject.getString("door_no").toString()
                            val streetName = addressObject.getString("street_name").toString()
                            val city = addressObject.getString("city").toString()
                            val pincode = addressObject.getString("pincode").toString()
                            val state = addressObject.getString("state").toString()
                        //    val landmark = addressObject.getString("landmark").toString()

                            // Construct the full address string
                            val fullAddress = "$doorNumber, $streetName, $city, $state - $pincode"

                            // Set the data in your binding
                            binding.etFirstName.setText(name)
                            binding.etLastName.setText(lastName)
                            binding.etMobileNumber.setText(mobile)
                            binding.etAlternateMobile.setText(alternateMobile)
                            binding.etDoorNumber.setText(doorNumber)
                            binding.etStreet.setText(streetName)
                            binding.etCity.setText(city)
                            binding.etPincode.setText(pincode)
                            binding.spinnerStates.setSelection(getStateIndex(state))
                            binding.etLandmark.setText("")

                            // Additional setting if needed
                            // binding.etAlternateMobile.setText(alternateMobile)
                            // binding.etDoorNumber.setText(doorNumber)
                        }
                    } else {
                       // Toast.makeText(this, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
              //  Toast.makeText(this, "Failed to retrieve addresses", Toast.LENGTH_SHORT).show()
            }
        }, this, Constant.MY_ADDRESS_LIST, params, true)
    }
    private fun pincode() {
        val params = HashMap<String, String>()
        params[Constant.PINCODE] = binding.etPincode.text.toString()

        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val dataObject = jsonObject.getJSONObject(Constant.DATA)
                        val postOfficeArray = dataObject.getJSONArray("PostOffice")

                        if (postOfficeArray.length() > 0) {
                            val postOffice = postOfficeArray.getJSONObject(0)
                            val district = postOffice.getString("District")
                            val state = postOffice.getString("State")

                            // Set the city (district) and state
                            binding.etCity.setText(district)
                            binding.spinnerStates.setSelection(getStateIndex(state))
                        } else {
                         //   Toast.makeText(this, "No Post Offices found for the given pincode.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                      //  Toast.makeText(this, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                 //   Toast.makeText(this, "Error parsing pincode data.", Toast.LENGTH_SHORT).show()
                }
            } else {
              //  Toast.makeText(this, "Failed to retrieve pincode data.", Toast.LENGTH_SHORT).show()
            }
        }, this, Constant.PINCODE_URL, params, false)
    }


    private fun getStateIndex(state: String): Int {
        val statesArray = resources.getStringArray(R.array.indian_states)
        return statesArray.indexOf(state)
    }



}