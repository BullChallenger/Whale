# Use official OpenJDK image as base image
FROM openjdk:17

CMD ["./gradlew", "clean", "build -x test"]

ARG JAR_FILE=build/libs/Whale-0.0.1-SNAPSHOT.jar

# Set the working directory inside the container
WORKDIR /Whale

# Copy the JAR file into the container
COPY ${JAR_FILE} whale.jar

# Expose the port that the application will run on
EXPOSE 8088

# Command to run the application
CMD ["java", "-jar", "whale.jar"]
