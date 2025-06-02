


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            background = Color.Black,
            surface = Color.DarkGray,
            primary = Color(0xFF2962FF),
            onPrimary = Color.White,
            onBackground = Color.White
        )
    } else {
        lightColorScheme(
            background = Color.White,
            surface = Color.White,
            primary = Color(0xFF2962FF),
            onPrimary = Color.White,
            onBackground = Color.Black
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
