FROM java:8-jdk-alpine

COPY ./target/api-gateway-1.0.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch api-gateway-1.0.jar'

ENTRYPOINT ["java","-jar","api-gateway-1.0.jar"]