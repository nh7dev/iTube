package dev.nh7.itube.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable


@Composable
fun MainTheme3(
    darkTheme: Boolean = true/*isSystemInDarkTheme()*/,
    content: @Composable () -> Unit
) {

    val colors: ColorScheme
    //val systemUIController = rememberSystemUiController()
    if (darkTheme) {
        colors = darkColorScheme()

        //systemUIController.setSystemBarsColor(color = colors.surface)
        //systemUIController.setNavigationBarColor(color = colors.surface)
    } else {
        colors = lightColorScheme()

        //systemUIController.setSystemBarsColor(color = colors.surface)
        //systemUIController.setNavigationBarColor(color = colors.primary)
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}