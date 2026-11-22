package co.edu.uts.serviciosestudiantiles.viewmodel.history

import co.edu.uts.serviciosestudiantiles.data.model.TuitionLiquidation

/**
 * Estado inmutable de la interfaz de usuario para el Módulo 3 (Historial y Consolidado Financiero).
 */
data class HistoryUiState(
    val liquidations: List<TuitionLiquidation> = emptyList(),
    val totalAccumulated: Double = 0.0,
    val isLoading: Boolean = false,
    val infoMessage: String? = null
) {
    val isEmpty: Boolean
        get() = !isLoading && liquidations.isEmpty()
}

