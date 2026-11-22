package co.edu.uts.serviciosestudiantiles.ui.components

import java.text.NumberFormat
import java.util.Locale

/**
 * Utilidad para dar formato monetario oficial de Colombia (COP).
 * Ejemplo: 420000 -> "$ 420.000 COP"
 */
object CurrencyUtils {
    private val colombianFormat = NumberFormat.getNumberInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }

    fun formatCop(amount: Double): String {
        return "$ ${colombianFormat.format(amount)} COP"
    }

    fun formatCopCompact(amount: Double): String {
        return "$ ${colombianFormat.format(amount)}"
    }
}

