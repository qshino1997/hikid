FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM tomcat:9.0-jdk17-temurin

# 1. Cài envsubst
USER root
RUN apt-get update && apt-get install -y gettext-base && rm -rf /var/lib/apt/lists/*

# 2. Xóa webapps mặc định và sao chép WAR
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/kid_store.war /usr/local/tomcat/webapps/ROOT.war

# 3. Đổi server.xml thành template
RUN mv /usr/local/tomcat/conf/server.xml /usr/local/tomcat/conf/server.xml.template

# 4. EXPOSE vẫn giữ 8080 (container-internal), Railway/Render dùng ENV PORT
EXPOSE 8080

# 5. ENTRYPOINT: trước khi run, patch server.xml
CMD envsubst '$$PORT' < /usr/local/tomcat/conf/server.xml.template \
     > /usr/local/tomcat/conf/server.xml \
  && catalina.sh run