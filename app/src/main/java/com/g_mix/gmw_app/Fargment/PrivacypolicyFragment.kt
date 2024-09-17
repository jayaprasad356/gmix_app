package com.g_mix.gmw_app.Fargment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.activity.MainActivity
import com.g_mix.gmw_app.databinding.FragmentPrivacypolicyBinding
import com.g_mix.gmw_app.databinding.FragmentTermsconditionBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class PrivacypolicyFragment : Fragment() {


    private lateinit var binding: FragmentPrivacypolicyBinding
    private lateinit var activity: Activity
    private lateinit var session: Session
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrivacypolicyBinding.inflate(inflater, container, false)
        activity = requireActivity()
        session = Session(activity)


        (activity as MainActivity).binding.rltoolbar.visibility = View.GONE
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.GONE
        (activity as MainActivity).binding.fabWhatsapp.visibility = View.GONE


        binding.ivBack.setOnClickListener {
            activity.onBackPressed()
        }

        fetchPrivacyPolicy()
        return binding.root
    }


    private fun fetchPrivacyPolicy() {
        val params: MutableMap<String, String> = HashMap()
        ApiConfig.RequestToVolley({ result, response ->
            Log.d("API Response", response) // Log the API response
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val dataArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)
                        val dataObject = dataArray.getJSONObject(0)
                        val privacyPolicy = dataObject.getString("privacy_policy")
                        Log.d("Privacy Policy", privacyPolicy) // Log the privacy policy content

                        binding.wvPrivacy.loadDataWithBaseURL("", privacyPolicy, "text/html", "UTF-8", "")
                    } else {
                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(activity, response, Toast.LENGTH_SHORT).show()
            }
        }, activity, Constant.PRIVACY_POLICY, params, true, 1)
    }


}