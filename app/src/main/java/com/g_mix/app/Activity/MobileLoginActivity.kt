package com.g_mix.app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.g_mix.app.Activity.BaseActivity
import com.g_mix.app.Activity.MainActivity
import com.g_mix.app.helper.*
import com.g_mix.app.databinding.ActivityMobileLoginBinding
import com.g_mix.app.activity.OtpActivity
import com.g_mix.app.helper.ApiConfig
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class MobileLoginActivity : BaseActivity() {

    lateinit var binding: ActivityMobileLoginBinding
    lateinit var activity: Activity
    lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMobileLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this
        session = Session(activity)

        binding.btnGenerateOtp.setOnClickListener {

            if (binding.etMobileNumber.text.toString().isEmpty()) {
                binding.etMobileNumber.error = "Please enter mobile number"
                return@setOnClickListener
            }
            else if (binding.etMobileNumber.text.toString().length != 10) {
                binding.etMobileNumber.error = "Please enter valid mobile number"
                return@setOnClickListener
            }
            else{
                session.setData(Constant.MOBILE,binding.etMobileNumber.text.toString())
                startActivity(Intent(activity, OtpActivity::class.java))
                finish()
            }
        }
    }
}