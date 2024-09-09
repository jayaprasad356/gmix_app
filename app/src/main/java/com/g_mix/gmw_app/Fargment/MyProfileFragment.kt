package com.g_mix.gmw_app.Fargment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.g_mix.gmw_app.Fragment.MyOrderFragment
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.activity.MainActivity
import com.g_mix.gmw_app.databinding.FragmentMyProfileBinding
import com.g_mix.gmw_app.databinding.FragmentSelectedAddressBinding
import com.g_mix.gmw_app.fragment.AddressFragment
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session


class MyProfileFragment : Fragment() {

    lateinit var binding: FragmentMyProfileBinding
    lateinit var session: Session


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        session = Session(requireActivity())


        binding.tvMobileNumber.text = session.getData(Constant.MOBILE)
        (activity as MainActivity).binding.rltoolbar.visibility = View.VISIBLE
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.VISIBLE
        (activity as MainActivity).binding.fabWhatsapp.visibility = View.VISIBLE


        binding.rlMyOrders.setOnClickListener {
            openMyOrdersFragment()
        }

        binding.rlShippingAddress.setOnClickListener {
            openShippingAddressFragment()
        }

        return binding.root
    }

    private fun openMyOrdersFragment() {
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val myOrderFragment = MyOrderFragment()

        // Replace current fragment with MyOrderFragment
        transaction.replace(R.id.fragment_container, myOrderFragment)
        transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
        transaction.commit()
    }
    private fun openShippingAddressFragment() {
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val shippingAddressFragment = ShippedAddressFragment()

        // Replace current fragment with MyOrderFragment
        transaction.replace(R.id.fragment_container, shippingAddressFragment)
        transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
        transaction.commit()
    }


}