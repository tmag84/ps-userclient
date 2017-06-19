package isel.ps.ps_userclient.models.parcelables

import android.os.Parcel
import android.os.Parcelable

data class mServiceWrapper(
        val services : List<mService>,
        val list_service_types: List<ServiceType>
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
            list_service_types = mutableListOf<ServiceType>().apply { source.readTypedList(this, ServiceType.CREATOR) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeTypedList(services)
            writeTypedList(list_service_types)
        }
    }
}
