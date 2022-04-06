package dev.nh7.itube.song

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

class Song(
    val id: Long,
    val fileName: String,
    val duration: Int?,
    val size: Int?
) {

    fun getContentUri(): Uri {
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
    }

}