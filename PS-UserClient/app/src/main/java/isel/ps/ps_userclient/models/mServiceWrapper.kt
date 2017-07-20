package isel.ps.ps_userclient.models

import android.os.Parcel
import android.os.Parcelable

data class mServiceWrapper(
        val services : List<mService>,
        val total_services: Int,
        val _links: List<Links>?,
        val curr_page: Int
) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<mServiceWrapper> {
            override fun createFromParcel(source: Parcel) = mServiceWrapper(source)
            override fun newArray(size: Int): Array<mServiceWrapper?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            services = mutableListOf<mService>().apply { source.readTypedList(this, mService.CREATOR) },
            total_services = source.readInt(),
            _links = mutableListOf<Links>().apply {source.readTypedList(this, Links.CREATOR)},
            curr_page = source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeTypedList(services)
            writeInt(total_services)
            writeTypedList(_links)
            writeInt(curr_page)
        }
    }
}
