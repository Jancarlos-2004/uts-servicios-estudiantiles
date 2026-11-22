package co.edu.uts.serviciosestudiantiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile
import co.edu.uts.serviciosestudiantiles.ui.components.UtsBottomNavigation
import co.edu.uts.serviciosestudiantiles.ui.components.UtsTopAppBar
import co.edu.uts.serviciosestudiantiles.ui.navigation.UtsScreen
import co.edu.uts.serviciosestudiantiles.ui.screens.AcademicOfferScreen
import co.edu.uts.serviciosestudiantiles.ui.screens.DashboardContent
import co.edu.uts.serviciosestudiantiles.ui.screens.DashboardScreen
import co.edu.uts.serviciosestudiantiles.ui.screens.EnrollmentScreen
import co.edu.uts.serviciosestudiantiles.ui.screens.HistoryScreen
import co.edu.uts.serviciosestudiantiles.ui.screens.ProfileScreen
import co.edu.uts.serviciosestudiantiles.ui.screens.StudentsListScreen
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsServiciosEstudiantilesTheme
import co.edu.uts.serviciosestudiantiles.viewmodel.dashboard.DashboardUiState
import co.edu.uts.serviciosestudiantiles.viewmodel.dashboard.DashboardViewModel
import co.edu.uts.serviciosestudiantiles.viewmodel.enrollment.EnrollmentViewModel
import co.edu.uts.serviciosestudiantiles.viewmodel.history.HistoryViewModel
import co.edu.uts.serviciosestudiantiles.viewmodel.institutional.InstitutionalViewModel
import co.edu.uts.serviciosestudiantiles.viewmodel.profile.ProfileViewModel
import co.edu.uts.serviciosestudiantiles.viewmodel.students.StudentsListViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as UtsApplication).container

        setContent {
            UtsServiciosEstudiantilesTheme {
                UtsMainApp(appContainer = appContainer)
            }
        }
    }
}

@Composable
fun UtsMainApp(
    appContainer: co.edu.uts.serviciosestudiantiles.data.repository.AppContainer,
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf(UtsScreen.DASHBOARD) }

    // Inicialización de ViewModels desacoplados utilizando sus factorías con AppContainer
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModel.provideFactory(
            repository = appContainer.profileRepository,
            institutionalRepository = appContainer.institutionalRepository,
            studentRegistryRepository = appContainer.studentRegistryRepository
        )
    )

    val enrollmentViewModel: EnrollmentViewModel = viewModel(
        factory = EnrollmentViewModel.provideFactory(
            profileRepository = appContainer.profileRepository,
            tuitionRepository = appContainer.tuitionRepository,
            institutionalRepository = appContainer.institutionalRepository
        )
    )

    val historyViewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModel.provideFactory(appContainer.tuitionRepository)
    )

    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.provideFactory(
            profileRepository = appContainer.profileRepository,
            tuitionRepository = appContainer.tuitionRepository,
            studentRegistryRepository = appContainer.studentRegistryRepository
        )
    )

    val institutionalViewModel: InstitutionalViewModel = viewModel(
        factory = InstitutionalViewModel.provideFactory(appContainer.institutionalRepository)
    )

    val studentsListViewModel: StudentsListViewModel = viewModel(
        factory = StudentsListViewModel.provideFactory(
            repository = appContainer.studentRegistryRepository,
            institutionalRepository = appContainer.institutionalRepository
        )
    )

    Scaffold(
        topBar = {
            UtsTopAppBar(
                title = when (currentScreen) {
                    UtsScreen.DASHBOARD -> "Portal Estudiantil UTS"
                    UtsScreen.PROFILE -> "Perfil Académico Oficial"
                    UtsScreen.ACADEMIC_OFFER -> "Oferta y Servicios UTS"
                    UtsScreen.ENROLLMENT -> "Liquidación y Tarifas"
                    UtsScreen.HISTORY -> "Historial Financiero"
                    UtsScreen.STUDENTS_LIST -> "Estudiantes Registrados UTS"
                },
                onNavigateBack = if (currentScreen == UtsScreen.STUDENTS_LIST) {
                    { currentScreen = UtsScreen.DASHBOARD }
                } else null
            )
        },
        bottomBar = {
            UtsBottomNavigation(
                currentScreen = currentScreen,
                onNavigateTo = { screen -> currentScreen = screen }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)

        when (currentScreen) {
            UtsScreen.DASHBOARD -> {
                DashboardScreen(
                    viewModel = dashboardViewModel,
                    onNavigateTo = { screen -> currentScreen = screen },
                    modifier = screenModifier
                )
            }

            UtsScreen.PROFILE -> {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onNavigateToStudentsList = { currentScreen = UtsScreen.STUDENTS_LIST },
                    modifier = screenModifier
                )
            }

            UtsScreen.ACADEMIC_OFFER -> {
                AcademicOfferScreen(
                    viewModel = institutionalViewModel,
                    modifier = screenModifier
                )
            }

            UtsScreen.ENROLLMENT -> {
                EnrollmentScreen(
                    viewModel = enrollmentViewModel,
                    onNavigateToProfile = { currentScreen = UtsScreen.PROFILE },
                    modifier = screenModifier
                )
            }

            UtsScreen.HISTORY -> {
                HistoryScreen(
                    viewModel = historyViewModel,
                    onNavigateToEnrollment = { currentScreen = UtsScreen.ENROLLMENT },
                    modifier = screenModifier
                )
            }

            UtsScreen.STUDENTS_LIST -> {
                StudentsListScreen(
                    viewModel = studentsListViewModel,
                    onNavigateBack = { currentScreen = UtsScreen.DASHBOARD },
                    onNavigateToProfile = { currentScreen = UtsScreen.PROFILE },
                    modifier = screenModifier
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UtsAppStructurePreview() {
    UtsServiciosEstudiantilesTheme {
        var currentScreen by remember { mutableStateOf(UtsScreen.DASHBOARD) }
        Scaffold(
            topBar = {
                UtsTopAppBar(
                    title = "Portal Estudiantil UTS"
                )
            },
            bottomBar = {
                UtsBottomNavigation(
                    currentScreen = currentScreen,
                    onNavigateTo = { currentScreen = it }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            DashboardContent(
                state = DashboardUiState(
                    studentProfile = StudentProfile(
                        fullName = "Carlos Andrés Pérez",
                        studentCode = "10987654",
                        faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                        institutionalEmail = "carlos.andres.perez.10987654@uts.edu.co",
                        programName = "Tecnología en Desarrollo de Sistemas Informáticos",
                        campus = "Bucaramanga"
                    ),
                    totalAccumulated = 2_836_466.0,
                    liquidationsCount = 1,
                    isProfileActive = true,
                    isLoading = false
                ),
                onNavigateTo = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
