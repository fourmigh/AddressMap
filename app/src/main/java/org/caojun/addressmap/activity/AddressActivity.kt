package org.caojun.addressmap.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.layout_address.*
import org.caojun.addressmap.R
import org.caojun.addressmap.room.Site
import org.caojun.addressmap.room.SiteDatabase
import org.caojun.areapicker.AreaPicker
import org.caojun.areapicker.OnPickerClickListener
import org.caojun.areapicker.PickerData
import org.jetbrains.anko.doAsync

class AddressActivity : AppCompatActivity() {

    companion object {
        const val Key_Site = "Key_Site"
    }

    private var site: Site? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_address)

        setSupportActionBar(toolbar)

        AreaPicker.init(this, btnArea, object : OnPickerClickListener {
            override fun onPickerClick(pickerData: PickerData) {
                btnArea.text = pickerData.selectText
                AreaPicker.dismiss()
            }

            override fun onPickerConfirmClick(pickerData: PickerData) {
                btnArea.text = pickerData.selectText
                AreaPicker.dismiss()
            }
        })

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

            if (isNew) {
                SiteDatabase.getDatabase(this@AddressActivity).getSiteDao().insert(site!!)
            } else {
                SiteDatabase.getDatabase(this@AddressActivity).getSiteDao().update(site!!)
            }
            finish()
        }
    }
}