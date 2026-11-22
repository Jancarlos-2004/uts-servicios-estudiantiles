# 🎓 Guía Oficial de Sustentación Universitaria — UTS
## Miniproyecto Final: Sistema Integrado de Servicios Estudiantiles UTS
**Materia:** Programación Móvil Nativa Android  
**Tecnologías:** Kotlin + Jetpack Compose + MVVM + StateFlow + Material Design 3 + Room

---

## 🎯 Resumen Ejecutivo de la Solución
Esta aplicación fue desarrollada siguiendo estrictamente el documento del docente. El sistema unifica tres módulos críticos:
1. **Módulo 1 (Perfil Académico):** Registro, validación y derivación automática del correo institucional institucional simulado.
2. **Módulo 2 (Liquidación Extracurricular):** Selección de cursos, cálculo exacto de becas (50%) y recargos ICETEX (5%), con **bloqueo de seguridad** si el perfil no existe.
3. **Módulo 3 (Historial y Consolidado):** Lista reactiva en `LazyColumn` y cálculo global en tiempo real de «Total Acumulado a Pagar».

---

## 💡 Preguntas Clave del Profesor y Respuestas Técnicas

### 1. ¿Por qué se utilizó la arquitectura MVVM y qué responsabilidad tiene cada paquete?
> **Respuesta:**  
> "Profesor, utilizamos **MVVM (Model-View-ViewModel)** porque garantiza la separación absoluta de responsabilidades recomendada por Google:
> - **`data`:** Maneja exclusivamente los modelos de dominio, las entidades de Room y los repositorios. No tiene ninguna referencia a la interfaz gráfica ni a Compose.
> - **`viewmodel`:** Es el cerebro del módulo; almacena el estado de la UI de forma reactiva, ejecuta las reglas de negocio (como el cálculo del 50% de beca o el 5% de ICETEX) y valida los formularios. Sobrevive a cambios de configuración como la rotación de pantalla.
> - **`ui`:** Es puramente declarativa mediante Jetpack Compose. Solo renderiza el estado que le entrega el ViewModel y le emite eventos de usuario cuando se presiona un botón o se cambia un switch. Jamás contiene cálculos financieros ni lógica de base de datos."

---

### 2. ¿Cómo funciona el Flujo Unidireccional de Datos (UDF) y el encapsulamiento de StateFlow?
> **Respuesta:**  
> "En cada ViewModel aplicamos el principio de encapsulación estricta:
> ```kotlin
> private val _uiState = MutableStateFlow(ProfileUiState())
> val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
> ```
> - El `MutableStateFlow` es **privado** (`_uiState`), lo que garantiza que solo los métodos del ViewModel puedan mutar el estado.
> - Se expone a la vista una versión inmutable de sólo lectura mediante `asStateFlow()`.
> - La UI emite eventos hacia el ViewModel (`onCourseSelected`, `onSaveProfile`), el ViewModel procesa la regla y emite un nuevo estado inmutable que recompone la UI automáticamente. Así se evita la manipulación accidental del estado desde las pantallas."

---

### 3. ¿Por qué usamos `collectAsStateWithLifecycle()` en lugar de `collectAsState()` tradicional?
> **Respuesta:**  
> "`collectAsState()` mantiene activa la recolección del Flow incluso cuando la pantalla pasa a segundo plano o está detenida (STOPPED), consumiendo memoria, procesamiento y batería innecesariamente.  
> Por el contrario, `collectAsStateWithLifecycle()` es consciente del ciclo de vida de Android: suspende la recolección cuando la actividad no es visible y la reanuda automáticamente cuando vuelve a primer plano (`Lifecycle.State.STARTED`), evitando fugas de memoria y optimizando el rendimiento."

---

### 4. ¿Por qué los cálculos financieros NUNCA deben realizarse dentro de los componentes Compose?
> **Respuesta:**  
> "En Jetpack Compose ocurre el proceso de **recomposición continua**. Cualquier cambio menor de estado o redibujado de la pantalla puede ejecutar el cuerpo del composable decenas o cientos de veces por segundo.  
> Si colocáramos cálculos matemáticos, formateo de divisas complejo o consultas en la pantalla, provocaríamos caídas de fotogramas (*jank*) y estados inconsistentes. En nuestra arquitectura, el cálculo se realiza **una única vez en el ViewModel** cuando cambia un parámetro de entrada y se expone listo para ser visualizado."

---

### 5. ¿Cómo implementaron el «Bloqueo de Seguridad» (Desafío del Semestre)?
> **Respuesta:**  
> "El bloqueo de seguridad se resolvió en dos niveles para máxima robustez:
> 1. **Nivel UI:** El botón «Liquidar» tiene la propiedad `enabled = state.canLiquidate`. Si no hay un perfil registrado, el botón se bloquea físicamente en la interfaz con estilo atenuado y se despliega una tarjeta de alerta con un botón directo al Módulo 1.
> 2. **Nivel ViewModel:** Dentro del método `onLiquidate()`, antes de generar cualquier transacción, se verifica:
>    ```kotlin
>    if (!state.isProfileCompleted || state.currentStudent == null) {
>        // Rechaza la operación y emite mensaje de bloqueo
>        return
>    }
>    ```
> Esto asegura que ni siquiera mediante eventos forzados se pueda violar la regla de negocio."

---

### 6. ¿Cómo se calculan el recargo del ICETEX y el consolidado global?
> **Respuesta:**  
> "Siguiendo la especificación matemática del profesor:
> 1. Obtenemos el costo base (ej. Programación Web = $420.000).
> 2. Si tiene beca, descontamos el 50% ($210.000) obteniendo un subtotal de $210.000.
> 3. El recargo del 5% del ICETEX se aplica **sobre el subtotal con descuento**, dando $10.500.
> 4. El total final es $220.500 COP.
> En el Módulo 3, el ViewModel suma en tiempo real todos los totales (`liquidations.sumOf { it.totalAmount }`) cada vez que el repositorio Room o en memoria emite una nueva lista, manteniendo la tarjeta destacada permanentemente sincronizada."

---

### 7. ¿Cómo se generó el correo institucional en el Módulo 1?
> **Respuesta:**  
> "Creamos una función de normalización en `ProfileViewModel` que utiliza `Normalizer` para descomponer caracteres con tildes (como `é`, `á`, `ñ`), eliminando los diacríticos con una expresión regular, convirtiendo a minúsculas y reemplazando espacios con puntos:
> - Entrada: `'Carlos Andrés Pérez'`, Código: `'10987654'`
> - Salida: `'carlos.andres.perez.10987654@uts.edu.co'`
> Esto previene correos con sintaxis inválida y asegura consistencia institucional."

---

## 🧪 Pruebas Unitarias Implementadas
El proyecto cuenta con un suite completo de pruebas unitarias en `app/src/test/java/co/edu/uts/serviciosestudiantiles/`:
- **`ProfileViewModelTest`:** Comprueba la normalización de correos, validación de nombres vacíos, códigos incompletos y persistencia.
- **`EnrollmentViewModelTest`:** Verifica los cálculos individuales y combinados (Beca + ICETEX con la fórmula del profesor) y el funcionamiento del bloqueo de seguridad.
- **`HistoryViewModelTest`:** Verifica el cálculo reactivo del Total Acumulado y la limpieza de registros.

