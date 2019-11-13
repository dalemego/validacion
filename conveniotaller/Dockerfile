FROM openjdk:8-jdk-alpine
ADD conveniotaller/build/libs/conveniotaller-0.0.1-SNAPSHOT.jar conveniotaller.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "conveniotaller.jar"]