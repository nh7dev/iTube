package dev.nh7.itube

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import dev.nh7.itube.browser.YoutubeDownload
import dev.nh7.itube.browser.YoutubeDownloadInfo
import dev.nh7.itube.utils.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    init {
        LOG("init MainViewModel")
    }

    // screens ==========================================

    private val currentScreen = mutableStateOf(Screen.SEARCH)

    fun openScreen(screen: Screen) {
        currentScreen.value = screen
    }

    fun getCurrentScreen(): Screen {
        return currentScreen.value
    }

    // download link ==========================================

    private val currentDownloadLink = mutableStateOf(null as YoutubeDownloadInfo?)

    fun updateDownloadLink(downloadLink: YoutubeDownloadInfo?) {
        currentDownloadLink.value = downloadLink
    }

    fun hasFoundDownloadLink(): Boolean {
        return currentDownloadLink.value != null
    }

    // download ==========================================

    private val currentDownload = mutableStateOf(null as YoutubeDownload?)
    private val currentProgress = mutableStateOf(0L)

    fun startDownload() {
        val downloadLink = currentDownloadLink.value ?: return

        val download = YoutubeDownload(downloadLink)

        currentDownload.value = download
        currentProgress.value = 0L

        Thread {

            val song = download.start(
                audioFileManager = audioFileManager,
                buffer = getPreference(Setting.DOWNLOAD_BUFFER_SIZE).toLong() * 1000L, //size in bytes
                onUpdateProgress = { progress ->
                    currentProgress.value = progress
                }
            )

            currentDownload.value = null

            if (song == null) {
                LOG("unsuccessful download")
                return@Thread
            }

            LOG("successful download")
            if (getPreference(Setting.PLAY_SONG_AFTER_DOWNLOAD).toBoolean()) {
                playDownload(song)
            }

            loadDownloadsList()

        }.start()
    }

    fun cancelDownload() {
        currentDownload.value?.cancel()
    }

    fun getCurrentDownload(): Triple<String, Long, Long>? {
        val download = currentDownload.value ?: return null
        return Triple(
            download.fileName,
            currentProgress.value,
            download.contentLength
        )
    }

    // download list ==========================================

    private val currentDownloadsList = mutableStateOf(emptyList<Song>())
    private var hasLoadedCurrentDownloadsList = false

    private val audioFileManager = AudioFileManager(getApplication())

    fun getDownloadsList(): List<Song> {
        if (!hasLoadedCurrentDownloadsList) {
            hasLoadedCurrentDownloadsList = true

            loadDownloadsList()
        }
        return currentDownloadsList.value
    }

    private fun loadDownloadsList() {
        Thread {
            val songs = audioFileManager.getFiles()
            currentDownloadsList.value = songs
        }.start()
    }

    fun deleteDownload(song: Song) {
        Thread {
            val rowsDeleted = audioFileManager.deleteFile(song)
            if (rowsDeleted == 1) {
                loadDownloadsList()
            }
        }.start()
    }

    fun renameDownload(song: Song, newFileName: String) {
        Thread {
            val rowsUpdated = audioFileManager.renameFile(song, newFileName)
            if (rowsUpdated == 1) {
                loadDownloadsList()
            }
        }.start()
    }

    fun playDownload(song: Song) {
        Thread {
            audioFileManager.playFile(song)
        }.start()
    }

    // preferences ==========================================

    private var currentPreferences = mutableStateMapOf<String, String>()

    private var hasLoadedPreferences = false

    fun loadPreferences() {
        if (!hasLoadedPreferences) {
            hasLoadedPreferences = true

            Thread {
                val context = getApplication() as MainApplication
                val preferences = context.getSharedPreferences("main", Context.MODE_PRIVATE)
                for (preference in preferences.all.entries) { //todo putall?
                    currentPreferences[preference.key] = preference.value.toString()
                }
            }.start()

        }
    }

    fun getPreferences(): Map<String, String> {
        return currentPreferences
    }

    private fun getPreference(setting: Setting): String {
        return currentPreferences[setting.name] ?: setting.default
    }

    fun setPreference(key: String, value: String) {
        currentPreferences[key] = value
        Thread {
            val context = getApplication() as MainApplication

            val preferences = context.getSharedPreferences("main", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString(key, value)
            editor.apply()

        }.start()
    }
}