FROM openjdk:8-jdk-alpine
ADD transformtaller/build/libs/transformtaller-0.0.1-SNAPSHOT.jar transformtaller.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "transformtaller.jar"]