# Usa Amazon Corretto 17 con Alpine para minimizar el tamaño
FROM amazoncorretto:17-alpine-jdk

# Establecer directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el JAR al contenedor
ADD target/fullstack-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que correrá la aplicación (Render lo detectará)
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "/app/app.jar"]