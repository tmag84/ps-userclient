package isel.ps.ps_userclient.models

import android.os.Parcel
import android.os.Parcelable

class ServiceEvent(
        val service_id: Int,
        val service_type: Int,
        val service_name: String,
        val service_location: String,
        val id: Int,
        val text: String,
        val creation_date: Long,
        val event_begin: Long,
        val event_end: Long
) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<ServiceEvent> {
            override fun createFromParcel(source: Parcel) = ServiceEvent(source)
            override fun newArray(size: Int): Array<ServiceEvent?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            service_id = source.readInt(),
            service_type = source.readInt(),
            service_name = source.readString(),
            service_location = source.readString(),
            id = source.readInt(),
            text = source.readString(),
            creation_date = source.readLong(),
            event_begin = source.readLong(),
            event_end = source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeInt(service_id)
            writeInt(service_type)
            writeString(service_name)
            writeString(service_location)
            writeInt(id)
            writeString(text)
            writeLong(creation_date)
            writeLong(event_begin)
            writeLong(event_end)
        }
    }
}