package org.caojun.addressmap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.layout_address.*
import org.caojun.areapicker.AreaPicker
import org.caojun.areapicker.OnPickerClickListener
import org.caojun.areapicker.PickerData

class AddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_address)

        setSupportActionBar(toolbar)

        AreaPicker.init(this, etArea, object : OnPickerClickListener {
            override fun onPickerClick(pickerData: PickerData) {
                etArea.text = pickerData.selectText
                AreaPicker.dismiss()
            }

            override fun onPickerConfirmClick(pickerData: PickerData) {
                etArea.text = pickerData.selectText
                AreaPicker.dismiss()
            }
        })
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
        finish()
    }
}