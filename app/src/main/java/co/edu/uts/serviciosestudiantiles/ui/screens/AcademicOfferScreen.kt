package co.edu.uts.serviciosestudiantiles.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.edu.uts.serviciosestudiantiles.data.model.AcademicProgram
import co.edu.uts.serviciosestudiantiles.data.model.Campus
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalSource
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalTariff
import co.edu.uts.serviciosestudiantiles.data.model.UserCategory
import co.edu.uts.serviciosestudiantiles.data.model.WellnessActivity
import co.edu.uts.serviciosestudiantiles.data.model.WellnessCategory
import co.edu.uts.serviciosestudiantiles.ui.components.CurrencyUtils
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsAmberAccent
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenContainer
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenPrimary
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsOnGreenContainer
import co.edu.uts.serviciosestudiantiles.viewmodel.institutional.InstitutionalUiState
import co.edu.uts.serviciosestudiantiles.viewmodel.institutional.InstitutionalViewModel

@Composable
fun AcademicOfferScreen(
    viewModel: InstitutionalViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Programas", "Tarifas", "Educación Continua", "Bienestar", "Fuentes")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = UtsGreenPrimary,
            edgePadding = 16.dp,
            divider = {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            maxLines = 1,
                            softWrap = false,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> ProgramsTab(state = state, viewModel = viewModel)
            1 -> TariffsTab(state = state)
            2 -> ContinuingEdTab(state = state, viewModel = viewModel)
            3 -> WellnessTab(state = state, viewModel = viewModel)
            4 -> SourcesTab(state = state)
        }
    }
}

