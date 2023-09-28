FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=build
RUN echo ${DEPENDENCY}
COPY ${DEPENDENCY}/libs/demo-backend-1.0.0-SNAPSHOT.jar /app/demo-backend.jar
ENTRYPOINT ["java","-jar","/app/demo-backend.jar"]
