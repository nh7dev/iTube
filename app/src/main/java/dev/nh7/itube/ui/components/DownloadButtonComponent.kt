package dev.nh7.itube.ui.components

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import dev.nh7.itube.R

@Composable
fun DownloadButtonComponent(
    onClickDownload: () -> Unit
) {
    ExtendedFloatingActionButton(
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.icon_download),
                contentDescription = "Download"
            )
        },
        text = { Text(text = "Download") },
        onClick = onClickDownload
    )
}