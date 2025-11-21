FROM gradle:9.2.1-jdk21 AS build
COPY --chown=gradle:gralde .. /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle :demo:build --no-daemon -x test

FROM amazoncorretto:21
COPY --from=build /home/gradle/src/demo/build/libs/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "app.jar"]