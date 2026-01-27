
# 🏟️ Synergy Sport Club | Mobile App
> **Migración integral del sistema de gestión deportiva a entorno móvil nativo Android.**

---

## 🛠️ Stack Tecnológico

| Lenguaje | IDE | Base de Datos | Arquitectura |
| :---: | :---: | :---: | :---: |
| ![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white) | ![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white) | ![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white) | ![DAO](https://img.shields.io/badge/Pattern-DAO%20%2F%20MVVM-orange?style=for-the-badge) |

---

## 📌 Resumen del Proyecto

Este proyecto representa la evolución del sistema de escritorio original hacia una solución de movilidad. **Synergy Sport Club** permite a los administradores y profesores del club gestionar las actividades, socios y estados de pago directamente desde el campo de juego, eliminando la dependencia de una PC de escritorio.

### 🌟 Funcionalidades Clave de la Migración
* **Gestión de Membresías:** Alta y consulta de socios con persistencia en base de datos local.
* **Control de Acceso:** Validación rápida de estados de socios (Activo/Inactivo).
* **Persistencia Robusta:** Implementación de `DBHelper` y patrones `DAO` para un manejo de datos seguro y eficiente.
* **UI Optimizada:** Adaptación de los complejos formularios de escritorio a una experiencia táctil intuitiva.

---

## 🏗️ Aspectos Técnicos Destacados

* **Migración de Lógica:** Adaptación de las reglas de negocio de C# a Kotlin, manteniendo la integridad de los datos.
* **Patrón DAO (Data Access Object):** Desacoplamiento total de la lógica de acceso a datos de la interfaz de usuario.
* **SQLite Nativo:** Optimización de consultas para garantizar velocidad incluso en dispositivos de gama media/baja.

---

## 📂 Estructura del Proyecto

```text
app/src/main/java/com/synergy/sportsclub/
 ├── data/          # DBHelper.kt y DAOs (Gestión de SQLite)
 ├── model/         # Entidades de datos (Socio, Deporte, Pago)
 ├── ui/            # Activities y Fragments (Interfaces)
 └── viewmodel/     # Lógica de estados y comunicación
```

🚀 Instalación y Setup

1. Clonar el repositorio

Bash
git clone [https://github.com/MarcosMartinProgram/synergySportsClub.git](https://github.com/MarcosMartinProgram/synergySportsClub.git)

2. Importar en Android Studio

Seleccionar "Open Existing Project".

Sincronizar Gradle para descargar las dependencias necesarias.

3. Ejecución

Compilar en un emulador o dispositivo físico (Android 7.0+ recomendado).

👤 Autor
Marcos Martin Técnico Superior en Desarrollo de Software, enfocado en la modernización de sistemas.
