# Etapa 1: Build con Maven
FROM maven:3.9.6-eclipse-temurin-22-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Ejecutable final
FROM eclipse-temurin:22-jre-alpine
WORKDIR /app

# Copiamos el jar construido
COPY --from=build /app/target/*.jar app.jar

# 👇 Copiamos la carpeta del wallet al contenedor
COPY WalletLimpieza /app/WalletLimpieza

# 👇 Apuntamos TNS_ADMIN a esa ruta dentro del contenedor
ENV TNS_ADMIN=/app/WalletLimpieza

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
