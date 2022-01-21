package dev.nh7.itube.ui.theme

import androidx.compose.material.*
import androidx.compose.runtime.Composable


@Composable
fun MainTheme(
    darkTheme: Boolean = true/*isSystemInDarkTheme()*/,
    content: @Composable () -> Unit
) {

    val colors: Colors
    //val systemUIController = rememberSystemUiController()
    if (darkTheme) {
        colors = darkColors()

        //systemUIController.setSystemBarsColor(color = colors.surface)
        //systemUIController.setNavigationBarColor(color = colors.surface)
    } else {
        colors = lightColors()

        //systemUIController.setSystemBarsColor(color = colors.surface)
        //systemUIController.setNavigationBarColor(color = colors.primary)
    }

    MaterialTheme(
        colors = colors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}