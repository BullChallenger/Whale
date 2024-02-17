# Use official OpenJDK image as base image
FROM openjdk:17

CMD ["./gradlew", "clean", "build -x test"]

ARG JAR_FILE=build/libs/Whale-0.0.1-SNAPSHOT.jar

# Set the working directory inside the container
WORKDIR /whale

# Copy the JAR file into the container
COPY ${JAR_FILE} whale.jar

# Expose the port that the application will run on
EXPOSE 8080

ENV PROFILE default

# Command to run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-jar","/whale/whale.jar"]
