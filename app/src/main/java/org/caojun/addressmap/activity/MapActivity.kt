package org.caojun.addressmap.activity

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.*
import kotlinx.android.synthetic.main.activity_map.*
import org.caojun.activity.BaseAppCompatActivity
import org.caojun.addressmap.R
import org.caojun.addressmap.room.Site
import org.caojun.addressmap.room.SiteDatabase
import org.caojun.utils.ActivityUtils
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity

class MapActivity : BaseAppCompatActivity(), LocationSource, AMapLocationListener, AMap.OnMarkerClickListener {


    private var mLocationChangedListener: LocationSource.OnLocationChangedListener? = null
    private var mLocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null

    private val hmMarkerSite = HashMap<Marker, Site>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView.onCreate(savedInstanceState)
        initialize()

        fab.setOnClickListener { view ->
            startActivity<AddressListActivity>()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()

        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION, object : ActivityUtils.RequestPermissionListener {
            override fun onSuccess() {
                addMarkersToMap()
            }

            override fun onFail() {
            }
        })
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
        if (marker.isInfoWindowShown) {
            val site = hmMarkerSite[marker]!!
            alert {
                title = site.name
                message = "导航、修改、打电话？"
                positiveButton("打电话") {
                    call(site.mobile)
                }
                neutralPressed("导航") {

                }
                negativeButton("修改") {
                    startActivity<AddressActivity>(AddressActivity.Key_Site to site)
                }
            }.show()
        } else {
            marker.showInfoWindow()
        }
        return true
    }

    private fun addMarkersToMap() {
        doAsync {
            val list = SiteDatabase.getDatabase(this@MapActivity).getSiteDao().queryAll()
            val aMap = mapView.map
            aMap.clear()
            hmMarkerSite.clear()
            for (i in list.indices) {
                addMarkerToMap(aMap, list[i])
            }
        }
    }

    private fun addMarkerToMap(aMap: AMap, site: Site) {

        val latLng = LatLng(site.latitude, site.longitude)

        val markerOption = MarkerOptions().icon(
            BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            .position(latLng)
            .title(site.name)
            .snippet(site.mobile + "\n" + site.address)
            .draggable(true)
        val marker = aMap.addMarker(markerOption)
        marker?.showInfoWindow()

        hmMarkerSite[marker] = site
    }
}