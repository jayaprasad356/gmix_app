package com.g_mix.gmw_app.Fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.g_mix.gmw_app.activity.MainActivity
import com.g_mix.gmw_app.Adapter.MyOrderAdapter
import com.g_mix.gmw_app.Model.OrderData
import com.g_mix.gmw_app.helper.Session
import com.g_mix.gmw_app.databinding.FragmentMyOrderBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class MyOrderFragment : Fragment() {
    private lateinit var binding: FragmentMyOrderBinding
    private lateinit var activity: Activity
    private lateinit var session: Session
    private val orderData = ArrayList<OrderData>()
    private lateinit var myOrderAdapter: MyOrderAdapter
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyOrderBinding.inflate(inflater, container, false)
        activity = requireActivity()
        session = Session(activity)

        (activity as MainActivity).binding.rltoolbar.visibility = View.GONE
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.GONE
        (activity as MainActivity).binding.fabWhatsapp.visibility = View.GONE



        binding.ivBack.setOnClickListener {
            activity.onBackPressed()
        }


        myOrderAdapter = MyOrderAdapter(activity, orderData)
        binding.lvOrderData.adapter = myOrderAdapter

        loadOrderList()

        return binding.root
    }

    private fun loadOrderList() {
        if (isLoading) return
        isLoading = true

        val params = buildProfileParams()
        ApiConfig.RequestToVolley({ result, response ->
            handleProfileResponse(result, response)
        }, activity, Constant.ORDERS_LIST, params, true, 1)

        Log.d("ORDERS_LIST", "ORDERS_LIST: " + Constant.ORDERS_LIST)
        Log.d("ORDERS_LIST", "ORDERS_LISTparams: " + params)
    }

    private fun buildProfileParams(): HashMap<String, String> {
        return hashMapOf(
            Constant.USER_ID to session.getData(Constant.USER_ID),
//            Constant.OFFSET to offset.toString(),
//            Constant.LIMIT to limit.toString(),
        )
    }

    private fun handleProfileResponse(result: Boolean, response: String) {
        isLoading = false

        if (result) {
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.getBoolean(Constant.SUCCESS)) {
                    updateProfileList(jsonObject)
                } else {
                    showProfileListError(jsonObject.getString(Constant.MESSAGE))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun showProfileListError(message: String) {
        binding.lvOrderData.visibility = View.GONE
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateProfileList(jsonObject: JSONObject) {
        binding.lvOrderData.visibility = View.VISIBLE

        val jsonArray = jsonObject.getJSONArray(Constant.DATA)
        val gson = Gson()

        orderData.clear()

        for (i in 0 until jsonArray.length()) {
            val orderProduct = gson.fromJson(jsonArray.getJSONObject(i).toString(), OrderData::class.java)
            Log.d("OrderData", "Loaded Order: $orderProduct") // Log order data
            orderData.add(orderProduct)
        }

        myOrderAdapter.notifyDataSetChanged()
        Log.d("HomeFragment", "Loaded Orders: ${orderData.size}")
    }

}

