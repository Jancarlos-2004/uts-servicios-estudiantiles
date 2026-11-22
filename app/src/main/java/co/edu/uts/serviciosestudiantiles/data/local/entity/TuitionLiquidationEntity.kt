package co.edu.uts.serviciosestudiantiles.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.edu.uts.serviciosestudiantiles.data.model.TuitionLiquidation

/**
 * Entidad Room para almacenar cada liquidación de matrícula o derecho pecuniario.
 * Guarda la vigencia ("2026", "2027") y la fuente oficial para trazabilidad histórica inmutable.
 */
@Entity(tableName = "tuition_liquidations")
data class TuitionLiquidationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val courseName: String,
    val baseCost: Double,
    val hasScholarship: Boolean,
    val hasIcetex: Boolean,
    val discountAmount: Double,
    val surchargeAmount: Double,
    val totalAmount: Double,
    val studentCode: String,
    val studentName: String,
    val vigencia: String = "2026",
    val benefitDescription: String = "",
    val tariffSource: String = "Resolución Oficial UTS",
    val timestamp: Long
) {
    fun toDomain(): TuitionLiquidation {
        return TuitionLiquidation(
            id = id,
            courseName = courseName,
            baseCost = baseCost,
            hasScholarship = hasScholarship,
            hasIcetex = hasIcetex,
            discountAmount = discountAmount,
            surchargeAmount = surchargeAmount,
            totalAmount = totalAmount,
            studentCode = studentCode,
            studentName = studentName,
            vigencia = vigencia,
            benefitDescription = benefitDescription,
            tariffSource = tariffSource,
            timestamp = timestamp
        )
    }

    companion object {
        fun fromDomain(model: TuitionLiquidation): TuitionLiquidationEntity {
            return TuitionLiquidationEntity(
                id = model.id,
                courseName = model.courseName,
                baseCost = model.baseCost,
                hasScholarship = model.hasScholarship,
                hasIcetex = model.hasIcetex,
                discountAmount = model.discountAmount,
                surchargeAmount = model.surchargeAmount,
                totalAmount = model.totalAmount,
                studentCode = model.studentCode,
                studentName = model.studentName,
                vigencia = model.vigencia,
                benefitDescription = model.benefitDescription,
                tariffSource = model.tariffSource,
                timestamp = model.timestamp
            )
        }
    }
}
