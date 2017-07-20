package isel.ps.ps_userclient.models

import android.os.Parcel
import android.os.Parcelable

class mUserEventWrapper(
        val total_events: Int,
        val events: List<ServiceEvent>,
        val _links: List<Links>?,
        val curr_page: Int
) : Parcelable {
    companion object {

        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<mUserEventWrapper> {
            override fun createFromParcel(source: Parcel) = mUserEventWrapper(source)
            override fun newArray(size: Int): Array<mUserEventWrapper?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            total_events = source.readInt(),
            events = mutableListOf<ServiceEvent>().apply { source.readTypedList(this, ServiceEvent.CREATOR) },
            _links = mutableListOf<Links>().apply {source.readTypedList(this, Links.CREATOR)},
            curr_page = source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeInt(total_events)
            writeTypedList(events)
            writeTypedList(_links)
            writeInt(curr_page)
        }
    }

}
