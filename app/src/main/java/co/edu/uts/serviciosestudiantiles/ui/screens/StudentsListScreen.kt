package co.edu.uts.serviciosestudiantiles.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.edu.uts.serviciosestudiantiles.data.model.Campus
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.Modality
import co.edu.uts.serviciosestudiantiles.data.model.RegisteredStudent
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsAmberAccent
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenContainer
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenPrimary
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsOnGreenContainer
import co.edu.uts.serviciosestudiantiles.viewmodel.students.StudentsListUiState
import co.edu.uts.serviciosestudiantiles.viewmodel.students.StudentsListViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StudentsListScreen(
    viewModel: StudentsListViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.successMessage, state.errorMessage) {
        state.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::onOpenCreateDialog,
                containerColor = UtsGreenPrimary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Registrar Estudiante")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Registrar Estudiante", fontWeight = FontWeight.Bold)
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        StudentsListContent(
            state = state,
            onSearchQueryChanged = viewModel::onSearchQueryChanged,
            onFacultyFilterSelected = viewModel::onFacultyFilterSelected,
            onCampusFilterSelected = viewModel::onCampusFilterSelected,
            onSelectStudentForDetail = viewModel::onSelectStudentForDetail,
            onOpenEditDialog = viewModel::onOpenEditDialog,
            onConfirmDeleteStudent = viewModel::onConfirmDeleteStudent,
            onNavigateToProfile = onNavigateToProfile,
            modifier = Modifier.padding(innerPadding)
        )
    }

    // 1. Diálogo de Ficha Completa del Estudiante
    state.studentForDetail?.let { student ->
        StudentDetailDialog(
            student = student,
            onDismiss = { viewModel.onSelectStudentForDetail(null) },
            onEdit = {
                viewModel.onSelectStudentForDetail(null)
                viewModel.onOpenEditDialog(student)
            }
        )
    }

    // 2. Diálogo de Edición de Estudiante
    state.studentToEdit?.let { student ->
        StudentFormDialog(
            title = "Editar Estudiante",
            initialStudent = student,
            isEditing = true,
            onDismiss = viewModel::onCloseEditDialog,
            onSave = { updated -> viewModel.onUpdateStudent(updated) },
            viewModel = viewModel
        )
    }

    // 3. Diálogo de Registro de Nuevo Estudiante
    if (state.isCreateDialogOpen) {
        StudentFormDialog(
            title = "Nuevo Registro de Estudiante",
            initialStudent = null,
            isEditing = false,
            onDismiss = viewModel::onCloseCreateDialog,
            onSave = { newStudent -> viewModel.onCreateStudent(newStudent) },
            viewModel = viewModel
        )
    }

    // 4. Diálogo de Confirmación para Eliminar
    state.studentToDelete?.let { student ->
        AlertDialog(
            onDismissRequest = viewModel::onDismissDelete,
            icon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text(text = "¿Eliminar estudiante?", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Esta acción eliminará de forma permanente el registro institucional de ${student.fullName} (Código: ${student.studentCode}, Doc: ${student.documentNumber}). ¿Deseas continuar?"
                )
            },
            confirmButton = {
                Button(
                    onClick = viewModel::onExecuteDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onDismissDelete) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun StudentsListContent(
    state: StudentsListUiState,
    onSearchQueryChanged: (String) -> Unit,
    onFacultyFilterSelected: (Faculty?) -> Unit,
    onCampusFilterSelected: (Campus?) -> Unit,
    onSelectStudentForDetail: (RegisteredStudent) -> Unit,
    onOpenEditDialog: (RegisteredStudent) -> Unit,
    onConfirmDeleteStudent: (RegisteredStudent) -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 88.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Título de la pantalla
            Text(
                text = "Estudiantes Registrados",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = UtsGreenPrimary
            )
            Text(
                text = "Módulo oficial de consulta general, búsqueda, filtrado y gestión de la comunidad estudiantil de las UTS.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Banner del Total de Estudiantes Registrados
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = UtsGreenContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Total de Estudiantes Registrados",
                            style = MaterialTheme.typography.labelMedium,
                            color = UtsOnGreenContainer
                        )
                        Text(
                            text = "${state.totalCount} estudiantes en base de datos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = UtsGreenPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(UtsGreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Buscador por Nombre, Código o Documento
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchQueryChanged,
                label = { Text("Buscar por nombre, código o documento...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = UtsGreenPrimary)
                },
                trailingIcon = {
                    if (state.searchQuery.isNotBlank()) {
                        IconButton(onClick = { onSearchQueryChanged("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpiar búsqueda")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = UtsGreenPrimary,
                    focusedLabelColor = UtsGreenPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filtros rápidos por Facultad
            Text(
                text = "Filtrar por Facultad:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = state.facultyFilter == null,
                        onClick = { onFacultyFilterSelected(null) },
                        label = { Text("Todas las facultades", maxLines = 1) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = UtsGreenContainer,
                            selectedLabelColor = UtsOnGreenContainer
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = state.facultyFilter == Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                        onClick = { onFacultyFilterSelected(Faculty.CIENCIAS_NATURALES_E_INGENIERIAS) },
                        label = { Text("Ingenierías", maxLines = 1) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = UtsGreenContainer,
                            selectedLabelColor = UtsOnGreenContainer
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = state.facultyFilter == Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                        onClick = { onFacultyFilterSelected(Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES) },
                        label = { Text("Socioeconómicas", maxLines = 1) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = UtsGreenContainer,
                            selectedLabelColor = UtsOnGreenContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Filtros rápidos por Sede
            Text(
                text = "Filtrar por Sede Física:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                item {
                    FilterChip(
                        selected = state.campusFilter == null,
                        onClick = { onCampusFilterSelected(null) },
                        label = { Text("Todas las sedes", maxLines = 1) }
                    )
                }
                items(Campus.entries.toTypedArray()) { campus ->
                    FilterChip(
                        selected = state.campusFilter == campus,
                        onClick = { onCampusFilterSelected(campus) },
                        label = { Text(campus.cityName, maxLines = 1) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${state.filteredStudents.size} estudiante(s) encontrado(s)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (state.filteredStudents.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = if (state.allStudents.isEmpty()) "No hay estudiantes registrados aún." else "No se encontraron estudiantes con los filtros seleccionados.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (state.allStudents.isEmpty()) {
                            Text(
                                text = "Puedes registrar estudiantes desde tu Perfil Académico o usando el botón inferior.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Listado de Tarjetas de Estudiantes (Ordenados por fecha más reciente)
        items(state.filteredStudents, key = { it.id }) { student ->
            StudentItemCard(
                student = student,
                onViewDetail = { onSelectStudentForDetail(student) },
                onEdit = { onOpenEditDialog(student) },
                onDelete = { onConfirmDeleteStudent(student) }
            )
        }
    }
}

@Composable
fun StudentItemCard(
    student: RegisteredStudent,
    onViewDetail: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Fila Superior: Nombre y Estado del Estudiante
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(UtsGreenContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = student.fullName.take(1).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = UtsGreenPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        // 1. Nombre completo
                        Text(
                            text = student.fullName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // 8. Estado del estudiante
                StudentStatusBadge(status = student.status)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // 2. Código estudiantil y 3. Documento de identidad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Badge, contentDescription = null, tint = UtsGreenPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Código: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = student.studentCode, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = UtsGreenPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Doc: ", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = student.documentNumber, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
            }

            // 4. Facultad
            Text(
                text = "Facultad: ${student.faculty.displayName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 5. Programa académico
            Text(
                text = student.programName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = UtsGreenPrimary
            )

            // 6. Sede y Modalidad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Place, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Sede: ${student.campus}", style = MaterialTheme.typography.labelMedium)
                }

                Text(text = "Modalidad: ${student.modality}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            // 7. Fecha de registro
            Text(
                text = "Registrado el: ${formatDate(student.registeredAt)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // Barra de Acciones del Estudiante
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onViewDetail) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp), tint = UtsGreenPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Ficha", color = UtsGreenPrimary, fontWeight = FontWeight.Bold)
                }

                TextButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Editar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun StudentStatusBadge(status: String) {
    val (bgColor, textColor) = when (status) {
        RegisteredStudent.STATUS_ACTIVO -> UtsGreenContainer to UtsOnGreenContainer
        RegisteredStudent.STATUS_MATRICULADO -> Color(0xFFE0F7FA) to Color(0xFF006064)
        RegisteredStudent.STATUS_INACTIVO -> Color(0xFFFFEBEE) to Color(0xFFB71C1C)
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Text(
            text = status,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun StudentDetailDialog(
    student: RegisteredStudent,
    onDismiss: () -> Unit,
    onEdit: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ficha del Estudiante UTS",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = UtsGreenPrimary
                    )

                    StudentStatusBadge(status = student.status)
                }

                HorizontalDivider()

                DetailItem(label = "Nombre Completo", value = student.fullName)
                DetailItem(label = "Código Estudiantil", value = student.studentCode)
                DetailItem(label = "Documento de Identidad", value = student.documentNumber)
                DetailItem(label = "Facultad Oficial", value = student.faculty.displayName)
                DetailItem(label = "Programa Académico", value = student.programName)
                DetailItem(label = "Nivel de Formación", value = student.academicLevel)
                DetailItem(label = "Sede Confirmada", value = student.campus)
                DetailItem(label = "Modalidad de Estudio", value = student.modality)
                DetailItem(label = "Correo Institucional", value = student.institutionalEmail)
                DetailItem(label = "Fecha y Hora de Registro", value = formatDate(student.registeredAt))

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cerrar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onEdit,
                        colors = ButtonDefaults.buttonColors(containerColor = UtsGreenPrimary)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Editar Datos", color = Color.White)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFormDialog(
    title: String,
    initialStudent: RegisteredStudent?,
    isEditing: Boolean,
    onDismiss: () -> Unit,
    onSave: (RegisteredStudent) -> Unit,
    viewModel: StudentsListViewModel
) {
    var fullName by remember { mutableStateOf(initialStudent?.fullName ?: "") }
    var studentCode by remember { mutableStateOf(initialStudent?.studentCode ?: "") }
    var documentNumber by remember { mutableStateOf(initialStudent?.documentNumber ?: "") }
    var selectedFaculty by remember { mutableStateOf(initialStudent?.faculty ?: Faculty.CIENCIAS_NATURALES_E_INGENIERIAS) }

    val availablePrograms = viewModel.institutionalRepository.getProgramsByFaculty(selectedFaculty)
    var selectedProgram by remember(selectedFaculty) {
        val initialMatch = availablePrograms.firstOrNull { it.name.equals(initialStudent?.programName, ignoreCase = true) }
        mutableStateOf(initialMatch ?: availablePrograms.firstOrNull())
    }

    var selectedCampus by remember { mutableStateOf(Campus.fromCityName(initialStudent?.campus) ?: Campus.BUCARAMANGA) }
    var selectedModality by remember { mutableStateOf(Modality.fromDisplayName(initialStudent?.modality) ?: Modality.PRESENCIAL) }
    var selectedStatus by remember { mutableStateOf(initialStudent?.status ?: RegisteredStudent.STATUS_ACTIVO) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var codeError by remember { mutableStateOf<String?>(null) }
    var docError by remember { mutableStateOf<String?>(null) }

    var isFacultyExpanded by remember { mutableStateOf(false) }
    var isProgramExpanded by remember { mutableStateOf(false) }
    var isCampusExpanded by remember { mutableStateOf(false) }
    var isModalityExpanded by remember { mutableStateOf(false) }
    var isStatusExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = UtsGreenPrimary
                )

                HorizontalDivider()

                // Campo Nombre
                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        nameError = if (it.trim().length < 3) "Mínimo 3 caracteres" else null
                    },
                    label = { Text("Nombre Completo *") },
                    isError = nameError != null,
                    supportingText = { nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo Código
                OutlinedTextField(
                    value = studentCode,
                    onValueChange = {
                        val filtered = it.filter { ch -> ch.isLetterOrDigit() }
                        studentCode = filtered
                        codeError = if (filtered.isBlank()) "Código obligatorio" else null
                    },
                    label = { Text("Código Estudiantil *") },
                    isError = codeError != null,
                    supportingText = { codeError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo Documento
                OutlinedTextField(
                    value = documentNumber,
                    onValueChange = {
                        val filtered = it.filter { ch -> ch.isLetterOrDigit() }
                        documentNumber = filtered
                        docError = if (filtered.length < 5) "Mínimo 5 dígitos" else null
                    },
                    label = { Text("Documento de Identidad (C.C. / T.I.) *") },
                    isError = docError != null,
                    supportingText = { docError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Selector Facultad
                ExposedDropdownMenuBox(
                    expanded = isFacultyExpanded,
                    onExpandedChange = { isFacultyExpanded = !isFacultyExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedFaculty.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Facultad *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isFacultyExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = isFacultyExpanded,
                        onDismissRequest = { isFacultyExpanded = false }
                    ) {
                        Faculty.entries.forEach { fac ->
                            DropdownMenuItem(
                                text = { Text(fac.displayName) },
                                onClick = {
                                    selectedFaculty = fac
                                    isFacultyExpanded = false
                                }
                            )
                        }
                    }
                }

                // Selector Programa
                ExposedDropdownMenuBox(
                    expanded = isProgramExpanded,
                    onExpandedChange = { isProgramExpanded = !isProgramExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedProgram?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Programa Académico *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isProgramExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = isProgramExpanded,
                        onDismissRequest = { isProgramExpanded = false }
                    ) {
                        availablePrograms.forEach { prog ->
                            DropdownMenuItem(
                                text = { Text(prog.name) },
                                onClick = {
                                    selectedProgram = prog
                                    isProgramExpanded = false
                                }
                            )
                        }
                    }
                }

                // Selector Sede
                ExposedDropdownMenuBox(
                    expanded = isCampusExpanded,
                    onExpandedChange = { isCampusExpanded = !isCampusExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCampus.cityName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Sede *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCampusExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = isCampusExpanded,
                        onDismissRequest = { isCampusExpanded = false }
                    ) {
                        Campus.entries.forEach { camp ->
                            DropdownMenuItem(
                                text = { Text(camp.cityName) },
                                onClick = {
                                    selectedCampus = camp
                                    isCampusExpanded = false
                                }
                            )
                        }
                    }
                }

                // Selector Modalidad
                ExposedDropdownMenuBox(
                    expanded = isModalityExpanded,
                    onExpandedChange = { isModalityExpanded = !isModalityExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedModality.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Modalidad *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isModalityExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = isModalityExpanded,
                        onDismissRequest = { isModalityExpanded = false }
                    ) {
                        Modality.entries.forEach { mod ->
                            DropdownMenuItem(
                                text = { Text(mod.displayName) },
                                onClick = {
                                    selectedModality = mod
                                    isModalityExpanded = false
                                }
                            )
                        }
                    }
                }

                // Selector Estado
                ExposedDropdownMenuBox(
                    expanded = isStatusExpanded,
                    onExpandedChange = { isStatusExpanded = !isStatusExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedStatus,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado del Estudiante *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isStatusExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = isStatusExpanded,
                        onDismissRequest = { isStatusExpanded = false }
                    ) {
                        RegisteredStudent.ALL_STATUSES.forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st) },
                                onClick = {
                                    selectedStatus = st
                                    isStatusExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val nameTrim = fullName.trim()
                            val codeTrim = studentCode.trim()
                            val docTrim = documentNumber.trim()

                            var valid = true
                            if (nameTrim.length < 3) {
                                nameError = "El nombre es obligatorio"
                                valid = false
                            }
                            if (codeTrim.isBlank()) {
                                codeError = "El código es obligatorio"
                                valid = false
                            }
                            if (docTrim.length < 5) {
                                docError = "Documento de mínimo 5 dígitos"
                                valid = false
                            }

                            if (valid && selectedProgram != null) {
                                val email = generateInstitutionalEmail(nameTrim, codeTrim)
                                val studentToSave = RegisteredStudent(
                                    id = initialStudent?.id ?: 0L,
                                    fullName = nameTrim,
                                    studentCode = codeTrim,
                                    documentNumber = docTrim,
                                    faculty = selectedFaculty,
                                    programName = selectedProgram!!.name,
                                    academicLevel = selectedProgram!!.level.displayName,
                                    campus = selectedCampus.cityName,
                                    modality = selectedModality.displayName,
                                    institutionalEmail = email,
                                    status = selectedStatus,
                                    registeredAt = initialStudent?.registeredAt ?: System.currentTimeMillis()
                                )
                                onSave(studentToSave)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = UtsGreenPrimary)
                    ) {
                        Text(if (isEditing) "Actualizar" else "Guardar", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CO"))
    return sdf.format(Date(timestamp))
}

private fun generateInstitutionalEmail(name: String, code: String): String {
    val cleanName = java.text.Normalizer.normalize(name.trim(), java.text.Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .lowercase()
        .replace("\\s+".toRegex(), ".")
        .replace("[^a-z0-9.]".toRegex(), "")
        .trim('.')

    val cleanCode = code.trim().lowercase().replace("[^a-z0-9]".toRegex(), "")
    return if (cleanName.isNotBlank() && cleanCode.isNotBlank()) {
        "$cleanName.$cleanCode@uts.edu.co"
    } else {
        "estudiante@uts.edu.co"
    }
}
