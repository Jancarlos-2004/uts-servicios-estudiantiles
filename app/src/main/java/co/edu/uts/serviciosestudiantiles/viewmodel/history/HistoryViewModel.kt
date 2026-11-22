package co.edu.uts.serviciosestudiantiles.viewmodel.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import co.edu.uts.serviciosestudiantiles.data.repository.TuitionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel del Módulo 3: Historial y Consolidado Financiero.
 *
 * Implementa:
 * - Observación reactiva de la lista de liquidaciones desde el repositorio.
 * - Cálculo automático y centralizado del «Total Acumulado a Pagar» en tiempo real.
 * - Sin cálculos de consolidado en la vista (UI limpia).
 * - Encapsulación UDF (MutableStateFlow privado -> StateFlow público).
 */
class HistoryViewModel(
    private val tuitionRepository: TuitionRepository
) : ViewModel() {

    // 1. Estado privado mutable (UDF)
    private val _uiState = MutableStateFlow(HistoryUiState(isLoading = true))

    // 2. Estado público inmutable
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        observeLiquidations()
    }

    private fun observeLiquidations() {
        viewModelScope.launch {
            tuitionRepository.getAllLiquidationsFlow().collect { list ->
                val accumulated = list.sumOf { it.totalAmount }
                _uiState.update { current ->
                    current.copy(
                        liquidations = list,
                        totalAccumulated = accumulated,
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Permite reiniciar o limpiar el historial si el estudiante lo requiere.
     */
    fun clearHistory() {
        viewModelScope.launch {
            tuitionRepository.clearHistory()
            _uiState.update {
                it.copy(
                    infoMessage = "Historial reiniciado correctamente."
                )
            }
        }
    }

    fun dismissMessage() {
        _uiState.update { it.copy(infoMessage = null) }
    }

    companion object {
        fun provideFactory(tuitionRepository: TuitionRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HistoryViewModel(tuitionRepository) as T
                }
            }
    }
}

