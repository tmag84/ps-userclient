package isel.ps.ps_userclient.models.parcelables

import android.os.Parcel
import android.os.Parcelable

data class Ranking(
        val user_email: String,
        val service_id : Int,
        val value: Int,
        val text: String,
        val creation_date: String
) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<Ranking> {
            override fun createFromParcel(source: Parcel) = Ranking(source)
            override fun newArray(size: Int): Array<Ranking?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            user_email = source.readString(),
            service_id = source.readInt(),
            value = source.readInt(),
            text = source.readString(),
            creation_date = source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeString(user_email)
            writeInt(service_id)
            writeInt(value)
            writeString(text)
            writeString(creation_date)
        }
    }
}