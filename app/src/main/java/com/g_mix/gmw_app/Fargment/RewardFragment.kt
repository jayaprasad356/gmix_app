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
import com.g_mix.gmw_app.Adapter.RewardAdapter
import com.g_mix.gmw_app.Model.Reward
import com.g_mix.gmw_app.databinding.FragmentRewardBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class RewardFragment : Fragment() {
    private lateinit var binding: FragmentRewardBinding
    private lateinit var activity: Activity
    private lateinit var session: Session
    private val rewardData = ArrayList<Reward>()
    private lateinit var rewardAdapter: RewardAdapter
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRewardBinding.inflate(inflater, container, false)
        activity = requireActivity()
        session = Session(activity)

        (activity as MainActivity).binding.rltoolbar.visibility = View.VISIBLE
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.VISIBLE


        binding.tvPoints.text = session.getData(Constant.POINTS)

        rewardAdapter = RewardAdapter(activity, rewardData)
        binding.gridView.numColumns = 2  // Set the number of columns to 2
        binding.gridView.adapter = rewardAdapter




        loadRewardList()

        return binding.root
    }

    private fun loadRewardList() {
        if (isLoading) return
        isLoading = true

        val params = buildRewardParams()
        ApiConfig.RequestToVolley({ result, response ->
            handleRewardResponse(result, response)
        }, activity, Constant.REWARD_LIST, params, true, 1)

        Log.d("REWARD_LIST", "REWARD_LIST: " + Constant.REWARD_LIST)
        Log.d("REWARD_LIST", "REWARD_LISTparams: " + params)
    }

    private fun buildRewardParams(): HashMap<String, String> {
        return hashMapOf(
            Constant.USER_ID to session.getData(Constant.USER_ID),
            // Add any other parameters needed for the request
        )
    }

    private fun handleRewardResponse(result: Boolean, response: String) {
        isLoading = false

        if (result) {
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.getBoolean(Constant.SUCCESS)) {
                    updateRewardList(jsonObject)
                } else {
                    showRewardListError(jsonObject.getString(Constant.MESSAGE))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun showRewardListError(message: String) {
        binding.gridView.visibility = View.GONE
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateRewardList(jsonObject: JSONObject) {
        binding.gridView.visibility = View.VISIBLE

        val jsonArray = jsonObject.getJSONArray(Constant.DATA)
        val gson = Gson()

        rewardData.clear()

        for (i in 0 until jsonArray.length()) {
            val rewardItem = gson.fromJson(jsonArray.getJSONObject(i).toString(), Reward::class.java)
            Log.d("RewardData", "Loaded Reward: $rewardItem") // Log reward data
            rewardData.add(rewardItem)
        }

        rewardAdapter.notifyDataSetChanged()
        Log.d("RewardFragment", "Loaded Rewards: ${rewardData.size}")
    }
}
