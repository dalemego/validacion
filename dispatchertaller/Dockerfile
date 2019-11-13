FROM openjdk:8-jdk-alpine
ADD dispatchertaller/build/libs/dispatchertaller-0.0.1-SNAPSHOT.jar dispatchertaller.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "dispatchertaller.jar"]