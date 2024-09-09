package com.g_mix.gmw_app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.g_mix.gmw_app.helper.*
import com.g_mix.gmw_app.databinding.ActivityMobileLoginBinding


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