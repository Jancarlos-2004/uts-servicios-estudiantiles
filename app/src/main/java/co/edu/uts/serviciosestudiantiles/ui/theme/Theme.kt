package co.edu.uts.serviciosestudiantiles.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = UtsGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = UtsGreenContainer,
    onPrimaryContainer = UtsOnGreenContainer,
    secondary = UtsAmberDark,
    onSecondary = Color.White,
    secondaryContainer = UtsAmberContainer,
    onSecondaryContainer = UtsOnAmberContainer,
    tertiary = UtsGreenLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = OnSurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    error = ErrorRed,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF67DC87),
    onPrimary = Color(0xFF00391A),
    primaryContainer = UtsGreenPrimary,
    onPrimaryContainer = Color(0xFFD4EED7),
    secondary = UtsAmberAccent,
    onSecondary = Color(0xFF452B00),
    background = Color(0xFF111411),
    surface = Color(0xFF191C19),
    onBackground = Color(0xFFE2E3DF),
    onSurface = Color(0xFFE2E3DF)
)

@Composable
fun UtsServiciosEstudiantilesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

