package com.g_mix.gmw_app.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.activity.CartActivity
import com.g_mix.gmw_app.activity.MainActivity
import com.g_mix.gmw_app.databinding.FragmentPaymentBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

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

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val view = binding.root

        session = Session(requireActivity())

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


            var quantity =  (activity as CartActivity).binding.tvQuantityVal.text.toString().toInt()
            totalQuantityPrice = itemPrice * quantity

            val deliveryCharges = 0.0
            val totalPrice = totalQuantityPrice + deliveryCharges

            // Set data to views
//            view.findViewById<TextView>(R.id.tvItemName).text = itemName
//            view.findViewById<TextView>(R.id.tvItemWeight).text = itemWeight
            view.findViewById<TextView>(R.id.tvPrice).text = "₹$totalQuantityPrice"
            view.findViewById<TextView>(R.id.tvDeliveryCharges).text = "₹$deliveryCharges"
            view.findViewById<TextView>(R.id.tvTotal).text = "₹$totalPrice"
            view.findViewById<TextView>(R.id.tvName).text = userName
            view.findViewById<TextView>(R.id.tvAddress).text = address
            view.findViewById<TextView>(R.id.tvMobileNumber).text = mobileNumber

            // Load item image
//            Glide.with(requireContext())
//                .load(itemImage)
//                .placeholder(R.drawable.demo_image)
//                .error(R.drawable.demo_image)
//                .into(view.findViewById(R.id.ivItemImage))

//            val tvQuantityVal = view.findViewById<TextView>(R.id.tvQuantityVal)
//            tvQuantityVal.text = quantity.toString()

        }

        // Initialize RadioButtons
        rdbSelectUPI = view.findViewById(R.id.rdbSelectUPI)
        rdbSelectCashOn = view.findViewById(R.id.rdbSelectCashOn)
        rdbSelectUPI.isChecked = true


        // Set listeners for RadioButtons
        rdbSelectUPI.setOnClickListener { onRadioButtonClicked(rdbSelectUPI) }
        rdbSelectCashOn.setOnClickListener { onRadioButtonClicked(rdbSelectCashOn) }

        selectedPaymentMode = "Prepaid"

        binding.btnConfirm.setOnClickListener {
            placeOrder()
        }

        return view
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

        val params = buildProfileParams()
        ApiConfig.RequestToVolley({ result, response ->
            handleProfileResponse(result, response)
        }, requireActivity(), Constant.PLACE_ORDER, params, true, 1)

        Log.d("PLACE_ORDER", "PLACE_ORDER: " + Constant.PLACE_ORDER)
        Log.d("PLACE_ORDER", "PLACE_ORDERparams: " + params)
    }

    private fun buildProfileParams(): HashMap<String, String> {
        return hashMapOf(
            Constant.USER_ID to session.getData(Constant.USER_ID),
            Constant.ADDRESS_ID to addressId.toString(),
            Constant.PRODUCT_ID to productId.toString(),
            Constant.PAYMENT_MODE to selectedPaymentMode,
            Constant.QUANTITY to (activity as CartActivity).binding.tvQuantityVal.text.toString()
        )
    }

    private fun handleProfileResponse(result: Boolean, response: String) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
