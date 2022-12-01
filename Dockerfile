FROM amazoncorretto:17-alpine-jdk
MAINTAINER teguh santoso
COPY target/soundAnalysis-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]