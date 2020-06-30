package com.bova.security.activity

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bova.security.Client
import com.bova.security.ImageCallback
import com.bova.security.R
import com.bova.security.activity.MainActivity.Companion.IP_ARG
import com.bova.security.activity.MainActivity.Companion.IP_CONFIG_ARG
import com.bova.security.activity.MainActivity.Companion.PORT_ARG
import kotlinx.android.synthetic.main.activity_picture.*
import kotlin.concurrent.thread


class PictureActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private lateinit var client: Client

    var ip = ""
    var port = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        pic.callback = this


        getSharedPreferences(IP_CONFIG_ARG, Context.MODE_PRIVATE)?.apply {
            ip = getString(IP_ARG, "")!!
            port = getString(PORT_ARG, "")!!
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

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        thread {
            client = Client(ip, port.toInt(), object : ImageCallback {
                override fun onImageComing(image: Bitmap) {
                    //清屏
                    val mCanvas = holder?.lockCanvas(null)
                    mCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                    mCanvas?.drawBitmap(image, 0f, 0f, Paint())
                    holder?.unlockCanvasAndPost(mCanvas)
                    try {
                        mCanvas?.drawBitmap(image, 0f, 0f, null)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {

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
    }
}