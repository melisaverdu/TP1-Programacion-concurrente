# Trabajo Práctico - Programación Concurrente

---

## Cómo empezar

### 1. Clonar el repositorio

```bash
git clone https://github.com/melisaverdu/TP1-Programacion-concurrente.git
cd TP1-Programacion-concurrente
```

---

### 2. Trabajar sobre la rama principal

```bash
git checkout feature/issue1
git pull origin feature/issue1
```

---

## Flujo de trabajo básico

### Ver cambios

```bash
git status
```

---

### Agregar archivos

```bash
git add .
```

---

### Hacer commit

```bash
git commit -m "tipo: descripción corta"
```

---

### Subir cambios

```bash
git push origin feature/issue1
```

---

## Convención de commits

Usamos el formato:

```
tipo: descripción
```

### Tipos permitidos:

* `feat` → nueva funcionalidad
* `fix` → corrección de errores
* `refactor` → mejora sin cambiar funcionalidad
* `test` → cambios en tests
* `chore` → cambios de configuración o estructura

### Ejemplos:

```
feat: implement scheduler logic
fix: avoid duplicate jobs in queue
refactor: simplify node assignment
chore: clean project structure
```

---

## Archivos que NO deben subirse

Nunca subir:

* carpetas `.idea/`
* archivos `.iml`
* configuraciones de IDE
* archivos temporales

---

## .gitignore

El proyecto ya incluye `.gitignore` para evitar subir:

* archivos de IntelliJ
* configuraciones locales
* builds

---

## Cómo ejecutar el proyecto

Desde `Main.java`:

* correr el programa
* observar logs en consola

---

## Sobre el proyecto

El sistema simula procesamiento concurrente de jobs con múltiples etapas:

* Scheduler (3 hilos)
* PreExecutionCheck (2 hilos)
* WorkerExecution (3 hilos)
* PostProcessing (2 hilos)

---

## Reglas importantes

* No acceder directamente a listas compartidas
* Usar siempre las clases de control (`JobQueue`, `NodeMatrix`)
* No modificar estructuras base sin avisar

---

## Trabajo en equipo

Antes de trabajar:

```bash
git pull origin feature/issue1
```

Después de trabajar:

```bash
git add .
git commit -m "tipo: cambio"
git push origin feature/issue1
```

---

## Problemas comunes

### No puedo hacer push

👉 hacer `git pull` antes

### Conflictos

👉 revisar archivos marcados y resolver manualmente

---

## Recomendación

Hacer commits pequeños y frecuentes para evitar conflictos.

---
