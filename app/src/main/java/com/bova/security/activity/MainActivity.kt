package com.bova.security.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bova.security.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var ipLength = 0
    private var portLength = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_ip!!.setOnTextChangedListener { length: Int ->
            ipLength = length
            setNextButtonEnabled()
        }

        et_port!!.setOnTextChangedListener { length: Int ->
            portLength = length
            setNextButtonEnabled()
        }

        btn_next.setOnClickListener {
            val ip = et_ip!!.text.toString()
            val port = et_port!!.text.toString()
            getSharedPreferences(IP_CONFIG_ARG, Context.MODE_PRIVATE).edit().apply {
                Log.e("MainActivity", "ip = $ip , port = $port")

                putString(IP_ARG, ip)
                putString(PORT_ARG, port)
                apply()
            }

            startActivity(Intent(this, PictureActivity::class.java))
            finish()
        }
    }

    private fun setNextButtonEnabled() {
        btn_next!!.isEnabled = ipLength > 0 && portLength > 0
    }

    companion object {
        const val IP_CONFIG_ARG = "ip_config"
        const val IP_ARG = "ip"
        const val PORT_ARG = "port"
    }
}