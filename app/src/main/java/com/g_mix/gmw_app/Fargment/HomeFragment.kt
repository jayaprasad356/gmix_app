package com.g_mix.gmw_app.Fargment

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.g_mix.gmw_app.helper.Session
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.g_mix.gmw_app.activity.MainActivity
import com.g_mix.gmw_app.Adapter.HomeAdapter
import com.g_mix.gmw_app.Adapter.SliderAdapter
import com.g_mix.gmw_app.Model.SliderItem
import com.g_mix.gmw_app.Model.HomeProduct
import com.g_mix.gmw_app.R
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
    private lateinit var sliderAdapter: SliderAdapter
    private var currentSlide = 0
    private val slideDelay: Long = 3000 // 3 seconds
    private val handler = Handler(Looper.getMainLooper())

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

        loadProductList()

        imageList()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Stop the auto slide when the fragment is destroyed
        handler.removeCallbacks(slideRunnable)
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

    private val slideRunnable = object : Runnable {
        override fun run() {
            if (currentSlide == sliderAdapter.itemCount) {
                currentSlide = 0
            }
            binding.sliderRecyclerView.setCurrentItem(currentSlide, true)
            currentSlide++
            handler.postDelayed(this, slideDelay)
        }
    }

    private fun addDotsIndicator() {
        val dotCount = sliderAdapter.itemCount
        binding.indicatorLayout.removeAllViews()

        for (i in 0 until dotCount) {
            val dot = ImageView(requireContext()).apply {
                setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.dot_inactive))
            }

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(8, 0, 8, 0)
            }
            binding.indicatorLayout.addView(dot, params)
        }

        // Initially set the first dot as active
        updateDotsIndicator(0)
    }

    private fun updateDotsIndicator(position: Int) {
        for (i in 0 until binding.indicatorLayout.childCount) {
            val dot = binding.indicatorLayout.getChildAt(i) as ImageView
            dot.setImageDrawable(
                if (i == position) {
                    ContextCompat.getDrawable(requireContext(), R.drawable.dot_active)
                } else {
                    ContextCompat.getDrawable(requireContext(), R.drawable.dot_inactive)
                }
            )
        }
    }

    private fun imageList() {
        val params: Map<String, String> = HashMap()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val dataArray = jsonObject.getJSONArray(Constant.DATA)

                        // Create a list to hold slider items
                        val sliderItems = mutableListOf<SliderItem>()

                        // Loop through the JSON array and create SliderItem objects
                        for (i in 0 until dataArray.length()) {
                            val itemObject = dataArray.getJSONObject(i)
                            val sliderItem = SliderItem(
                                id = itemObject.getInt("id"),
                                name = itemObject.getString("name"),
                                link = itemObject.getString("link"),
                                image = itemObject.getString("image"),
                                updatedAt = itemObject.getString("updated_at"),
                                createdAt = itemObject.getString("created_at"),
                            )
                            sliderItems.add(sliderItem)
                        }

                        // Initialize the slider adapter with the list of slider items
                        sliderAdapter = SliderAdapter(sliderItems)
                        binding.sliderRecyclerView.adapter = sliderAdapter

                        // Start auto slide
                        handler.postDelayed(slideRunnable, slideDelay)

                        // Add dots indicator
                        addDotsIndicator()

                        // Track manual slide to update the current slide and dots
                        binding.sliderRecyclerView.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                super.onPageSelected(position)
                                currentSlide = position
                                updateDotsIndicator(position)
                            }
                        })

                    } else {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.IMAGE_SLIDERS, params, true)
    }
}