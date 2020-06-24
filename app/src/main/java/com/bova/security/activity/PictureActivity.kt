package com.bova.security.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bova.security.Client
import com.bova.security.ImageCallback
import com.bova.security.R
import kotlinx.android.synthetic.main.activity_picture.*
import kotlin.concurrent.thread


class PictureActivity : AppCompatActivity() {
    private lateinit var client: Client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        thread {
            client = Client("10.0.0.116", 8888, object : ImageCallback {
                override fun onImageComing(image: Bitmap) {
                    Log.e("image", "iamge")
                    runOnUiThread {
                        pic.setImageBitmap(image)
                    }
                }
            })
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