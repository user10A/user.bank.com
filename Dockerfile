FROM maven:3.9.1-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml /app/
RUN mvn dependency:go-offline -B


COPY src /app/src
RUN mvn package -DskipTests -B


FROM eclipse-temurin:17-jdk
WORKDIR /app


ENV JAVA_OPTS=""

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]