package org.caojun.addressmap.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.layout_list.*
import org.caojun.adapter.CommonAdapter
import org.caojun.adapter.bean.AdapterItem
import org.caojun.addressmap.R
import org.caojun.addressmap.adapter.SiteItem
import org.caojun.addressmap.room.Site
import org.caojun.addressmap.room.SiteDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class AddressListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_list)

        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_address_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_add) {
            startActivity<AddressActivity>()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        doAsync {
            val list = SiteDatabase.getDatabase(this@AddressListActivity).getSiteDao().queryAll()
            uiThread {
                listView?.adapter = object : CommonAdapter<Site>(list, 1) {
                    override fun createItem(type: Any?): AdapterItem<*> {
                        return SiteItem(this@AddressListActivity)
                    }
                }
            }
        }
    }
}