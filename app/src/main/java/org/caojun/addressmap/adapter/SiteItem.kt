package org.caojun.addressmap.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.caojun.activity.BaseAppCompatActivity
import org.caojun.adapter.bean.AdapterItem
import org.caojun.addressmap.R
import org.caojun.addressmap.activity.AddressActivity
import org.caojun.addressmap.room.Site
import org.jetbrains.anko.startActivity

class SiteItem(private val context: Context): AdapterItem<Site> {

    private var tvName: TextView? = null
    private var btnMobile: Button? = null
    private var btnAddress: Button? = null

    override fun getLayoutResId(): Int {
        return R.layout.item_address
    }

    override fun bindViews(root: View) {
        tvName = root.findViewById(R.id.tvName)
        btnMobile = root.findViewById(R.id.btnMobile)
        btnAddress = root.findViewById(R.id.btnAddress)
    }

    override fun setViews() {
    }

    override fun handleData(t: Site, position: Int) {
        tvName?.text = t.name
        btnMobile?.text = t.mobile
        btnAddress?.text = t.area + t.address

        if (TextUtils.isEmpty(t.mobile)) {
            btnMobile?.visibility = View.GONE
        } else {
            btnMobile?.visibility = View.VISIBLE

            btnMobile?.setOnClickListener {
                (context as BaseAppCompatActivity).call(t.mobile)
            }
        }

        if (TextUtils.isEmpty(btnAddress?.text)) {
            btnAddress?.visibility = View.GONE
        } else {
            btnAddress?.visibility = View.VISIBLE
        }

        tvName?.setOnClickListener {
            context.startActivity<AddressActivity>(AddressActivity.Key_Site to t)
        }
    }
}