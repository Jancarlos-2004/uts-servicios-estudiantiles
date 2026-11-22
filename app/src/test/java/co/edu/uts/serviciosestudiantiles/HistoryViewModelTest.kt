package co.edu.uts.serviciosestudiantiles

import co.edu.uts.serviciosestudiantiles.data.model.TuitionLiquidation
import co.edu.uts.serviciosestudiantiles.testutil.FakeTuitionRepository
import co.edu.uts.serviciosestudiantiles.viewmodel.history.HistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeTuitionRepository
    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeTuitionRepository()
        viewModel = HistoryViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialEmptyState_hasZeroAccumulated() = runTest {
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("Debe iniciar con lista vacía", state.isEmpty)
        assertEquals(0, state.liquidations.size)
        assertEquals(0.0, state.totalAccumulated, 0.001)
    }

    @Test
    fun addingLiquidations_updatesListAndTotalAccumulatedInRealTime() = runTest {
        // Liquidación 1: Inglés Técnico por $350.000
        fakeRepository.saveLiquidation(
            TuitionLiquidation(
                courseName = "Inglés Técnico",
                baseCost = 350_000.0,
                hasScholarship = false,
                hasIcetex = false,
                discountAmount = 0.0,
                surchargeAmount = 0.0,
                totalAmount = 350_000.0,
                studentCode = "10987",
                studentName = "Carlos Pérez"
            )
        )
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertFalse(state.isEmpty)
        assertEquals(1, state.liquidations.size)
        assertEquals(350_000.0, state.totalAccumulated, 0.001)

        // Liquidación 2: Programación Web con Beca e ICETEX ($220.500)
        fakeRepository.saveLiquidation(
            TuitionLiquidation(
                courseName = "Programación Web",
                baseCost = 420_000.0,
                hasScholarship = true,
                hasIcetex = true,
                discountAmount = 210_000.0,
                surchargeAmount = 10_500.0,
                totalAmount = 220_500.0,
                studentCode = "10987",
                studentName = "Carlos Pérez"
            )
        )
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertEquals(2, state.liquidations.size)
        // Total acumulado: 350.000 + 220.500 = 570.500
        assertEquals(570_500.0, state.totalAccumulated, 0.001)
    }

    @Test
    fun clearHistory_resetsAllData() = runTest {
        fakeRepository.saveLiquidation(
            TuitionLiquidation(
                courseName = "Ciberseguridad",
                baseCost = 500_000.0,
                hasScholarship = false,
                hasIcetex = false,
                discountAmount = 0.0,
                surchargeAmount = 0.0,
                totalAmount = 500_000.0,
                studentCode = "10987",
                studentName = "Carlos Pérez"
            )
        )
        advanceUntilIdle()
        assertEquals(500_000.0, viewModel.uiState.value.totalAccumulated, 0.001)

        viewModel.clearHistory()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isEmpty)
        assertEquals(0, state.liquidations.size)
        assertEquals(0.0, state.totalAccumulated, 0.001)
    }
}

