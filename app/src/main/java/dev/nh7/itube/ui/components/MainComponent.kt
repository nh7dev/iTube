package dev.nh7.itube.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.nh7.itube.MainViewModel
import dev.nh7.itube.utils.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainComponent() {

    val viewModel: MainViewModel = viewModel()

    Scaffold(

        topBar = {
            TopBarComponent(
                selectedScreen = viewModel.getCurrentScreen(),
                onOpenScreen = { screen -> viewModel.openScreen(screen) }
            )
        },

        bottomBar = {
            BottomBarComponent(
                selectedScreen = viewModel.getCurrentScreen(),
                onOpenScreen = { screen -> viewModel.openScreen(screen) }
            )
        },

        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (viewModel.hasFoundDownloadLink() && viewModel.getCurrentScreen() == Screen.SEARCH) {
                DownloadButtonComponent(onClickDownload = {
                    viewModel.startDownload()
                })
            }
        }

    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {

            when (viewModel.getCurrentScreen()) {

                Screen.SEARCH -> {
                    BrowserComponent(onUpdateDownloadInfo = { downloadInfo ->
                        viewModel.updateDownloadLink(downloadLink = downloadInfo)
                    })
                }

                Screen.DOWNLOADS -> {

                    DownloadListComponent(
                        songs = viewModel.getDownloadsList(),
                        onClickDownload = { song ->
                            viewModel.playDownload(song = song)
                        },
                        onRenameDownload = { song, newFileName ->
                            viewModel.renameDownload(song = song, newFileName = newFileName)
                        },
                        onDeleteDownload = { song ->
                            viewModel.deleteDownload(song = song)
                        }
                    )
                }

                Screen.SETTINGS -> {

                    viewModel.loadPreferences()

                    SettingsComponent(
                        settings = viewModel.getPreferences(),
                        onSettingChange = { key, value ->
                            viewModel.setPreference(key, value)
                        }
                    )
                }

            }

            val currentDownload = viewModel.getCurrentDownload()
            if (currentDownload != null) {
                DownloadDialogComponent(
                    fileName = currentDownload.first,
                    progressBytes = currentDownload.second,
                    sizeBytes = currentDownload.third,
                    onClickCancel = {
                        viewModel.cancelDownload()
                    }
                )
            }
        }
    }
}