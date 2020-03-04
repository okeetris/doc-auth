FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:8-jdk-alpine
COPY --from=build /home/app/target/rg_auth_svc-0.0.1.jar /usr/local/lib/app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]

