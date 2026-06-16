# ---------- Etapa 1: build ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos primero solo el pom.xml para cachear las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Ahora copiamos el resto del código y compilamos
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- Etapa 2: runtime ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos el jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
