package com.g_mix.gmw_app.Fragment

import android.app.Activity
import android.os.Bundle
import com.g_mix.gmw_app.helper.Session
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.g_mix.gmw_app.activity.MainActivity
import com.g_mix.gmw_app.Adapter.HomeAdapter
import com.g_mix.gmw_app.Model.HomeProduct
import com.g_mix.gmw_app.databinding.FragmentHomeBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var activity: Activity
    private lateinit var session: Session
    private lateinit var homeAdapter: HomeAdapter
    private val gridItems = ArrayList<HomeProduct>()
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        activity = requireActivity()
        session = Session(activity)

        (activity as MainActivity).binding.rltoolbar.visibility = View.VISIBLE
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.VISIBLE

        homeAdapter = HomeAdapter(activity, gridItems)

        binding.gridView.adapter = homeAdapter

        binding.gridView.numColumns = 2

        //binding.tvMobileNumber.text = session.getData(Constant.MOBILE)

        loadProductList()

        return binding.root
    }

    private fun loadProductList() {
        if (isLoading) return
        isLoading = true

        val params = buildProfileParams()
        ApiConfig.RequestToVolley({ result, response ->
            handleProfileResponse(result, response)
        }, activity, Constant.PRODUCT_LIST, params, true, 1)
    }

    private fun buildProfileParams(): HashMap<String, String> {
        return hashMapOf()
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
        binding.gridView.visibility = View.GONE
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateProfileList(jsonObject: JSONObject) {
        binding.gridView.visibility = View.VISIBLE

        val jsonArray = jsonObject.getJSONArray(Constant.DATA)
        val gson = Gson()

        gridItems.clear()

        for (i in 0 until jsonArray.length()) {
            val product = gson.fromJson(jsonArray.getJSONObject(i).toString(), HomeProduct::class.java)
            gridItems.add(product)
        }

        homeAdapter.notifyDataSetChanged()
    }

}

