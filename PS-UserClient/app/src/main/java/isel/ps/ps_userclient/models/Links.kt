package isel.ps.ps_userclient.models

import android.os.Parcel
import android.os.Parcelable

class Links(
        val Href: String,
        val Rel: String

): Parcelable {
    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<Links> {
            override fun createFromParcel(source: Parcel) = Links(source)
            override fun newArray(size: Int): Array<Links?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            Href = source.readString(),
            Rel = source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeString(Href)
            writeString(Rel)
        }
    }
}
