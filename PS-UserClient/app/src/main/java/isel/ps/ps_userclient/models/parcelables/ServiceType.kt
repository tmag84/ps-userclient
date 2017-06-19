package isel.ps.ps_userclient.models.parcelables

import android.os.Parcel
import android.os.Parcelable

data class ServiceType(
        val id_type : Int,
        val name: String
) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<ServiceType> {
            override fun createFromParcel(source: Parcel) = ServiceType(source)
            override fun newArray(size: Int): Array<ServiceType?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            id_type = source.readInt(),
            name = source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeInt(id_type)
            writeString(name)
        }
    }
}