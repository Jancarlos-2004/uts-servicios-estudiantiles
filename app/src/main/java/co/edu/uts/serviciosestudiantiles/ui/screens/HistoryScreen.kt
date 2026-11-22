package co.edu.uts.serviciosestudiantiles.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.edu.uts.serviciosestudiantiles.data.model.TuitionLiquidation
import co.edu.uts.serviciosestudiantiles.ui.components.CurrencyUtils
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsAmberAccent
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenContainer
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenDark
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsGreenPrimary
import co.edu.uts.serviciosestudiantiles.ui.theme.UtsServiciosEstudiantilesTheme
import co.edu.uts.serviciosestudiantiles.viewmodel.history.HistoryUiState
import co.edu.uts.serviciosestudiantiles.viewmodel.history.HistoryViewModel

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onNavigateToEnrollment: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Observación de estado reactivo mediante collectAsStateWithLifecycle
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryContent(
        state = state,
        onNavigateToEnrollment = onNavigateToEnrollment,
        onClearHistory = viewModel::clearHistory,
        modifier = modifier
    )
}

@Composable
fun HistoryContent(
    state: HistoryUiState,
    onNavigateToEnrollment: () -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showClearConfirmDialog by remember { mutableStateOf(false) }

    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            title = { Text("Reiniciar Historial") },
            text = { Text("¿Estás seguro de que deseas eliminar todas las liquidaciones registradas?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearHistory()
                        showClearConfirmDialog = false
                    }
                ) {
                    Text("Sí, reiniciar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título del módulo
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Módulo 3: Historial y Consolidado",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = UtsGreenPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Registro de liquidaciones realizadas y consolidado financiero acumulado.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (state.liquidations.isNotEmpty()) {
                    IconButton(onClick = { showClearConfirmDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Limpiar historial",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        // TARJETA DESTACADA: «Total Acumulado a Pagar» (Calculado en tiempo real en ViewModel)
        item {
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
                        .padding(22.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "CONSOLIDADO GLOBAL",
                                style = MaterialTheme.typography.labelLarge,
                                color = UtsAmberAccent,
                                fontWeight = FontWeight.Bold
                            )

                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = UtsAmberAccent,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Total Acumulado a Pagar",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = CurrencyUtils.formatCop(state.totalAccumulated),
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${state.liquidations.size} matrícula(s) extracurricular(es) registrada(s)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        // Listado de Liquidaciones
        item {
            Text(
                text = "Detalle de Liquidaciones Registradas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Estado Vacío
        if (state.isEmpty) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No hay liquidaciones registradas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Las liquidaciones que generes en el Módulo 2 aparecerán aquí detalladas y sumarán al consolidado.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onNavigateToEnrollment,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = UtsGreenPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Ir a Liquidar Curso (Módulo 2)")
                        }
                    }
                }
            }
        } else {
            items(
                items = state.liquidations,
                key = { it.id }
            ) { item ->
                LiquidationHistoryCard(liquidation = item)
            }
        }
    }
}

@Composable
private fun LiquidationHistoryCard(
    liquidation: TuitionLiquidation,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Cabecera: Nombre del curso y fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
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
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = UtsGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = liquidation.courseName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = liquidation.formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Desglose de valores
            HistoryDetailRow(
                label = "Costo base oficial:",
                value = CurrencyUtils.formatCop(liquidation.baseCost)
            )

            if (liquidation.vigencia.isNotBlank()) {
                HistoryDetailRow(
                    label = "Vigencia reglamentaria:",
                    value = liquidation.vigencia
                )
            }

            if (liquidation.discountAmount > 0.0) {
                val benefitLabel = if (liquidation.benefitDescription.isNotBlank()) {
                    "Descuento (${liquidation.benefitDescription}):"
                } else {
                    "Descuento aplicado:"
                }
                HistoryDetailRow(
                    label = benefitLabel,
                    value = "- ${CurrencyUtils.formatCop(liquidation.discountAmount)}",
                    valueColor = UtsGreenPrimary
                )
            }

            if (liquidation.hasIcetex) {
                HistoryDetailRow(
                    label = "Modalidad de pago:",
                    value = "Financiado con Crédito ICETEX (0% recargo UTS)",
                    valueColor = UtsGreenPrimary
                )
            }

            HistoryDetailRow(
                label = "Estudiante:",
                value = "${liquidation.studentName} (${liquidation.studentCode})"
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Total Final Destacado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Liquidado:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = CurrencyUtils.formatCop(liquidation.totalAmount),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = UtsGreenPrimary
                )
            }
        }
    }
}

@Composable
private fun HistoryDetailRow(
    label: String,
    value: String,
    valueColor: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = valueColor
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistoryScreenPreview() {
    UtsServiciosEstudiantilesTheme {
        HistoryContent(
            state = HistoryUiState(
                liquidations = listOf(
                    TuitionLiquidation(
                        id = 1L,
                        courseName = "Programación Web",
                        baseCost = 420_000.0,
                        hasScholarship = true,
                        hasIcetex = true,
                        discountAmount = 210_000.0,
                        surchargeAmount = 10_500.0,
                        totalAmount = 220_500.0,
                        studentCode = "10987654",
                        studentName = "Carlos Andrés Pérez"
                    )
                ),
                totalAccumulated = 220_500.0,
                isLoading = false
            ),
            onNavigateToEnrollment = {},
            onClearHistory = {}
        )
    }
}
