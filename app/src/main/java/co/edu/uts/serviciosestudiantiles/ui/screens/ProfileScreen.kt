package co.edu.uts.serviciosestudiantiles.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.edu.uts.serviciosestudiantiles.data.model.AcademicProgram
import co.edu.uts.serviciosestudiantiles.data.model.Campus
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.Modality
import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenContainer
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenPrimary
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsOnGreenContainer
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsServiciosEstudiantilesTheme
import co.edu.uts.serviciosestudiantiles.viewmodel.profile.ProfileUiState
import co.edu.uts.serviciosestudiantiles.viewmodel.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToStudentsList: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileContent(
        state = state,
        onFullNameChanged = viewModel::onFullNameChanged,
        onStudentCodeChanged = viewModel::onStudentCodeChanged,
        onDocumentNumberChanged = viewModel::onDocumentNumberChanged,
        onFacultySelected = viewModel::onFacultySelected,
        onProgramSelected = viewModel::onProgramSelected,
        onCampusSelected = viewModel::onCampusSelected,
        onModalitySelected = viewModel::onModalitySelected,
        onSaveProfile = viewModel::onSaveProfile,
        onNavigateToStudentsList = onNavigateToStudentsList,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    state: ProfileUiState,
    onFullNameChanged: (String) -> Unit,
    onStudentCodeChanged: (String) -> Unit,
    onDocumentNumberChanged: (String) -> Unit = {},
    onFacultySelected: (Faculty) -> Unit,
    onProgramSelected: (AcademicProgram) -> Unit = {},
    onCampusSelected: (Campus) -> Unit = {},
    onModalitySelected: (Modality) -> Unit = {},
    onSaveProfile: () -> Unit,
    onNavigateToStudentsList: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    var isFacultyDropdownExpanded by remember { mutableStateOf(false) }
    var isProgramDropdownExpanded by remember { mutableStateOf(false) }
    var isCampusDropdownExpanded by remember { mutableStateOf(false) }
    var isModalityDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cabecera descriptiva del Módulo 1
        Text(
            text = "Módulo 1: Registro y Perfil Académico UTS",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = UtsGreenPrimary
        )

        Text(
            text = "Ingresa tu información académica oficial. El sistema generará tu correo institucional (@uts.edu.co) y habilitará los servicios y liquidaciones financieras.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Mensaje de éxito al guardar
        if (state.successMessage != null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = UtsGreenContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = UtsGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = state.successMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = UtsOnGreenContainer
                    )
                }
            }
        }

        // Formulario de Captura de Datos
        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Datos Oficiales del Estudiante",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // 1. Nombre Completo
                OutlinedTextField(
                    value = state.fullName,
                    onValueChange = onFullNameChanged,
                    label = { Text("Nombre Completo *") },
                    placeholder = { Text("Ej: Carlos Andrés Pérez") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = UtsGreenPrimary
                        )
                    },
                    isError = state.nameError != null,
                    supportingText = {
                        if (state.nameError != null) {
                            Text(text = state.nameError, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("Obligatorio. Mínimo 3 caracteres")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UtsGreenPrimary,
                        focusedLabelColor = UtsGreenPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // 2. Código Estudiantil
                OutlinedTextField(
                    value = state.studentCode,
                    onValueChange = onStudentCodeChanged,
                    label = { Text("Código Estudiantil *") },
                    placeholder = { Text("Ej: 1098765432") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Badge,
                            contentDescription = null,
                            tint = UtsGreenPrimary
                        )
                    },
                    isError = state.codeError != null,
                    supportingText = {
                        if (state.codeError != null) {
                            Text(text = state.codeError, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("Código institucional numérico o alfanumérico")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UtsGreenPrimary,
                        focusedLabelColor = UtsGreenPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // 3. Documento de Identidad (Cédula / Tarjeta de Identidad)
                OutlinedTextField(
                    value = state.documentNumber,
                    onValueChange = onDocumentNumberChanged,
                    label = { Text("Documento de Identidad (C.C. / T.I.) *") },
                    placeholder = { Text("Ej: 1098765432") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Badge,
                            contentDescription = null,
                            tint = UtsGreenPrimary
                        )
                    },
                    isError = state.documentError != null,
                    supportingText = {
                        if (state.documentError != null) {
                            Text(text = state.documentError, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("Obligatorio para evitar registros duplicados")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UtsGreenPrimary,
                        focusedLabelColor = UtsGreenPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // 4. Menú Desplegable de Facultad Oficial
                ExposedDropdownMenuBox(
                    expanded = isFacultyDropdownExpanded,
                    onExpandedChange = { isFacultyDropdownExpanded = !isFacultyDropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = state.selectedFaculty?.displayName ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Facultad Oficial UTS *") },
                        placeholder = { Text("Selecciona tu facultad oficial") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = UtsGreenPrimary
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isFacultyDropdownExpanded)
                        },
                        isError = state.facultyError != null,
                        supportingText = {
                            if (state.facultyError != null) {
                                Text(text = state.facultyError, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = UtsGreenPrimary,
                            focusedLabelColor = UtsGreenPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )

                    ExposedDropdownMenu(
                        expanded = isFacultyDropdownExpanded,
                        onDismissRequest = { isFacultyDropdownExpanded = false }
                    ) {
                        Faculty.entries.forEach { faculty ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(faculty.displayName, fontWeight = FontWeight.Bold)
                                        Text(faculty.shortName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                },
                                onClick = {
                                    onFacultySelected(faculty)
                                    isFacultyDropdownExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }

                // 4. Menú Desplegable de Programa Académico Oficial (Filtrado por Facultad)
                if (state.selectedFaculty != null) {
                    ExposedDropdownMenuBox(
                        expanded = isProgramDropdownExpanded,
                        onExpandedChange = { isProgramDropdownExpanded = !isProgramDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = state.selectedProgram?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Programa Académico Oficial *") },
                            placeholder = { Text("Selecciona tu programa académico") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = UtsGreenPrimary
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isProgramDropdownExpanded)
                            },
                            isError = state.programError != null,
                            supportingText = {
                                if (state.programError != null) {
                                    Text(text = state.programError, color = MaterialTheme.colorScheme.error)
                                } else if (state.selectedProgram != null) {
                                    Text("SNIES: ${state.selectedProgram.sniesCode} • ${state.selectedProgram.level.displayName}")
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = UtsGreenPrimary,
                                focusedLabelColor = UtsGreenPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )

                        ExposedDropdownMenu(
                            expanded = isProgramDropdownExpanded,
                            onDismissRequest = { isProgramDropdownExpanded = false }
                        ) {
                            state.availablePrograms.forEach { program ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(program.name, fontWeight = FontWeight.SemiBold)
                                            Text(
                                                "${program.level.displayName} • SNIES ${program.sniesCode}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    },
                                    onClick = {
                                        onProgramSelected(program)
                                        isProgramDropdownExpanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }

                    // 5. Selector de Sede Física Confirmada
                    val availableCampuses = state.selectedProgram?.campuses ?: Campus.entries.toList()
                    ExposedDropdownMenuBox(
                        expanded = isCampusDropdownExpanded,
                        onExpandedChange = { isCampusDropdownExpanded = !isCampusDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = state.selectedCampus?.cityName ?: Campus.BUCARAMANGA.cityName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Sede / Campus UTS *") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Place, contentDescription = null, tint = UtsGreenPrimary)
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCampusDropdownExpanded)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = UtsGreenPrimary,
                                focusedLabelColor = UtsGreenPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )

                        ExposedDropdownMenu(
                            expanded = isCampusDropdownExpanded,
                            onDismissRequest = { isCampusDropdownExpanded = false }
                        ) {
                            availableCampuses.forEach { campus ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(campus.cityName, fontWeight = FontWeight.Bold)
                                            Text(campus.address, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    },
                                    onClick = {
                                        onCampusSelected(campus)
                                        isCampusDropdownExpanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }

                    // 6. Selector de Modalidad Confirmada
                    val availableModalities = state.selectedProgram?.modalities ?: listOf(Modality.PRESENCIAL)
                    ExposedDropdownMenuBox(
                        expanded = isModalityDropdownExpanded,
                        onExpandedChange = { isModalityDropdownExpanded = !isModalityDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = state.selectedModality?.displayName ?: Modality.PRESENCIAL.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Modalidad de Estudio *") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Computer, contentDescription = null, tint = UtsGreenPrimary)
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isModalityDropdownExpanded)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = UtsGreenPrimary,
                                focusedLabelColor = UtsGreenPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )

                        ExposedDropdownMenu(
                            expanded = isModalityDropdownExpanded,
                            onDismissRequest = { isModalityDropdownExpanded = false }
                        ) {
                            availableModalities.forEach { modality ->
                                DropdownMenuItem(
                                    text = { Text(modality.displayName) },
                                    onClick = {
                                        onModalitySelected(modality)
                                        isModalityDropdownExpanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }

                // Previsualización del Correo Institucional Simulado (Regla de Negocio)
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(UtsGreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Correo Institucional UTS (Generado):",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = state.generatedEmail.ifBlank { "nombre.codigo@uts.edu.co" },
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = UtsGreenPrimary
                            )
                        }
                    }
                }

                // Botón Guardar Perfil
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onSaveProfile()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UtsGreenPrimary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (state.isSaved) "Actualizar Perfil Académico" else "Guardar Perfil Académico",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (onNavigateToStudentsList != null) {
                    androidx.compose.material3.OutlinedButton(
                        onClick = onNavigateToStudentsList,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = UtsGreenPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Consultar Estudiantes Registrados",
                            style = MaterialTheme.typography.titleSmall,
                            color = UtsGreenPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Resumen del Perfil Registrado Actualmente
        if (state.currentProfile != null) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = UtsGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Perfil Estudiantil UTS Activo",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = UtsGreenPrimary
                        )
                    }

                    Text(
                        text = "Estudiante: ${state.currentProfile.fullName}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Código: ${state.currentProfile.studentCode}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Facultad: ${state.currentProfile.faculty.displayName}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (state.currentProfile.programName.isNotBlank()) {
                        Text(
                            text = "Programa: ${state.currentProfile.programName}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = UtsGreenPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    if (state.currentProfile.campus.isNotBlank() || state.currentProfile.modality.isNotBlank()) {
                        Text(
                            text = "Sede: ${state.currentProfile.campus.ifBlank { "Bucaramanga" }} • Modalidad: ${state.currentProfile.modality.ifBlank { "Presencial" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "Correo UTS: ${state.currentProfile.institutionalEmail}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = UtsGreenPrimary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    UtsServiciosEstudiantilesTheme {
        ProfileContent(
            state = ProfileUiState(
                fullName = "Carlos Andrés Pérez",
                studentCode = "10987654",
                documentNumber = "1098765432",
                selectedFaculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                generatedEmail = "carlos.andres.perez.10987654@uts.edu.co",
                isSaved = true,
                currentProfile = StudentProfile(
                    fullName = "Carlos Andrés Pérez",
                    studentCode = "10987654",
                    documentNumber = "1098765432",
                    faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                    institutionalEmail = "carlos.andres.perez.10987654@uts.edu.co",
                    programName = "Tecnología en Desarrollo de Sistemas Informáticos",
                    academicLevel = "Nivel Tecnológico",
                    campus = "Bucaramanga",
                    modality = "Presencial"
                )
            ),
            onFullNameChanged = {},
            onStudentCodeChanged = {},
            onDocumentNumberChanged = {},
            onFacultySelected = {},
            onProgramSelected = {},
            onCampusSelected = {},
            onModalitySelected = {},
            onSaveProfile = {}
        )
    }
}
