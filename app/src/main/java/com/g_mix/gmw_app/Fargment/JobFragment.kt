package com.g_mix.gmw_app.Fargment

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.g_mix.gmw_app.activity.MainActivity
import com.g_mix.gmw_app.databinding.FragmentJobBinding
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class JobFragment : Fragment() {


    lateinit var binding: FragmentJobBinding
    lateinit var session: Session
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobBinding.inflate(inflater, container, false)
        session = Session(requireActivity())

        val genderOptions = listOf("Select Gender", "male", "female", "other")
        val adapter = ArrayAdapter(requireActivity(), R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapter

        (activity as MainActivity).binding.rltoolbar.visibility = View.GONE
        (activity as MainActivity).binding.bottomNavigationView.visibility = View.GONE
        (activity as MainActivity).binding.fabWhatsapp.visibility = View.GONE

        var gender = ""

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGender = genderOptions[position]
                if(selectedGender != "Select Gender"){
                    gender = selectedGender
                } else {
                 //   Toast.makeText(activity, "Please select Gender", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }


        binding.btnSubmit.setOnClickListener {
            val name = binding.etName.text.toString()
            val mobile = binding.etMobileNumber.text.toString()
            val place = binding.etPlace.text.toString()
            val qualification = binding.etQualification.text.toString()
            val experience = binding.etExperience.text.toString()
            val age = binding.etAge.text.toString()
//            val gender = binding.etGender.text.toString()

            if (validateInputs(name,mobile,place,qualification,experience,age,gender)){
                resell(name,mobile,place,qualification,experience,age,gender)
            }



        }

        return binding.root
    }


    private fun resell(
        name: String,
        mobile: String,
        place: String,
        qualification: String,
        experience: String,
        age: String,
        gender: String
    ) {
        val params = HashMap<String, String>()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        params[Constant.NAME] = name
        params[Constant.MOBILE] = mobile
        params[Constant.PLACE] = place
        params[Constant.QUALIFICATION] = qualification
        params[Constant.EXPERIENCE] = experience
        params[Constant.AGE] = age
        params[Constant.GENDER] = gender
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(requireActivity(), jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressed()

                    }
                    else {
                        Toast.makeText(
                            requireActivity(),
                            jsonObject.getString(Constant.MESSAGE),
                            Toast.LENGTH_SHORT
                        ).show()

                    }



                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.UPDATE_RESELLS, params, true, 1)
    }


    private fun validateInputs(
        name: String,
        mobile: String,
        place: String,
        qualification: String,
        experience: String,
        age: String,
        gender: String
    ): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(activity, "Please enter Name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (mobile.isEmpty()) {
            Toast.makeText(activity, "Please enter Mobile Number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (mobile.length < 10) {
            Toast.makeText(activity, "Please enter Valid Mobile Number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (place.isEmpty()) {
            Toast.makeText(activity, "Please enter Place", Toast.LENGTH_SHORT).show()
            return false
        }
        if (qualification.isEmpty()) {
            Toast.makeText(activity, "Please enter Qualification", Toast.LENGTH_SHORT).show()
            return false
        }

        if (experience.isEmpty()) {
            Toast.makeText(activity, "Please enter Experience", Toast.LENGTH_SHORT).show()
            return false
        }

        if (age.isEmpty()) {
            Toast.makeText(activity, "Please enter Age", Toast.LENGTH_SHORT).show()
            return false
        }
        if (gender.isEmpty()) {
            Toast.makeText(activity, "Please select Gender", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


}