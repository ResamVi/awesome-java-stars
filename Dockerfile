FROM gradle:jdk21-alpine AS build
WORKDIR /project
COPY . .
RUN ./gradlew bootJar

FROM eclipse-temurin:21-jdk
COPY --from=build /project/src/build/libs/src.jar .

ENTRYPOINT ["java", "-jar", "src.jar"]
