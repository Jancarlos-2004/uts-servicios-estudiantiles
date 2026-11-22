# 🚀 Sistema Integrado de Servicios Estudiantiles — UTS
**Unidades Tecnológicas de Santander**  
*Miniproyecto Final de Programación Móvil Nativa Android*

---

## 📋 Descripción General del Proyecto
Aplicación móvil nativa desarrollada para los estudiantes de las **Unidades Tecnológicas de Santander (UTS)**. Permite la autogestión de servicios estudiantiles a través de tres módulos esenciales:
1. **Módulo 1 — Registro y Perfil Académico:** Captura de datos, validaciones institucionales y generación automática de correo institucional simulado (`nombre.codigo@uts.edu.co`).
2. **Módulo 2 — Liquidación de Matrícula Extracurricular:** Selección de cursos libres, aplicación de beneficios (Beca de Excelencia UTS al 50%) y financiación ICETEX (5% de recargo administrativo), con **Bloqueo de Seguridad (Desafío del Semestre)**.
3. **Módulo 3 — Historial y Consolidado Financiero:** Listado reactivo de liquidaciones y consolidado en tiempo real del «Total Acumulado a Pagar».

---

## 🏛️ Arquitectura Obligatoria: MVVM Estricto y UDF

El proyecto implementa una arquitectura **MVVM (Model-View-ViewModel)** rigurosa con separación de responsabilidades en tres capas fundamentales:

```
co.edu.uts.serviciosestudiantiles/
├── data/                      # CAPA DE DATOS (Sin dependencias de UI)
│   ├── model/                 # Modelos de dominio puros (StudentProfile, TuitionLiquidation, Faculty, ExtracurricularCourse)
│   ├── local/                 # Persistencia Local con Room Database
│   │   ├── entity/            # Entidades de base de datos SQLite (StudentProfileEntity, TuitionLiquidationEntity)
│   │   ├── dao/               # Data Access Objects reactivos con Flow (StudentProfileDao, TuitionLiquidationDao)
│   │   └── UtsDatabase.kt     # Definición de RoomDatabase
│   └── repository/            # Repositorios (ProfileRepository, TuitionRepository, AppContainer)
│
├── viewmodel/                 # CAPA DE LÓGICA DE NEGOCIO (Sin elementos Compose)
│   ├── profile/               # ProfileViewModel + ProfileUiState (Generación de correo, validaciones)
│   ├── enrollment/            # EnrollmentViewModel + EnrollmentUiState (Fórmulas financieras, Bloqueo de Seguridad)
│   ├── history/               # HistoryViewModel + HistoryUiState (Cálculo del Total Acumulado en tiempo real)
│   └── dashboard/             # DashboardViewModel + DashboardUiState (Resumen del portal estudiantil)
│
└── ui/                        # CAPA DE PRESENTACIÓN (Jetpack Compose + Material Design 3)
    ├── theme/                 # Identidad visual UTS (Colores institucionales, tipografías y tema M3)
    ├── navigation/            # Rutas y barra de navegación inferior (UtsScreen)
    ├── components/            # Componentes reutilizables (UtsTopAppBar, UtsBottomNavigation, CurrencyUtils)
    └── screens/               # Pantallas Compose (DashboardScreen, ProfileScreen, EnrollmentScreen, HistoryScreen)
```

### 🔄 Flujo Unidireccional de Datos (UDF)
1. **Estado Inmutable:** Los ViewModels encapsulan el estado mediante:
   ```kotlin
   private val _uiState = MutableStateFlow(UiState())
   val uiState: StateFlow<UiState> = _uiState.asStateFlow()
   ```
2. **Eventos:** La UI nunca altera las variables del ViewModel directamente; envía eventos (ej: `onCourseSelected()`, `onScholarshipToggled()`, `onSaveProfile()`).
3. **Consumo Eficiente con Ciclo de Vida:** Todas las pantallas observan el estado usando:
   ```kotlin
   val state by viewModel.uiState.collectAsStateWithLifecycle()
   ```
   Esto evita fugas de memoria y detiene la recolección cuando la pantalla pasa a segundo plano.

---

## 🧮 Fórmulas Financieras y Reglas de Negocio

### 1. Generación de Correo Institucional (Módulo 1)
- Formato: `nombre.codigo@uts.edu.co`
- Sanitización estricta en `ProfileViewModel`:
  - Remueve tildes y marcas diacríticas (`á` -> `a`, `ñ` -> `n`).
  - Convierte a minúsculas.
  - Reemplaza espacios por puntos (`Carlos Andrés` -> `carlos.andres`).
  - Concatena el código estudiantil.

