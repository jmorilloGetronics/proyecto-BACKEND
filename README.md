# Proyecto Leo Backend

Backend REST del proyecto Concesionario Leo. Expone una API de coches en Spring Boot para ser consumida desde el repositorio de frontend.

## Tecnologias
- Java 17
- Spring Boot 3.2
- Maven
- JUnit 5

## Requisitos
- JDK 17 instalado
- Maven instalado
- Puerto 8080 libre

## Arranque en local
```bash
mvn spring-boot:run
```

La API queda disponible en:
- http://localhost:8080/api/coches

## Pruebas
```bash
mvn test
```

Tests actuales:
- Carga de contexto Spring
- Validacion de respuesta no vacia del catalogo de coches

## Build
```bash
mvn clean package
```

Artefacto generado:
- target/proyecto-leo-backend-1.0-SNAPSHOT.jar

Ejecucion del jar:
```bash
java -jar target/proyecto-leo-backend-1.0-SNAPSHOT.jar
```

## Endpoints

### GET /api/coches
Devuelve el catalogo de coches.

Ejemplo de respuesta:
```json
[
	{
		"id": "1",
		"marca": "Toyota",
		"modelo": "Corolla",
		"precio": 25000,
		"enStock": true
	}
]
```

### GET /api/coches/{id}
Devuelve un mensaje de ejemplo con el id solicitado.

## CI/CD (GitLab + Jenkins)
Este repositorio se integra con Jenkins mediante Shared Library corporativa y Jenkinsfile en raiz.

Puntos importantes:
- El repo incluye pom.xml en la raiz.
- Se usa TOOL = 'MAVEN'.
- El pipeline de empresa esperado es pipelineEmpresaJava.
- Si el job no es multibranch, configurar la rama objetivo en Jenkins (por ejemplo pruebaN1Leo).

## Estructura principal
```text
src/main/java/com/leo/backend/
	App.java
	Coche.java
	CocheController.java

src/test/java/com/leo/backend/
	AppTest.java
```

## Notas
- CORS esta habilitado en el controlador para permitir consumo desde frontend.
- application.properties existe pero esta vacio (configuracion por defecto de Spring Boot).