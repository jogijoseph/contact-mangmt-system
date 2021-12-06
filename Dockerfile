FROM amazoncorretto:11-alpine-jdk
COPY target/project-0.0.1-SNAPSHOT.jar project-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/project-0.0.1-SNAPSHOT.jar"]
