FROM gradle:7.6.0-jdk17 AS build

COPY . /home/app
WORKDIR /home/app

RUN gradle build

FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=build /home/app/build/libs/*.jar /app/app.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/app.jar"]
