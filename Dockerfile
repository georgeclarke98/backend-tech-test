FROM openjdk:17-oracle
MAINTAINER george
COPY target/ffern-0.0.1-SNAPSHOT.jar ffern-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/ffern-0.0.1-SNAPSHOT.jar"]
