package co.edu.uts.serviciosestudiantiles.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.edu.uts.serviciosestudiantiles.data.model.ExtracurricularCourse
import co.edu.uts.serviciosestudiantiles.data.model.Faculty
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalBenefit
import co.edu.uts.serviciosestudiantiles.data.model.InstitutionalTariff
import co.edu.uts.serviciosestudiantiles.data.model.StudentProfile
import co.edu.uts.serviciosestudiantiles.ui.components.CurrencyUtils
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenContainer
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenPrimary
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsOnGreenContainer
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsServiciosEstudiantilesTheme
import co.edu.uts.serviciosestudiantiles.viewmodel.enrollment.EnrollmentUiState
import co.edu.uts.serviciosestudiantiles.viewmodel.enrollment.EnrollmentViewModel

@Composable
fun EnrollmentScreen(
    viewModel: EnrollmentViewModel,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    EnrollmentContent(
        state = state,
        onNavigateToProfile = onNavigateToProfile,
        onCourseSelected = viewModel::onCourseSelected,
        onScholarshipToggled = viewModel::onScholarshipToggled,
        onIcetexToggled = viewModel::onIcetexToggled,
        onVigenciaSelected = viewModel::onVigenciaSelected,
        onTariffSelected = viewModel::onTariffSelected,
        onBenefitSelected = viewModel::onBenefitSelected,
        onIcetexFundingToggled = viewModel::onIcetexFundingToggled,
        onLiquidate = viewModel::onLiquidate,
        modifier = modifier
    )
}

