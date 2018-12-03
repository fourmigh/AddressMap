package org.caojun.addressmap.utils

import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Poi
import com.amap.api.navi.AmapNaviPage
import com.amap.api.navi.AmapNaviParams
import com.amap.api.navi.AmapNaviType
import org.caojun.addressmap.room.Site

object GDMapUtils {

    var amapLocation: AMapLocation? = null

    fun doNavigate(context: Context, startName: String, siteEnd: Site) {
        val llStart = if (amapLocation == null) null else LatLng(amapLocation!!.latitude, amapLocation!!.longitude)
        val llEnd = LatLng(siteEnd.latitude, siteEnd.longitude)
        val params = AmapNaviParams(Poi(startName, llStart, ""), null, Poi(siteEnd.area + siteEnd.address, llEnd, ""), AmapNaviType.DRIVER)
        AmapNaviPage.getInstance().showRouteActivity(context.applicationContext, params, null)
    }
}