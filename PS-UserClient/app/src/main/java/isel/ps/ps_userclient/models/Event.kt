package isel.ps.ps_userclient.models

import android.os.Parcel
import android.os.Parcelable

data class Event(
        val service_id: Int,
        val id: Int,
        val text: String,
        val creation_date: Long,
        val event_begin: Long,
        val event_end: Long
): Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<Event> {
            override fun createFromParcel(source: Parcel) = Event(source)
            override fun newArray(size: Int): Array<Event?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            service_id = source.readInt(),
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
            writeInt(id)
            writeString(text)
            writeLong(creation_date)
            writeLong(event_begin)
            writeLong(event_end)
        }
    }
}

