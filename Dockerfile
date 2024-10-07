FROM openjdk:8-jre-alpine as runner

WORKDIR /app

COPY /target/*.jar ./cadence.jar

ENTRYPOINT ["java","-jar", "cadence.jar"]