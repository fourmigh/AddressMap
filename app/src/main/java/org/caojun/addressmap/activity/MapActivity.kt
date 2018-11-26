package org.caojun.addressmap.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MyLocationStyle
import kotlinx.android.synthetic.main.activity_map.*
import org.caojun.addressmap.R
import org.jetbrains.anko.startActivityForResult

class MapActivity : AppCompatActivity(), LocationSource, AMapLocationListener, AMap.OnMarkerClickListener {

    companion object {
        const val RequestCode_Address = 1
    }

    private var mLocationChangedListener: LocationSource.OnLocationChangedListener? = null
    private var mLocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView.onCreate(savedInstanceState)
        initialize()

        fab.setOnClickListener { view ->
            startActivityForResult<AddressActivity>(RequestCode_Address)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun initialize() {
        val aMap = mapView.map
        aMap.uiSettings.isRotateGesturesEnabled = true//旋转手势
        aMap.moveCamera(CameraUpdateFactory.zoomBy(6f))
        aMap.setLocationSource(this)// 设置定位监听
        // 自定义系统定位蓝点
        val myLocationStyle = MyLocationStyle()
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0))
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0f)
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0))
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.myLocationStyle = myLocationStyle
        aMap.isMyLocationEnabled = true// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.uiSettings.isMyLocationButtonEnabled = true// 定位按钮
        aMap.uiSettings.isTiltGesturesEnabled = true//倾斜
        aMap.uiSettings.isCompassEnabled = true//指南针
        aMap.uiSettings.isScaleControlsEnabled = true//比例尺
        aMap.setOnMarkerClickListener(this)
    }

    override fun deactivate() {
        mLocationChangedListener = null
        mLocationClient?.stopLocation()
        mLocationClient?.onDestroy()
        mLocationClient = null
    }

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        mLocationChangedListener = listener
        if (mLocationClient == null) {
            mLocationClient = AMapLocationClient(this)
            mLocationOption = AMapLocationClientOption()
            // 设置定位监听
            mLocationClient?.setLocationListener(this)
            // 设置为高精度定位模式
            mLocationOption?.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            // 只是为了获取当前位置，所以设置为单次定位
            mLocationOption?.isOnceLocation = (true)
            // 设置定位参数
            mLocationClient?.setLocationOption(mLocationOption)
            mLocationClient?.startLocation()
        }
    }

    override fun onLocationChanged(amapLocation: AMapLocation) {
        mLocationChangedListener?.onLocationChanged(amapLocation)// 显示系统小蓝点
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return true
    }
}