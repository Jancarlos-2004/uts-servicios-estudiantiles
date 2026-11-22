package co.edu.uts.serviciosestudiantiles.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import co.edu.uts.serviciosestudiantiles.ui.navigation.UtsScreen
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenContainer
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenPrimary
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsOnGreenContainer

@Composable
fun UtsBottomNavigation(
    currentScreen: UtsScreen,
    onNavigateTo: (UtsScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val bottomNavScreens = listOf(
        UtsScreen.DASHBOARD,
        UtsScreen.PROFILE,
        UtsScreen.ACADEMIC_OFFER,
        UtsScreen.ENROLLMENT,
        UtsScreen.HISTORY
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        bottomNavScreens.forEach { screen ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigateTo(screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                        contentDescription = screen.title
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = UtsOnGreenContainer,
                    selectedTextColor = UtsGreenPrimary,
                    indicatorColor = UtsGreenContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

