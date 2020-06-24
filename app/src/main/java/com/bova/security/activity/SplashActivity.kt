package com.bova.security.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bova.security.R
import com.bova.security.activity.MainActivity.Companion.IP_ARG
import com.bova.security.activity.MainActivity.Companion.IP_CONFIG_ARG

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ip = getSharedPreferences(IP_CONFIG_ARG, Context.MODE_PRIVATE).getString(IP_ARG, "")
        startActivity(
            Intent(
                this,
                if (ip!!.isEmpty()) MainActivity::class.java else PictureActivity::class.java
            )
        )
    }
}