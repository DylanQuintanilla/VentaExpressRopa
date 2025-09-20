# 📱 VentaExpressRopa — Sistema de Gestión de Ventas de ropa para Móvil

> **Desarrollado por:**  
> Dylan Alfonso Quintanilla Rivera — `QR240095`  
> Oscar Vladimir Alarcón Mendoza — `AM221856`  
> Ashley Gabriela Valdez González — `VG240979`  
>  
> 🎓 Universidad Don Bosco — Escuela de Ingeniería en Computación  
> 📅 Asignatura: Desarrollo de Software para Móvil  
> 🧩 Segundo Desafío Práctico — 15%

---

## 📌 Descripción del Proyecto

Aplicación móvil desarrollada en **Android Studio con Kotlin**, que simula un **sistema de ventas completo** integrando:

- 🔐 **Firebase Authentication** (Login/Registro con correo, GitHub y Facebook).
- 🗃️ **Firebase Realtime Database** organizada por UID de usuario.
- 🧭 Patrón arquitectónico **MVC**.
- 📋 Uso de `RecyclerView` para gestionar listas de productos, clientes y ventas.
- 💰 Funcionalidades completas de gestión de inventario, clientes y registro de ventas.

Cada empleado autenticado **solo ve y gestiona sus propios datos**, garantizando seguridad y organización por usuario.

---

## 🎬 Demostración en Video

### 1. Funcionamiento General y Estructura MVC + RecyclerView + Firebase

[![Demo Completa - Funcionamiento y Arquitectura](https://img.youtube.com/vi/PMBBISQDRIY/0.jpg)](https://youtu.be/PMBBISQDRIY)

> En este video se presenta la estructura, el uso del RecyclerView, la conexión con Firebase (autenticación y base de datos), y la funcionalidad completa de la app.

---

### 2. Vista Rápida de la Interfaz y Flujo de Ventas

[![Vista Rápida - Interfaz y Flujo de Ventas](https://img.youtube.com/vi/FeAYapEAS1A/0.jpg)](https://youtube.com/shorts/FeAYapEAS1A?feature=share)

> Video corto que muestra la experiencia de usuario, navegación entre pantallas y registro rápido de una venta.

---

## 🧩 Funcionalidades Implementadas

✅ **Login / Registro de Empleados**  
- Autenticación con correo/contraseña, GitHub y Facebook.  
- Acceso restringido a personal autorizado.

✅ **Menú Principal**  
- Navegación hacia: Productos, Clientes y Ventas.

✅ **Gestión de Productos** *(RecyclerView)*  
- Agregar, editar, eliminar y ver stock.  
- Campos: `id`, `nombre`, `descripción`, `precio`, `stock`.

✅ **Gestión de Clientes** *(RecyclerView)*  
- Registrar y administrar clientes.  
- Campos: `id`, `nombre`, `correo`, `teléfono`.

✅ **Registro de Ventas**  
- Seleccionar cliente y productos (con cantidades).  
- Cálculo automático del total.  
- Guardado con fecha y referencia a cliente/productos.  
- Campos: `id`, `cliente`, `lista de productos`, `total`, `fecha`.

✅ **Organización por UID**  
- Cada empleado solo ve y gestiona sus propios registros.

✅ **Uso de Recursos Android**  
- Strings, dimens, colors — correctamente organizados.

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **IDE**: Android Studio
- **Base de Datos**: Firebase Realtime Database
- **Autenticación**: Firebase Authentication
- **UI Components**: RecyclerView, CardView, TextInputLayout, etc.

---

⭐ **Hecho con 💙 por Dylan, Oscar y Ashley**  
📌 Repositorio creado para fines académicos — Universidad Don Bosco.
