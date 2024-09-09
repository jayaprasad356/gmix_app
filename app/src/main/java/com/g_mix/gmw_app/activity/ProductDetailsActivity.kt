package com.g_mix.gmw_app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.g_mix.gmw_app.Adapter.ReviewAdapter
import com.g_mix.gmw_app.Model.Review
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.databinding.ActivityAddressDetailBinding
import com.g_mix.gmw_app.databinding.ActivityProductDetailsBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ProductDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityProductDetailsBinding
    lateinit var activity: Activity
    lateinit var session: Session
    private var count = 1 // Initialize with default count value
    private var isLoading = false
    private var productId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        session = Session(activity)

         productId = intent.getStringExtra("PRODUCT_ID")
        val itemName = intent.getStringExtra("ITEM_NAME")
        val itemPrice = intent.getStringExtra("ITEM_PRICE")?.toDoubleOrNull() ?: 0.0
        val itemImage = intent.getStringExtra("ITEM_IMAGE")
        val itemWeight = intent.getStringExtra("ITEM_WEIGHT")
        val itemDescription = intent.getStringExtra("ITEM_DESCRIPTION")
        val itemRatings = intent.getStringExtra("ITEM_RATINGS")

        binding.tvRatings.text = itemRatings


        binding.wvDiscription.loadData(itemDescription ?: "", "text/html", "UTF-8")

        binding.tvTotal.text = "₹${itemPrice.format(2)}"
        binding.tvProductName.text = itemName
        binding.tvProductWeight.text = itemWeight
        Glide.with(activity).load(itemImage).placeholder(R.drawable.demo_image)
            .into(binding.ivProductImage)

        // Set default selection to Description
        setSelectedTab(true)

        // Handle description click
        binding.tvDiscription.setOnClickListener {
            setSelectedTab(true)
            // Add logic to display the description details if needed
        }

        // Handle review click
        binding.tvReview.setOnClickListener {
            setSelectedTab(false)
            // Add logic to display reviews if needed
        }

        updateCount()

        binding.ivAdd.setOnClickListener {
            count++
            updateCount()
        }

        binding.ivSub.setOnClickListener {
            if (count > 1) {
                count--
                updateCount()
            }
        }

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvReview.layoutManager = linearLayoutManager


        binding.btnBuynow.setOnClickListener {
            // startactivity
            val intent = Intent(activity, CartActivity::class.java)
            intent.putExtra("PRODUCT_ID", productId)
            intent.putExtra("ITEM_NAME", itemName)
            intent.putExtra("ITEM_PRICE", itemPrice.toString())
            intent.putExtra("ITEM_IMAGE", itemImage)
            intent.putExtra("ITEM_WEIGHT", itemWeight)
            intent.putExtra("ITEM_QUANTITY", binding.tvCount.text.toString())
            intent.putExtra("ITEM_DESCRIPTION", itemDescription)
            startActivity(intent)

        }

        review()
    }

    private fun updateCount() {
        binding.tvCount.text = count.toString()

        val itemPrice = intent.getStringExtra("ITEM_PRICE")?.toDoubleOrNull() ?: 0.0
        val totalPrice = itemPrice * count
        binding.tvTotal.text = "₹${totalPrice.format(2)}" // Format to 2 decimal places
    }

    private fun setSelectedTab(isDescriptionSelected: Boolean) {
        if (isDescriptionSelected) {
            // Description selected, change color accordingly
            binding.tvDiscription.setTextColor(resources.getColor(R.color.darkGreen))
            binding.tvReview.setTextColor(resources.getColor(`in`.aabhasjindal.otptextview.R.color.grey))
            binding.rvReview.visibility = View.GONE
            binding.llDiscription.visibility = View.VISIBLE
        } else {
            // Review selected, change color accordingly
            binding.tvDiscription.setTextColor(resources.getColor(`in`.aabhasjindal.otptextview.R.color.grey))
            binding.tvReview.setTextColor(resources.getColor(R.color.darkGreen))
            binding.rvReview.visibility = View.VISIBLE
            binding.llDiscription.visibility = View.GONE
        }
    }

    private fun review() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.PRODUCT_ID] = productId.toString()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)
                        val g = Gson()
                        val review: ArrayList<Review> = ArrayList()
                        // Limiting to the first five items
                        val limit = minOf(jsonArray.length(), 5)
                        for (i in 0 until limit) {
                            val jsonObject1 = jsonArray.getJSONObject(i)
                            val group: Review = g.fromJson(jsonObject1.toString(), Review::class.java)
                            review.add(group)
                        }

                        val adapter = ReviewAdapter(activity, review)
                        binding.rvReview.adapter = adapter
                    } else {
                        // Handle error
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.REVIEWS_LIST, params, true)
    }

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)
}
