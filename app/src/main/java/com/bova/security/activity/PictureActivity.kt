package com.bova.security.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import com.bova.security.util.BovaFileObserver
import com.bova.security.util.Util
import kotlinx.android.synthetic.main.activity_picture.*
import kotlinx.android.synthetic.main.dialog_feature.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class PictureActivity : AppCompatActivity() {
    private var client: Client? = null

    var ip = ""
    var port = ""

    private var isVibrationSwitchOpened = false
    private var vibrator: Vibrator? = null

    private var isPause = false

    private var lastSaveTime = 0L

    private var bovaFileObserver: BovaFileObserver? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_picture)

        bovaFileObserver = BovaFileObserver(
            mPath = if (Build.BRAND == "Xiaomi") { // 小米手机
                Environment.getExternalStorageDirectory().path + "/DCIM/Camera/"
            } else {
                Environment.getExternalStorageDirectory().path + "/DCIM/"
            }, mContext = this
        )
        bovaFileObserver?.startWatching()

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
                    jumpToGallery()
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

                    client?.reset()
                    startActivity(Intent(this@PictureActivity, MainActivity::class.java))
                    finish()
                }
            }

            if (!dialog.isShowing) {
                dialog.show()
            }
        }

        client = Client(ip, port.toInt(), object : ImageCallback {
            override fun onImageComing(image: Bitmap, isNeedAlarm: Boolean) {
                if (isPause)
                    return

                runOnUiThread {
                    pic.setImageBitmap(image)
                }

                if (System.currentTimeMillis() - lastSaveTime >= 3000L) {
                    saveToGallery(image)
                    lastSaveTime = System.currentTimeMillis()
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

    @SuppressLint("SimpleDateFormat")
    private fun saveToGallery(bitmap: Bitmap) {
        val format = SimpleDateFormat("yyyyMMddHHmmss")
        val bitmapName = format.format(Date()) + "_bova" + ".JPEG"
        val isSuccess = Util.saveBitmap(applicationContext, bitmap, bitmapName)
    }

    private fun jumpToGallery() {
        val packageManager = packageManager
        val intent: Intent?
        intent = packageManager.getLaunchIntentForPackage("com.android.gallery3d")
        intent?.let {
            startActivity(it)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        bovaFileObserver?.stopWatching()
    }

    companion object {
        const val TAG = "PictureActivity"
    }
}