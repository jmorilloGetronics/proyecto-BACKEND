# 🚀 Backend Service - Lab Becarios

Este repositorio contiene la lógica del lado del servidor para el proyecto de pruebas de integración continua. Está diseñado para integrarse automáticamente con la infraestructura de Jenkins y SonarQube de la organización.

## 🛠 Tecnologías y Stack
* **Lenguaje:** Java 17 (OpenJDK)
* **Gestor de Construcción:** Maven
* **Análisis de Calidad:** SonarQube
* **Reporte de Cobertura:** JaCoCo (v0.8.8)

## 🔄 Pipeline de CI/CD (Jenkins)
El proyecto utiliza una **Shared Library** corporativa que automatiza las siguientes fases:

1. **Test Unitarios:** Ejecución de pruebas mediante `maven-surefire-plugin` con la opción `-fae` (fail at end) para obtener un reporte completo.
2. **Code Quality:** Escaneo estático en SonarQube buscando vulnerabilidades y code smells.
3. **Security Scan:** Análisis de dependencias con `OWASP Dependency Check` para detectar librerías obsoletas o peligrosas.
4. **Coverage:** Generación de informes XML de cobertura de código para su visualización en el dashboard de calidad.

## 📋 Requisitos para el correcto funcionamiento
Para que el pipeline de Jenkins no falle, este repositorio mantiene:
* Un archivo `pom.xml` en la raíz con las dependencias necesarias.
* Un `Jenkinsfile` que define la variable `TOOL = 'MAVEN'`.
* Tests unitarios bajo la ruta estándar `src/test/java`.

## ⚙️ Variables de Entorno Clave
El pipeline espera las siguientes configuraciones (gestionadas en el Jenkinsfile):
* `JDK_VERSION`: 'OpenJDK 17u13'
* `MAVEN_VERSION`: Versión configurada en el entorno global de Jenkins.
* `SONAR_KEY`: Identificador único del proyecto en SonarQube.