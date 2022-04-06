package dev.nh7.itube.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import dev.nh7.itube.utils.Screen


@Composable
fun TopBarComponent(
    selectedScreen: Screen,
    onOpenScreen: (screen: Screen) -> Unit
) {

    SmallTopAppBar(

        title = {
            Text(text = selectedScreen.displayName)
        },

        actions = {
            Screen.values().forEach { screen ->
                if (!screen.showInNavigationBar) {

                    IconButton(onClick = { onOpenScreen(screen) }) {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = screen.displayName
                        )
                    }

                }
            }
        }

    )
}