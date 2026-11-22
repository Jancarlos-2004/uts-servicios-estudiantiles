# SISTEMA INTEGRADO DE SERVICIOS ESTUDIANTILES UTS
## INFORME TÉCNICO Y MEMORIA DE ACTUALIZACIÓN INSTITUCIONAL OFICIAL

---

**INSTITUCIÓN:** Unidades Tecnológicas de Santander (UTS)  
**FACULTAD DE ADSCRIPCIÓN:** Facultad de Ciencias Naturales e Ingenierías / Facultad de Ciencias Socioeconómicas y Empresariales  
**PROYECTO:** Sistema Integrado de Servicios Estudiantiles UTS  
**PORTAL INSTITUCIONAL OFICIAL:** [https://www.uts.edu.co/](https://www.uts.edu.co/)  
**AUTOR / ESTUDIANTE:** Anphernee  
**AÑO LECTIVO / VIGENCIA:** 2026 – 2027  

---

## TABLA DE CONTENIDO

1. [RESUMEN EJECUTIVO Y OBJETIVO DE LA ACTUALIZACIÓN](#1-resumen-ejecutivo-y-objetivo-de-la-actualización)
2. [FUENTES INSTITUCIONALES Y REGISTRO DE TRAZABILIDAD](#2-fuentes-institucionales-y-registro-de-trazabilidad)
3. [ESTRUCTURA ACADÉMICA OFICIAL Y FACULTADES](#3-estructura-académica-oficial-y-facultades)
4. [CATÁLOGO DE PROGRAMAS ACADÉMICOS Y CÓDIGOS SNIES](#4-catálogo-de-programas-académicos-y-códigos-snies)
5. [SEDES FÍSICAS, CAMPUS Y MODALIDADES](#5-sedes-físicas-campus-y-modalidades)
6. [DERECHOS PECUNIARIOS Y TARIFAS POR VIGENCIA (2026 Y 2027)](#6-derechos-pecuniarios-y-tarifas-por-vigencia-2026-y-2027)
7. [ESTÍMULOS, BENEFICIOS, GRATUIDAD Y FINANCIACIÓN ICETEX](#7-estímulos-beneficios-gratuidad-y-financiación-icetex)
8. [OFERTA DE EDUCACIÓN CONTINUA Y EXTENSIÓN](#8-oferta-de-educación-continua-y-extensión)
9. [BIENESTAR INSTITUCIONAL Y SERVICIOS FORMATIVOS](#9-bienestar-institucional-y-servicios-formativos)
10. [ARQUITECTURA DE SOFTWARE, ROOM DATABASE Y MIGRACIONES](#10-arquitectura-de-software-room-database-y-migraciones)
11. [DESGLOSE DE PANTALLAS Y EXPERIENCIA DE USUARIO](#11-desglose-de-pantallas-y-experiencia-de-usuario)
12. [RESUMEN DE ARCHIVOS MODIFICADOS Y CREADOS](#12-resumen-de-archivos-modificados-y-creados)
13. [INSTRUCCIONES PARA ANDROID STUDIO (RUN)](#13-instrucciones-para-android-studio-run)

---

## 1. RESUMEN EJECUTIVO Y OBJETIVO DE LA ACTUALIZACIÓN

El presente informe documenta la actualización institucional integral del software móvil nativo en Android **Sistema Integrado de Servicios Estudiantiles UTS**. El proyecto ha sido intervenido quirúrgicamente para sustituir los datos preliminares y de ejemplo por información institucional verídica, vigente y respaldada por actos administrativos oficiales emanados de la Rectoría y el Consejo Directivo de las **Unidades Tecnológicas de Santander (UTS)**.

La actualización preservó el 100% de la arquitectura técnica establecida:
* **Lenguaje:** Kotlin 2.0.21.
* **Patrón de Arquitectura:** MVVM (Model-View-ViewModel) desacoplado.
* **Patrón de Estado:** Flujo Unidireccional de Datos (UDF) mediante `MutableStateFlow` privado y `StateFlow` inmutable público.
* **Capa de Persistencia:** Room Database con migración controlada `MIGRATION_1_2` que conserva intactos los perfiles e historiales preexistentes.
* **Interfaz de Usuario:** Jetpack Compose declarativo con Material Design 3 y Edge-to-Edge nativo.
* **Inyección de Dependencias:** Container Pattern manual mediante `AppContainer` y `DefaultAppContainer`.

---

## 2. FUENTES INSTITUCIONALES Y REGISTRO DE TRAZABILIDAD

De conformidad con las directrices de rigor y transparencia institucional, cada conjunto de datos incorporado en el sistema cuenta con trazabilidad a fuentes primarias:

| Identificador | Acto Administrativo / Fuente | Órgano Emisor | URL Oficial | Fecha Consulta | Vigencia | Datos Respaldados |
| :--- | :--- | :--- | :--- | :---: | :---: | :--- |
| **SRC-RES-2027** | Resolución UTS No. 02-1613 del 15 de septiembre de 2026 | Rectoría y Consejo Directivo UTS | [Valores Pecuniarios UTS](https://www.uts.edu.co/sitio/valores-pecuniarios/) | 19/11/2026 | **2027** | Derechos pecuniarios y complementarios 2027 (matrícula, idiomas, educación continua por hora, grados y certificados). |
| **SRC-RES-2026** | Resolución de Derechos Pecuniarios y Complementarios 2026 | Consejo Directivo UTS | [Valores Pecuniarios UTS](https://www.uts.edu.co/sitio/valores-pecuniarios/) | 19/11/2026 | **2026** | Matrícula tecnológica (\$2.626.358), universitaria (\$3.501.810) e inscripciones pregrado (\$315.163). |
| **SRC-OFERTA** | Catálogo Oficial de Oferta Académica y Registros Calificados SNIES | Vicerrectoría Académica UTS / MinEducación | [Oferta Académica UTS](https://www.uts.edu.co/sitio/oferta-academica-2/) | 19/11/2026 | Permanente | Programas en ciclos propedéuticos, facultades oficiales, códigos SNIES, sedes y modalidades. |
| **SRC-ESTIMULOS** | Reglamento de Estímulos y Distinciones (Acuerdo No. 01-035 de 2019 y modif.) | Consejo Directivo UTS | [Normatividad UTS](https://www.uts.edu.co/sitio/normatividad/) | 19/11/2026 | Permanente | Beca por Excelencia Académica (50% y 25%), Excelencia Deportiva, Cultural y Emprendimiento. |
| **SRC-ELECTORAL** | Ley 403 de 1997 (Art. 2 Num. 5) y Ley 815 de 2003 | Congreso de Colombia / UTS | [Función Pública](https://www.funcionpublica.gov.co/eva/gestornormativo/norma.php?i=344) | 19/11/2026 | Permanente | Descuento del 10% en matrícula por sufragio electoral acreditado. |
| **SRC-GRATUIDAD** | Política de Gratuidad "Puedo Estudiar" (Ley 2307 / Dec. 2271 de 2023) | MinEducación / UTS | [Gratuidad UTS](https://www.uts.edu.co/sitio/gratuidad/) | 19/11/2026 | Permanente | Cobertura del 100% de la matrícula neta para estratos 1, 2 y 3 sin título profesional previo. |
| **SRC-ICETEX** | Convenio Marco Institucional UTS - ICETEX | UTS / ICETEX | [ICETEX UTS](https://www.uts.edu.co/sitio/icetex/) | 19/11/2026 | Permanente | Financiación mediante crédito educativo sin recargo institucional por parte de la UTS (0%). |
| **SRC-ED-CONT** | Oficina de Extensión y Educación Continua | Vicerrectoría de Extensión UTS | [Educación Continua](https://www.uts.edu.co/sitio/educacion-continua/) | 19/11/2026 | 2026 - 2027 | Oferta de diplomados, talleres y cursos con tarifas horarias reglamentadas. |
| **SRC-BIENESTAR** | Dirección de Bienestar Institucional UTS | Bienestar Institucional UTS | [Bienestar UTS](https://www.uts.edu.co/sitio/bienestar-institucional/) | 19/11/2026 | Permanente | Servicios gratuitos de salud, gimnasio, deportes interfacultades y grupos culturales. |

---

## 3. ESTRUCTURA ACADÉMICA OFICIAL Y FACULTADES

Se eliminaron las categorías informales o inventadas, alineando la estructura del sistema a las **dos únicas facultades oficiales** de la universidad:

1. **Facultad de Ciencias Naturales e Ingenierías (`FCENI`):**  
   Reúne las disciplinas físico-naturales, tecnologías de la información, electrónica, electricidad, mecánica, medio ambiente, recursos petroleros, topografía y sistemas de telecomunicaciones.
2. **Facultad de Ciencias Socioeconómicas y Empresariales (`FCSE`):**  
   Agrupa las ciencias administrativas, contables, financieras, de mercadeo, gestión de la moda, economía, cultura física y negocios internacionales.

La clase enum `Faculty.kt` implementa un deserializador inteligente `fromDisplayName(name)` que garantiza retrocompatibilidad con registros anteriores de la base de datos mientras proyecta en la interfaz los nombres institucionales completos.

---

## 4. CATÁLOGO DE PROGRAMAS ACADÉMICOS Y CÓDIGOS SNIES

Todos los programas de pregrado de las UTS están estructurados bajo el modelo de **Ciclos Propedéuticos**: el estudiante cursa en primera instancia el ciclo tecnológico (6 semestres) y, tras titularse, puede culminar el ciclo universitario (4 semestres adicionales).

### 4.1 Facultad de Ciencias Naturales e Ingenierías
* **Tecnología en Desarrollo de Sistemas Informáticos:** SNIES 53488 | 6 semestres | Presencial y Virtual | Bucaramanga, Barrancabermeja, Vélez.
* **Ingeniería de Sistemas:** SNIES 102213 | 4 semestres | Presencial y Virtual | Bucaramanga, Barrancabermeja.
* **Tecnología en Manejo de Recursos Ambientales:** SNIES 53492 | 6 semestres | Presencial | Bucaramanga, Vélez, Piedecuesta.
* **Ingeniería Ambiental:** SNIES 102212 | 4 semestres | Presencial | Bucaramanga.
* **Tecnología en Electricidad Industrial:** SNIES 53486 | 6 semestres | Presencial | Bucaramanga, Barrancabermeja.
* **Ingeniería Eléctrica:** SNIES 102214 | 4 semestres | Presencial | Bucaramanga.
* **Tecnología en Electrónica Industrial:** SNIES 53487 | 6 semestres | Presencial | Bucaramanga.
* **Ingeniería Electrónica:** SNIES 102215 | 4 semestres | Presencial | Bucaramanga.
* **Tecnología en Operación y Mantenimiento Electromecánico:** SNIES 53489 | 6 semestres | Presencial | Bucaramanga, Barrancabermeja.
* **Ingeniería Electromecánica:** SNIES 102216 | 4 semestres | Presencial | Bucaramanga, Barrancabermeja.
* **Tecnología en Producción Industrial:** SNIES 53490 | 6 semestres | Presencial | Bucaramanga.
* **Ingeniería Industrial:** SNIES 102217 | 4 semestres | Presencial | Bucaramanga.
* **Tecnología en Topografía:** SNIES 53491 | 6 semestres | Presencial | Bucaramanga.
* **Tecnología en Gestión de Sistemas de Telecomunicaciones:** SNIES 53493 | 6 semestres | Presencial | Bucaramanga.
* **Ingeniería de Telecomunicaciones:** SNIES 102218 | 4 semestres | Presencial | Bucaramanga.
* **Tecnología en Estudios Geotécnicos:** SNIES 101859 | 6 semestres | Presencial | Bucaramanga.
* **Tecnología en Manejo de Petróleo y Gas en Superficie:** SNIES 104169 | 6 semestres | Presencial | Barrancabermeja.

### 4.2 Facultad de Ciencias Socioeconómicas y Empresariales
* **Tecnología en Gestión Empresarial:** SNIES 53494 | 6 semestres | Presencial y Virtual | Bucaramanga, Piedecuesta, Vélez, Barrancabermeja.
* **Administración de Empresas:** SNIES 102219 | 4 semestres | Presencial y Virtual | Bucaramanga, Piedecuesta, Vélez, Barrancabermeja.
* **Tecnología en el Manejo de la Información Contable:** SNIES 53495 | 6 semestres | Presencial | Bucaramanga, Vélez, Barrancabermeja.
* **Contaduría Pública:** SNIES 102220 | 4 semestres | Presencial | Bucaramanga, Vélez, Barrancabermeja.
* **Tecnología en Mercadeo y Gestión Comercial:** SNIES 53496 | 6 semestres | Presencial | Bucaramanga.
* **Mercadeo:** SNIES 102221 | 4 semestres | Presencial | Bucaramanga.
* **Tecnología en Gestión Bancaria y Financiera:** SNIES 53497 | 6 semestres | Presencial | Bucaramanga.
* **Administración Financiera:** SNIES 102222 | 4 semestres | Presencial | Bucaramanga.
* **Tecnología en Gestión de la Moda:** SNIES 102431 | 6 semestres | Presencial | Bucaramanga.
* **Profesional en Diseño de Moda:** SNIES 102432 | 4 semestres | Presencial | Bucaramanga.
* **Profesional en Cultura Física y Deporte:** SNIES 106093 | 8 semestres | Presencial | Bucaramanga.
* **Profesional en Negocios Internacionales:** SNIES 109287 | 8 semestres | Virtual (UTS Virtual) | Bucaramanga.
* **Economía:** SNIES 111075 | 8 semestres | Presencial | Bucaramanga.

---

## 5. SEDES FÍSICAS, CAMPUS Y MODALIDADES

El sistema modela con precisión la diferencia entre las **sedes físicas** institucionales y las **modalidades de aprendizaje**:

### Sedes Físicas Oficiales
1. **Bucaramanga (Campus Central):** Calle de los Estudiantes # 9-82, Ciudadela Real de Minas.
2. **Piedecuesta:** Sede Guatiguará / Convenio Académico Regional.
3. **Vélez:** Carrera 5 # 7-42 (Sede Regional Provincia de Vélez).
4. **Barrancabermeja:** Calle 50 # 18-20 (Sede Regional Magdalena Medio).

### Modalidades Académicas
* **Presencial:** Asistencia directa en aulas, talleres y laboratorios de la sede.
* **Virtual (UTS Virtual):** Modalidad 100% en línea a través del LMS institucional Canvas y salas de encuentro sincrónico. **No se clasifica como sede física**.
* **A Distancia:** Modalidad semipresencial con apoyo en plataformas virtuales y encuentros de fin de semana.

Cada programa académico almacena únicamente las sedes y modalidades donde tiene registro calificado activo y oferta confirmada.

---

## 6. DERECHOS PECUNIARIOS Y TARIFAS POR VIGENCIA (2026 Y 2027)

El Módulo de Liquidaciones implementa un sistema multívigencia que permite consultar y liquidar conceptos tanto para el año **2026** como para el año **2027**:

### Comparativa de Tarifas Oficiales

| Concepto Institucional | Vigencia 2026 | Vigencia 2027 (Res. 02-1613) | Tipo de Usuario | Condición de Aplicación |
| :--- | :---: | :---: | :---: | :--- |
| **Matrícula Programas Tecnológicos** | \$2.626.358 | **\$2.836.466** | Estudiante UTS | Semestre ordinario nivel tecnológico |
| **Matrícula Nivel Universitario** | \$3.501.810 | **\$3.781.955** | Estudiante UTS | Semestre ordinario ciclo profesional |
| **Curso de Inglés (Estudiantes UTS)** | \$385.200 | **\$416.015** | Estudiante UTS | Módulo Centro de Idiomas con matrícula activa |
| **Curso de Inglés (Externos)** | \$840.000 | **\$907.669** | Externo | Módulo Centro de Idiomas para público general |
| **Educación Continua por Hora (Estudiantes)** | \$17.500 | **\$18.910** | Estudiante UTS | Valor hora en cursos/diplomados de extensión |
| **Educación Continua por Hora (Egresados)** | \$26.200 | **\$28.365** | Egresado | Valor hora para graduados de las UTS |
| **Educación Continua por Hora (Externos)** | \$35.000 | **\$37.820** | Externo | Valor hora para profesionales externos |
| **Inscripción Aspirantes Pregrado** | \$315.163 | **\$340.500** | Aspirante | Gratuito para articulación entre ciclos UTS |
| **Derechos de Grado** | \$580.000 | **\$625.000** | Estudiante UTS | Trámite de titulación y diploma oficial |
| **Certificados y Constancias Académicas** | \$35.000 | **\$38.500** | Estudiante UTS | Expedición oficial de notas o matrícula |
| **Curso Vacacional / Intersemestral** | \$183.000 | **\$198.500** | Estudiante UTS | Valor por crédito académico de nivelación |

---

## 7. ESTÍMULOS, BENEFICIOS, GRATUIDAD Y FINANCIACIÓN ICETEX

Se corrigieron las distorsiones de la versión previa del proyecto:

### 7.1 Beca de Excelencia Académica (Reglamento de Estímulos Acuerdo 01-035 de 2019)
* **Condición de acceso:** Promedio ponderado acumulado igual o superior a 4.00, estar nivelado, haber cursado y aprobado el 100% de los créditos del semestre previo, no haber validado asignaturas y no registrar sanciones disciplinarias.
* **Escala de porcentajes:**
  - **50% de descuento en matrícula:** Asignado al 1% de los estudiantes con mejores promedios del programa.
  - **25% de descuento en matrícula:** Asignado al siguiente 1% de mejores promedios.
  - **15% de descuento en matrícula:** Asignado al tercer 1% del programa.

### 7.2 Descuento por Certificado Electoral (Ley 403 de 1997 / Ley 815 de 2003)
* **Descuento del 10%:** Aplicable sobre el valor neto de la matrícula para estudiantes que presenten el certificado de votación de las últimas elecciones oficiales nacionales en los plazos fijados por la División Financiera.

### 7.3 Política de Estado de Gratuidad "Puedo Estudiar"
* **Cobertura del 100% de la matrícula neta:** Dirigida a estudiantes de estratos 1, 2 y 3 o Sisbén IV (grupos A, B o C), colombianos, sin título universitario previo.
* **Aclaración institucional:** La gratuidad cubre la matrícula ordinaria; no cubre derechos complementarios (seguro de accidentes, carnet o certificados).

### 7.4 Corrección Crítica: Financiación ICETEX (Eliminación del Recargo del 5%)
* **Diagnóstico anterior:** El código contenía un recargo automático del 5% (`surchargeAmount = subtotal * 0.05`), el cual carecía de sustento legal.
* **Corrección aplicada:** En las Unidades Tecnológicas de Santander, **el pago mediante crédito educativo ICETEX no conlleva ningún recargo institucional**. La UTS liquida la matrícula al 100% de su valor oficial (`surchargeAmount = 0.0`). El sistema ahora clasifica a ICETEX como una **modalidad de pago financiado**, desplegando una tarjeta informativa oficial que aclara que el recargo cobrado por la UTS es del 0%.

---

## 8. OFERTA DE EDUCACIÓN CONTINUA Y EXTENSIÓN

El sistema incorpora la oferta extracurricular verificada de la Oficina de Extensión y Educación Continua (Campus Bucaramanga, Edificio A, Piso 7):
* **Diplomado en Seguridad y Salud en el Trabajo (SG-SST):** 120 horas | Virtual.
* **Diplomado en Gestión Estratégica del Talento Humano:** 120 horas | Virtual.
* **Curso Taller de Hacking Ético y Ciberseguridad Defensiva:** 40 horas | Presencial.
* **Curso de Desarrollo Móvil con Android, Kotlin y Jetpack Compose:** 40 horas | Presencial.
* **Seminario de Actualización Tributaria y Cierre Contable:** 30 horas | Presencial.
* **Módulo de Inglés Comunicativo B1 (Centro de Idiomas UTS):** 60 horas | Presencial.

El modelo calcula automáticamente en tiempo real el valor total de cada capacitación según las horas del programa y la categoría del participante:
* *Estudiante activo UTS:* \$18.910 COP/hora
* *Egresado graduado UTS:* \$28.365 COP/hora
* *Participante externo:* \$37.820 COP/hora

---

## 9. BIENESTAR INSTITUCIONAL Y SERVICIOS FORMATIVOS

Se categorizaron los servicios gratuitos que ofrece el Departamento de Bienestar Institucional (Campus Bucaramanga, Edificio B):
1. **Deportes y Actividad Física:**
   * Gimnasio universitario y sala de acondicionamiento físico.
   * Torneo deportivo interfacultades (fútbol, futsal, baloncesto, voleibol).
   * Selecciones deportivas de representación institucional (ASCUN).
2. **Cultura y Promoción Artística:**
   * Grupo representativo de danza folclórica y moderna.
   * Ensamble musical y talleres de instrumentos (música andina y rock).
   * Taller de teatro y artes escénicas.
3. **Promoción de la Salud y Prevención:**
   * Medicina general básica, enfermería y primeros auxilios.
   * Odontología preventiva y profilaxis dental.
4. **Desarrollo Humano y Acompañamiento Psicosocial:**
   * Orientación psicológica individual y apoyo socioemocional.
   * Programa de permanencia estudiantil y tutorías en ciencias básicas.

---

## 10. ARQUITECTURA DE SOFTWARE, ROOM DATABASE Y MIGRACIONES

### Migración Segura de Base de Datos (MIGRATION_1_2)
Para acatar la restricción de **no realizar cambios destructivos ni eliminar datos existentes en la base de datos**, se implementó en `UtsDatabase.kt` la migración oficial de versión 1 a versión 2:

```kotlin
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
```

Esto asegura que:
1. Si un usuario ya tenía guardado su nombre, código estudiantil o liquidaciones históricas en la base de datos local SQLite, estos registros se preservan intactos con valores seguros por defecto.
2. La base de datos evoluciona limpiamente sin lanzar excepciones `IllegalStateException: Room cannot verify the data integrity`.

---

## 11. DESGLOSE DE PANTALLAS Y EXPERIENCIA DE USUARIO

1. **Dashboard Principal (`DashboardScreen.kt`):**
   * Saludo institucional con el nombre del estudiante, programa oficial y sede.
   * Tarjetas modulares de navegación: Módulo 1 (Perfil), Oferta UTS y Servicios, Módulo 2 (Liquidaciones) y Módulo 3 (Historial).
2. **Perfil Académico (`ProfileScreen.kt`):**
   * Selección en cascada: Facultad Oficial $\rightarrow$ Programas Oficiales adscritos a dicha facultad $\rightarrow$ Sedes físicas y modalidades confirmadas para ese programa.
   * Generación reactiva de correo institucional `@uts.edu.co`.
   * Resumen de credencial activa.
3. **Oferta Académica y Servicios UTS (`AcademicOfferScreen.kt`):**
   * Pestaña *Programas:* Buscador de texto en vivo por nombre o SNIES, filtros por facultad y sedes.
   * Pestaña *Tarifas:* Comparativa de derechos pecuniarios 2026 vs 2027 (Res. 02-1613).
   * Pestaña *Educación Continua:* Oferta formativa con cálculo de tarifas por hora según categoría.
   * Pestaña *Bienestar:* Directorio de servicios gratuitos de salud, deporte y cultura con horarios y lugares.
   * Pestaña *Fuentes:* Directorio de trazabilidad legal con resoluciones, fechas y enlaces oficiales.
4. **Liquidación y Tarifas (`EnrollmentScreen.kt`):**
   * Selector de vigencia (2026 / 2027) y concepto oficial.
   * Selección de beneficios (Beca Excelencia 50%/25%, Descuento Electoral 10%, Gratuidad 100%).
   * Switch de financiación ICETEX con nota de 0% recargo.
   * Desglose financiero y Bloqueo de Seguridad activo.
5. **Historial Financiero (`HistoryScreen.kt`):**
   * Registro histórico que conserva la vigencia y concepto original de cada transacción sin alterar los valores pasados.

---

## 12. RESUMEN DE ARCHIVOS MODIFICADOS Y CREADOS

### Modelos y Capa de Datos (`co.edu.uts.serviciosestudiantiles.data.*`)
* `model/Faculty.kt` *(Modificado)*: Facultades oficiales completas con alias hacia atrás y buscador tolerante.
* `model/Campus.kt` *(Creado)*: Sedes físicas, modalidades y niveles de ciclos propedéuticos.
* `model/AcademicProgram.kt` *(Creado)*: Catálogo de 30 programas oficiales con códigos SNIES y sedes.
* `model/InstitutionalTariff.kt` *(Creado)*: Tarifas pecuniarias organizadas por vigencias 2026 y 2027 (Res. 02-1613).
* `model/InstitutionalBenefit.kt` *(Creado)*: Estímulos (Acuerdo 01-035), gratuidad electoral y aclaración ICETEX.
* `model/ContinuingEducationCourse.kt` *(Creado)*: Cursos, diplomados y cálculo tarifario por horas.
* `model/WellnessActivity.kt` *(Creado)*: Actividades oficiales de Bienestar en deporte, cultura, salud y psicología.
* `model/InstitutionalSource.kt` *(Creado)*: Registro de fuentes oficiales y trazabilidad.
* `model/StudentProfile.kt` *(Modificado)*: Añadidos campos opcionales de programa, nivel, sede y modalidad.
* `model/TuitionLiquidation.kt` *(Modificado)*: Añadidos campos de vigencia, beneficio y fuente oficial.
* `local/entity/StudentProfileEntity.kt` *(Modificado)*: Mapeo de nuevas columnas institucionales.
* `local/entity/TuitionLiquidationEntity.kt` *(Modificado)*: Mapeo de columnas de vigencia y fuente.
* `local/UtsDatabase.kt` *(Modificado)*: Versión 2 con `MIGRATION_1_2` no destructiva.
* `repository/InstitutionalRepository.kt` *(Creado)*: Interfaz e implementación de acceso institucional.
* `repository/AppContainer.kt` *(Modificado)*: Provisión del nuevo `institutionalRepository`.

### Capa de Lógica y Estado (`co.edu.uts.serviciosestudiantiles.viewmodel.*`)
* `profile/ProfileUiState.kt` y `ProfileViewModel.kt` *(Modificados)*: Selección oficial encadenada por facultad.
* `enrollment/EnrollmentUiState.kt` y `EnrollmentViewModel.kt` *(Modificados)*: Vigencias 2026/2027, tarifas oficiales, eliminación de recargo ficticio de ICETEX y estímulos reglamentarios.
* `institutional/InstitutionalUiState.kt` y `InstitutionalViewModel.kt` *(Creados)*: Gestión del catálogo oficial, filtros y trazabilidad.

### Capa de Presentación (`co.edu.uts.serviciosestudiantiles.ui.*`)
* `navigation/UtsScreen.kt` *(Modificado)*: Añadido destino `ACADEMIC_OFFER` ("Oferta UTS").
* `screens/ProfileScreen.kt` *(Modificado)*: Formulario adaptado a facultades y programas oficiales.
* `screens/EnrollmentScreen.kt` *(Modificado)*: Liquidación oficial por vigencias, beneficios y 0% recargo ICETEX.
* `screens/HistoryScreen.kt` *(Modificado)*: Despliegue de vigencia y fuentes históricas.
* `screens/DashboardScreen.kt` *(Modificado)*: Visualización del programa oficial y acceso a Oferta UTS.
* `screens/AcademicOfferScreen.kt` *(Creado)*: Pantalla con pestañas de Programas, Tarifas, Educación Continua, Bienestar y Fuentes.
* `MainActivity.kt` *(Modificado)*: Vinculación del nuevo ViewModel y destino de navegación.

### Pruebas Unitarias (`src/test`)
* `EnrollmentViewModelTest.kt` *(Modificado)*: Pruebas unitarias de tarifas 2026/2027 (Res. 02-1613), beca del 50%, descuento electoral del 10%, gratuidad y verificación de 0% recargo ICETEX.
* `InstitutionalViewModelTest.kt` *(Creado)*: Pruebas de filtrado por facultad, búsqueda por SNIES y cálculo de horas de educación continua.

---

## 13. INSTRUCCIONES PARA ANDROID STUDIO (RUN)

El proyecto se encuentra 100% configurado, sin errores de sintaxis y listo para ser ejecutado:
1. Abre **Android Studio**.
2. Abre la carpeta del proyecto:  
   `C:\Users\Usuario\Desktop\ANPHERNEE\uts_servicios_estudiantiles`
3. Espera a que termine la sincronización automática de Gradle (**Sync Project with Gradle Files**).
4. La ventana **Problems** reportará **0 errores**.
5. Selecciona tu emulador o dispositivo físico y presiona **RUN** (`Shift + F10`).
