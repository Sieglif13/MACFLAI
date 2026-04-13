# MACFLAI - Plataforma Adaptativa de Preparación PAES (Lenguaje y Comunicación)

Sistema de preparación académica de alto rendimiento, impulsado por Inteligencia Artificial estructurada. Este proyecto interactúa en tiempo real con el orquestador local Sinclair (Gemma 4 9.6B) para diagnosticar, evaluar y generar de forma paramétrica desafíos orientados al currículum PAES.

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Firebase](https://img.shields.io/badge/firebase-%23039BE5.svg?style=for-the-badge&logo=firebase)

---

## Arquitectura del Sistema

El ecosistema de código de MACFLAI (Android y Networking) implementa las bases de **Clean Architecture** estructurado de manera re-activa bajo el patrón de diseño **MVVM (Model-View-ViewModel)**. 

La meta principal de esta separación técnica es asegurar que el orquestador de Inteligencia Artificial viva en completo desconocimiento de la UI, facilitando alta mantenibilidad para despliegues a gran escala (100+ usuarios concurrentes).

### Componentes de Clean Code

| Capa Estructural | Responsabilidad en la Plataforma |
| :--- | :--- |
| **Domain** | Modelos estrictos (`User.kt`) e interfacs puras (ej. `AuthRepository`), totalmente agnósticos a componentes Android. |
| **Data** | Concreciones de interfaces (ej. `FirebaseAuthRepository`, `SinclairRepository`). Actúa como puente entre Network (Retrofit), la API de Google Play Services y la base de datos distribuida. |
| **ViewModel** | Alojamiento del `SinclairUiState` mediante Mutables de `StateFlow`. Procesamiento de heurísticas de carga sin bloqueos de Frame y con resiliencia ante excepciones. |
| **UI** | Composables modulares y reactivos sin acceso lógico. Reflejan representaciones binarias de estados (CustomLoading, Success, Idle). |

---

## Stack Tecnológico (Frontend)

El entorno Android, configurado en Gradle usando TOML Version Catalogs, cuenta con:

* **Desarrollo Base:** Kotlin Coroutines y Jetpack Compose.
* **Inyección de Dependencias Estricta:** Dagger-Hilt procesado a través de **KSP (Kotlin Symbol Processing)**, manejando proveedurijas por defecto y `@Singleton` para optimización de memoria.
* **Capa de Red Modular:** Retrofit y OkHttp envueltos en patrones Factory.
* **Asincronía en Google APIs:** Corrutinas ligadas a Play Services mediante `kotlinx-coroutines-play-services` para gestionar de manera síncrona/segura el retorno de promesas (Tasks) sin bloquear subprocesos de red.

---

## Sistema de Seguridad e Identidad

Debido al peso computacional de las operaciones enviadas hacia Gemma 4, MACFLAI provee un esquema de identidad seguro y encapsulado, respaldado en Firebase Authentication. La comunicación consta de los siguientes flujos de seguridad:

* **Aislamiento en Peticiones:** La Capa de Presentación jamás retiene o solicita tokens criptográficos al usuario final.
* **Inyección en Red:** Un Interceptor `AuthInterceptor` en OkHttp, instanciado vía Dagger-Hilt, secuestra cada flujo HTTPS originado por el dispositivo y exige al repositorio proveer credenciales válidas en segundo plano.
* **Modificación de Headers:** La petición saliente se nutre del encabezado HTTP estandarizado `Authorization: Bearer <idToken>` de forma automática.

---

## El Motor Sinclair (Gemma 4 9.6B)

El servidor central Node.js actúa como muralla y árbitro interpretador para Sinclair.

* **Tracking por alumno:** El Middleware intercepta el Bearer Token enviado por el cliente Kotlin y destila dinámicamente el `uid` (ID único de usuario validado por Google).
* **Isolation the Ventana Lógica:** El historial conversacional y pedagógico se almacena asignando bloques perimetrales atados al `uid`.
* **Capacidad 256K Context:** La arquitectura Gemma 4 admite gigantescos bloques documentales de información PAES. El aislamiento de UID garantiza que el contexto de los ejes evaluados por el Alumno A nunca contaminará el historial inferencial del Alumno B.

---

## Instrucciones de Configuración y Primeros Pasos

El repositorio Android está estructurado para despliegue inmediato de compilaciones, sin embargo, su entorno se encuentra resguardado por exclusión git.

> [!WARNING]  
> Integración JSON
> De acuerdo en los estándares de seguridad contemporáneos, el archivo de firmas de nube `google-services.json` así como certificados internos de Android, NO se publican en el repositorio.

Sigue estos pasos con estricto rigor para ensamblar un Build local:

1. Autentícate en el panel base de la Consola de Firebase correspondiente al tenant educativo de MACFLAI.
2. Extrae y guarda el archivo vital de servicios en crudo. 
3. Dirígete a la ruta del directorio clonado y pega el archivo en: `MACFLAI/app/google-services.json`.
4. Cerciórate de contar con conexión a red abierta para las descargas de paquetes Maven y KSP plugins locales.
5. Lanza la orden primaria: `./gradlew assembleDebug` o sincroniza sobre la vista estandarizada de IntelliJ/Android Studio.
