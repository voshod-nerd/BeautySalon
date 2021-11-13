FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
WORKDIR /temp
ENTRYPOINT ["java","-jar","/app.jar"]