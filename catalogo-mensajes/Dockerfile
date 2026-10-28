# Etapa 1: compilar con Maven
FROM maven:3-eclipse-temurin-27 AS compilacion
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

# Etapa 2: imagen liviana solo con el JRE
FROM eclipse-temurin:27-jre
WORKDIR /app
RUN useradd --system --uid 10001 aplicacion
COPY --from=compilacion /app/target/*.jar catalogo-mensajes.jar
USER aplicacion
EXPOSE 8082
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "catalogo-mensajes.jar"]
