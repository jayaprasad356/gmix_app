package com.g_mix.gmw_app.Adapter

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.g_mix.gmw_app.Fargment.EditAddressFragment
import com.g_mix.gmw_app.Fargment.TermsconditionFragment
import com.g_mix.gmw_app.Model.Addresslist
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import org.json.JSONException
import org.json.JSONObject

class AddresslistAdapter(
    private val activity: Activity,
    private val fragmentManager: androidx.fragment.app.FragmentManager,
    private var addressList: MutableList<Addresslist>, // Change to MutableList
    private val onAddressSelected: (Addresslist) -> Unit
) : RecyclerView.Adapter<AddresslistAdapter.AddressViewHolder>() {

    private var selectedPosition = 0
    private val session: Session = Session(activity)

    init {
        if (addressList.isNotEmpty()) {
            onAddressSelected(addressList[selectedPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addressList[position]
        holder.tvName.text = "${address.first_name} ${address.last_name}"
        holder.tvAddress.text = "${address.door_no}, ${address.street_name}, ${address.city}, ${address.state} - ${address.pincode}"
        holder.tvMobile.text = address.mobile
        holder.radioButton.isChecked = position == selectedPosition

        holder.radioButton.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            onAddressSelected(address)
        }

        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            onAddressSelected(address)
        }

        holder.ivEdit.setOnClickListener {
            val transaction = fragmentManager.beginTransaction()

            // Create an instance of the fragment
            val editAddressFragment = EditAddressFragment()

            // Create a Bundle to pass data
            val bundle = Bundle()
            bundle.putString("address_id", address.id.toString())  // Replace 'address.id' with the value you want to pass

            // Set the bundle to the fragment
            editAddressFragment.arguments = bundle

            // Replace the fragment and add to back stack
            transaction.replace(R.id.fragment_container, editAddressFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        holder.ivDelete.setOnClickListener {
            AlertDialog.Builder(activity)
                .setTitle("Delete Address")
                .setMessage("Are you sure you want to delete this address?")
                .setPositiveButton("Yes") { _, _ ->
                    deleteAddress(address.id.toString(), position) // Pass position
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        val tvMobile: TextView = view.findViewById(R.id.tvMobile)
        val radioButton: RadioButton = view.findViewById(R.id.rb)
        val ivEdit: ImageView = view.findViewById(R.id.ivEdit)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
    }

    // Add a function to update the list after deletion
    private fun removeAddress(position: Int) {
        addressList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, addressList.size)
    }

    private fun deleteAddress(addressId: String, position: Int) {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        params[Constant.ADDRESS_ID] = addressId

        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity, "Address deleted successfully", Toast.LENGTH_SHORT).show()

                        // Remove the deleted address from the list and refresh the RecyclerView
                        removeAddress(position)

                    } else {
                        Toast.makeText(activity, "Failed to delete address", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.DELETE_ADDRESS, params, true)
    }
}


