package org.caojun.addressmap.utils

import android.content.Context
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.Poi
import com.amap.api.navi.AmapNaviPage
import com.amap.api.navi.AmapNaviParams
import org.caojun.addressmap.room.Site

object GDMapUtils {

    var aMap: AMap? = null
    var hmSiteMarker = HashMap<Int, Marker>()

    fun clear() {
        aMap = null
        hmSiteMarker.clear()
    }

    fun doNavigate(context: Context, site: Site) {
        val llEnd = LatLng(site.latitude, site.longitude)
        val params = AmapNaviParams(Poi(site.area + site.address, llEnd, ""))
        AmapNaviPage.getInstance().showRouteActivity(context.applicationContext, params, null)
    }

    fun moveMap(site: Site) {
        val zoom = aMap?.cameraPosition?.zoom?:18f
        val tilt = aMap?.cameraPosition?.tilt?:30f
        val bearing = aMap?.cameraPosition?.bearing?:0f
        val cameraPosition = CameraPosition(LatLng(site.latitude, site.longitude), zoom, tilt, bearing)
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        aMap?.moveCamera(cameraUpdate)
        val marker = GDMapUtils.hmSiteMarker[site.id]
        marker?.showInfoWindow()
    }
}