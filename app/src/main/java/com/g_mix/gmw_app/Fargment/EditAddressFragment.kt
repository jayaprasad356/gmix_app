package com.g_mix.gmw_app.Fargment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.databinding.FragmentEditAddressBinding
import com.g_mix.gmw_app.databinding.FragmentShippedAddressBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class EditAddressFragment : Fragment() {



    lateinit var binding: FragmentEditAddressBinding
    lateinit var session: Session
    private var addressId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditAddressBinding.inflate(inflater, container, false)
        session = Session(requireActivity())




        // Retrieve the passed value using arguments
         addressId = arguments?.getString("address_id")
        Addressdetails(addressId)


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
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
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
        val state = binding.spinnerStates.text.toString()

        if (validateInputs(name, mobileNumber, alternateMobile, doorNumber, street, city, state, pincode)) {
            // Call API to add address
            val params = buildProfileParams(
                name, lastName, mobileNumber, alternateMobile, street, doorNumber, "", pincode, city, state
            )
            ApiConfig.RequestToVolley({ result, response ->
                handleProfileResponse(result, response)
            }, requireActivity(), Constant.UPDATE_ADDRESS, params, true, 1)
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
            Constant.ADDRESS_ID to addressId.toString(),
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

                        requireActivity().onBackPressed()

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
                                binding.spinnerStates.setText(state)
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


    private fun Addressdetails(addressId: String?) {
        val params = HashMap<String, String>()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        params[Constant.ADDRESS_ID] = addressId.toString()
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
                            binding.spinnerStates.setText(state)
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
        }, requireActivity(), Constant.ADDRESS_DETAILS, params, true)
    }


}