package com.g_mix.gmw_app.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.g_mix.gmw_app.Model.Addresslist
import com.g_mix.gmw_app.R

class AddresslistAdapter(
    private val addressList: List<Addresslist>,
    private val onAddressSelected: (Addresslist) -> Unit
) : RecyclerView.Adapter<AddresslistAdapter.AddressViewHolder>() {

    // Variable to store the selected position
    private var selectedPosition = -1 // No selection by default

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = addressList[position]

        // Bind the data from the Addresslist model to the view
        holder.tvName.text = "${address.first_name} ${address.last_name}"
        holder.tvAddress.text = "${address.door_no}, ${address.street_name}, ${address.city}, ${address.state} - ${address.pincode}"
        holder.tvMobile.text = address.mobile

        // Manage the selection state of the RadioButton
        holder.radioButton.isChecked = position == selectedPosition

        // Set a click listener on the RadioButton
        holder.radioButton.setOnClickListener {
            // Update the selected position
            selectedPosition = position

            // Notify the adapter that the data has changed so it can update the view
            notifyDataSetChanged()

            // Trigger the callback to pass the selected address to the fragment
            onAddressSelected(address)
        }
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    // ViewHolder class to hold references to each view in the layout
    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        val tvMobile: TextView = view.findViewById(R.id.tvMobile)
        val radioButton: RadioButton = view.findViewById(R.id.rb)
    }
}

