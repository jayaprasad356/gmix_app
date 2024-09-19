package com.g_mix.gmw_app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.g_mix.gmw_app.Adapter.AddresslistAdapter
import com.g_mix.gmw_app.Fargment.SelectedAddressFragment
import com.g_mix.gmw_app.Model.Addresslist
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.databinding.ActivityCartBinding
import com.g_mix.gmw_app.databinding.ActivityProductDetailsBinding
import com.g_mix.gmw_app.fragment.AddressFragment
import com.g_mix.gmw_app.fragment.PaymentFragment
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartActivity : BaseActivity() {

    lateinit var binding: ActivityCartBinding
    lateinit var activity: Activity
    lateinit var session: Session
    private var isLoading = false




    private lateinit var fm: FragmentManager

    private val paymentFragment = PaymentFragment()
    private val selectedAddressBinding = SelectedAddressFragment()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        session = Session(activity)

        val itemImage = intent.getStringExtra("ITEM_IMAGE")
        val itemName = intent.getStringExtra("ITEM_NAME")
        val itemPrice = intent.getStringExtra("ITEM_PRICE")
        val itemQuantity = intent.getStringExtra("ITEM_QUANTITY")
        val itemTotal = intent.getStringExtra("ITEM_TOTAL")
        val itemWeight = intent.getStringExtra("ITEM_WEIGHT")



        Glide.with(activity).load(itemImage).placeholder(R.drawable.demo_image)
            .into(binding.ivItemImage)
        binding.tvItemName.text = itemName
        binding.tvItemWeight.text = itemWeight
        binding.tvQuantityVal.text = itemQuantity



      //  setSelectedTab(true)





        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        var productId = intent.getStringExtra("PRODUCT_ID")





        fm = supportFragmentManager
        fm.beginTransaction().replace(R.id.fragment_container, selectedAddressBinding).commit()









//        binding.btnPayment.setOnClickListener {
//            fm = supportFragmentManager
//            fm.beginTransaction().replace(R.id.fragment_container, paymentFragment).commit()
//            binding.llAddress.visibility = View.GONE
//            binding.llAddAddress.visibility = View.GONE
//            binding.btnPayment.visibility = View.GONE
//            intent.putExtra("PRODUCT_ID", productId)
//            intent.putExtra("ITEM_NAME", itemName)
//            intent.putExtra("ITEM_PRICE", itemPrice)
//            intent.putExtra("ITEM_IMAGE", itemImage)
//            intent.putExtra("ITEM_WEIGHT", itemWeight)
//            intent.putExtra("NAME", name)
//            intent.putExtra("MOBILE_NUMBER", mobile)
//            intent.putExtra("ADDRESS", address)
//            intent.putExtra("ADDRESS_ID", addressId)
//        }







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
    ): java.util.HashMap<String, String> {

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



                   // setSelectedTab(false)

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

}