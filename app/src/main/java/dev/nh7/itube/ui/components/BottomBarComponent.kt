package dev.nh7.itube.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import dev.nh7.itube.utils.Screen


@Composable
fun BottomBarComponent(
    selectedScreen: Screen,
    onOpenScreen: (screen: Screen) -> Unit
) {

    NavigationBar {

        Screen.values().forEach { screen ->
            if (screen.showInNavigationBar) {

                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = screen.displayName
                        )
                    },
                    label = { Text(text = screen.displayName) },
                    selected = selectedScreen == screen,
                    onClick = { onOpenScreen(screen) }
                )

            }
        }

    }
}