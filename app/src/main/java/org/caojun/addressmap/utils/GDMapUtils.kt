package org.caojun.addressmap.utils

import android.content.Context
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Poi
import com.amap.api.navi.AmapNaviPage
import com.amap.api.navi.AmapNaviParams
import org.caojun.addressmap.room.Site

object GDMapUtils {

    fun doNavigate(context: Context, site: Site) {
        val llEnd = LatLng(site.latitude, site.longitude)
        val params = AmapNaviParams(Poi(site.area + site.address, llEnd, ""))
        AmapNaviPage.getInstance().showRouteActivity(context.applicationContext, params, null)
    }
}