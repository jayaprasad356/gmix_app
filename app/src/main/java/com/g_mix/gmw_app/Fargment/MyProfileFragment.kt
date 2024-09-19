package com.g_mix.gmw_app.Fargment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.g_mix.gmw_app.Fragment.MyOrderFragment
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.activity.MainActivity
import com.g_mix.gmw_app.databinding.FragmentMyProfileBinding
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

        binding.rlPrivacyPolicy.setOnClickListener{
            openPrivacyPolicyFragment()
        }

        binding.rlTermsCondition.setOnClickListener{
            openTermsConditionFragment()
        }

        binding.rlLogout.setOnClickListener{
            showLogoutConfirmationDialog()
        }

        binding.rlRefundPolicy.setOnClickListener{
            openRefundFragment()
        }

        binding.rlJobs.setOnClickListener{
            openJobsFragment()
        }


        return binding.root
    }



    private fun openRefundFragment() {
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val RefundFragment = RefundFragment()

        // Replace current fragment with MyOrderFragment
        transaction.replace(R.id.fragment_container, RefundFragment)
        transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
        transaction.commit()
    }

    private fun openJobsFragment() {

        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val jobsFragment = JobFragment()

        // Replace current fragment with MyOrderFragment
        transaction.replace(R.id.fragment_container, jobsFragment)
        transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
        transaction.commit()


    }


    private fun showLogoutConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(activity)
            .setMessage("Are you sure you want to logout?")
            .setCancelable(true)
            .setPositiveButton("Logout") { dialog, _ ->
                // Perform logout action
                session.logoutUser(activity)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialogBuilder.show()

        // Change button text colors
        dialogBuilder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireActivity(), R.color.primary))
        dialogBuilder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireActivity(), R.color.text_grey))
    }


    private fun openTermsConditionFragment() {
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val termsconditionFragment = TermsconditionFragment()

        // Replace current fragment with MyOrderFragment
        transaction.replace(R.id.fragment_container, termsconditionFragment)
        transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
        transaction.commit()
    }

    private fun openPrivacyPolicyFragment() {
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val privacypolicyFragment = PrivacypolicyFragment()

        // Replace current fragment with MyOrderFragment
        transaction.replace(R.id.fragment_container, privacypolicyFragment)
        transaction.addToBackStack(null) // Optional: Add to backstack to allow going back
        transaction.commit()
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