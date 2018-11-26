package org.caojun.addressmap.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import org.caojun.adapter.CommonAdapter
import org.caojun.adapter.bean.AdapterItem
import org.caojun.addressmap.R
import org.caojun.addressmap.adapter.SiteItem
import org.caojun.addressmap.room.Site
import org.caojun.addressmap.room.SiteDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AddressFragment : Fragment() {

    private var listView: ListView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.layout_list, null)

        listView = view.findViewById(R.id.listView)

        return view
    }

    override fun onResume() {
        super.onResume()
        doAsync {
            val list = SiteDatabase.getDatabase(activity!!).getSiteDao().queryAll()
            uiThread {
                listView?.adapter = object : CommonAdapter<Site>(list, 1) {
                    override fun createItem(type: Any?): AdapterItem<*> {
                        return SiteItem(activity!!)
                    }
                }
            }
        }
    }
}