package dev.nh7.itube.download

import dev.nh7.itube.song.Song
import dev.nh7.itube.song.SongManager
import dev.nh7.itube.utils.LOG
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.FileChannel


class YoutubeDownload(downloadInfo: YoutubeDownloadInfo) {

    private val downloadUrl = downloadInfo.downloadUrl.toString()

    /*
    private val artistAndTitle = downloadInfo.videoTitle.split(" - ".toRegex(), 2)
    private val artist = if (artistAndTitle.size == 2) artistAndTitle[0] else "Unknown Artist"
    private val title = if (artistAndTitle.size == 2) artistAndTitle[1] else artistAndTitle[0]
     */

    val fileName = downloadInfo.videoTitle.plus(".mp3") //actually .webm

    private val range = downloadInfo.downloadUrl.getQueryParameter("range")!!
    val contentLength = downloadInfo.downloadUrl.getQueryParameter("clen")!!.toLong()

    private var cancel = false

    fun cancel() {
        cancel = true
    }

    fun start(
        songManager: SongManager,
        buffer: Long,
        onUpdateProgress: (progress: Long) -> Unit
    ): Song? {

        val startTime = System.currentTimeMillis()
        LOG("STARTED DOWNLOAD $fileName")

        onUpdateProgress(0L)

        val song = songManager.createFile(fileName) ?: return null
        val outputStream = songManager.openFile(song) ?: return null

        outputStream.use { output ->

            val outChannel = output.channel

            var start = 0L
            while (start < contentLength) {

                if (cancel) {
                    cancel = false
                    //todo delete file
                    LOG("CANCELED DOWNLOAD")
                    return null
                }

                val end = start + buffer - 1L
                writePartToFile(outChannel, start, end)
                start += buffer

                onUpdateProgress(start)
            }

        }

        val time = System.currentTimeMillis() - startTime
        LOG("ENDED DOWNLOAD ($time ms)")

        return song
    }

    private fun writePartToFile(outChannel: FileChannel, startRange: Long, endRange: Long) {
        val url = downloadUrl.replace(range, "$startRange-$endRange")
        val inputStream = URL(url).openStream()
        inputStream.use { input ->
            val inChannel = Channels.newChannel(input)
            outChannel.transferFrom(inChannel, startRange, Long.MAX_VALUE)
        }
    }
}