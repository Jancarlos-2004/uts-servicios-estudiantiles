package co.edu.uts.serviciosestudiantiles.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.HistoryEdu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Destinos de navegación principales de la aplicación.
 */
enum class UtsScreen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    DASHBOARD(
        route = "dashboard",
        title = "Inicio",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    PROFILE(
        route = "profile",
        title = "Perfil",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    ),
    ACADEMIC_OFFER(
        route = "academic_offer",
        title = "Oferta UTS",
        selectedIcon = Icons.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.MenuBook
    ),
    ENROLLMENT(
        route = "enrollment",
        title = "Liquidación",
        selectedIcon = Icons.Filled.Calculate,
        unselectedIcon = Icons.Outlined.Calculate
    ),
    HISTORY(
        route = "history",
        title = "Historial",
        selectedIcon = Icons.Filled.HistoryEdu,
        unselectedIcon = Icons.Outlined.HistoryEdu
    ),
    STUDENTS_LIST(
        route = "students_list",
        title = "Estudiantes",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
}
