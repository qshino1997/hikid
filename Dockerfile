# Giai đoạn 1: Build ứng dụng bằng Maven
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app

# Copy toàn bộ mã nguồn vào container
COPY . .

# Build file .war
RUN mvn clean package -DskipTests

# Giai đoạn 2: Deploy vào Tomcat
FROM tomcat:9.0-jdk17-temurin

# Xóa ứng dụng mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy file WAR đã build ở giai đoạn 1 vào Tomcat
COPY --from=build /app/target/kid_store.war /usr/local/tomcat/webapps/ROOT.war

# Expose cổng 8080
EXPOSE 8080

# Lệnh mặc định để khởi động Tomcat
CMD ["catalina.sh", "run"]
