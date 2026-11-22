package co.edu.uts.serviciosestudiantiles.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile
import co.edu.uts.serviciosestudiantiles.ui.components.CurrencyUtils
import co.edu.uts.serviciosestudiantiles.ui.navigation.UtsScreen
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsAmberAccent
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenDark
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenPrimary
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsServiciosEstudiantilesTheme
import co.edu.uts.serviciosestudiantiles.viewmodel.dashboard.DashboardUiState
import co.edu.uts.serviciosestudiantiles.viewmodel.dashboard.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateTo: (UtsScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardContent(
        state = state,
        onNavigateTo = onNavigateTo,
        modifier = modifier
    )
}

@Composable
fun DashboardContent(
    state: DashboardUiState,
    onNavigateTo: (UtsScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner de Bienvenida Institucional
        ElevatedCard(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = UtsGreenPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(UtsGreenDark, UtsGreenPrimary)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "PORTAL ESTUDIANTIL UTS",
                                style = MaterialTheme.typography.labelLarge,
                                color = UtsAmberAccent,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (state.isProfileActive) {
                                    "¡Hola, ${state.studentProfile?.fullName}!"
                                } else {
                                    "Bienvenido a Servicios UTS"
                                },
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            if (state.isProfileActive && state.studentProfile?.programName?.isNotBlank() == true) {
                                Text(
                                    text = "${state.studentProfile.programName} • Sede ${state.studentProfile.campus.ifBlank { "Bucaramanga" }}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = UtsAmberAccent,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (state.isProfileActive && state.studentProfile != null) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.15f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = state.studentProfile.institutionalEmail,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Registra tus datos en el Perfil Académico oficial para habilitar la liquidación de matrícula y derechos pecuniarios.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // Resumen Financiero Rápido
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total Acumulado Liquidado",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = CurrencyUtils.formatCop(state.totalAccumulated),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = UtsGreenPrimary
                    )
                    Text(
                        text = "${state.liquidationsCount} concepto(s) registrado(s)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(UtsGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Text(
            text = "Módulos y Servicios del Sistema",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Tarjeta Módulo 1: Perfil
        DashboardModuleCard(
            title = "Módulo 1: Perfil Académico Oficial",
            subtitle = if (state.isProfileActive) {
                "Código: ${state.studentProfile?.studentCode} • ${state.studentProfile?.faculty?.displayName}"
            } else {
                "Registro pendiente (Requerido para liquidar)"
            },
            icon = Icons.Default.Person,
            badgeText = if (state.isProfileActive) "Completado" else "Pendiente",
            badgeColor = if (state.isProfileActive) UtsGreenPrimary else UtsAmberAccent,
            badgeIcon = if (state.isProfileActive) Icons.Default.CheckCircle else Icons.Default.Warning,
            onClick = { onNavigateTo(UtsScreen.PROFILE) }
        )

        // Tarjeta Módulo Administrativo: Estudiantes Registrados
        DashboardModuleCard(
            title = "Estudiantes Registrados",
            subtitle = "Consulta general, búsqueda por documento o código, edición y gestión institucional",
            icon = Icons.Default.School,
            badgeText = if (state.registeredStudentsCount > 0) "${state.registeredStudentsCount} Registrados" else "Directorio UTS",
            badgeColor = UtsGreenPrimary,
            badgeIcon = Icons.Default.CheckCircle,
            onClick = { onNavigateTo(UtsScreen.STUDENTS_LIST) }
        )

        // Tarjeta Oferta Académica y Servicios UTS
        DashboardModuleCard(
            title = "Oferta Académica, Tarifas y Servicios",
            subtitle = "Catálogo oficial, códigos SNIES, tarifas 2026/2027, educación continua y bienestar",
            icon = Icons.Default.MenuBook,
            badgeText = "Verificado UTS",
            badgeColor = UtsGreenPrimary,
            badgeIcon = Icons.Default.CheckCircle,
            onClick = { onNavigateTo(UtsScreen.ACADEMIC_OFFER) }
        )

        // Tarjeta Módulo 2: Liquidación
        DashboardModuleCard(
            title = "Módulo 2: Liquidación y Tarifas Oficiales",
            subtitle = if (state.isProfileActive) {
                "Habilitado • Vigencias 2026/2027, Beca Excelencia UTS y Gratuidad"
            } else {
                "Bloqueado • Requiere registro previo de perfil académico"
            },
            icon = Icons.Default.Calculate,
            badgeText = if (state.isProfileActive) "Habilitado" else "Bloqueado",
            badgeColor = if (state.isProfileActive) UtsGreenPrimary else MaterialTheme.colorScheme.error,
            badgeIcon = if (state.isProfileActive) Icons.Default.CheckCircle else Icons.Default.Warning,
            onClick = { onNavigateTo(UtsScreen.ENROLLMENT) }
        )

        // Tarjeta Módulo 3: Historial
        DashboardModuleCard(
            title = "Módulo 3: Historial y Consolidado",
            subtitle = "Consulta todas las liquidaciones registradas y el consolidado acumulado",
            icon = Icons.Default.HistoryEdu,
            badgeText = "${state.liquidationsCount} Registros",
            badgeColor = UtsGreenPrimary,
            badgeIcon = Icons.Default.CheckCircle,
            onClick = { onNavigateTo(UtsScreen.HISTORY) }
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun DashboardModuleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeText: String,
    badgeColor: Color,
    badgeIcon: ImageVector,
    onClick: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = badgeIcon,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelMedium,
                        color = badgeColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Ir al módulo",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    UtsServiciosEstudiantilesTheme {
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
            onNavigateTo = {}
        )
    }
}
