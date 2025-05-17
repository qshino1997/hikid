# Dùng Tomcat có sẵn JDK
FROM tomcat:9.0-jdk17-temurin

# Xóa các ứng dụng mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy file .war của bạn vào thư mục webapps và đổi tên thành ROOT.war
COPY target/kid_store.war /usr/local/tomcat/webapps/ROOT.war

# Mặc định CMD là chạy Tomcat