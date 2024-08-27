package com.g_mix.app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.*
import com.g_mix.app.Activity.BaseActivity
import com.g_mix.app.Activity.MainActivity
import com.google.firebase.auth.*
import com.g_mix.app.helper.*
import com.g_mix.app.R
import com.g_mix.app.databinding.ActivityOtpBinding
import com.g_mix.app.helper.ApiConfig
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class OtpActivity : BaseActivity() {

    lateinit var binding: ActivityOtpBinding
    lateinit var activity: Activity
    lateinit var session: Session




    private var countDownTimer: CountDownTimer? = null
    private val COUNTDOWN_TIME = 10000L // 45 seconds in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_otp)
        binding = ActivityOtpBinding.inflate(layoutInflater)

        activity = this
        session = Session(activity)

        startCountdown()

        binding.ivBack.setOnClickListener {
            finish()  // This will close the current activity and go back to the previous one
        }

        binding.btnVerifyOTP.setOnClickListener {

            if (binding.otpview.getOTP().length == 6) {

                login()

            } else {
                Toast.makeText(this, "Please enter valid OTP", Toast.LENGTH_SHORT).show()
            }
        }


            // Set click listener for the "Resend" button
            binding.btnResendOTP.setOnClickListener {
                resetCountdown()
                startCountdown()
              Toast.makeText(this, "OTP sent successfully", Toast.LENGTH_SHORT).show()

                // Disable the button
                binding.btnResendOTP.isEnabled = false
            }

        setContentView(binding.root)
    }

    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(COUNTDOWN_TIME, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                // Update UI to show remaining seconds
                binding.btnResendOTP.text = "Resend in $secondsLeft seconds"
            }

            override fun onFinish() {
                // Enable the button when countdown finishes
                binding.btnResendOTP.isEnabled = true
                binding.btnResendOTP.text = "Resend OTP"
            }
        }.start()
    }

    private fun login() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.MOBILE] = session.getData(Constant.MOBILE)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject: JSONObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val registered = jsonObject.getString("registered")
                        if (registered == "true") {
//                            val `object` = JSONObject(response)
//                            val jsonobj = `object`.getJSONObject(Constant.DATA)

                            Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()

                            val intent = Intent(activity, MainActivity::class.java)
                            session.setData(Constant.IS_LOGIN, "1")
                            startActivity(intent)
                            finish()
                        } else if (registered == "false") {
                            Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()

//                            val intent = Intent(activity,MobileLoginActivity::class.java)
                            session.setData("login", "0")
//                            startActivity(intent)
//                            finish()
                        }
                    } else {
                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, activity, Constant.LOGIN, params, true, 1)

        Log.d("LOGIN", "LOGIN: " + Constant.LOGIN)
        Log.d("LOGIN", "LOGINparams: " + params)
    }

    private fun resetCountdown() {
        countDownTimer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        resetCountdown()
    }
}