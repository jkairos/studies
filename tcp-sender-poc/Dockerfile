FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=build
RUN echo ${DEPENDENCY}
COPY ${DEPENDENCY}/libs/tcp-sender-poc-1.0.0-SNAPSHOT.jar /app/tcp-sernder-poc.jar
ENTRYPOINT ["java","-jar","/app/demo-backend.jar"]
