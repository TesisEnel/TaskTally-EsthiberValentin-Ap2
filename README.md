# 🌟 TaskTally 🌟

TaskTally es una aplicación Android diseñada para la **gestión de tareas familiares**. Permite a los padres (Mentores) asignar tareas a sus hijos (Gemas). 
Las Gemas ganan puntos al completar tareas y pueden canjearlos por recompensas dentro de una tienda virtual familiar.

---

## 🚀 Tecnologías Utilizadas

- **Android Studio**
- **Kotlin**
- **Jetpack Compose**
- **Room Database**
- **Retrofit**
- **Hilt**
- **SQL Server**
- **C#** (para la API)

---

## 👥 Roles en el Sistema

### Mentor
Usuario padre/madre con permisos para administrar tareas, recompensas y la zona familiar.

### Gema
Usuario hijo/hija encargado de completar tareas y canjear puntos por recompensas.

---

##  Funcionalidades por Rol

### 👨‍💼 Mentor

- **Iniciar Sesión:**  
  Acceso mediante usuario y contraseña.

- **Registrarse:**  
  Creación de una cuenta nueva seleccionando el rol de Mentor.

- **Lista de Tareas:**  
  - Ver todas las tareas creadas
  - Editar o eliminar tareas existentes

- **Crear Tarea:**  
  Formulario para registrar nuevas tareas.

- **Tienda (Recompensas):**  
  - Ver todas las recompensas creadas
  - Editar o eliminar recompensas

- **Crear Recompensa:**  
  Formulario para registrar nuevas recompensas.

- **Gestión de Zona:**  
  - Administrar la zona familiar
  - Ver código de acceso
  - Gestionar las Gemas unidas

- **Cerrar Sesión:**  
  Cerrar sesión como Mentor.

---

### 💎 Gema

- **Iniciar Sesión:**  
  Acceso mediante usuario y contraseña.

- **Registrarse:**  
  Crear una cuenta seleccionando el rol de Gema.

- **Unirse a Zona:**  
  Ingresar el código proporcionado por el mentor para acceder a la zona familiar.

- **Mis Tareas:**  
  Ver todas las tareas asignadas con sus estados:
  - Pendiente
  - Iniciada
  - Completada

- **Iniciar Tarea:**  
  Cambiar el estado de una tarea de "pendiente" a "iniciada".

- **Completar Tarea:**  
  Marcar una tarea como completada y recibir los puntos correspondientes.

- **Filtrar por Fecha:**  
  Filtrar las tareas según el día asignado.

- **Tienda:**  
  Ver todas las recompensas disponibles creadas por el Mentor.

- **Canjear Recompensa:**  
  Al canjear, se muestra:
  - Costo en puntos
  - Puntos actuales
  - Puntos restantes

---

## 📌 Notas Finales

TaskTally está diseñado para fomentar la responsabilidad y la colaboración familiar mediante un sistema de recompensas que motiva a los hijos a completar sus tareas diarias.
