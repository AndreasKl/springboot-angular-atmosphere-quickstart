FROM dockerfile/java:oracle-java8 

RUN apt-get update
RUN apt-get install -y maven

ADD . /code
WORKDIR /code
RUN ["mvn", "clean", "install"]

EXPOSE 8080
CMD ["java", "-jar", "target/toast-on-atmosphere-0.0.6-SNAPSHOT.jar"]