### 2. Liquidación de Cursos Extracurriculares (Módulo 2)
Cursos y costos base oficiales:
- **Inglés Técnico:** $350.000 COP
- **Programación Web:** $420.000 COP
- **Ciberseguridad:** $500.000 COP

Procedimiento matemático implementado en `EnrollmentViewModel`:
$$\text{Descuento Beca} = \begin{cases} \text{Costo Base} \times 0.50 & \text{si Beca Activa} \\ 0 & \text{en caso contrario} \end{cases}$$
$$\text{Subtotal} = \text{Costo Base} - \text{Descuento Beca}$$
$$\text{Recargo ICETEX} = \begin{cases} \text{Subtotal} \times 0.05 & \text{si Financia con ICETEX} \\ 0 & \text{en caso contrario} \end{cases}$$
$$\text{Total Final} = \text{Subtotal} + \text{Recargo ICETEX}$$

*Ejemplo de verificación oficial:*
- Curso: Programación Web ($420.000 COP)
- Beca: 50% = -$210.000 COP $\rightarrow$ Subtotal: $210.000 COP
- ICETEX: 5% sobre subtotal = +$10.500 COP
- **Total Final:** $220.500 COP

### 3. Bloqueo de Seguridad (Desafío del Semestre)
- Si el perfil académico está vacío o incompleto, el botón **«Liquidar» permanece inhabilitado (`enabled = false`)**.
- El `EnrollmentViewModel` valida internamente el estado del estudiante y rechaza cualquier solicitud si no existe registro previo en el Módulo 1.
- La interfaz despliega una tarjeta de alerta con un acceso directo para registrar el perfil de inmediato.

### 4. Consolidado Financiero en Tiempo Real (Módulo 3)
- La tarjeta destacada en la parte superior calcula la sumatoria acumulada de todas las liquidaciones persistidas mediante:
  ```kotlin
  val accumulated = list.sumOf { it.totalAmount }
  ```
- No se realiza ningún cálculo matemático dentro de los composables de la UI.

---

## 💾 Persistencia de Información con Room Database
Se implementó persistencia local robusta mediante **Android Jetpack Room**:
- **`UtsDatabase`**: Base de datos SQLite gestionada (`uts_servicios_estudiantiles.db`).
- **`StudentProfileEntity` & `StudentProfileDao`**: Almacena el perfil activo de manera permanente.
- **`TuitionLiquidationEntity` & `TuitionLiquidationDao`**: Guarda cada transacción histórica con su fecha y desglose.
- **Patrón Repositorio**: La UI y los ViewModels desconocen los detalles SQL de la base de datos; consumen `Flow<StudentProfile?>` y `Flow<List<TuitionLiquidation>>`.

---

## 🎨 Identidad Visual Institucional UTS
- **Verde Institucional Principal:** `#006837`
- **Ámbar / Dorado Secundario:** `#FBB03B`
- **Verde Oscuro de Contraste:** `#004724`
- **Fondo Limpio M3:** `#F9FAF9` / `#FFFFFF`
- **Componentes Material Design 3:** `Card`, `ElevatedCard`, `Column`, `Row`, `LazyColumn`, `OutlinedTextField`, `ExposedDropdownMenuBox`, `Checkbox`, `Switch`, `NavigationBar`.

---

## 🛠️ Tecnologías y Dependencias
- **Lenguaje:** Kotlin 2.0.21
- **UI Toolkit:** Jetpack Compose (BOM 2024.10.01)
- **Diseño:** Material Design 3
- **Ciclo de Vida:** Lifecycle Runtime Compose 2.8.7 (`collectAsStateWithLifecycle`)
- **Persistencia:** Room Database 2.6.1 + KSP
- **Pruebas Unitarias:** JUnit 4 + Kotlinx Coroutines Test

---

## 🚀 Instrucciones para Abrir y Ejecutar en Android Studio

1. **Abrir Android Studio** (Ladybug, Koala o superior).
2. Seleccionar **File > Open...** y navegar hasta la carpeta:
   `uts_servicios_estudiantiles`
3. Esperar que Gradle sincronice las dependencias del catálogo de versiones (`gradle/libs.versions.toml`).
4. Seleccionar un emulador Android (API 26+) o un dispositivo físico conectado con depuración USB habilitada.
5. Hacer clic en **Run 'app'** (ícono de reproducción verde o `Shift + F10`).
6. Para ejecutar las pruebas unitarias:
   - Clic derecho en la carpeta `app/src/test` $\rightarrow$ **Run 'Tests in 'co.edu.uts.serviciosestudiantiles''**.

