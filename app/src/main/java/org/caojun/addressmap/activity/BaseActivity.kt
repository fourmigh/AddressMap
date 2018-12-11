package org.caojun.addressmap.activity

import org.caojun.activity.BaseAppCompatActivity

open class BaseActivity : BaseAppCompatActivity() {

    companion object {
        const val RequestCode_Address = 1
        const val RequestCode_AddressList = 2
    }

    var isDataChanged = false
}