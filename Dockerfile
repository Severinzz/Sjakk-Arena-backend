FROM openjdk:8-jdk-alpine
COPY ./target/sjakkarena-0.0.1-SNAPSHOT.jar /usr/src/sjakkarena/
WORKDIR /usr/src/sjakkarena
EXPOSE 8080
ENTRYPOINT ["java","-jar","sjakkarena-0.0.1-SNAPSHOT.jar"]
