package com.bova.security.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bova.security.Client
import com.bova.security.ImageCallback
import com.bova.security.R
import com.bova.security.activity.MainActivity.Companion.IP_ARG
import com.bova.security.activity.MainActivity.Companion.IP_CONFIG_ARG
import com.bova.security.activity.MainActivity.Companion.PORT_ARG
import kotlinx.android.synthetic.main.activity_picture.*
import kotlinx.android.synthetic.main.dialog_feature.view.*
import kotlin.concurrent.thread


class PictureActivity : AppCompatActivity() {
    private lateinit var client: Client

    var ip = ""
    var port = ""

    private var isVibrationSwitchOpened = false
    private var vibrator: Vibrator? = null

    private var isPause = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_picture)


        val gestureDetector = GestureDetector(this, object : SimpleOnGestureListener() {
            /**
             * 发生确定的单击时执行
             * @param e
             * @return
             */
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean { //单击事件
                return super.onSingleTapConfirmed(e)
            }

            /**
             * 双击发生时的通知
             * @param e
             * @return
             */
            override fun onDoubleTap(e: MotionEvent): Boolean { //双击事件
                isPause = !isPause
                Toast.makeText(
                    this@PictureActivity,
                    if (isPause) "已暂停" else "开始播放",
                    Toast.LENGTH_SHORT
                ).show()
                return super.onDoubleTap(e)
            }

            /**
             * 双击手势过程中发生的事件，包括按下、移动和抬起事件
             * @param e
             * @return
             */
            override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                return super.onDoubleTapEvent(e)
            }

            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }
        })

        pic.apply {
            setOnTouchListener { p0, p1 -> gestureDetector.onTouchEvent(p1) }
        }

        vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator?

        getSharedPreferences(IP_CONFIG_ARG, Context.MODE_PRIVATE)?.apply {
            ip = getString(IP_ARG, "")!!
            port = getString(PORT_ARG, "")!!
        }

        iv_feature.setOnClickListener {
            val dialog = Dialog(this, R.style.loadingDialog)

            val featureView = layoutInflater.inflate(R.layout.dialog_feature, null)

            dialog.apply {
                setContentView(featureView)
                setCancelable(true)
                setCanceledOnTouchOutside(true)
            }

            featureView.apply {
                btn_open_gallery.setOnClickListener {
                    dialog.dismiss()
                }

                sw_alarm.apply {
                    isChecked = isVibrationSwitchOpened
                    setOnCheckedChangeListener { _, isChecked ->
                        isVibrationSwitchOpened = isChecked
                        dialog.dismiss()
                    }
                }

                btn_reset.setOnClickListener {
                    dialog.dismiss()
                    getSharedPreferences(IP_CONFIG_ARG, Context.MODE_PRIVATE).edit().clear().apply()
                    finish()
                }
            }

            if (!dialog.isShowing) {
                dialog.show()
            }
        }

        thread {
            client = Client(ip, port.toInt(), object : ImageCallback {
                override fun onImageComing(image: Bitmap, isNeedAlarm: Boolean) {
                    if (isPause)
                        return

                    runOnUiThread {
                        pic.setImageBitmap(image)
                    }

                    if (isVibrationSwitchOpened && isNeedAlarm) {
                        vibrator?.apply {
                            vibrate(200)
                        }
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

    companion object {
        val PICTURE_URLS = arrayOf(
            "https://gpsbike.oss-accelerate.aliyuncs.com/Data/Gavatar/2018/11/30/19_20181130033000-100-100.png?x-oss-process=image/resize,m_fill,h_100,w_100",
            "https://appapi.igpsport.com:8086/Data/Gavatar/2019/08/07/135223_20190807103143-100-100.jpg",
            "http://i.imgur.com/DvpvklR.png"
        )
    }
}