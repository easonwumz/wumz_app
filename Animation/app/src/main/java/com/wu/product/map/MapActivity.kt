package com.wu.product.map

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.amap.api.maps.AMap
import com.amap.api.maps.model.MyLocationStyle
import com.wu.product.R
import com.wu.product.base.BaseActivity
import kotlinx.android.synthetic.main.activity_map.*

/**
 * Created by Admin
 * Date 2019/9/8.
 */
class MapActivity : BaseActivity() {


    protected var needPermission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            BACK_LOCATION_PERMISSION)

    companion object {
        private val BACK_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION"

        fun getLaunchIntent(context: Context) = Intent(context, MapActivity::class.java)
    }

    override val layoutId: Int
        get() = R.layout.activity_map

    override fun initView(savedInstanceState: Bundle?) {
//        initPermission()
        //此方法必须重写
        mapView.onCreate(savedInstanceState)
        val aMap: AMap = mapView.map

        val myLocationSyle = MyLocationStyle()
        myLocationSyle.interval(2000)
        aMap.myLocationStyle = myLocationSyle
        aMap.isMyLocationEnabled = true
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    fun initPermission() {
        if (Build.VERSION.SDK_INT > 28 &&
                applicationContext.applicationInfo.targetSdkVersion > 28) {
            needPermission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    BACK_LOCATION_PERMISSION)
        }
        if (Build.VERSION.SDK_INT > 23
                && applicationInfo.targetSdkVersion >= 23) {
            for (perm in needPermission) {
                if (checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(perm)) {
                        requestPermissions(arrayOf(perm), 0)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (requestCode == 0) {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        val aMap: AMap? = mapView.map
                    } else {
                        showWaringDialog()
                    }
                }
            }
        }
    }

    private fun showWaringDialog() {
        val dialog = AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage("请前往设置中打开相关权限，否则功能无法正常运行！")
                .setPositiveButton("确定") { dialog1, which -> finish() }.show()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}