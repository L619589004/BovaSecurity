package com.bova.security.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bova.security.R
import com.bova.security.activity.MainActivity.Companion.IP_ARG
import com.bova.security.activity.MainActivity.Companion.IP_CONFIG_ARG
import com.bova.security.activity.MainActivity.Companion.PORT_ARG
import kr.co.namee.permissiongen.PermissionFail
import kr.co.namee.permissiongen.PermissionGen
import kr.co.namee.permissiongen.PermissionSuccess

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        PermissionGen.with(this@SplashActivity).addRequestCode(100).permissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).request()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    @SuppressLint("LongLogTag")
    @PermissionSuccess(requestCode = 100)
    fun requestPermissionsSuccess() {
        Log.i("requestPermissionsSuccess", "111")

        var ip = ""
        var port = ""
        getSharedPreferences(IP_CONFIG_ARG, Context.MODE_PRIVATE)?.apply {
            ip = getString(IP_ARG, "")!!
            port = getString(PORT_ARG, "")!!

            Log.e("SplashActivity", "ip = $ip , port = $port")
        }

        startActivity(
            Intent(
                this,
                if (ip.isNotEmpty() && port.isNotEmpty()) PictureActivity::class.java else MainActivity::class.java
            )
        )
    }

    @PermissionFail(requestCode = 100)
    fun requestPermissionsFail() {
        Toast.makeText(this, "请打开相关权限，我们不会收集您的私人信息", Toast.LENGTH_SHORT).show()
    }
}