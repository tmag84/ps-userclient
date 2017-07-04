package isel.ps.ps_userclient.models.parcelables

import android.os.Parcel
import android.os.Parcelable

class ServiceEvent(
        val service_id: Int,
        val service_type: Int,
        val service_name: String,
        val id: Int,
        val text: String,
        val creation_date: String,
        val event_date: String
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
            id = source.readInt(),
            text = source.readString(),
            creation_date = source.readString(),
            event_date = source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeInt(service_id)
            writeInt(service_type)
            writeString(service_name)
            writeInt(id)
            writeString(text)
            writeString(creation_date)
            writeString(event_date)
        }
    }
}