@Composable
private fun ProgramsTab(
    state: InstitutionalUiState,
    viewModel: InstitutionalViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Catálogo Oficial de Oferta Académica",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = UtsGreenPrimary
            )
            Text(
                text = "Programas tecnológicos y universitarios articulados por Ciclos Propedéuticos.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Buscador
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                label = { Text("Buscar programa o código SNIES") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = UtsGreenPrimary)
                },
                trailingIcon = {
                    if (state.searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filtros rápidos por Facultad
            Text(
                text = "Filtrar por Facultad:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = state.facultyFilter == null,
                        onClick = { viewModel.onFacultyFilterSelected(null) },
                        label = { Text("Todas", maxLines = 1, softWrap = false) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = UtsGreenContainer,
                            selectedLabelColor = UtsOnGreenContainer
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = state.facultyFilter == Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                        onClick = { viewModel.onFacultyFilterSelected(Faculty.CIENCIAS_NATURALES_E_INGENIERIAS) },
                        label = { Text("Ingenierías", maxLines = 1, softWrap = false) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = UtsGreenContainer,
                            selectedLabelColor = UtsOnGreenContainer
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = state.facultyFilter == Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES,
                        onClick = { viewModel.onFacultyFilterSelected(Faculty.CIENCIAS_SOCIOECONOMICAS_Y_EMPRESARIALES) },
                        label = { Text("Socioeconómicas", maxLines = 1, softWrap = false) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = UtsGreenContainer,
                            selectedLabelColor = UtsOnGreenContainer
                        )
                    )
                }
            }

            // Filtros rápidos por Sede
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Filtrar por Sede Física:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                item {
                    FilterChip(
                        selected = state.campusFilter == null,
                        onClick = { viewModel.onCampusFilterSelected(null) },
                        label = { Text("Todas las sedes", maxLines = 1, softWrap = false) }
                    )
                }
                items(Campus.entries.toTypedArray()) { campus ->
                    FilterChip(
                        selected = state.campusFilter == campus,
                        onClick = { viewModel.onCampusFilterSelected(campus) },
                        label = { Text(campus.cityName, maxLines = 1, softWrap = false) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${state.filteredPrograms.size} programas encontrados",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(state.filteredPrograms, key = { it.id }) { program ->
            ProgramCard(program = program)
        }
    }
}

@Composable
private fun ProgramCard(program: AcademicProgram) {
    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = UtsGreenContainer
                ) {
                    Text(
                        text = program.level.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = UtsOnGreenContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "SNIES: ${program.sniesCode}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = program.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = program.faculty.displayName,
                style = MaterialTheme.typography.bodySmall,
                color = UtsGreenPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Título que otorga: ${program.degreeTitle}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Duración:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "${program.durationSemesters} semestres", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(text = "Modalidades:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = program.modalities.joinToString { it.displayName }, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(text = "Sedes confirmadas:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = program.campuses.joinToString { it.cityName }, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TariffsTab(state: InstitutionalUiState) {
    var vigenciaTab by remember { mutableIntStateOf(0) }
    val tariffs = if (vigenciaTab == 0) state.tariffs2027 else state.tariffs2026
    val resolutionTitle = if (vigenciaTab == 0) {
        "Resolución UTS No. 02-1613 del 15 de septiembre de 2026 (Vigencia 2027)"
    } else {
        "Resolución de Derechos Pecuniarios y Complementarios 2026 UTS"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Derechos Pecuniarios y Tarifas Oficiales",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = UtsGreenPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = vigenciaTab == 0,
                        onClick = { vigenciaTab = 0 },
                        label = { Text("Vigencia 2027 (Res. 02-1613)", maxLines = 1, softWrap = false) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = UtsGreenContainer,
                            selectedLabelColor = UtsOnGreenContainer
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = vigenciaTab == 1,
                        onClick = { vigenciaTab = 1 },
                        label = { Text("Vigencia 2026", maxLines = 1, softWrap = false) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = UtsGreenContainer,
                            selectedLabelColor = UtsOnGreenContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Acto oficial: $resolutionTitle",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        items(tariffs, key = { it.id }) { tariff ->
            ElevatedCard(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = tariff.conceptName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = tariff.condition,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Aplica a: ${tariff.userType}",
                            style = MaterialTheme.typography.labelSmall,
                            color = UtsGreenPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = CurrencyUtils.formatCop(tariff.amount),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = UtsGreenPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun ContinuingEdTab(
    state: InstitutionalUiState,
    viewModel: InstitutionalViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Oferta Oficial de Educación Continua",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = UtsGreenPrimary
            )
            Text(
                text = "Diplomados, talleres y cursos de extensión. Tarifa por hora según Res. 02-1613 para 2027.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Calcular tarifas según tu categoría de participante:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(UserCategory.entries.toTypedArray()) { category ->
                    FilterChip(
                        selected = state.selectedUserCategory == category,
                        onClick = { viewModel.onUserCategoryChanged(category) },
                        label = {
                            Text(
                                text = category.displayName,
                                maxLines = 1,
                                softWrap = false,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = UtsGreenContainer,
                            selectedLabelColor = UtsOnGreenContainer
                        )
                    )
                }
            }
        }

        items(state.continuingEducationOffer, key = { it.id }) { course ->
            val computedPrice = course.calculatePrice(state.selectedUserCategory)
            ElevatedCard(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = UtsGreenContainer
                        ) {
                            Text(
                                text = course.type,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = UtsOnGreenContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = "${course.durationHours} horas académicas",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = course.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = course.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Valor (${state.selectedUserCategory.displayName}):",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = CurrencyUtils.formatCop(computedPrice),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = UtsGreenPrimary
                            )
                        }

                        Text(
                            text = course.modality.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WellnessTab(
    state: InstitutionalUiState,
    viewModel: InstitutionalViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Bienestar Institucional UTS",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = UtsGreenPrimary
            )
            Text(
                text = "Servicios gratuitos de salud, torneos deportivos, grupos culturales y acompañamiento psicosocial.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = state.wellnessCategoryFilter == null,
                        onClick = { viewModel.onWellnessCategoryFilterSelected(null) },
                        label = { Text("Todas", maxLines = 1, softWrap = false) }
                    )
                }
                items(WellnessCategory.entries.toTypedArray()) { cat ->
                    FilterChip(
                        selected = state.wellnessCategoryFilter == cat,
                        onClick = { viewModel.onWellnessCategoryFilterSelected(cat) },
                        label = { Text(cat.displayName, maxLines = 1, softWrap = false) }
                    )
                }
            }
        }

        items(state.wellnessActivities, key = { it.id }) { act ->
            ElevatedCard(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = UtsGreenContainer
                        ) {
                            Text(
                                text = act.category.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = UtsOnGreenContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = "Gratuito para estudiantes",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = UtsGreenPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = act.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = act.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    Text(
                        text = "Horario: ${act.schedule}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Lugar: ${act.location}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Requisitos: ${act.requirements}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SourcesTab(state: InstitutionalUiState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Trazabilidad y Fuentes Oficiales",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = UtsGreenPrimary
            )
            Text(
                text = "Toda la información académica, normativa y financiera corresponde a actos oficiales vigentes de las Unidades Tecnológicas de Santander.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(state.officialSources, key = { it.id }) { src ->
            ElevatedCard(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = UtsGreenContainer
                        ) {
                            Text(
                                text = "Vigencia: ${src.vigencia}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = UtsOnGreenContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = "Consultado: ${src.consultationDate}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = src.sourceName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = UtsGreenPrimary
                    )

                    Text(
                        text = "Emisor: ${src.issuingBody}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = src.legalSummary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    Text(
                        text = "Datos respaldados: ${src.supportedDataType}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Enlace institucional: ${src.url}",
                        style = MaterialTheme.typography.labelSmall,
                        color = UtsGreenPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


