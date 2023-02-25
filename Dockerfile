FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} TtuckTak_Server.jar
ENTRYPOINT ["java","-jar","TtuckTak_Server.jar", "--spring.profiles.active=dev"]