package org.caojun.addressmap.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeResult
import kotlinx.android.synthetic.main.layout_address.*
import org.caojun.addressmap.R
import org.caojun.addressmap.room.Site
import org.caojun.addressmap.room.SiteDatabase
import org.caojun.areapicker.AreaPicker
import org.caojun.areapicker.OnPickerClickListener
import org.caojun.areapicker.PickerData
import org.jetbrains.anko.doAsync
import com.amap.api.services.geocoder.GeocodeQuery
import org.caojun.activity.BaseAppCompatActivity


class AddressActivity : BaseAppCompatActivity() {

    companion object {
        const val Key_Site = "Key_Site"
        const val Key_Province = "Key_Province"
    }

    private var site: Site? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_address)

        setSupportActionBar(toolbar)

        val province = intent.getStringExtra(Key_Province)

        AreaPicker.init(this, btnArea, object : OnPickerClickListener {
            override fun onPickerClick(pickerData: PickerData) {
                btnArea.text = pickerData.selectText
                AreaPicker.dismiss()
            }

            override fun onPickerConfirmClick(pickerData: PickerData) {
                btnArea.text = pickerData.selectText
                AreaPicker.dismiss()
            }
        }, province)

        site = intent.getParcelableExtra(Key_Site)
        if (site != null) {
            etName.setText(site?.name)
            etMobile.setText(site?.mobile)
            btnArea.text = site?.area
            etAddress.setText(site?.address)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_address, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_save) {
            doSave()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun doSave() {
        doAsync {
            var isNew = false
            if (site == null) {
                site = Site()
                isNew = true
            }
            site?.name = etName.text.toString()
            site?.mobile = etMobile.text.toString()
            site?.area = btnArea.text.toString()
            site?.address = etAddress.text.toString()

            searchGEO(isNew, site!!)
        }
    }

    private fun searchGEO(isNew: Boolean, site: Site) {
        val geocodeSearch = GeocodeSearch(this)
        geocodeSearch.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
            override fun onRegeocodeSearched(result: RegeocodeResult, rCode: Int) {

            }

            override fun onGeocodeSearched(result: GeocodeResult, rCode: Int) {
                val latlon = result.geocodeAddressList[0].latLonPoint
                site.latitude = latlon.latitude
                site.longitude = latlon.longitude

                doAsync {

                    if (isNew) {
                        SiteDatabase.getDatabase(this@AddressActivity).getSiteDao().insert(site)
                    } else {
                        SiteDatabase.getDatabase(this@AddressActivity).getSiteDao().update(site)
                    }
                    finish()
                }
            }
        })
        val query = GeocodeQuery(site.address, site.area)
        geocodeSearch.getFromLocationNameAsyn(query)
    }
}