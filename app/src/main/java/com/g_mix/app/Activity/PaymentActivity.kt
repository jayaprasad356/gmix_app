package com.g_mix.app.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.g_mix.app.R
import com.g_mix.app.databinding.ActivityPaymentBinding
import com.g_mix.app.helper.ApiConfig
import com.g_mix.app.helper.Constant
import com.g_mix.app.helper.Session
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class PaymentActivity : BaseActivity() {

    lateinit var binding: ActivityPaymentBinding
    lateinit var activity: Activity
    lateinit var session: Session
    private var isLoading = false

    private lateinit var rdbSelectUPI: RadioButton
    private lateinit var rdbSelectCashOn: RadioButton
    private var selectedPaymentMode: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        session = Session(activity)

        var productId = intent.getStringExtra("PRODUCT_ID")
        var itemName = intent.getStringExtra("ITEM_NAME")
        var itemPrice = intent.getStringExtra("ITEM_PRICE")?.toDoubleOrNull() ?: 0.0
        var itemImage = intent.getStringExtra("ITEM_IMAGE")
        var itemWeight = intent.getStringExtra("ITEM_WEIGHT")
        var userName = intent.getStringExtra("NAME")
        var mobileNumber = intent.getStringExtra("MOBILE_NUMBER")
        var address = intent.getStringExtra("ADDRESS")
        var addressId = intent.getStringExtra("ADDRESS_ID")

        var quantity = 1
        val totalQuantityPrice = itemPrice * quantity

        val totalPrice = totalQuantityPrice + 10

        // Find views and set data
        findViewById<TextView>(R.id.tvItemName).text = itemName
        findViewById<TextView>(R.id.tvItemWeight).text = itemWeight
        findViewById<TextView>(R.id.tvPrice).text = "₹$itemPrice"
        findViewById<TextView>(R.id.tvDeliveryCharges).text = "₹" + "10"
        findViewById<TextView>(R.id.tvTotal).text = "₹$totalPrice"
        findViewById<TextView>(R.id.tvName).text = userName
        findViewById<TextView>(R.id.tvAddress).text = address
        findViewById<TextView>(R.id.tvMobileNumber).text = mobileNumber


        // Initialize RadioButtons
        rdbSelectUPI = findViewById(R.id.rdbSelectUPI)
        rdbSelectCashOn = findViewById(R.id.rdbSelectCashOn)

        // Set listeners for RadioButtons
        rdbSelectUPI.setOnClickListener { onRadioButtonClicked(rdbSelectUPI) }
        rdbSelectCashOn.setOnClickListener { onRadioButtonClicked(rdbSelectCashOn) }

        Glide.with(this)
            .load(itemImage)
            .placeholder(R.drawable.demo_image)
            .error(R.drawable.demo_image)
            .into(findViewById(R.id.ivItemImage))

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        val tvQuantityVal = findViewById<TextView>(R.id.tvQuantityVal)
        val btnIncrease = findViewById<ImageView>(R.id.btnIncrease)
        val btnDecrease = findViewById<ImageView>(R.id.btnDecrease)

        tvQuantityVal.text = quantity.toString()

        btnIncrease.setOnClickListener {
            quantity += 1
            tvQuantityVal.text = quantity.toString()

            val totalQuantityPrice = itemPrice * quantity
            val totalPrice = totalQuantityPrice + 10
            findViewById<TextView>(R.id.tvTotal).text = "₹$totalPrice"
        }

        btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity -= 1
                tvQuantityVal.text = quantity.toString()

                val totalQuantityPrice = itemPrice * quantity
                val totalPrice = totalQuantityPrice + 10
                findViewById<TextView>(R.id.tvTotal).text = "₹$totalPrice"
            }
        }

        binding.btnConfirm.setOnClickListener {
            if(selectedPaymentMode != ""){
                placeOrder(
                    productId ?: "",
                    itemPrice.toString() ?: "",
                    addressId ?: "",
                    "100",
//                    deliveryCharges ?: ""
                )
            } else {
                Toast.makeText(activity, "Please select your payment method.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onRadioButtonClicked(selectedRadioButton: RadioButton) {
        // Uncheck the other RadioButton
        if (selectedRadioButton == rdbSelectUPI) {
            rdbSelectCashOn.isChecked = false
            selectedPaymentMode = "upi"
        } else if (selectedRadioButton == rdbSelectCashOn) {
            rdbSelectUPI.isChecked = false
            selectedPaymentMode = "cod"
        }

        // Mark the selected RadioButton as checked
        selectedRadioButton.isChecked = true
    }

    private fun placeOrder(
        productId: String,
        itemPrice: String,
        addressId: String,
        deliveryCharges: String,
        ) {
        if (isLoading) return
        isLoading = true

        val params = buildProfileParams(
            productId,
            itemPrice,
            addressId,
            deliveryCharges)
        ApiConfig.RequestToVolley({ result, response ->
            handleProfileResponse(result, response)
        }, activity, Constant.PLACE_ORDER, params, true, 1)

        Log.d("PLACE_ORDER", "PLACE_ORDER: " + Constant.PLACE_ORDER)
        Log.d("PLACE_ORDER", "PLACE_ORDERparams: " + params)
    }

    private fun buildProfileParams(
        productId: String,
        itemPrice: String,
        addressId: String,
        deliveryCharges: String,
        ): HashMap<String, String> {return hashMapOf(
            Constant.USER_ID to session.getData(Constant.USER_ID),
            Constant.PRODUCT_ID to productId,
            Constant.ADDRESS_ID to addressId,
            Constant.PRICE to itemPrice,
            Constant.DELIVERY_CHARGE to deliveryCharges,
            Constant.PAYMENT_MODE to selectedPaymentMode,
        )
    }

    private fun handleProfileResponse(result: Boolean, response: String) {
        isLoading = false

        if (result) {
            try {
                val jsonObject: JSONObject = JSONObject(response)
                if (jsonObject.getBoolean(Constant.SUCCESS)) {
                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}