package co.edu.uts.serviciosestudiantiles.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import co.edu.uts.serviciosestudiantiles.data.repository.ProfileRepository
import co.edu.uts.serviciosestudiantiles.data.repository.StudentRegistryRepository
import co.edu.uts.serviciosestudiantiles.data.repository.TuitionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel del Dashboard Principal del Portal de Autoservicio UTS.
 */
class DashboardViewModel(
    private val profileRepository: ProfileRepository,
    private val tuitionRepository: TuitionRepository,
    private val studentRegistryRepository: StudentRegistryRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            val studentCountFlow = studentRegistryRepository?.getStudentCountFlow() ?: flowOf(0)

            combine(
                profileRepository.getProfileFlow(),
                tuitionRepository.getAllLiquidationsFlow(),
                studentCountFlow
            ) { profile, liquidations, count ->
                DashboardUiState(
                    studentProfile = profile,
                    totalAccumulated = liquidations.sumOf { it.totalAmount },
                    liquidationsCount = liquidations.size,
                    registeredStudentsCount = count,
                    isProfileActive = profile != null && profile.isValid,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.update { newState }
            }
        }
    }

    companion object {
        fun provideFactory(
            profileRepository: ProfileRepository,
            tuitionRepository: TuitionRepository,
            studentRegistryRepository: StudentRegistryRepository? = null
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DashboardViewModel(profileRepository, tuitionRepository, studentRegistryRepository) as T
                }
            }
    }
}

