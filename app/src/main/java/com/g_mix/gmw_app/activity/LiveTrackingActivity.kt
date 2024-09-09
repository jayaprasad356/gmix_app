package com.g_mix.gmw_app.activity

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.g_mix.gmw_app.R

class LiveTrackingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_tracking)

        val ivBack = findViewById<ImageButton>(R.id.ivBack)
        ivBack.setOnClickListener {
            onBackPressed()
        }

        val webView = findViewById<WebView>(R.id.webView) // Assuming there's a WebView with this ID
        webView.webViewClient = WebViewClient() // Ensures links open in the WebView instead of a browser

        val trackingUrl = intent.getStringExtra("TRACKING_URL")
        trackingUrl?.let {
            webView.loadUrl(it)
        }
    }

}
