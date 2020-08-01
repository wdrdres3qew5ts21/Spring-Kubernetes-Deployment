FROM openjdk:11

WORKDIR /app

ADD ./target/demo-0.0.1-SNAPSHOT.jar .

CMD [ "java","-jar","demo-0.0.1-SNAPSHOT.jar" ]