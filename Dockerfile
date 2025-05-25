FROM maven:3-eclipse-temurin-24 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests


FROM eclipse-temurin:24

RUN useradd -m springuser
WORKDIR /home/springuser

COPY --from=build /app/target/*.jar app.jar
RUN chown springuser:springuser app.jar
USER springuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]