package com.g_mix.app.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.g_mix.app.R
import com.g_mix.app.activity.MobileLoginActivity
import com.g_mix.app.helper.Constant
import com.g_mix.app.helper.Session
import java.util.*

class SplashActivity : Activity() {
    lateinit var session: Session
    lateinit var activity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this@SplashActivity

        FirebaseApp.initializeApp(activity)

        val resources = resources
        val dm = resources.displayMetrics
        val configuration = resources.configuration
        configuration.setLocale(Locale(Constant.LANGUAGE_CODE.lowercase(Locale.getDefault())))
        resources.updateConfiguration(configuration, dm)

        session = Session(activity)

        session.setBoolean("update_skip", false)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor =
            ContextCompat.getColor(activity, R.color.white)
        setContentView(R.layout.activity_splash)

        val login = session.getData(Constant.IS_LOGIN) ?: "0"

        Handler(Looper.getMainLooper()).postDelayed({
            if(login == "1" && login != "0" && login.isNotEmpty() && login != null){
            startActivity(Intent(activity, MainActivity::class.java))
            finish()
            } else {
                startActivity(Intent(activity, MobileLoginActivity::class.java))
                finish()
            }
        }, 3000)

    }
}