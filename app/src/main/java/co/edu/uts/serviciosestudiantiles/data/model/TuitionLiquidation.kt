package co.edu.uts.serviciosestudiantiles.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Modelo de dominio que representa una liquidación de matrícula o derecho pecuniario registrado.
 * Conserva la vigencia y la fuente oficial para que los registros históricos nunca alteren
 * sus valores cuando se actualicen las tarifas en el futuro.
 *
 * @param id Identificador único autoincremental
 * @param courseName Nombre del concepto o programa liquidado
 * @param baseCost Costo base oficial según la vigencia
 * @param hasScholarship Indica si se aplicó beneficio institucional o beca
 * @param hasIcetex Indica si el pago es financiado con crédito ICETEX
 * @param discountAmount Valor en pesos del descuento aplicado
 * @param surchargeAmount Valor del recargo (0.0 por defecto; no hay recargo ICETEX oficial)
 * @param totalAmount Valor final neto a pagar
 * @param studentCode Código del estudiante que realizó la liquidación
 * @param studentName Nombre del estudiante que realizó la liquidación
 * @param vigencia Año de vigencia de la tarifa ("2026" o "2027")
 * @param benefitDescription Descripción del beneficio o estímulo aplicado
 * @param tariffSource Acto administrativo oficial de respaldo
 * @param timestamp Fecha y hora exacta de la liquidación en milisegundos
 */
data class TuitionLiquidation(
    val id: Long = 0L,
    val courseName: String,
    val baseCost: Double,
    val hasScholarship: Boolean,
    val hasIcetex: Boolean,
    val discountAmount: Double,
    val surchargeAmount: Double = 0.0,
    val totalAmount: Double,
    val studentCode: String,
    val studentName: String,
    val vigencia: String = "2026",
    val benefitDescription: String = "",
    val tariffSource: String = "Resolución Oficial UTS",
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Retorna la fecha formateada en formato legible en español de Colombia.
     */
    val formattedDate: String
        get() {
            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale("es", "CO"))
            return sdf.format(Date(timestamp))
        }
}
