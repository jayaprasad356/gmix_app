package com.g_mix.gmw_app.Fargment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.g_mix.gmw_app.Adapter.AddresslistAdapter
import com.g_mix.gmw_app.Model.Addresslist
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.databinding.FragmentAddressBinding
import com.g_mix.gmw_app.databinding.FragmentSelectedAddressBinding
import com.g_mix.gmw_app.fragment.AddressFragment
import com.g_mix.gmw_app.fragment.PaymentFragment
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class SelectedAddressFragment : Fragment() {

    lateinit var binding: FragmentSelectedAddressBinding
    lateinit var session: Session

    private lateinit var productId: String
    private lateinit var itemName: String
    private lateinit var itemPrice: String
    private lateinit var itemImage: String
    private lateinit var itemWeight: String
    private lateinit var name: String
    private lateinit var mobile: String
    private lateinit var address: String
    private lateinit var addressId: String
    private lateinit var addressSuccess: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectedAddressBinding.inflate(inflater, container, false)
        session = Session(requireActivity())

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvAddresslist.layoutManager = linearLayoutManager

        addressList()

        binding.tvAddAddress.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val transaction = fm.beginTransaction()

            val bundle = Bundle()
            bundle.putString("Address_in_fragment", "0")
            val addressFragment = AddressFragment().apply {
                arguments = bundle
            }
            transaction.replace(R.id.fragment_container, addressFragment)
            transaction.addToBackStack(null) // Optional: to add the transaction to the back stack
            transaction.commit()
        }



        productId = requireActivity().intent.getStringExtra("PRODUCT_ID").toString()
      itemName = requireActivity().intent.getStringExtra("ITEM_NAME").toString()
         itemPrice = requireActivity().intent.getStringExtra("ITEM_PRICE").toString()
         itemImage = requireActivity().intent.getStringExtra("ITEM_IMAGE").toString()
         itemWeight = requireActivity().intent.getStringExtra("ITEM_WEIGHT").toString()


        binding.btnPayment.setOnClickListener {
            if (addressSuccess == "false") {
                Toast.makeText(activity, "Please add address", Toast.LENGTH_SHORT).show()
            } else {
                val fm = requireActivity().supportFragmentManager
                val transaction = fm.beginTransaction()

                // Create a bundle to pass data
                val bundle = Bundle().apply {
                    putString("PRODUCT_ID", productId)
                    putString("ITEM_NAME", itemName)
                    putString("ITEM_PRICE", itemPrice)
                    putString("ITEM_IMAGE", itemImage)
                    putString("ITEM_WEIGHT", itemWeight)
                    putString("NAME", name)
                    putString("MOBILE_NUMBER", mobile)
                    putString("ADDRESS", address)
                    putString("ADDRESS_ID", addressId)
                }

                // Create a new instance of PaymentFragment and set the bundle as its arguments
                val paymentFragment = PaymentFragment().apply {
                    arguments = bundle
                }

                // Replace the current fragment with PaymentFragment
                transaction.replace(R.id.fragment_container, paymentFragment)
                transaction.addToBackStack(null) // Optional: to add the transaction to the back stack
                transaction.commit()
            }
        }








        return binding.root
    }

    private fun addressList() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID) // Or another parameter based on your API

        activity?.let {
            ApiConfig.RequestToVolley({ result, response ->
                if (result) {
                    try {
                        val jsonObject = JSONObject(response)
                        addressSuccess = jsonObject.getBoolean(Constant.SUCCESS).toString()
                        if (jsonObject.getBoolean(Constant.SUCCESS)) {
                            val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)
                            val g = Gson()
                            val addressList: ArrayList<Addresslist> = ArrayList()

                            // Limiting to the first five items
                            val limit = minOf(jsonArray.length(), 5)
                            for (i in 0 until limit) {
                                val jsonObject1 = jsonArray.getJSONObject(i)
                                val address: Addresslist = g.fromJson(jsonObject1.toString(), Addresslist::class.java)
                                addressList.add(address)
                            }

                            // Pass the Activity and addressList to the adapter
                            val adapter = AddresslistAdapter(requireActivity(), requireActivity().supportFragmentManager, addressList) { selectedAddress ->
                                // When an address is selected, store its details for passing later
                                name = selectedAddress.first_name + " " + selectedAddress.last_name
                                mobile = selectedAddress.mobile.toString()
                                address = "${selectedAddress.door_no}, ${selectedAddress.street_name}, ${selectedAddress.city}, ${selectedAddress.state} - ${selectedAddress.pincode}"
                                addressId = selectedAddress.id.toString()
                            }
                            binding.rvAddresslist.adapter = adapter

                        } else {
                            // Handle error
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }, it, Constant.MY_ADDRESS_LIST, params, true)
        }
    }

}