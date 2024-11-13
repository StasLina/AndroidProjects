package com.example.gson

import android.os.Parcel
import android.os.Parcelable

/*
data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String
)
{
    fun getURI(): String {
        return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}_z.jpg"
    }
}
*/

data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String
) : Parcelable {

    // Десерилезуем объект из parcel
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun describeContents(): Int {
        return  0;
    }

    // серилизуем объект Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(owner)
        parcel.writeString(secret)
        parcel.writeString(server)
        parcel.writeInt(farm)
        parcel.writeString(title)
    }

    // Требуется для серелизации
    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }

    fun getURI(): String {
        return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}_z.jpg"
    }
}

data class PhotoPage(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: String,
    val photo: List<Photo>
)

data class Wrapper(
    val photos: PhotoPage
)
