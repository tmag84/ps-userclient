package isel.ps.ps_userclient.models.parcelables

import android.os.Parcel
import android.os.Parcelable

class mError(val type:String,
             val title:String,
             val detail:String,
             val instance:String,
             val status:Int
) : Parcelable {
    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<mError> {
            override fun createFromParcel(source: Parcel) = mError(source)
            override fun newArray(size: Int): Array<mError?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            type = source.readString(),
            title = source.readString(),
            detail = source.readString(),
            instance = source.readString(),
            status = source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeString(type)
            writeString(title)
            writeString(detail)
            writeString(instance)
            writeInt(status)
        }
    }
}