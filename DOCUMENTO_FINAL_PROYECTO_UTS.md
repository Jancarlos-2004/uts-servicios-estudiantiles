# SISTEMA INTEGRADO DE SERVICIOS ESTUDIANTILES UTS
## INFORME TÉCNICO Y MEMORIA DE ARQUITECTURA DE SOFTWARE

---

**INSTITUCIÓN:** Unidades Tecnológicas de Santander (UTS)  
**FACULTAD:** Ciencias Naturales e Ingenierías  
**PROGRAMA:** Tecnología en Desarrollo de Sistemas Informáticos / Ingeniería de Sistemas  
**ASIGNATURA:** Desarrollo de Aplicaciones Móviles / Programación Avanzada  
**PROYECTO:** Sistema Integrado de Servicios Estudiantiles UTS  
**ESTUDIANTE / AUTOR:** Anphernee  
**AÑO ACADÉMICO:** 2026  

---

## TABLA DE CONTENIDO

1. [INTRODUCCIÓN Y JUSTIFICACIÓN](#1-introducción-y-justificación)
2. [OBJETIVOS](#2-objetivos)
   - 2.1 Objetivo General
   - 2.2 Objetivos Específicos
3. [DESCRIPCIÓN GENERAL Y REGLAS DE NEGOCIO DEL SISTEMA](#3-descripción-general-y-reglas-de-negocio-del-sistema)
   - 3.1 Módulo 1: Registro y Perfil Académico
   - 3.2 Módulo 2: Liquidación de Matrícula Extracurricular (Bloqueo de Seguridad)
   - 3.3 Módulo 3: Historial y Consolidado Financiero
   - 3.4 Dashboard de Control General
4. [ARQUITECTURA DE SOFTWARE Y PATRONES DE DISEÑO](#4-arquitectura-de-software-y-patrones-de-diseño)
   - 4.1 Patrón Arquitectónico MVVM (Model-View-ViewModel)
   - 4.2 Flujo Unidireccional de Datos (UDF - Unidirectional Data Flow)
   - 4.3 Inyección de Dependencias Manual (Manual DI con AppContainer)
   - 4.4 Persistencia Local Reactiva con Room Database y Kotlin Flow
   - 4.5 Interfaz Gráfica Declarativa con Jetpack Compose y Material Design 3
5. [EXPLICACIÓN DETALLADA DEL CÓDIGO FUENTE POR CAPAS](#5-explicación-detallada-del-código-fuente-por-capas)
   - 5.1 Capa de Modelo y Dominio (`data.model`)
   - 5.2 Capa de Base de Datos Local (`data.local`)
   - 5.3 Capa de Repositorios y Contenedor (`data.repository`)
   - 5.4 Capa de Lógica y Estado: ViewModels (`viewmodel.*`)
   - 5.5 Capa de Interfaz de Usuario y Navegación (`ui.*`)
   - 5.6 Módulo de Pruebas Unitarias Automatizadas (`src/test`)
6. [FÓRMULAS FINANCIERAS Y CASOS DE PRUEBA MATEMÁTICOS](#6-fórmulas-financieras-y-casos-de-prueba-matemáticos)
7. [GUÍA DE EJECUCIÓN Y OPERACIÓN EN ANDROID STUDIO](#7-guía-de-ejecución-y-operación-en-android-studio)
8. [CONCLUSIONES Y LECCIONES APRENDIDAS](#8-conclusiones-y-lecciones-aprendidas)

---

## 1. INTRODUCCIÓN Y JUSTIFICACIÓN

En el contexto actual de transformación digital en las instituciones de educación superior, el acceso ágil, seguro y centralizado a los servicios académicos y administrativos representa una necesidad de primer orden. Las **Unidades Tecnológicas de Santander (UTS)** demandan aplicaciones móviles que no solo exhiban una interfaz estética alineada a su identidad institucional, sino que incorporen arquitecturas sólidas, escalables, reactivas y verificadas mediante pruebas rigurosas.

El proyecto **Sistema Integrado de Servicios Estudiantiles UTS** fue concebido y desarrollado como una aplicación móvil nativa para la plataforma Android utilizando el lenguaje **Kotlin** y el kit de herramientas declarativo moderno **Jetpack Compose** con directrices de **Material Design 3**.

La aplicación aborda de manera modular e integral los requerimientos cotidianos del estudiante uteísta: la gestión y generación de identidad digital universitaria, la liquidación financiera parametrizada de asignaturas extracurriculares con beneficios institucionales (Beca de Excelencia) y facilidades de financiamiento (ICETEX), así como la auditoría y consulta del histórico de transacciones con consolidación acumulativa en tiempo real.

---

## 2. OBJETIVOS

### 2.1 Objetivo General
Diseñar, estructurar e implementar una aplicación móvil nativa en Android bajo el patrón arquitectónico MVVM (Model-View-ViewModel), Flujo Unidireccional de Datos (UDF) y persistencia reactiva en Room, que brinde a los estudiantes de las Unidades Tecnológicas de Santander (UTS) una herramienta integral de autogestión académica y financiera.

### 2.2 Objetivos Específicos
1. **Desarrollar el Módulo de Registro y Perfil Académico:** Capturar y validar la información esencial del estudiante (nombre, código institucional y facultad), aplicando un algoritmo determinístico de sanitización de caracteres para generar automáticamente el correo institucional `@uts.edu.co`.
2. **Implementar el Módulo de Liquidación Extracurricular con Bloqueo de Seguridad:** Parametrizar la oferta académica oficial de cursos libres con sus tarifas vigentes, incorporando la lógica financiera de descuentos por Beca de Excelencia UTS (50%) y recargos administrativos por crédito ICETEX (5%), restringiendo la liquidación mediante un mecanismo de control de acceso si no existe un perfil registrado.
3. **Construir el Módulo de Historial y Consolidado Financiero:** Garantizar la persistencia histórica en base de datos relacional SQLite/Room y calcular de forma reactiva y centralizada en el ViewModel el gran total acumulado a pagar.
4. **Implementar Navegación Desacoplada y Diseño Institucional:** Crear un tema gráfico exclusivo que respete la paleta cromática verde/ámbar de las UTS, una barra de navegación inferior (`NavigationBar`) y componentes accesibles de Material 3.
5. **Verificar la Confiabilidad Mediante Pruebas Automatizadas:** Desarrollar una suite de pruebas unitarias que certifique la precisión matemática de los cálculos financieros, la robustez del generador de correos y la efectividad del bloqueo de seguridad.

---

## 3. DESCRIPCIÓN GENERAL Y REGLAS DE NEGOCIO DEL SISTEMA

La aplicación se estructura en torno a tres módulos interconectados y una pantalla principal (Dashboard), orquestados bajo reglas de negocio estrictas:

```
+-------------------------------------------------------------------------+
|                       PORTAL ESTUDIANTIL UTS                            |
+--------------------+--------------------+-------------------------------+
|     MÓDULO 1       |     MÓDULO 2       |           MÓDULO 3            |
|  Perfil Académico  | Liquidación Cursos |      Historial Financiero     |
|                    |                    |                               |
| • Nombre y Código  | • Catálogo Cursos  | • Lista de liquidaciones      |
| • Selección Facultad| • Beca UTS (50%)  | • Desglose por ítem           |
| • Correo Generado  | • ICETEX (5%)      | • Total Acumulado Centralizado|
|   (@uts.edu.co)    | • Bloqueo de       | • Función de reinicio         |
| • Persistencia DB  |   Seguridad Activo |                               |
+--------------------+--------------------+-------------------------------+
```

### 3.1 Módulo 1: Registro y Perfil Académico
- **Campos Requeridos:** Nombre Completo (mínimo 3 caracteres alfabéticos), Código Estudiantil (numérico/alfanumérico único) y Facultad Académica.
- **Facultades Oficiales:**
  * Facultad de Ciencias Naturales e Ingenierías.
  * Facultad de Ciencias Socioeconómicas y Empresariales.
- **Regla de Generación de Correo Institucional Simulado:**
  El correo institucional se construye en tiempo real según el formato:  
  $$\text{correo} = \text{nombre\_normalizado}.\text{codigo}@uts.edu.co$$
  *Tratamiento de caracteres:* Eliminación de tildes y diacríticos (ejemplo: *Andrés* $\rightarrow$ *andres*, *Muñoz* $\rightarrow$ *munoz*), eliminación de espacios en blanco reemplazándolos por puntos, conversión a minúsculas y descarte de símbolos especiales.

### 3.2 Módulo 2: Liquidación de Matrícula Extracurricular
- **Oferta de Cursos Libres y Costos Base:**
  1. *Inglés Técnico:* \$350.000 COP
  2. *Programación Web:* \$420.000 COP
  3. *Ciberseguridad:* \$500.000 COP
- **Reglas Financieras Oficiales:**
  1. **Beca de Excelencia UTS:** Descuento del **50%** calculado directamente sobre el costo base del curso seleccionado.  
     $$\text{Descuento Beca} = \text{Costo Base} \times 0.50$$
     $$\text{Subtotal} = \text{Costo Base} - \text{Descuento Beca}$$
  2. **Financiación con ICETEX:** Recargo administrativo del **5%** calculado sobre el **subtotal resultante** (después de aplicar el descuento de la beca, en caso de aplicar).  
     $$\text{Recargo ICETEX} = \text{Subtotal} \times 0.05$$
     $$\text{Total Final a Pagar} = \text{Subtotal} + \text{Recargo ICETEX}$$
- **Bloqueo de Seguridad (Desafío de Integridad del Sistema):**
  Un estudiante **no puede liquidar ningún curso** si no ha registrado previamente un perfil académico válido en el Módulo 1. En la interfaz se presenta una tarjeta de advertencia roja con ícono de candado, el botón de liquidación permanece inhabilitado y el ViewModel rechaza cualquier invocación directa garantizando que ninguna transacción huérfana ingrese a la base de datos.

### 3.3 Módulo 3: Historial y Consolidado Financiero
- Muestra el listado cronológico de todas las liquidaciones generadas, indicando fecha y hora exacta, nombre del curso, estudiante asociado, costo base, beneficios aplicados y costo total.
- **Cálculo Centralizado del «Total Acumulado a Pagar»:** El consolidado económico no se calcula en la vista de Compose, sino que es computado por el `HistoryViewModel` a partir de un flujo reactivo (`Flow`) emitido por Room Database.
- Permite la limpieza/reinicio seguro del historial mediante un diálogo de confirmación emergente (`AlertDialog`).

### 3.4 Dashboard de Control General
- Saluda al estudiante con su nombre y correo institucional cuando su perfil está registrado.
- Muestra un resumen financiero destacado con el acumulado global y el número de cursos liquidados.
- Funciona como centro de mando con accesos directos e indicadores dinámicos del estado de cada módulo (*Completado*, *Pendiente*, *Habilitado*, *Bloqueado*).

---

## 4. ARQUITECTURA DE SOFTWARE Y PATRONES DE DISEÑO

El proyecto aplica rigurosamente las pautas de arquitectura recomendadas por Google para Android moderno:

```
               ARQUITECTURA DE TRES CAPAS EN EL PROYECTO UTS

+--------------------------------------------------------------------------+
|                        CAPA DE PRESENTACIÓN (UI)                         |
|  [DashboardScreen]   [ProfileScreen]   [EnrollmentScreen]  [HistoryScreen]
|          |                 |                  |                  |       |
|  Observa StateFlow  Observa StateFlow  Observa StateFlow Observa StateFlow|
|  Envía Eventos      Envía Eventos      Envía Eventos     Envía Eventos   |
+----------|-----------------|------------------|------------------|-------+
           v                 v                  v                  v
+--------------------------------------------------------------------------+
|                         CAPA DE ESTADO (VIEWMODEL)                       |
| [DashboardViewModel] [ProfileViewModel] [EnrollmentViewModel] [HistoryVM]|
|                                                                          |
|       • Administra MutableStateFlow / Expone StateFlow inmutable         |
|       • Contiene lógica de negocio pura y fórmulas financieras           |
|       • Desacoplado totalmente de las vistas de Android / Compose        |
+-----------------------------------|--------------------------------------+
                                    v
+--------------------------------------------------------------------------+
|                         CAPA DE DOMINIO Y DATOS                          |
|             [ProfileRepository]     <--->     [TuitionRepository]        |
|                                    |                                     |
|                 [StudentProfileDao] <---> [TuitionLiquidationDao]        |
|                                    |                                     |
|                               [UtsDatabase]                              |
|                              (SQLite / Room)                             |
+--------------------------------------------------------------------------+
```

### 4.1 Patrón Arquitectónico MVVM (Model-View-ViewModel)
- **Model:** Clases de datos inmutables (`StudentProfile`, `TuitionLiquidation`, `ExtracurricularCourse`) y entidades de persistencia.
- **View:** Funciones `@Composable` puras y libres de lógica de negocio o de formateo financiero directo. Su única responsabilidad es dibujar la interfaz y transmitir eventos de usuario (clicks, entradas de texto, switches).
- **ViewModel:** Mantiene el estado de la UI persistente ante rotaciones o cambios de configuración, interactúa con los repositorios y ejecuta las transformaciones de estado.

### 4.2 Flujo Unidireccional de Datos (UDF - Unidirectional Data Flow)
Para evitar inconsistencias o efectos secundarios:
1. El ViewModel expone un único `StateFlow<UiState>` de solo lectura a la UI.
2. Internamente mantiene un `MutableStateFlow<UiState>` privado.
3. La UI emite eventos invocando funciones públicas del ViewModel.
4. El ViewModel procesa el evento, actualiza el estado interno mediante `.update { ... }`, y el nuevo estado desciende automáticamente a la UI mediante `collectAsStateWithLifecycle()`.

### 4.3 Inyección de Dependencias Manual (Manual DI con AppContainer)
Para evitar la sobrecarga y fragilidad de frameworks como Dagger o Hilt en entornos evaluativos, se implementó el patrón **Container Pattern**:
- `AppContainer` (interfaz) y `DefaultAppContainer` (implementación) instancian una única vez la base de datos `UtsDatabase` de manera diferida (`by lazy`) y proveen los repositorios requeridos.
- La clase `UtsApplication` expone el contenedor a nivel de aplicación (`applicationContext`).
- Cada ViewModel cuenta con un `ViewModelProvider.Factory` estático para instanciarse recibiendo únicamente sus repositorios inyectados por constructor.

### 4.4 Persistencia Local Reactiva con Room Database
Se utilizó **Room Database** (abstracción oficial de SQLite en Android):
- Permite operaciones asíncronas con corrutinas de Kotlin (`suspend fun`).
- Retorna flujos reactivos `Flow<List<T>>` que emiten nuevas emisiones instantáneamente cada vez que una tabla se modifica en disco, manteniendo el Dashboard y el Historial sincronizados en tiempo real sin requerir peticiones manuales de refresco.

### 4.5 Interfaz Gráfica con Jetpack Compose y Material Design 3
- Adopción integral de **Material 3 (M3)**.
- Soporte para tema claro y tema oscuro mediante `darkColorScheme` y `lightColorScheme`.
- Integración de **Edge-to-Edge** moderno (`enableEdgeToEdge()`) en `MainActivity`.
- Componentes oficiales: `Scaffold`, `TopAppBar`, `NavigationBar`, `ElevatedCard`, `ExposedDropdownMenuBox` y `AlertDialog`.

---

## 5. EXPLICACIÓN DETALLADA DEL CÓDIGO FUENTE POR CAPAS

A continuación se desglosa el rol, estructura y justificación de cada archivo que compone el proyecto.

### 5.1 Capa de Modelo y Dominio (`co.edu.uts.serviciosestudiantiles.data.model`)

#### `Faculty.kt`
Enum que modela las dos facultades institucionales:
```kotlin
enum class Faculty(val displayName: String) {
    INGENIERIAS("Ingenierías"),
    CIENCIAS_SOCIOECONOMICAS("Ciencias Socioeconómicas");

    companion object {
        fun fromDisplayName(name: String): Faculty? {
            return entries.find { it.displayName.equals(name, ignoreCase = true) }
        }
    }
}
```
*Propósito:* Elimina cadenas quemadas en la interfaz y garantiza tipado estricto. El método utilitario `fromDisplayName` permite deserializar valores almacenados en texto plano desde la base de datos hacia el enum tipado.

#### `StudentProfile.kt`
Modelo del perfil del estudiante registrado:
```kotlin
data class StudentProfile(
    val id: Long = 1L,
    val fullName: String,
    val studentCode: String,
    val faculty: Faculty,
    val institutionalEmail: String,
    val registeredAt: Long = System.currentTimeMillis()
) {
    val isValid: Boolean
        get() = fullName.isNotBlank() && studentCode.isNotBlank() && institutionalEmail.isNotBlank()
}
```
*Propósito:* Representa el estudiante activo en sesión. La propiedad calculada `isValid` valida que ningún campo crítico esté vacío, siendo la base lógica utilizada por el Bloqueo de Seguridad.

#### `ExtracurricularCourse.kt`
Enum que centraliza la oferta académica institucional de educación continua:
```kotlin
enum class ExtracurricularCourse(
    val title: String,
    val basePrice: Double,
    val description: String
) {
    INGLES_TECNICO(
        title = "Inglés Técnico",
        basePrice = 350_000.0,
        description = "Competencias comunicativas en inglés para entornos profesionales y tecnológicos."
    ),
    PROGRAMACION_WEB(
        title = "Programación Web",
        basePrice = 420_000.0,
        description = "Desarrollo moderno frontend y backend con tecnologías web actuales."
    ),
    CIBERSEGURIDAD(
        title = "Ciberseguridad",
        basePrice = 500_000.0,
        description = "Fundamentos de protección de redes, seguridad de datos y ethical hacking."
    );

    companion object {
        fun fromTitle(title: String): ExtracurricularCourse {
            return entries.firstOrNull { it.title.equals(title, ignoreCase = true) } ?: PROGRAMACION_WEB
        }
    }
}
```
*Propósito:* Mantiene las tarifas oficiales (\$350.000, \$420.000 y \$500.000 COP) inmutables y centralizadas en un único punto del código fuente.

#### `TuitionLiquidation.kt`
Modelo representativo de una liquidación consolidada:
```kotlin
data class TuitionLiquidation(
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
    val timestamp: Long = System.currentTimeMillis()
) {
    val formattedDate: String
        get() {
            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale("es", "CO"))
            return sdf.format(Date(timestamp))
        }
}
```
*Propósito:* Guarda la instantánea matemática de la liquidación para auditoría. La propiedad `formattedDate` estandariza la fecha al formato colombiano (`dd/MM/yyyy hh:mm a`).

---

### 5.2 Capa de Base de Datos Local (`co.edu.uts.serviciosestudiantiles.data.local`)

#### `UtsDatabase.kt`
Clase abstracta de configuración de Room:
```kotlin
@Database(
    entities = [
        StudentProfileEntity::class,
        TuitionLiquidationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class UtsDatabase : RoomDatabase() {
    abstract fun studentProfileDao(): StudentProfileDao
    abstract fun tuitionLiquidationDao(): TuitionLiquidationDao

    companion object {
        @Volatile
        private var INSTANCE: UtsDatabase? = null

        fun getDatabase(context: Context): UtsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UtsDatabase::class.java,
                    "uts_servicios_estudiantiles.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```
*Propósito:* Implementa el patrón Singleton seguro frente a concurrencia (`@Volatile` y bloque `synchronized`) para crear y compartir la base de datos `uts_servicios_estudiantiles.db`.

#### `StudentProfileDao.kt` y `TuitionLiquidationDao.kt`
Interfaces de acceso a datos mediante SQL enriquecido:
- `StudentProfileDao`: Permite observar reactivamente el perfil único mediante `fun getProfileFlow(): Flow<StudentProfileEntity?>`, insertar o sobreescribir el perfil con política de conflicto `OnConflictStrategy.REPLACE`, y reiniciar el registro.
- `TuitionLiquidationDao`: Consulta todas las liquidaciones en orden cronológico descendente (`ORDER BY timestamp DESC`), inserta nuevos comprobantes y expone la consulta de agregación:
  ```sql
  SELECT COALESCE(SUM(totalAmount), 0.0) FROM tuition_liquidations
  ```
  La función `COALESCE` garantiza que si la tabla está vacía, Room devuelva `0.0` en lugar de `null`.

#### `StudentProfileEntity.kt` y `TuitionLiquidationEntity.kt`
Entidades mapeadas a tablas SQLite con métodos de mapeo bidireccional `toDomain()` y `fromDomain(model)`. Esto aísla la estructura de base de datos de los modelos que manejan la interfaz de usuario.

---

### 5.3 Capa de Repositorios e Inyección (`co.edu.uts.serviciosestudiantiles.data.repository`)

#### `ProfileRepository.kt` y `TuitionRepository.kt`
Interfaces y clases concretas (`RoomProfileRepository`, `RoomTuitionRepository`):
- Ocultan la complejidad de Room a los ViewModels.
- Transforman entidades de persistencia a modelos de dominio limpios mediante operadores de Flow (`.map { entity -> entity.toDomain() }`).

#### `AppContainer.kt` y `UtsApplication.kt`
Implementación de Inyección Manual:
```kotlin
class DefaultAppContainer(private val context: Context) : AppContainer {
    private val database: UtsDatabase by lazy {
        UtsDatabase.getDatabase(context)
    }

    override val profileRepository: ProfileRepository by lazy {
        RoomProfileRepository(database.studentProfileDao())
    }

    override val tuitionRepository: TuitionRepository by lazy {
        RoomTuitionRepository(database.tuitionLiquidationDao())
    }
}
```
`UtsApplication` hereda de `android.app.Application` y crea el contenedor en su `onCreate()`. Se encuentra declarada en el `AndroidManifest.xml` en la etiqueta `<application android:name=".UtsApplication">`.

---

### 5.4 Capa de Lógica y Estado: ViewModels (`co.edu.uts.serviciosestudiantiles.viewmodel.*`)

#### Módulo 1: `ProfileViewModel.kt` y `ProfileUiState.kt`
- **Generador de Correo Institucional:**
  La función `calculateInstitutionalEmail` implementa una normalización idiomática de dos pasos:
  ```kotlin
  private fun normalizeString(input: String): String {
      val nfd = Normalizer.normalize(input, Normalizer.Form.NFD)
      val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
      return pattern.matcher(nfd).replaceAll("").lowercase()
  }
  ```
  Elimina acentos antes de reemplazar secuencias de espacios por un solo punto, garantizando direcciones de correo como `carlos.andres.perez.10987654@uts.edu.co`.
- **Validaciones en Tiempo Real:** Verifica que el nombre supere 3 caracteres y que el código contenga caracteres válidos. Al presionar "Guardar Perfil", almacena en base de datos y activa la bandera `isSaved = true`.

#### Módulo 2: `EnrollmentViewModel.kt` y `EnrollmentUiState.kt`
- **Fórmulas de Liquidación:**
  ```kotlin
  val base = course.basePrice
  val discount = if (scholarship) round(base * 0.50) else 0.0
  val subtotal = base - discount
  val surcharge = if (icetex) round(subtotal * 0.05) else 0.0
  val total = subtotal + surcharge
  ```
  Aplica la función matemática `round()` para evitar problemas de precisión en números de punto flotante.
- **Bloqueo de Seguridad en Código:**
  ```kotlin
  fun onLiquidate() {
      val state = _uiState.value
      if (!state.isProfileCompleted || state.currentStudent == null) {
          _uiState.update {
              it.copy(securityLockMessage = "Operación denegada: debes completar el perfil académico antes de liquidar.")
          }
          return
      }
      // Procede a persistir en TuitionRepository...
  }
  ```
  El bloqueo se verifica tanto en la vista (deshabilitando el botón) como en el ViewModel (abortando la ejecución), proporcionando **defensa en profundidad**.

#### Módulo 3: `HistoryViewModel.kt` y `HistoryUiState.kt`
- Se suscribe al `getAllLiquidationsFlow()` del repositorio.
- Calcula el consolidado financiero de forma centralizada:
  ```kotlin
  val accumulated = list.sumOf { it.totalAmount }
  ```
- Provee la operación `clearHistory()` que borra todos los registros en base de datos de forma asíncrona dentro del `viewModelScope`.

#### General: `DashboardViewModel.kt` y `DashboardUiState.kt`
- Combina reactivamente dos flujos de datos (`profileRepository.getProfileFlow()` y `tuitionRepository.getAllLiquidationsFlow()`) mediante el operador `combine(...)` de corrutinas para emitir un estado unificado con los contadores y el estado del estudiante en el portal.

---

### 5.5 Capa de Interfaz de Usuario y Navegación (`co.edu.uts.serviciosestudiantiles.ui.*`)

#### `MainActivity.kt`
Punto de entrada de la aplicación.
- Configura `enableEdgeToEdge()`.
- Obtiene `appContainer` desde `(application as UtsApplication).container`.
- Inicializa los cuatro ViewModels utilizando sus respectivas factorías desacopladas.
- Administra el estado de la pantalla activa mediante una variable `var currentScreen by remember { mutableStateOf(UtsScreen.DASHBOARD) }`.
- Envuelve las vistas dentro de un `Scaffold` con `UtsTopAppBar` y `UtsBottomNavigation`.

#### `ProfileScreen.kt`
Pantalla del Módulo 1. Contiene:
- Formulario de captura de datos con `OutlinedTextField` reactivos a errores de validación.
- Selector de foco `val focusManager = LocalFocusManager.current` para navegación fluida mediante el teclado en pantalla.
- Selector desplegable de facultad (`ExposedDropdownMenuBox`) con la API vigente de Material 3:
  ```kotlin
  Modifier
      .fillMaxWidth()
      .menuAnchor(MenuAnchorType.PrimaryNotEditable)
  ```
- Tarjeta de previsualización dinámica del correo institucional generado en tiempo real.
- Tarjeta de confirmación con el resumen del perfil activo guardado.

#### `EnrollmentScreen.kt`
Pantalla del Módulo 2. Contiene:
- Componente visual de **Bloqueo de Seguridad:** Tarjeta en color contenedor de error (`errorContainer`) con botón de redirección inmediata al Módulo 1 si el perfil no existe.
- Catálogo de cursos representados en tarjetas clicables que resaltan visualmente el curso seleccionado con el color verde institucional (`UtsGreenContainer`).
- `Checkbox` para alternar la Beca de Excelencia UTS (50%).
- `Switch` interactivo para financiar con crédito ICETEX (5%).
- Tarjeta de **Desglose Financiero en Tiempo Real** que detalla Costo Base, Descuento, Subtotal, Recargo y Total Final a Pagar.
- Botón de Liquidación que se inhabilita automáticamente cuando el Bloqueo de Seguridad está activo.

#### `HistoryScreen.kt`
Pantalla del Módulo 3. Contiene:
- Tarjeta principal con gradiente institucional verde (`Brush.horizontalGradient`) que destaca el **Consolidado Global Acumulado a Pagar**.
- Lista reactiva construida con `LazyColumn` que renderiza tarjetas de liquidación individuales mostrando la fecha, el curso, el estudiante y el desglose de valores.
- Diálogo modal de confirmación (`AlertDialog`) para vaciar el historial de forma segura.

#### `DashboardScreen.kt`
Pantalla de inicio que consolida el estado global del sistema, bienvenida personalizada y tarjetas modulares de acceso rápido.

#### Componentes, Tema y Navegación
- `UtsTopAppBar.kt`: Barra superior con el escudo vectorial de las UTS (`ic_uts_logo.xml`) y título dinámico según la sección.
- `UtsBottomNavigation.kt`: Barra de navegación inferior con items correspondientes al enum `UtsScreen`.
- `Color.kt` y `Theme.kt`: Paleta cromática oficial de las Unidades Tecnológicas de Santander:
  * Verde Primario: `#006837`
  * Verde Oscuro: `#004724`
  * Verde Claro: `#2E854B`
  * Ámbar / Dorado de Acento: `#FBB03B`
- `CurrencyUtils.kt`: Formateador numérico oficial para pesos colombianos (`Locale("es", "CO")`), transformando valores a representaciones legibles como `$ 220.500 COP`.

---

### 5.6 Módulo de Pruebas Unitarias Automatizadas (`src/test`)

El proyecto incluye una batería exhaustiva de pruebas unitarias que no dependen del framework de Android y ejecutan a máxima velocidad utilizando `kotlinx-coroutines-test`:

- `FakeRepositories.kt`: Implementaciones de prueba en memoria (`FakeProfileRepository`, `FakeTuitionRepository`) que simulan la base de datos Room mediante `MutableStateFlow`.
- `ProfileViewModelTest.kt`: Certifica que el generador de correos normaliza acentos (*"Carlos Andrés Pérez"* $\rightarrow$ *"carlos.andres.perez.10987654@uts.edu.co"*), gestiona errores si el nombre tiene menos de 3 caracteres y persiste perfiles correctamente.
- `EnrollmentViewModelTest.kt`: Valida matemáticamente los costos base, la aplicación del 50% de beca, el 5% de ICETEX y la combinación de ambos, además de certificar que el Bloqueo de Seguridad impide liquidar sin perfil.
- `HistoryViewModelTest.kt`: Comprueba que el total acumulado inicie en \$0 COP, que incremente con cada liquidación y que se reinicie al invocar `clearHistory()`.

---

## 6. FÓRMULAS FINANCIERAS Y CASOS DE PRUEBA MATEMÁTICOS

A continuación se presentan los escenarios oficiales de liquidación financiera implementados en el Módulo 2 y validados mediante pruebas unitarias:

### Matriz de Tarifas Oficiales y Casos de Liquidación

| Curso | Costo Base | Beca UTS (50%) | ICETEX (5%) | Descuento | Subtotal | Recargo | Total a Pagar |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **Inglés Técnico** | \$350.000 | No | No | \$0 | \$350.000 | \$0 | **\$350.000 COP** |
| **Inglés Técnico** | \$350.000 | Sí | No | \$175.000 | \$175.000 | \$0 | **\$175.000 COP** |
| **Programación Web** | \$420.000 | No | Sí | \$0 | \$420.000 | \$21.000 | **\$441.000 COP** |
| **Programación Web** | \$420.000 | Sí | Sí | \$210.000 | \$210.000 | \$10.500 | **\$220.500 COP** |
| **Ciberseguridad** | \$500.000 | Sí | No | \$250.000 | \$250.000 | \$0 | **\$250.000 COP** |
| **Ciberseguridad** | \$500.000 | Sí | Sí | \$250.000 | \$250.000 | \$12.500 | **\$262.500 COP** |

### Demostración Matemática del Caso Mixto (Beca + ICETEX)
Tomando el curso de **Programación Web** (\$420.000 COP) con ambos beneficios seleccionados:
1. $\text{Costo Base} = \$420.000$
2. $\text{Descuento por Beca (50\%)} = \$420.000 \times 0.50 = \$210.000$
3. $\text{Subtotal con Beca} = \$420.000 - \$210.000 = \$210.000$
4. $\text{Recargo ICETEX (5\% sobre Subtotal)} = \$210.000 \times 0.05 = \$10.500$
5. $\text{Total a Pagar} = \$210.000 + \$10.500 = \mathbf{\$220.500\text{ COP}}$

Este valor coincide exactamente con el caso de prueba estipulado en las directrices docentes y verificado en `EnrollmentViewModelTest.kt`.

---

## 7. GUÍA DE EJECUCIÓN Y OPERACIÓN EN ANDROID STUDIO

### Requisitos del Entorno
- **IDE:** Android Studio Ladybug (2024.2.1) o superior.
- **JDK:** Java Development Kit 17 (versión estándar configurada en `compileOptions` y `jvmTarget`).
- **Android SDK:** Compile SDK 35, Min SDK 26 (Android 8.0 Oreo), Target SDK 35.
- **Gradle:** Versión de Gradle Wrapper 8.10.2 con Android Gradle Plugin (AGP) 8.7.2.

### Pasos para Ejecutar el Proyecto
1. Abrir Android Studio.
2. Seleccionar la opción **Open** y navegar hasta la carpeta del proyecto:  
   `C:\Users\Usuario\Desktop\ANPHERNEE\uts_servicios_estudiantiles`
3. Esperar a que Android Studio finalice la indexación y la sincronización de Gradle (`Sync Project with Gradle Files`).
4. Verificar que la ventana **Problems / Project Errors** reporte **0 errores de compilación**.
5. Seleccionar un emulador Android (por ejemplo, Pixel 8 con API 34 o 35) o un dispositivo físico con depuración USB habilitada.
6. Presionar el botón verde **RUN** (`Shift + F10`).
7. La aplicación compilará en modo Debug e iniciará directamente en el **Portal Estudiantil UTS**.

---

## 8. CONCLUSIONES Y LECCIONES APRENDIDAS

1. **Eficiencia de la Arquitectura MVVM + UDF:** La separación de responsabilidades permitió mantener una lógica de negocio pura y testeable, aislando a Compose de las operaciones de base de datos y garantizando que la UI sea una representación reactiva pura del estado.
2. **Defensa en Profundidad con el Bloqueo de Seguridad:** La implementación del bloqueo tanto en el componente visual (deshabilitando el botón y mostrando alertas informativas) como en el ViewModel (abortando cualquier ejecución no autorizada) demuestra la aplicación correcta de patrones de integridad de software.
3. **Calidad de Código y Mantenibilidad:** La corrección rigurosa de referencias (`LocalFocusManager.current`), la adopción de APIs vigentes en Material 3 (`MenuAnchorType.PrimaryNotEditable`) y el mantenimiento de una suite de pruebas automatizadas aseguran un ciclo de vida duradero y libre de regresiones técnicas.
4. **Identidad Institucional Lograda:** El software cumple con todos los requerimientos funcionales y estéticos exigidos por las Unidades Tecnológicas de Santander, ofreciendo una experiencia de usuario fluida, intuitiva y profesional.
