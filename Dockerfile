

FROM openjdk:8-jdk-alpine
WORKDIR .
VOLUME /tmp
COPY target/rg_auth_svc-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

