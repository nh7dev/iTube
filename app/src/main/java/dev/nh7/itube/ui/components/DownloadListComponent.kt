package dev.nh7.itube.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.nh7.itube.R
import dev.nh7.itube.song.Song


@Composable
fun DownloadListComponent(
    songs: List<Song>,
    onClickDownload: (song: Song) -> Unit,
    onRenameDownload: (song: Song, newFileName: String) -> Unit,
    onDeleteDownload: (song: Song) -> Unit
) {

    val editSong = remember { mutableStateOf(null as Song?) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(songs) { song ->
            DownloadListElementComponent(
                song = song,
                onClickSong = onClickDownload,
                onLongClickSong = { editSong.value = song })
            Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
        }
    }

    val song = editSong.value
    if (song != null) {
        EditDownloadDialog(
            song = song,
            onRenameSong = onRenameDownload,
            onDeleteSong = onDeleteDownload,
            onDismiss = { editSong.value = null }
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DownloadListElementComponent(
    song: Song,
    onClickSong: (song: Song) -> Unit,
    onLongClickSong: (song: Song) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClickSong(song) },
                onLongClick = { onLongClickSong(song) }),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = R.drawable.icon_music),
            //colorFilter = ColorFilter.tint(color = Color.Black), //todo improve
            contentDescription = "Play",
            modifier = Modifier
                .size(56.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray)
        )

        Text(
            text = song.fileName, maxLines = 2, modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )

    }
}

@Composable
private fun EditDownloadDialog(
    song: Song,
    onRenameSong: (song: Song, newFileName: String) -> Unit,
    onDeleteSong: (song: Song) -> Unit,
    onDismiss: () -> Unit
) {

    val newFileName = remember { mutableStateOf(song.fileName) }

    AlertDialog(
        icon = {
            Icon(painter = painterResource(id = R.drawable.icon_edit), contentDescription = "Edit")
        },
        title = {
            Text("Edit Download")
        },
        text = {
            TextField(
                value = newFileName.value,
                onValueChange = { value -> newFileName.value = value },
                label = { Text(text = "File Name") },
                singleLine = true,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        },
        dismissButton = {
            Button(onClick = {
                onDeleteSong(song)
                onDismiss()
            }) {
                Text(text = "Delete File")
            }
        },
        confirmButton = {
            Button(onClick = {
                onRenameSong(song, newFileName.value)
                onDismiss()
            }) {
                Text(text = "Save File")
            }
        },
        onDismissRequest = onDismiss
    )
}