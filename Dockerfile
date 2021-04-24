FROM amazoncorretto:11
COPY app.jar /
CMD ["java", "-jar", "/app.jar"] 
