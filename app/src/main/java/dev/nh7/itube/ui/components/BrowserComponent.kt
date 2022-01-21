package dev.nh7.itube.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import dev.nh7.itube.MainApplication
import dev.nh7.itube.browser.Browser
import dev.nh7.itube.browser.YoutubeDownloadInfo


@Composable
fun BrowserComponent(onUpdateDownloadInfo: (YoutubeDownloadInfo?) -> Unit) {

    val application = LocalContext.current.applicationContext as MainApplication

    //Box(modifier = Modifier.fillMaxSize()) {

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            if (application.browser == null) {
                application.browser = Browser(context, onUpdateDownloadInfo)
            }
            application.browser!!
        }
    )


    /*SmallFloatingActionButton(
        modifier = Modifier
            .padding(16.dp)
            .align(Alignment.BottomStart),
        onClick = {
            if (application.browser?.canGoBack() == true) {
                application.browser?.goBack()
            }
        }) {
        Icon(
            painter = painterResource(id = R.drawable.icon_back),
            contentDescription = "Back"
        )
    }

}*/
}