package dev.nh7.itube.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.nh7.itube.R

@Composable
fun DownloadDialogComponent(
    fileName: String,
    progressBytes: Long,
    sizeBytes: Long,
    onClickCancel: () -> Unit
) {

    val percent = if (sizeBytes > 0) (progressBytes * 100 / sizeBytes).toFloat() / 100 else 0f
    val progressMB = "%.1f".format(progressBytes / 1000000f)
    val sizeMB = "%.1f".format(sizeBytes / 1000000f)

    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.icon_download),
                contentDescription = "Download"
            )
        },
        title = {
            Text("Downloading")
        },
        text = {
            DialogBody(
                fileName = fileName,
                progressPercent = percent,
                progressMB = progressMB,
                sizeMB = sizeMB
            )
        },
        confirmButton = {
            Button(onClick = onClickCancel) {
                Text(text = "Cancel Download")
            }
        },
        onDismissRequest = {}
    )
}

@Composable
private fun DialogBody(
    fileName: String,
    progressPercent: Float,
    progressMB: String,
    sizeMB: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = fileName, maxLines = 1)
        LinearProgressIndicator(
            progress = progressPercent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = "$progressMB MB")
            Text(text = "$sizeMB MB")
        }
        /*
        Box(
            modifier = Modifier
                //.weight(1f)
                .padding(8.dp)
                .border(width = 2.dp, color = Color.Black)
        ) {
            Image(
                painter = painterResource(id = R.drawable.wallpaper),
                contentDescription = "Wallpaper"
            )
        }
        */
    }
}