FROM amazoncorretto:11-alpine-jdk
COPY target/cms-0.0.1-SNAPSHOT.jar cms-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/cms-0.0.1-SNAPSHOT.jar"]
