FROM maven:3.8.1-jdk-11 as builder
COPY ./pom.xml /tmp
COPY src/ /tmp/src/
WORKDIR /tmp
RUN mvn package
ARG aws_region
ARG aws_access_key_id
ARG aws_secret_access_key
ENV AWS_REGION=$aws_region
ENV AWS_ACCESS_KEY_ID=$aws_access_key_id
ENV AWS_SECRET_ACCESS_KEY=$aws_secret_access_key
RUN mvn package
FROM openjdk:11
COPY --from=builder /tmp/target/accounts_api-0.0.1-SNAPSHOT.jar /tmp/
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/tmp/accounts_api-0.0.1-SNAPSHOT.jar"]