@Composable
fun EnrollmentContent(
    state: EnrollmentUiState,
    onNavigateToProfile: () -> Unit,
    onCourseSelected: (ExtracurricularCourse) -> Unit = {},
    onScholarshipToggled: (Boolean) -> Unit = {},
    onIcetexToggled: (Boolean) -> Unit = {},
    onVigenciaSelected: (String) -> Unit = {},
    onTariffSelected: (InstitutionalTariff) -> Unit = {},
    onBenefitSelected: (InstitutionalBenefit) -> Unit = {},
    onIcetexFundingToggled: (Boolean) -> Unit = {},
    onLiquidate: () -> Unit,
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
        // Título del módulo
        Text(
            text = "Módulo 2: Liquidación de Matrícula y Derechos Pecuniarios",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = UtsGreenPrimary
        )

        Text(
            text = "Calcula liquidaciones oficiales de matrícula o derechos pecuniarios según la vigencia reglamentada, aplicando beneficios institucionales verificados.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // BLOQUEO DE SEGURIDAD (Obligatorio)
        if (!state.isProfileCompleted) {
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.error),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Bloqueo de seguridad",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Bloqueo de Seguridad Activo",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }

                    Text(
                        text = "El botón de liquidación permanece bloqueado porque aún no se ha registrado un perfil académico oficial en el sistema.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )

                    Button(
                        onClick = onNavigateToProfile,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Completar Perfil Académico (Módulo 1)",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Mensaje de éxito al liquidar
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

        // 1. Selector de Vigencia Oficial
        Text(
            text = "1. Selecciona la Vigencia Oficial",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterChip(
                selected = state.selectedVigencia == InstitutionalTariff.VIGENCIA_2027,
                onClick = { onVigenciaSelected(InstitutionalTariff.VIGENCIA_2027) },
                label = { Text("Vigencia 2027 (Res. 02-1613)") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = UtsGreenContainer,
                    selectedLabelColor = UtsOnGreenContainer
                ),
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = state.selectedVigencia == InstitutionalTariff.VIGENCIA_2026,
                onClick = { onVigenciaSelected(InstitutionalTariff.VIGENCIA_2026) },
                label = { Text("Vigencia 2026") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = UtsGreenContainer,
                    selectedLabelColor = UtsOnGreenContainer
                ),
                modifier = Modifier.weight(1f)
            )
        }

        // 2. Selección de Concepto Oficial de Liquidación
        Text(
            text = "2. Concepto o Tarifa Oficial a Liquidar",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        state.availableTariffs.forEach { tariff ->
            val isSelected = state.selectedTariff?.id == tariff.id
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) UtsGreenContainer else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) UtsGreenPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTariffSelected(tariff) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) UtsGreenPrimary else MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = tariff.conceptName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) UtsOnGreenContainer else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = tariff.condition,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSelected) UtsOnGreenContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = CurrencyUtils.formatCopCompact(tariff.amount),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = UtsGreenPrimary
                    )
                }
            }
        }

        // 3. Selección de Estímulos y Beneficios Verificados
        Text(
            text = "3. Estímulos y Beneficios Institucionales",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

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
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.availableBenefits.forEach { benefit ->
                    val isChecked = state.selectedBenefit.id == benefit.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onBenefitSelected(benefit) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isChecked,
                            onClick = { onBenefitSelected(benefit) },
                            colors = RadioButtonDefaults.colors(selectedColor = UtsGreenPrimary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = benefit.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = benefit.legalBasis,
                                style = MaterialTheme.typography.bodySmall,
                                color = UtsGreenPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = benefit.requirements,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (benefit != state.availableBenefits.last()) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    }
                }
            }
        }

        // 4. Financiación con ICETEX (Aclaración Oficial)
        Text(
            text = "4. Modalidad de Financiación",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        ElevatedCard(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Pago Financiado con Crédito ICETEX",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Línea de crédito educativo oficial con convenio institucional UTS.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = state.isIcetexFunding,
                        onCheckedChange = onIcetexFundingToggled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = UtsGreenPrimary
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = UtsGreenPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Aclaración Oficial: La UTS no aplica ningún recargo administrativo a las liquidaciones con ICETEX. El valor a pagar corresponde a la tarifa neta autorizada.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // 5. Desglose Financiero Oficial en Tiempo Real
        Text(
            text = "5. Desglose Financiero Oficial",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FinancialRow(
                    label = "Concepto:",
                    value = state.selectedTariff?.conceptName ?: state.selectedCourse.title,
                    isBold = false
                )
                FinancialRow(
                    label = "Vigencia Oficial:",
                    value = state.selectedVigencia,
                    isBold = false
                )
                FinancialRow(
                    label = "Tarifa Base Oficial:",
                    value = CurrencyUtils.formatCop(state.baseCost),
                    isBold = false
                )

                if (state.discountAmount > 0.0) {
                    FinancialRow(
                        label = "Descuento (${state.selectedBenefit.name}):",
                        value = "- ${CurrencyUtils.formatCop(state.discountAmount)}",
                        valueColor = UtsGreenPrimary,
                        isBold = false
                    )
                    FinancialRow(
                        label = "Subtotal Liquidado:",
                        value = CurrencyUtils.formatCop(state.subtotalAmount),
                        isBold = false
                    )
                }

                FinancialRow(
                    label = "Recargo Administrativo UTS:",
                    value = "$ 0 COP (0% Oficial)",
                    isBold = false
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                FinancialRow(
                    label = "Total Neto a Pagar:",
                    value = CurrencyUtils.formatCop(state.totalAmount),
                    valueColor = UtsGreenPrimary,
                    isBold = true,
                    isHeadline = true
                )

                Text(
                    text = "Fuente: ${state.tariffSource}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Botón Liquidar (Sujeto al Bloqueo de Seguridad)
        Button(
            onClick = onLiquidate,
            enabled = state.canLiquidate,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = UtsGreenPrimary,
                contentColor = Color.White,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(
                imageVector = if (state.canLiquidate) Icons.Default.ReceiptLong else Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (state.canLiquidate) "Generar Liquidación Oficial" else "Bloqueado (Requiere Perfil Académico)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun FinancialRow(
    label: String,
    value: String,
    valueColor: Color = Color.Unspecified,
    isBold: Boolean = false,
    isHeadline: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isHeadline) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = if (isHeadline) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.bodyLarge,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
            color = valueColor
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EnrollmentScreenPreview() {
    UtsServiciosEstudiantilesTheme {
        EnrollmentContent(
            state = EnrollmentUiState(
                selectedVigencia = InstitutionalTariff.VIGENCIA_2027,
                baseCost = 2_836_466.0,
                discountAmount = 1_418_233.0,
                subtotalAmount = 1_418_233.0,
                totalAmount = 1_418_233.0,
                isProfileCompleted = true,
                currentStudent = StudentProfile(
                    fullName = "Carlos Andrés Pérez",
                    studentCode = "10987654",
                    faculty = Faculty.CIENCIAS_NATURALES_E_INGENIERIAS,
                    institutionalEmail = "carlos.andres.perez.10987654@uts.edu.co"
                )
            ),
            onNavigateToProfile = {},
            onLiquidate = {}
        )
    }
}
