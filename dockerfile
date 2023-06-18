# Используйте базовый образ с Java
FROM openjdk:17

# Установка рабочей директории внутри контейнера
WORKDIR /app

ARG JAR_FILE=target/demo_hh_stat-0.0.1-SNAPSHOT.jar

# Копирование JAR-файла внутрь контейнера
COPY ${JAR_FILE} demo_hh_stat-0.0.1-SNAPSHOT.jar
COPY Manifest.mf /app/Manifest.mf

# Команда запуска приложения
CMD ["java", "-jar", "demo_hh_stat-0.0.1-SNAPSHOT.jar", "-m", "Manifest.mf"]
