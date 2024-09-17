package com.g_mix.gmw_app.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SplashActivity : Activity() {
    lateinit var session: Session
    lateinit var activity: Activity
    private var handler: Handler? = null


    private var currentVersion: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        activity = this
        session = Session(activity)
        handler = Handler()

        //     val tvTitle = "<font color='#F8B328'>We</font> "+"<font color='#00B251'>Agri</font>"
        //     val textView = findViewById<TextView>(R.id.textView)
        //     textView.text = Html.fromHtml(tvTitle)
        setupViews()


    }



    private fun setupViews() {
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            currentVersion = pInfo.versionCode.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        appupdate()
    }

//    private fun checkForUpdates() {
//          // This would come from your server
//        val currentVersionCode = packageManager.getPackageInfo(packageName, 0).versionCode
//
//        if (currentVersionCode < latestVersionCode) {
//            // Show bottom dialog prompting user to update the app
//            showUpdateDialog()
//        } else {
//            // No update needed, proceed with normal flow
//            GotoActivity()
//        }
//    }

    private fun showUpdateDialog(link: String, description: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_dialog_update, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(false);

        val btnUpdate = view.findViewById<View>(R.id.btnUpdate)
        val dialogMessage = view.findViewById<TextView>(R.id.dialog_message)
        dialogMessage.text = description


        btnUpdate.setOnClickListener(View.OnClickListener {
            val url = link;
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        })


        // Customize your bottom dialog here
        // For example, you can set text, buttons, etc.

        bottomSheetDialog.show()
    }

    private fun GotoActivity() {
        handler?.postDelayed({
            if (session!!.getBoolean("is_logged_in")) {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(activity, MobileLoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 100)
    }


    private fun appupdate() {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session!!.getData(Constant.USER_ID)
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)


                        val latestVersion = jsonArray.getJSONObject(0).getString(Constant.VERSION)
                        val link = jsonArray.getJSONObject(0).getString(Constant.LINK)
                        //   Toast.makeText(activity,latestVersion + currentVersion!!.toInt() , Toast.LENGTH_SHORT).show()
                        val description = jsonArray.getJSONObject(0).getString("description")
                        if (currentVersion!!.toInt() >= latestVersion.toInt()) {
                            GotoActivity()
                        } else {
                            showUpdateDialog(link,description)

                        }

                    } else {
//                        val jsonArray: JSONArray = jsonObject.getJSONArray(Constant.DATA)
//
//
//                        val latestVersion = jsonArray.getJSONObject(0).getString(Constant.VERSION)
//                        val link = jsonArray.getJSONObject(0).getString(Constant.LINK)
//                        val description = jsonArray.getJSONObject(0).getString("description")
//                        if (currentVersion!!.toInt() == latestVersion.toInt()) {
//                            GotoActivity()
//                        } else {
//                            showUpdateDialog(link, description)
//                        }


                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }, activity, Constant.APPUPDATE, params, true)

        // Return a dummy intent, as the actual navigation is handled inside the callback

    }

}