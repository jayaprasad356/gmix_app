package com.g_mix.gmw_app.Fargment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.g_mix.gmw_app.Adapter.CategoryAdapter
import com.g_mix.gmw_app.Model.CategoryItem
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.activity.MainActivity
import com.g_mix.gmw_app.databinding.FragmentCategoryBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap
import androidx.core.content.ContextCompat

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var activity: Activity
    private lateinit var session: Session
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        activity = requireActivity()
        session = Session(activity)

        (activity as MainActivity).binding.rltoolbar.visibility = View.VISIBLE
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.VISIBLE

        setupNavigationRail()
        binding.navigationRail.llJuice.setBackgroundColor(ContextCompat.getColor(activity, R.color.primary))
        binding.navigationRail.tvJuice.setTextColor(ContextCompat.getColor(activity, R.color.white))
        loadCategoryItems("1")

        return binding.root
    }

    private fun setupNavigationRail() {
        val navJuice = binding.navigationRail.llJuice
        val navFaceMask = binding.navigationRail.llFaceMask

        navJuice.setOnClickListener {
            selectCategory("Juice")
        }

        navFaceMask.setOnClickListener {
            selectCategory("Face Mask")
        }
    }

    private fun selectCategory(category: String) {
        resetCategorySelection()

        when (category) {
            "Juice" -> {
                binding.navigationRail.llJuice.setBackgroundColor(ContextCompat.getColor(activity, R.color.primary))
                binding.navigationRail.tvJuice.setTextColor(ContextCompat.getColor(activity, R.color.white))
                loadCategoryItems("1")
            }
            "Face Mask" -> {
                binding.navigationRail.llFaceMask.setBackgroundColor(ContextCompat.getColor(activity, R.color.primary))
                binding.navigationRail.tvFaceMask.setTextColor(ContextCompat.getColor(activity, R.color.white))
                loadCategoryItems("2")
            }
        }
    }

    private fun resetCategorySelection() {
        binding.navigationRail.llJuice.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.transparent)) // Reset color
        binding.navigationRail.tvJuice.setTextColor(ContextCompat.getColor(activity, android.R.color.black))
        binding.navigationRail.llFaceMask.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.transparent)) // Reset color
        binding.navigationRail.tvFaceMask.setTextColor(ContextCompat.getColor(activity, android.R.color.black))

    }

    private fun loadCategoryItems(categoryId: String) {
        val params = HashMap<String, String>()
        params[Constant.CATEGORY_ID] = categoryId

        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("TAG", "categoryList: "+jsonObject )
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val dataArray = jsonObject.getJSONArray(Constant.DATA)

                        val categoryItems = mutableListOf<CategoryItem>()

                        for (i in 0 until dataArray.length()) {
                            val itemObject = dataArray.getJSONObject(i)
                            val categoryItem = CategoryItem(
                                id = itemObject.getInt("id"),
                                categoryId = itemObject.getInt("category_id"),
                                categoryName = itemObject.getString("category_name"),
                                name = itemObject.getString("name"),
                                unit = itemObject.getString("unit"),
                                measurement = itemObject.getString("measurement"),
                                quantity = itemObject.getString("quantity"),
                                description = itemObject.getString("description"),
                                price = itemObject.getString("price"),
                                image = itemObject.getString("image"),
                                ratings = itemObject.getString("ratings"),
                                updatedAt = itemObject.getString("updated_at"),
                                createdAt = itemObject.getString("created_at"),
                            )
                            categoryItems.add(categoryItem)
                        }

                        // Initialize GridLayoutManager for 2 columns
                        binding.rvCategoryItem.layoutManager = GridLayoutManager(activity, 2)

                        // Initialize the adapter with the list of category items
                        categoryAdapter = CategoryAdapter(categoryItems)
                        binding.rvCategoryItem.adapter = categoryAdapter

                    } else {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Log.d("TAG", "categoryList: "+e )
                    e.printStackTrace()
                }
            }
        }, activity, Constant.CATEGORY_PRODUCT_LIST, params, true)
    }
}