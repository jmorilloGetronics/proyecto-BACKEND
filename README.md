# Proyecto Leo Backend

Backend REST del proyecto Concesionario Leo. Expone una API de coches en Spring Boot para ser consumida desde el repositorio de frontend.

## Tecnologias
- Java 17
- Spring Boot 3.2
- Maven
- JUnit 5
- MockMvc
- JaCoCo
- Springdoc OpenAPI

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

Documentacion Swagger:
- http://localhost:8080/swagger-ui/index.html

## Pruebas
```bash
mvn test
```

Suite de calidad completa (tests + cobertura):
```bash
mvn verify
```

Cobertura JaCoCo generada en:
- target/site/jacoco/index.html

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

### POST /api/login
Autentica usuario y devuelve token + rol.

Credenciales disponibles:
- usuario / password -> USER
- admin / pass123 -> ADMIN

### POST /api/logout
Cierra sesion del token actual.

### GET /api/coches
Devuelve el catalogo de coches (requiere token USER o ADMIN).

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
Devuelve un coche por id (requiere token USER o ADMIN).

### POST /api/coches
Crea un coche (solo ADMIN).

### PUT /api/coches/{id}
Actualiza un coche (solo ADMIN).

### DELETE /api/coches/{id}
Elimina un coche (solo ADMIN).

Todas las peticiones autenticadas deben incluir:
- Authorization: Bearer <token>

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
	controller/
		CocheController.java
	error/
		GlobalExceptionHandler.java
	model/
		Coche.java

src/test/java/com/leo/backend/
	AppTest.java
```

## Notas
- CORS esta habilitado en el controlador para permitir consumo desde frontend.
- La API devuelve errores consistentes en JSON (status, error, message, path).