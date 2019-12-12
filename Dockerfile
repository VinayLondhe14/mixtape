FROM openjdk:8u222-jre-slim-buster

VOLUME /app/data
COPY build/libs/mixtape-service.jar /
ENTRYPOINT ["java", "-Done-jar.silent=true", "-jar", "/mixtape-service.jar", "mixtape.json", "changes.json", "output.json"]