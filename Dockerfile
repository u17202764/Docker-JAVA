# Etapa 1: Construcción con Maven
FROM maven:3.9.9-amazoncorretto-17 AS builder

WORKDIR /app

# Copiar solo el archivo pom.xml y descargar dependencias
COPY . .
RUN mvn dependency:go-offline

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests
# Etapa 2: Imagen final para ejecutar la aplicación
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
