package dev.nh7.itube;

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import dev.nh7.itube.utils.LOG
import java.io.File
import java.io.FileOutputStream

class AudioFileManager(private val context: Context) {

    fun getFiles(): MutableList<Song> {
        val contentResolver = context.contentResolver

        val collection = getMusicCollection()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            //MediaStore.Audio.Media.TITLE,
            //MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
        )

        val selection = "${MediaStore.Audio.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("%/${MUSIC_DIRECTORY_PATH}/%")

        val sortOrder = null

        val query = contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        val list = mutableListOf<Song>()

        query?.use { cursor ->

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val fileNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            //val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            //val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val fileName = cursor.getString(fileNameColumn)
                //val title = cursor.getString(titleColumn)
                //val artist = cursor.getString(artistColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)
                list.add(Song(id, fileName, duration, size))
                LOG("- $id   |   $fileName   |   $duration   |   $size")
            }
        }

        return list
    }

    fun createFile(fileName: String): Song? {

        val contentResolver = context.contentResolver

        val collection = getMusicCollection()

        val songDetails = ContentValues().apply {
            //put(MediaStore.Audio.Media.TITLE, title)
            //put(MediaStore.Audio.Media.ARTIST, artist)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Audio.Media.RELATIVE_PATH, MUSIC_DIRECTORY_PATH)
            } else {
                val file = File(getMusicDirectory(), fileName)
                put(MediaStore.Audio.Media.DATA, file.absolutePath)
            }
        }

        val contentUri = contentResolver.insert(collection, songDetails) ?: return null
        val id = contentUri.lastPathSegment?.toLong() ?: return null
        return Song(id, fileName, null, null)
    }

    fun openFile(song: Song): FileOutputStream? {
        return context.contentResolver.openOutputStream(song.getContentUri()) as FileOutputStream?
    }

    fun playFile(song: Song) {
        val intent = Intent(Intent.ACTION_VIEW, song.getContentUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) //todo remove?
        context.startActivity(intent)
    }

    fun renameFile(song: Song, newFileName: String): Int {
        val contentResolver = context.contentResolver

        val songDetails = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, newFileName)
        }

        return contentResolver.update(
            song.getContentUri(),
            songDetails,
            null,
            null
        )
    }

    fun deleteFile(song: Song): Int {
        val contentResolver = context.contentResolver

        val selection = null
        val selectionArgs = null

        return contentResolver.delete(song.getContentUri(), selection, selectionArgs)
    }

    private fun getMusicCollection(): Uri {
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
        return collection
    }

    private val MUSIC_DIRECTORY_PATH = Environment.DIRECTORY_MUSIC + "/iTube"

    private fun getMusicDirectory(): File? {
        val musicDirectory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
            "iTube"
        )
        if (!musicDirectory.exists()) {
            val success = musicDirectory.mkdirs()
            if (!success) {
                LOG("ERROR CREATING MUSIC DIRECTORY ${musicDirectory.absolutePath}")
                return null
            }
        }
        return musicDirectory
    }
}
