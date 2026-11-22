package co.edu.uts.serviciosestudiantiles.viewmodel.dashboard

import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile

/**
 * Estado inmutable para la pantalla principal o Dashboard.
 */
data class DashboardUiState(
    val studentProfile: StudentProfile? = null,
    val totalAccumulated: Double = 0.0,
    val liquidationsCount: Int = 0,
    val registeredStudentsCount: Int = 0,
    val isProfileActive: Boolean = false,
    val isLoading: Boolean = true
)

