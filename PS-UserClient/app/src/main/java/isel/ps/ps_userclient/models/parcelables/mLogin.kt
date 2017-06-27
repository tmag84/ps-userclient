package isel.ps.ps_userclient.models.parcelables

import android.os.Parcel
import android.os.Parcelable

data class mLogin(
        val access_token : String,
        val token_type : String,
        val expires_in : Int
) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = object : Parcelable.Creator<mLogin> {
            override fun createFromParcel(source: Parcel) = mLogin(source)
            override fun newArray(size: Int): Array<mLogin?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            access_token = source.readString(),
            token_type = source.readString(),
            expires_in = source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeString(access_token)
            writeString(token_type)
            writeInt(expires_in)
        }
    }
}
