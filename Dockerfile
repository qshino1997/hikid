# Giai đoạn 1: Build bằng Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Tạo thư mục chứa mã nguồn
WORKDIR /app

# Copy toàn bộ mã nguồn vào container
COPY . .

# Build file WAR
RUN mvn clean package -DskipTests


# Giai đoạn 2: Deploy vào Tomcat
FROM tomcat:9.0-jdk17-temurin

# Xóa các ứng dụng mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy file WAR từ giai đoạn build sang Tomcat và đổi tên thành ROOT.war
COPY --from=build /app/target/kid_store.war /usr/local/tomcat/webapps/ROOT.war

# Mặc định CMD là chạy Tomcat
EXPOSE 8080
CMD ["catalina.sh", "run"]
