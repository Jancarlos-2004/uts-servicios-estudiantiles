package co.edu.uts.serviciosestudiantiles.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import co.edu.uts.serviciosestudiantiles.data.local.dao.RegisteredStudentDao
import co.edu.uts.serviciosestudiantiles.data.local.dao.StudentProfileDao
import co.edu.uts.serviciosestudiantiles.data.local.dao.TuitionLiquidationDao
import co.edu.uts.serviciosestudiantiles.data.local.entity.RegisteredStudentEntity
import co.edu.uts.serviciosestudiantiles.data.local.entity.StudentProfileEntity
import co.edu.uts.serviciosestudiantiles.data.local.entity.TuitionLiquidationEntity

/**
 * Base de datos Room para la persistencia local de Servicios Estudiantiles UTS.
 * Versión 3: Incorpora módulo general de Estudiantes Registrados y documento de identidad.
 * Aplica migraciones secuenciales para conservar intactos todos los perfiles e historiales preexistentes.
 */
@Database(
    entities = [
        StudentProfileEntity::class,
        TuitionLiquidationEntity::class,
        RegisteredStudentEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class UtsDatabase : RoomDatabase() {

    abstract fun studentProfileDao(): StudentProfileDao
    abstract fun tuitionLiquidationDao(): TuitionLiquidationDao
    abstract fun registeredStudentDao(): RegisteredStudentDao

    companion object {
        @Volatile
        private var INSTANCE: UtsDatabase? = null

        /**
         * Migración segura de versión 1 a versión 2.
         * Añade las nuevas columnas con valores por defecto sin pérdida de datos.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE student_profile ADD COLUMN programName TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE student_profile ADD COLUMN academicLevel TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE student_profile ADD COLUMN campus TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE student_profile ADD COLUMN modality TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE tuition_liquidations ADD COLUMN vigencia TEXT NOT NULL DEFAULT '2026'")
                db.execSQL("ALTER TABLE tuition_liquidations ADD COLUMN benefitDescription TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE tuition_liquidations ADD COLUMN tariffSource TEXT NOT NULL DEFAULT 'Resolución Oficial UTS'")
            }
        }

        /**
         * Migración segura de versión 2 a versión 3.
         * Crea la tabla general de estudiantes registrados e incorpora el campo documento de identidad.
         */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE student_profile ADD COLUMN documentNumber TEXT NOT NULL DEFAULT ''")
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `registered_students` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `fullName` TEXT NOT NULL,
                        `studentCode` TEXT NOT NULL,
                        `documentNumber` TEXT NOT NULL,
                        `faculty` TEXT NOT NULL,
                        `programName` TEXT NOT NULL,
                        `academicLevel` TEXT NOT NULL,
                        `campus` TEXT NOT NULL,
                        `modality` TEXT NOT NULL,
                        `institutionalEmail` TEXT NOT NULL,
                        `status` TEXT NOT NULL,
                        `registeredAt` INTEGER NOT NULL
                    )
                """.trimIndent())
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_registered_students_studentCode` ON `registered_students` (`studentCode`)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_registered_students_documentNumber` ON `registered_students` (`documentNumber`)")
            }
        }

        fun getDatabase(context: Context): UtsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UtsDatabase::class.java,
                    "uts_servicios_estudiantiles.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
