package com.bova.security.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bova.security.Client
import com.bova.security.ImageCallback
import com.bova.security.R
import com.bova.security.activity.MainActivity.Companion.IP_ARG
import com.bova.security.activity.MainActivity.Companion.IP_CONFIG_ARG
import com.bova.security.activity.MainActivity.Companion.PORT_ARG
import kotlinx.android.synthetic.main.activity_picture.*
import java.net.ConnectException
import kotlin.concurrent.thread


class PictureActivity : AppCompatActivity() {
    private lateinit var client: Client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        var ip = ""
        var port = ""
        getSharedPreferences(IP_CONFIG_ARG, Context.MODE_PRIVATE)?.apply {
            ip = getString(IP_ARG, "")!!
            port = getString(PORT_ARG, "")!!
        }

        thread {
            client = Client(ip, port.toInt(), object : ImageCallback {
                override fun onImageComing(image: Bitmap) {
                    Log.e("image", "iamge")
                    runOnUiThread {
                        pic.setImageBitmap(image)
                    }
                }

                override fun onSocketConnectError() {
                    runOnUiThread {
                        Toast.makeText(
                            this@PictureActivity,
                            "Socket连接异常，请检查IP端口是否配置正确",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }

        btn_reset.setOnClickListener {
            getSharedPreferences(IP_CONFIG_ARG, Context.MODE_PRIVATE).edit().clear().apply()
            finish()
        }

    }

    companion object {
        val PICTURE_URLS = arrayOf(
            "https://gpsbike.oss-accelerate.aliyuncs.com/Data/Gavatar/2018/11/30/19_20181130033000-100-100.png?x-oss-process=image/resize,m_fill,h_100,w_100",
            "https://appapi.igpsport.com:8086/Data/Gavatar/2019/08/07/135223_20190807103143-100-100.jpg",
            "http://i.imgur.com/DvpvklR.png"
        )
    }
}