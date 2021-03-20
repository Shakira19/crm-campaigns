FROM adoptopenjdk:11-jre-hotspot
COPY target/campaigns-1.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]