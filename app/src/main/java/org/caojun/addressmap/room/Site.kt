package org.caojun.addressmap.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * 地址信息
 */
@Entity
class Site : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


    var name: String = ""//联系人
    var mobile: String = ""//手机号
    var area: String = ""//所在地区（省市区）
    var address: String = ""//详细地址
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeString(mobile)
        dest.writeString(area)
        dest.writeString(address)
        dest.writeDouble(latitude)
        dest.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor()

    constructor(_in: Parcel) : this() {
        id = _in.readInt()
        name = _in.readString()
        mobile = _in.readString()
        area = _in.readString()
        address = _in.readString()
        latitude = _in.readDouble()
        longitude = _in.readDouble()
    }

    companion object CREATOR : Parcelable.Creator<Site> {
        override fun createFromParcel(_in: Parcel): Site {
            return Site(_in)
        }

        override fun newArray(size: Int): Array<Site?> {
            return arrayOfNulls(size)
        }
    }
}