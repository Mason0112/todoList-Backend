# 使用一個包含 OpenJDK 的基礎映像檔
FROM openjdk:17-jdk-slim

# 設定維護者資訊 (可選)
LABEL maintainer="your-email@example.com"

# 設定工作目錄
WORKDIR /app

# 將建構好的 Spring Boot JAR 檔案複製到容器中
# 假設你的 JAR 檔案位於 ./build/libs/your-app-name.jar
COPY build/libs/your-app-name.jar app.jar

# 暴露應用程式的埠
EXPOSE 8080

# 啟動 Spring Boot 應用程式
ENTRYPOINT ["java", "-jar", "app.jar"]