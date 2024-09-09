package com.g_mix.gmw_app.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.g_mix.gmw_app.Fargment.SelectedAddressFragment
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.databinding.FragmentAddressBinding
import com.g_mix.gmw_app.databinding.FragmentPaymentBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class AddressFragment : Fragment() {

    lateinit var binding: FragmentAddressBinding
    lateinit var session: Session
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        session = Session(requireActivity())

        val spinner: Spinner = binding.spinnerStates

        val statesArray = resources.getStringArray(R.array.indian_states)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        userDetails()

        arguments?.let {
            val productId = it.getString("PRODUCT_ID")
            val itemName = it.getString("ITEM_NAME")
            val itemPrice = it.getString("ITEM_PRICE")
            val itemImage = it.getString("ITEM_IMAGE")
            val itemWeight = it.getString("ITEM_WEIGHT")


        }

        binding.etPincode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    pincode()
                }
            }
        })

        applyFocusChangeListeners()

        binding.btnAddAddrsss.setOnClickListener {
            addAddress()
        }

        return binding.root
    }

    private fun applyFocusChangeListeners() {
        focusChangeListener(binding.etFirstName, binding.cvFirstname)
        focusChangeListener(binding.etLastName, binding.cvLastname)
        focusChangeListener(binding.etMobileNumber, binding.cvMobile)
        focusChangeListener(binding.etAlternateMobile, binding.cvAlternateMobile)
        focusChangeListener(binding.etPincode, binding.cvPincode)
        focusChangeListener(binding.etDoorNumber, binding.cvDoorNumber)
        focusChangeListener(binding.etStreet, binding.cvStreet)
        focusChangeListener(binding.etCity, binding.cvCity)
        focusChangeListener(binding.etLandmark, binding.cvLandmark)
    }

    private fun focusChangeListener(input: TextInputEditText, card: MaterialCardView) {
        input.setOnFocusChangeListener { _, hasFocus ->
            card.strokeColor = if (hasFocus) ContextCompat.getColor(requireContext(), R.color.primary)
            else ContextCompat.getColor(requireContext(), android.R.color.transparent)
        }
    }

    private fun addAddress() {
        val name = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val mobileNumber = binding.etMobileNumber.text.toString()
        val alternateMobile = binding.etAlternateMobile.text.toString()
        val street = binding.etStreet.text.toString()
        val doorNumber = binding.etDoorNumber.text.toString()
        val pincode = binding.etPincode.text.toString()
        val city = binding.etCity.text.toString()
        val state = binding.spinnerStates.selectedItem.toString()

        if (validateInputs(name, mobileNumber, alternateMobile, doorNumber, street, city, state, pincode)) {
            // Call API to add address
            val params = buildProfileParams(
                name, lastName, mobileNumber, alternateMobile, street, doorNumber, "", pincode, city, state
            )
            ApiConfig.RequestToVolley({ result, response ->
                handleProfileResponse(result, response)
            }, requireActivity(), Constant.ADD_ADDRESS, params, true, 1)
        }
    }

    private fun validateInputs(
        name: String, mobileNumber: String, alternateMobile: String, doorNumber: String,
        street: String, city: String, state: String, pincode: String
    ): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(activity, "Please enter Name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (mobileNumber.isEmpty() || mobileNumber.length != 10) {
            Toast.makeText(activity, "Please enter valid mobile number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (alternateMobile.isEmpty() || alternateMobile.length != 10) {
            Toast.makeText(activity, "Please enter valid alternate mobile number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (doorNumber.isEmpty()) {
            Toast.makeText(activity, "Please enter door number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (street.isEmpty()) {
            Toast.makeText(activity, "Please enter street", Toast.LENGTH_SHORT).show()
            return false
        }
        if (city.isEmpty()) {
            Toast.makeText(activity, "Please enter city", Toast.LENGTH_SHORT).show()
            return false
        }
        if (state == "Select State") {
            Toast.makeText(activity, "Please select a state", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pincode.isEmpty() || pincode.length != 6) {
            Toast.makeText(activity, "Please enter valid pincode", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun buildProfileParams(
        name: String, lastName: String, mobileNumber: String, alternateMobile: String,
        street: String, doorNumber: String, landmark: String, pincode: String,
        city: String, state: String
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

    private fun handleProfileResponse(result: Boolean, response: String) {
        if (result) {
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.getBoolean(Constant.SUCCESS)) {
                    val addressId = jsonObject.getJSONObject(Constant.DATA).getString(Constant.ID)
                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                    // Handle success, navigate to another fragment or activity if needed
                    val fm = requireActivity().supportFragmentManager
                    val transaction = fm.beginTransaction()
                    transaction.replace(R.id.fragment_container, SelectedAddressFragment())
                    transaction.addToBackStack(null) // Optional: to add the transaction to the back stack
                    transaction.commit()
                } else {
                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                Toast.makeText(activity, "An error occurred while parsing data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun userDetails() {
        val params = HashMap<String, String>()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)

        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray = jsonObject.getJSONArray(Constant.DATA)
                        if (jsonArray.length() > 0) {
                            val addressObject = jsonArray.getJSONObject(0)
                            // Populate the views with the data from the first address
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.USERDETAILS, params, true, 1)
    }

    private fun pincode() {
        val params = HashMap<String, String>()
        params[Constant.PINCODE] = binding.etPincode.text.toString()

        activity?.let {
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
            }, it, Constant.PINCODE_URL, params, false)
        }
    }

    private fun getStateIndex(state: String): Int {
        val statesArray = resources.getStringArray(R.array.indian_states)
        return statesArray.indexOf(state)
    }

}
