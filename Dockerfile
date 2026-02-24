FROM maven:3.9.8-eclipse-temurin-17

WORKDIR /app
COPY . /app

# baixa deps (cache)
RUN mvn -q -DskipTests dependency:go-offline

CMD ["mvn", "-q", "test"]