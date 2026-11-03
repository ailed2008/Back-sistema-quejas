# Etapa 1: Construcción (Build) con Maven y Java 21
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar los archivos de Maven
COPY .mvn .mvn
COPY mvnw pom.xml ./
COPY src src

# Dar permisos de ejecución a Maven Wrapper y compilar el proyecto (omitiendo tests)
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución ligera (Runtime)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el archivo JAR generado desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Render asigna dinámicamente un puerto mediante la variable PORT, por defecto usamos el 8080
EXPOSE 8080

# Comando de inicio del contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]