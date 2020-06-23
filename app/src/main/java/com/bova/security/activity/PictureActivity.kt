package com.bova.security.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bova.security.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_picture.*
import java.util.*


class PictureActivity : AppCompatActivity() {
    private var timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        timer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    Log.e("PictureActivity", "11111")
                    Picasso.get()
                        .load(PICTURE_URLS[(0..2).random()])
                        .into(pic)
                }
            }
        }, 1000, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    companion object {
        val PICTURE_URLS = arrayOf(
            "https://gpsbike.oss-accelerate.aliyuncs.com/Data/Gavatar/2018/11/30/19_20181130033000-100-100.png?x-oss-process=image/resize,m_fill,h_100,w_100",
            "https://appapi.igpsport.com:8086/Data/Gavatar/2019/08/07/135223_20190807103143-100-100.jpg",
            "http://i.imgur.com/DvpvklR.png"
        )
    }
}