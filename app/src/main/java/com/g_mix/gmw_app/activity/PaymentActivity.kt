package com.g_mix.gmw_app.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.databinding.ActivityPaymentBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.HashMap

class PaymentActivity : BaseActivity() {

    lateinit var binding: ActivityPaymentBinding
    lateinit var activity: Activity
    lateinit var session: Session
    private var isLoading = false

    private lateinit var rdbSelectUPI: RadioButton
    private lateinit var rdbSelectCashOn: RadioButton
    private var selectedPaymentMode: String = ""
    private var totalQuantityPrice: Double = 0.0
    private var itemPrice: Double = 0.0
    private var addressId: String? = null
    private var productId: String? = null

    private val REQUEST_IMAGE_GALLERY = 2

    var imageUri: Uri? = null
    var filePath1: String? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        session = Session(activity)


         productId = intent.getStringExtra("PRODUCT_ID")
        var itemName = intent.getStringExtra("ITEM_NAME")
         itemPrice = intent.getStringExtra("ITEM_PRICE")?.toDoubleOrNull() ?: 0.0
        var itemImage = intent.getStringExtra("ITEM_IMAGE")
        var itemWeight = intent.getStringExtra("ITEM_WEIGHT")
        var userName = intent.getStringExtra("NAME")
        var mobileNumber = intent.getStringExtra("MOBILE_NUMBER")
        var address = intent.getStringExtra("ADDRESS")
         addressId = intent.getStringExtra("ADDRESS_ID")

        var quantity = 1
         totalQuantityPrice = itemPrice * quantity




        val deliveryCharges = 0.0
        val totalPrice = totalQuantityPrice + deliveryCharges




        // Find views and set data
        findViewById<TextView>(R.id.tvItemName).text = itemName
        findViewById<TextView>(R.id.tvItemWeight).text = itemWeight
        findViewById<TextView>(R.id.tvPrice).text = "₹$itemPrice"
        findViewById<TextView>(R.id.tvDeliveryCharges).text = "₹" + deliveryCharges
        findViewById<TextView>(R.id.tvTotal).text = "₹$totalPrice"
        findViewById<TextView>(R.id.tvName).text = userName
        findViewById<TextView>(R.id.tvAddress).text = address
        findViewById<TextView>(R.id.tvMobileNumber).text = mobileNumber


        // Initialize RadioButtons
        rdbSelectUPI = findViewById(R.id.rdbSelectUPI)
        rdbSelectCashOn = findViewById(R.id.rdbSelectCashOn)
        rdbSelectUPI.isChecked = true




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
        tvQuantityVal.text = quantity.toString()


        binding.btnConfirm.setOnClickListener {
            if (selectedPaymentMode != "") {
                placeOrder()
            }
            else if (rdbSelectUPI.isChecked)  {
                selectedPaymentMode = "Prepaid"
                placeOrder()
            }
            else if (rdbSelectCashOn.isChecked) {
                selectedPaymentMode = "COD"
                binding.llScreenUploding.visibility = View.GONE

            }
            else {
                Toast.makeText(activity, "Please select your payment method.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.btnUploadScreenshots.setOnClickListener {
            pickImageFromGallery()
        }

        binding.btnCopyUpi.setOnClickListener {
            copyUpiIdToClipboard()
        }
    }

    private fun copyUpiIdToClipboard() {
        val upiId = binding.tvUpiId.text.toString()
        if (upiId.isNotEmpty()) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("UPI ID", upiId)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "UPI ID copied to clipboard", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "UPI ID is empty", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to calculate the total price based on the selected payment method


    private fun onRadioButtonClicked(selectedRadioButton: RadioButton) {
        // Uncheck the other RadioButton
        if (selectedRadioButton == rdbSelectUPI) {
            rdbSelectCashOn.isChecked = false
            selectedPaymentMode = "Prepaid"
            val deliveryCharges = 0.0
            val totalPrice = totalQuantityPrice + deliveryCharges
            findViewById<TextView>(R.id.tvPrice).text = "₹$itemPrice"
            findViewById<TextView>(R.id.tvDeliveryCharges).text = "₹ 0.0"
            findViewById<TextView>(R.id.tvTotal).text = "₹$totalPrice"

        } else if (selectedRadioButton == rdbSelectCashOn) {
            rdbSelectUPI.isChecked = false
            selectedPaymentMode = "COD"
            val deliveryCharges = session.getData(Constant.DELIVERY_CHARGE).toDoubleOrNull() ?: 0.0
            val totalPrice = totalQuantityPrice + deliveryCharges
            findViewById<TextView>(R.id.tvPrice).text = "₹$itemPrice"
            findViewById<TextView>(R.id.tvDeliveryCharges).text = "₹" + session.getData(Constant.DELIVERY_CHARGE)
            findViewById<TextView>(R.id.tvTotal).text = "₹$totalPrice"
        }

        // Mark the selected RadioButton as checked
        selectedRadioButton.isChecked = true
    }

    private fun placeOrder(
    ) {
        if (isLoading) return
        isLoading = true

        val params = buildProfileParams()
        ApiConfig.RequestToVolley({ result, response ->
            handleProfileResponse(result, response)
        }, activity, Constant.PLACE_ORDER, params, true, 1)

        Log.d("PLACE_ORDER", "PLACE_ORDER: " + Constant.PLACE_ORDER)
        Log.d("PLACE_ORDER", "PLACE_ORDERparams: " + params)
    }

    private fun buildProfileParams(): HashMap<String, String> {
        return hashMapOf(
            Constant.USER_ID to session.getData(Constant.USER_ID),
            Constant.ADDRESS_ID to addressId.toString(),
            Constant.PRODUCT_ID to productId.toString(),
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
                    startActivity(Intent(activity, MainActivity::class.java))
                    finishAffinity()
                } else {
                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_GALLERY
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY) {
                imageUri = data?.data
                CropImage.activity(imageUri)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(this)

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result: CropImage.ActivityResult? = CropImage.getActivityResult(data)
                if (result != null) {
                    filePath1 = result.getUriFilePath(activity, true)
                    val imgFile = File(filePath1)
                    if (imgFile.exists()) {
                        val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                        binding.btnUploadScreenshots.text = getString(R.string.screenshot_uploaded)
                        binding.btnUploadScreenshots.setTextColor(ContextCompat.getColor(this, R.color.darkGreen))
                    }
                }
            }
        }
    }





}

