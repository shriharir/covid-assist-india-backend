FROM openjdk:8u111-jdk-alpine
VOLUME /tmp
ADD /build/libs/covidassistindia-*-SNAPSHOT.jar covidassistindia-backend.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/covidassistindia-backend.jar"]