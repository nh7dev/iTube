package dev.nh7.itube.browser

import android.net.Uri
import android.webkit.*
import dev.nh7.itube.utils.LOG

class BrowserClient : WebViewClient() {

    /*
    https://www.youtube.com/playlist?list=$playlistId&playnext=$number&index=$number
    https://www.youtube.com/video?v=$videoId
     */

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {

        val url = request?.url
        val host = url?.host
        if (host?.endsWith("youtube.com") == false) {
            LOG("blocked url: $url")
            return true
        }

        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {

        val browser = view as Browser

        if (browser.downloadUrl != null) {
            return null
        }

        val url = request?.url
        if (url?.path == "/videoplayback" && url.getQueryParameter("mime") == "audio/webm") {

            browser.downloadUrl = url

            LOG("new downloadUrl: ${browser.downloadUrl}")

            browser.updateDownloadInfo()

            /*
            val connection = URL(url.toString()).openConnection()
            val headers = connection.headerFields
            LOG("its ${headers["Content-Type"]}")
            LOG("=================")
            for (entry in headers.entries) {
                LOG("${entry.key} - ${entry.value}")
            }
            */
        }

        return super.shouldInterceptRequest(view, request)
    }

    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {

        val browser = view as Browser

        browser.videoUrl = Uri.parse(url)
        browser.videoTitle = null
        browser.downloadUrl = null

        LOG("new videoUrl: ${browser.videoUrl}")

        browser.updateDownloadInfo()

        super.doUpdateVisitedHistory(view, url, isReload)
    }

    class Chrome : WebChromeClient() {

        override fun onReceivedTitle(view: WebView?, title: String?) {

            val browser = view as Browser

            val suffix = " - YouTube"
            browser.videoTitle =
                if (title?.endsWith(suffix) == true)
                    title.substring(0, title.length - suffix.length)
                else title

            LOG("new title: ${browser.videoTitle}")

            browser.updateDownloadInfo()

            super.onReceivedTitle(view, title)
        }
    }
}