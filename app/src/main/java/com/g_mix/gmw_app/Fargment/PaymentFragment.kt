package com.g_mix.gmw_app.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.activity.CartActivity
import com.g_mix.gmw_app.activity.MainActivity
import com.g_mix.gmw_app.databinding.FragmentPaymentBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import com.google.androidbrowserhelper.trusted.LauncherActivity
import org.json.JSONException
import org.json.JSONObject
import android.app.Activity // Import this

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private lateinit var session: Session
    private var isLoading = false

    private lateinit var rdbSelectUPI: RadioButton
    private lateinit var rdbSelectCashOn: RadioButton
    private var selectedPaymentMode: String = ""
    private var totalQuantityPrice: Double = 0.0
    private var itemPrice: Double = 0.0
    private var addressId: String? = null
    private var productId: String? = null
    private var totalPrice: Double? = null

    private lateinit var launcherActivityResultLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val view = binding.root

        session = Session(requireActivity())

        // Initialize the result launcher for handling the return from LauncherActivity
        launcherActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) { // Updated here
                // Reset confirm button state when returning from LauncherActivity
                binding.btnConfirm.isEnabled = true
                isLoading = false
            }
        }

        // Retrieve passed data using arguments
        arguments?.let { bundle ->
            productId = bundle.getString("PRODUCT_ID")
            val itemName = bundle.getString("ITEM_NAME")
            itemPrice = bundle.getString("ITEM_PRICE")?.toDoubleOrNull() ?: 0.0
            val itemImage = bundle.getString("ITEM_IMAGE")
            val itemWeight = bundle.getString("ITEM_WEIGHT")
            val userName = bundle.getString("NAME")
            val mobileNumber = bundle.getString("MOBILE_NUMBER")
            val address = bundle.getString("ADDRESS")
            addressId = bundle.getString("ADDRESS_ID")

            val quantity = (activity as CartActivity).binding.tvQuantityVal.text.toString().toInt()
            totalQuantityPrice = itemPrice * quantity
            val deliveryCharges = 0.0
            totalPrice = totalQuantityPrice + deliveryCharges

            // Set data to views
            view.findViewById<TextView>(R.id.tvPrice).text = "₹$totalQuantityPrice"
            view.findViewById<TextView>(R.id.tvDeliveryCharges).text = "₹$deliveryCharges"
            view.findViewById<TextView>(R.id.tvTotal).text = "₹$totalPrice"
            view.findViewById<TextView>(R.id.tvName).text = userName
            view.findViewById<TextView>(R.id.tvAddress).text = address
            view.findViewById<TextView>(R.id.tvMobileNumber).text = mobileNumber
        }

        // Initialize RadioButtons
        rdbSelectUPI = view.findViewById(R.id.rdbSelectUPI)
        rdbSelectCashOn = view.findViewById(R.id.rdbSelectCashOn)
        rdbSelectUPI.isChecked = true

        // Set listeners for RadioButtons
        rdbSelectUPI.setOnClickListener { onRadioButtonClicked(rdbSelectUPI) }
        rdbSelectCashOn.setOnClickListener { onRadioButtonClicked(rdbSelectCashOn) }

        selectedPaymentMode = "Prepaid"

        // Confirm button click listener
        binding.btnConfirm.setOnClickListener {
            if (!isLoading) {
                placeOrder()
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // Reset loading state when returning to this fragment
        isLoading = false
    }

    private fun onRadioButtonClicked(selectedRadioButton: RadioButton) {
        if (selectedRadioButton == rdbSelectUPI) {
            rdbSelectCashOn.isChecked = false
            selectedPaymentMode = "Prepaid"
            updatePrice(0.0)
        } else if (selectedRadioButton == rdbSelectCashOn) {
            rdbSelectUPI.isChecked = false
            selectedPaymentMode = "COD"
            val deliveryCharges = session.getData(Constant.DELIVERY_CHARGE).toDoubleOrNull() ?: 0.0
            updatePrice(deliveryCharges)
        }
        selectedRadioButton.isChecked = true
    }

    private fun updatePrice(deliveryCharges: Double) {
        val totalPrice = totalQuantityPrice + deliveryCharges
        binding.tvPrice.text = "₹$totalQuantityPrice"
        binding.tvDeliveryCharges.text = "₹$deliveryCharges"
        binding.tvTotal.text = "₹$totalPrice"
    }

    private fun placeOrder() {
        if (isLoading) return
        isLoading = true

        if (selectedPaymentMode == "Prepaid") {
            initiatePaymentLink()
        } else if (selectedPaymentMode == "COD") {
            val params = buildOrderParams()
            ApiConfig.RequestToVolley({ result, response ->
                handleOrderResponse(result, response)
            }, requireActivity(), Constant.PLACE_ORDER, params, true, 1)
        }
    }

    private fun buildOrderParams(): HashMap<String, String> {
        return hashMapOf(
            Constant.USER_ID to session.getData(Constant.USER_ID),
            Constant.ADDRESS_ID to addressId.toString(),
            Constant.PRODUCT_ID to productId.toString(),
            Constant.PAYMENT_MODE to selectedPaymentMode,
            Constant.QUANTITY to (activity as CartActivity).binding.tvQuantityVal.text.toString(),
        )
    }

    private fun handleOrderResponse(result: Boolean, response: String) {
        isLoading = false

        if (result) {
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.getBoolean(Constant.SUCCESS)) {
                    Toast.makeText(requireActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finishAffinity()
                } else {
                    Toast.makeText(requireActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun initiatePaymentLink() {
        val buyerName = session.getData(Constant.NAME).ifEmpty { "Guest" }
        val userId = session.getData(Constant.USER_ID)
        val quantity = (activity as CartActivity).binding.tvQuantityVal.text.toString()
        val purpose = "$userId-$addressId-$productId-$quantity"

        val params = HashMap<String, String>().apply {
            put("buyer_name", buyerName)
           put("amount", totalPrice.toString())
         //   put("amount", "10.00")
            put("email", "test@gmail.com")
            put("phone", session.getData(Constant.MOBILE))
            put("purpose", purpose)
        }

        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    Log.d("FULL_RESPONSE", response)
                    val jsonObject = JSONObject(response)
                    val longUrl = jsonObject.getString("longurl")
                    val intent = Intent(requireActivity(), LauncherActivity::class.java).apply {
                        data = Uri.parse(longUrl)
                    }
                    launcherActivityResultLauncher.launch(intent)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(requireActivity(), "JSON Parsing error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }, requireActivity(), Constant.PAYMENT_LINK, params, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
