package com.g_mix.gmw_app.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.g_mix.gmw_app.R
import com.google.androidbrowserhelper.trusted.TwaLauncher



class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web) // Use an appropriate layout

        // String URL
        val paymentUrl = intent.getStringExtra("payment_url")

        // Convert the String URL to a Uri object
        val uri = Uri.parse(paymentUrl)

        // Launch the Trusted Web Activity
        val launcher = TwaLauncher(this)
        launcher.launch(uri)
    }
}
