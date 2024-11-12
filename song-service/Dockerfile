FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY mvnw ./
COPY .mvn/ .mvn
COPY pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install -DskipTests

RUN mkdir -p target && java -Djarmode=layertools -jar target/*.jar extract

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]