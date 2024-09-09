package com.g_mix.gmw_app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.*
import com.g_mix.gmw_app.helper.*
import com.g_mix.gmw_app.databinding.ActivityOtpBinding
import com.g_mix.gmw_app.helper.ApiConfig
import org.json.JSONException
import com.g_mix.gmw_app.R

import org.json.JSONObject
import java.util.*

class OtpActivity : BaseActivity() {

    lateinit var binding: ActivityOtpBinding
    lateinit var activity: Activity
    lateinit var session: Session
    var otp = ""




    private var countDownTimer: CountDownTimer? = null
    private val COUNTDOWN_TIME = 45000L // 45 seconds in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_otp)
        binding = ActivityOtpBinding.inflate(layoutInflater)

        activity = this
        session = Session(activity)

        startCountdown()

        otp()

        binding.btnVerifyOTP.setOnClickListener {

            val enteredOtp = binding.otpview.getOTP() // Get the OTP entered by the user

            if (enteredOtp.length == 6) {
                if (enteredOtp == "011011") {
                    // If the default OTP is entered, bypass the actual OTP verification
                    login()
                } else {
                    // Proceed with the normal OTP verification
                    verifyOtp()
                }
            } else {
                Toast.makeText(this, "Please enter valid OTP", Toast.LENGTH_SHORT).show()
            }
        }




        // Set click listener for the "Resend" button
        binding.btnResendOTP.setOnClickListener {
            resetCountdown()
            startCountdown()
            otp()
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
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val registered = jsonObject.getString("registered")
                        if (registered == "true") {
                            val dataObject = jsonObject.getJSONObject(Constant.DATA)
                            val userId = dataObject.getString(Constant.ID).toString()

                            session.setData(Constant.USER_ID, userId)

                            Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()

                            val intent = Intent(activity, MainActivity::class.java)
                            session!!.setBoolean("is_logged_in", true)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Log.e("JSONException", "Error parsing JSON: " + e.message)
                    e.printStackTrace()
                }
            }
        }, activity, Constant.LOGIN, params, true, 1)
    }



    private fun verifyOtp() {
        //  binding.otpView =  000000

        if (otp == binding.otpview.getOTP()) {
            login()
        }
        else {
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
        }

    }

    private fun sendotp(otp : String) {
        val params: MutableMap<String, String> = HashMap()
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                Toast.makeText(this,"OTP Sent Successfully", Toast.LENGTH_SHORT).show()
            } else {
                // Toast.makeText(this, , Toast.LENGTH_SHORT).show()
                Toast.makeText(this,"OTP Failed", Toast.LENGTH_SHORT).show()

            }
        }, this, Constant.getOTPUrl("64045a300411033f",session!!.getData(Constant.MOBILE),otp), params, true)

    }
    private fun otp() {
        val params = HashMap<String, String>()
        params[Constant.MOBILE] = session!!.getData(Constant.MOBILE)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("SIGNUP_RES", response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val dataObject = jsonObject.getJSONObject(Constant.DATA)

                         otp = dataObject.getString("otp")
                       //  Toast.makeText(this, otp, Toast.LENGTH_SHORT).show()
                    //    Toast.makeText(this, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()

                        sendotp(otp)
                    } else {
                        Toast.makeText(this, "1" + jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }, this, Constant.OTP, params, true)
    }

    private fun resetCountdown() {
        countDownTimer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        resetCountdown()
    }
}