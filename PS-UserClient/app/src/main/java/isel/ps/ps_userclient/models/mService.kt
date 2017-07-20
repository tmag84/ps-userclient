package isel.ps.ps_userclient.models

import android.os.Parcel
import android.os.Parcelable

data class mService(
        val id : Int,
        val name: String,
        val description: String,
        val provider_email: String,
        val contact_number: Int,
        val contact_name: String,
        val contact_location: String,
        val service_type: Int,
        var n_subscribers: Int,
        val avg_rank: Double,
        var subscribed: Boolean,
        val service_events: List<Event>,
        val service_notices: List<Notice>,
        val service_rankings: List<Ranking>
) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<mService> {
            override fun createFromParcel(source: Parcel) = mService(source)
            override fun newArray(size: Int): Array<mService?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            id = source.readInt(),
            name = source.readString(),
            description = source.readString(),
            provider_email = source.readString(),
            contact_number = source.readInt(),
            contact_name = source.readString(),
            contact_location = source.readString(),
            service_type = source.readInt(),
            n_subscribers = source.readInt(),
            avg_rank = source.readDouble(),
            subscribed = source.readInt()==1,
            service_events = mutableListOf<Event>().apply { source.readTypedList(this, Event.CREATOR) },
            service_notices = mutableListOf<Notice>().apply { source.readTypedList(this, Notice.CREATOR) },
            service_rankings = mutableListOf<Ranking>().apply { source.readTypedList(this, Ranking.CREATOR) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeInt(id)
            writeString(name)
            writeString(description)
            writeString(provider_email)
            writeInt(contact_number)
            writeString(contact_name)
            writeString(contact_location)
            writeInt(service_type)
            writeInt(n_subscribers)
            writeDouble(avg_rank)
            writeInt(if(subscribed) 1 else 0)
            writeTypedList(service_events)
            writeTypedList(service_notices)
            writeTypedList(service_rankings)
        }
    }
}