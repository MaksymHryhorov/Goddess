FROM adoptopenjdk:11-jre-hotspot
COPY target/application-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "application-0.0.1-SNAPSHOT.jar"]