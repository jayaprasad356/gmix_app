package com.g_mix.gmw_app.Fargment

import android.os.Bundle
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
            //  Toast.makeText(activity, Address_in_fragment, Toast.LENGTH_SHORT).show()

        }

        binding.btnReview.setOnClickListener{
            Review()
        }


        return binding.root

    }

    private fun Review() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        params[Constant.REVIEWS] = binding.etReview.toString()
        params[Constant.ORDER_ID] = order_id.toString()

        activity?.let {
            ApiConfig.RequestToVolley({ result, response ->
                if (result) {
                    try {
                        val jsonObject = JSONObject(response)
                        if (jsonObject.getBoolean(Constant.SUCCESS)) {
                            Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                            requireActivity().onBackPressed()


                        } else {
                            Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                            // Handle error
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }, it, Constant.UPDATE_REVIEWS, params, true)
        }
    }




}