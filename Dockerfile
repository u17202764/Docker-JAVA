# Etapa 1: Construcción con Maven
FROM maven:3.9.9-amazoncorretto-17 AS builder

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo pom.xml y el código fuente al contenedor
COPY pom.xml /app/
COPY src /app/src/

# Ejecuta Maven para compilar el proyecto y generar el archivo JAR
RUN mvn clean install -DskipTests

# Etapa 2: Imagen final para ejecutar la aplicación
FROM openjdk:17-jdk

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo JAR desde la etapa de construcción
COPY --from=builder /app/target/*.jar /app/app.jar

# Exponer el puerto donde tu aplicación se ejecuta
EXPOSE 8082

# Define el comando de inicio
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
