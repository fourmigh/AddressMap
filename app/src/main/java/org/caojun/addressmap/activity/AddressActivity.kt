package org.caojun.addressmap.activity

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import org.jetbrains.anko.alert

class AddressActivity : BaseActivity() {

    companion object {
        const val Key_Site = "Key_Site"
        const val Key_Province = "Key_Province"
    }

    private var site: Site? = null
    private var adCode = ""
    private var areaCode = ""
    private var zipCode = ""

    private var isSaveMenuEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_address)

        setSupportActionBar(toolbar)

        val province = intent.getStringExtra(Key_Province)

        AreaPicker.init(this, btnArea, object : OnPickerClickListener {
            override fun onPickerClick(pickerData: PickerData) {
                onPicker(pickerData)
            }

            override fun onPickerConfirmClick(pickerData: PickerData) {
                onPicker(pickerData)
            }
        }, province)

        site = intent.getParcelableExtra(Key_Site)
        adCode = site?.adCode?:""
        areaCode = site?.areaCode?:""
        zipCode = site?.zipCode?:""
        etName.setText(site?.name)
        etMobile.setText(site?.mobile)
        btnArea.text = site?.area
        etAddress.setText(site?.address)

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkSaveMenu()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }

        etName.addTextChangedListener(textWatcher)
        etMobile.addTextChangedListener(textWatcher)
        etAddress.addTextChangedListener(textWatcher)
    }

    private fun updateSaveMenu(isEnabled: Boolean) {
        isSaveMenuEnabled = isEnabled
        invalidateOptionsMenu()
    }

    private fun onPicker(pickerData: PickerData) {
        btnArea.text = pickerData.selectText
        adCode = pickerData.adCode?:""
        areaCode = pickerData.areaCode?:""
        zipCode = pickerData.zipCode?:""
        AreaPicker.dismiss()

        checkSaveMenu()
    }

    private fun checkSaveMenu() {
        val name = etName.text.toString()
        val mobile = etMobile.text.toString()
        val area = btnArea.text.toString()
        val address = etAddress.text.toString()

        if (site == null) {
            updateSaveMenu(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(mobile) || !TextUtils.isEmpty(mobile) || !TextUtils.isEmpty(address))
        } else if (name != site?.name || mobile != site?.mobile || area != site?.area || address != site?.address) {
            updateSaveMenu(true)
        } else {
            updateSaveMenu(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_address, menu)
        val saveMenu = menu.add(0, R.id.action_save, 0, R.string.contact_save)
        saveMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        saveMenu.isEnabled = isSaveMenuEnabled

        if (site != null) {
            val deleteMenu = menu.add(0, R.id.action_delete, 0, R.string.contact_delete)
            deleteMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
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

        if (id == R.id.action_delete) {
            doDelete()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun doDelete() {
        alert {
            messageResource = R.string.confirm_delete
            positiveButton(android.R.string.ok) {
                doAsync {
                    SiteDatabase.getDatabase(this@AddressActivity).getSiteDao().delete(site!!)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
            negativeButton(android.R.string.cancel) {}
        }.show()
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
            site?.adCode = adCode
            site?.areaCode = areaCode
            site?.zipCode = zipCode

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
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })
        val query = GeocodeQuery(site.address, site.adCode)
        geocodeSearch.getFromLocationNameAsyn(query)
    }
}