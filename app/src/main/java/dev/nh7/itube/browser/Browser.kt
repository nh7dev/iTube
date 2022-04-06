package dev.nh7.itube.browser

import android.content.Context
import android.net.Uri
import android.webkit.WebView
import dev.nh7.itube.download.YoutubeDownloadInfo
import dev.nh7.itube.utils.LOG

class Browser(context: Context, val onUpdateDownloadInfo: (YoutubeDownloadInfo?) -> Unit) :
    WebView(context) {

    var videoUrl: Uri? = null
    var videoTitle: String? = null
    var downloadUrl: Uri? = null

    init {
        LOG("init Browser")

        val browser = this

        browser.webViewClient = BrowserClient()
        browser.webChromeClient = BrowserClient.Chrome()

        browser.settings.javaScriptEnabled = true

        browser.loadUrl("https://youtube.com")
    }

    fun updateDownloadInfo() {
        val downloadInfo = if (videoUrl != null && videoTitle != null && downloadUrl != null)
            YoutubeDownloadInfo(videoUrl!!, videoTitle!!, downloadUrl!!) else null
        onUpdateDownloadInfo(downloadInfo)
    }

    fun getHtml(html: String, callback: (html: String) -> Unit) {
        LOG("the html request is $html")
        evaluateJavascript(html) { returnValue ->
            callback(returnValue)
            LOG("the html response is $returnValue")
        }
    }
}