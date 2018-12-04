package org.caojun.addressmap.adapter

import android.view.View
import android.widget.Button
import android.widget.TextView
import org.caojun.adapter.bean.AdapterItem
import org.caojun.addressmap.R
import org.caojun.addressmap.room.Site
import org.caojun.addressmap.utils.GDMapUtils

class NameItem: AdapterItem<Site> {

    private var tvName: TextView? = null

    override fun getLayoutResId(): Int {
        return R.layout.item_address
    }

    override fun bindViews(root: View) {
        tvName = root.findViewById(R.id.tvName)
        val btnMobile = root.findViewById<Button>(R.id.btnMobile)
        val btnAddress = root.findViewById<Button>(R.id.btnAddress)

        tvName?.textSize = 24f
        btnMobile.visibility = View.GONE
        btnAddress.visibility = View.GONE
    }

    override fun setViews() {
    }

    override fun handleData(t: Site, position: Int) {
        tvName?.text = t.name

        tvName?.setOnClickListener {
            GDMapUtils.moveMap(t)
        }
    }
}