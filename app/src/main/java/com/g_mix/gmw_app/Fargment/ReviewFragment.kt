package com.g_mix.gmw_app.Fargment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.g_mix.gmw_app.Adapter.MyOrderAdapter
import com.g_mix.gmw_app.Model.OrderData
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.databinding.FragmentReviewBinding
import com.g_mix.gmw_app.databinding.FragmentSelectedAddressBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap


class ReviewFragment : Fragment() {


    lateinit var binding: FragmentReviewBinding
    lateinit var session: Session
    var order_id: String? = null
    var ratingValue: String? = null
    var reviews: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(layoutInflater)
        session = Session(requireContext())

        binding.ibClose.setOnClickListener{
            requireActivity().onBackPressed()
        }

        arguments?.let { bundle ->
            order_id = bundle.getString("order_id")
            ratingValue = bundle.getString("ratings")
            reviews = bundle.getString("reviews")
            //  Toast.makeText(activity, Address_in_fragment, Toast.LENGTH_SHORT).show()

            // Set the rating value in the RatingBar
            ratingValue?.let {
                binding.ratingBar.rating = it.toFloat()  // Set the rating value
            }

            reviews?.let {
                binding.etReview.setText(it)  // Set the review text
            }

        }

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            // You can update the `ratings` variable if you want to keep track of changes
            ratingValue = rating.toString()

//            rating(ratingValue!!, order_id!!)
        }

        binding.btnReview.setOnClickListener{
            reviewBtn()
        }


        return binding.root

    }

    private fun reviewBtn() {
        val review = binding.etReview.text.toString()
        if (ratingValue == null) {
            Toast.makeText(
                requireActivity(),
                "Please select rating",
                Toast.LENGTH_SHORT
            ).show()
        } else if (review.isEmpty()) {
            Toast.makeText(
                requireActivity(),
                "Please write review",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            rating(ratingValue!!, order_id!!)
        }
    }

    private fun rating(ratingValue: String, id: String) {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        params[Constant.ORDER_ID] = id
        params[Constant.RATINGS] = ratingValue
        params[Constant.REVIEWS] = binding.etReview.text.toString()
        activity?.let {
            ApiConfig.RequestToVolley({ result, response ->
                if (result) {
                    try {
                        val jsonObject = JSONObject(response)
                        if (jsonObject.getBoolean(Constant.SUCCESS)) {
                            Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                            loadOrderList()
                        } else {
                            Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                            // Handle error
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }, it, Constant.UPDATE_RATINGS, params, true)
        }
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
//
//    private fun review() {
//        val params: MutableMap<String, String> = HashMap()
//        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
//        params[Constant.REVIEWS] = binding.etReview.toString()
//        params[Constant.ORDER_ID] = order_id.toString()
//
//        activity?.let {
//            ApiConfig.RequestToVolley({ result, response ->
//                if (result) {
//                    try {
//                        val jsonObject = JSONObject(response)
//                        if (jsonObject.getBoolean(Constant.SUCCESS)) {
//                            Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
//                            requireActivity().onBackPressed()
//
//
//                        } else {
//                            Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
//                            // Handle error
//                        }
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }, it, Constant.UPDATE_REVIEWS, params, true)
//        }
//    }




}