package com.g_mix.gmw_app.Fargment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.g_mix.gmw_app.activity.MainActivity
import com.g_mix.gmw_app.Adapter.MyOrderAdapter
import com.g_mix.gmw_app.Model.OrderData
import com.g_mix.gmw_app.helper.Session
import com.g_mix.gmw_app.databinding.FragmentMyOrderBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.google.gson.Gson
import org.json.JSONArray
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


        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.lvOrderData.layoutManager = linearLayoutManager


        loadOrderList()

        return binding.root
    }

    private fun loadOrderList() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID) // Or another parameter based on your API

        activity?.let {
            ApiConfig.RequestToVolley({ result, response ->
                if (result) {
                    try {
                        val jsonObject = JSONObject(response)
                        if (jsonObject.getBoolean(Constant.SUCCESS)) {
                            val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)
                            val g = Gson()
                            val orderData: ArrayList<OrderData> = ArrayList()

                            // Limiting to the first five items
                            val limit = minOf(jsonArray.length(), 5)
                            for (i in 0 until limit) {
                                val jsonObject1 = jsonArray.getJSONObject(i)
                                val address: OrderData = g.fromJson(jsonObject1.toString(), OrderData::class.java)
                                orderData.add(address)
                            }

                            // Pass the Activity and addressList to the adapter
                            val adapter = MyOrderAdapter(requireActivity(), requireActivity().supportFragmentManager, orderData) { selectedAddress ->
                                // When an address is selected, store its details for passing later

                            }
                            binding.lvOrderData.adapter = adapter

                        } else {
                            // Handle error
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }, it, Constant.ORDERS_LIST, params, true)
        }
    }




}

