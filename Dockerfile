#
# Build stage
#
FROM maven AS build
WORKDIR /home/app
COPY src ./src
COPY pom.xml .
RUN mvn clean package

#
# Package stage
#
FROM openjdk
COPY --from=build /home/app/target/file-integration.jar /opt/file-integration/file-integration.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/opt/file-integration/file-integration.jar"]