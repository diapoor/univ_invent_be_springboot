# Build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
# Chạy lệnh này để tải dependencies trước
RUN mvn dependency:go-offline
# Sao chép mã nguồn vào thư mục làm việc
COPY src ./src
# Build project và tạo file JAR
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
# Sao chép file JAR từ build stage sang run stage
COPY --from=build /app/target/univInvent-0.0.1-SNAPSHOT.jar univinvent.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "univinvent.jar"]
