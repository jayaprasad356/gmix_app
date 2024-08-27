package com.g_mix.app.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        session = Session(activity)

        var id = intent.getStringExtra("ID")
        var itemName = intent.getStringExtra("ITEM_NAME")
        var itemPrice = intent.getStringExtra("ITEM_PRICE")
        var itemImage = intent.getStringExtra("ITEM_IMAGE")
        var itemWeight = intent.getStringExtra("ITEM_WEIGHT")

        // Find views and set data
        findViewById<TextView>(R.id.tvItemName).text = itemName
        findViewById<TextView>(R.id.tvItemWeight).text = itemWeight

        Glide.with(this)
            .load(itemImage)
            .placeholder(R.drawable.demo_image)
            .error(R.drawable.demo_image)
            .into(findViewById(R.id.ivItemImage))

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnConfirm.setOnClickListener {
            placeOrder(
                id ?: "",
                itemName ?: "",
                itemPrice ?: "",
                itemImage ?: "",
                itemWeight ?: ""
            )
        }
    }



    private fun placeOrder( id: String, itemName: String, itemPrice: String, itemImage: String, itemWeight: String) {
        if (isLoading) return
        isLoading = true

        val params = buildProfileParams( id, itemName, itemPrice, itemImage, itemWeight)
        ApiConfig.RequestToVolley({ result, response ->
            handleProfileResponse(result, response)
        }, activity, Constant.PRODUCT_LIST, params, true, 1)
    }

    private fun buildProfileParams( id: String, itemName: String, itemPrice: String, itemImage: String, itemWeight: String): HashMap<String, String> {return hashMapOf(
            Constant.USER_ID to "1",
//            Constant.USER_ID to session.getData(Constant.USER_ID),
            Constant.PRODUCT_ID to "1",
//            Constant.PRODUCT_ID to id,
            Constant.ADDRESS_ID to "1",
//            Constant.ADDRESS_ID to ADDRESS_ID,
            Constant.PRICE to itemPrice,
            Constant.DELIVERY_CHARGE to "100",
            Constant.PAYMENT_MODE to "cod",
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