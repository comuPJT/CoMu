# build stage
FROM openjdk:8-jdk-alpine as builder
# RUN addgroup -S spring && adduser -S spring -G spring
# USER spring:spring

COPY . .
# RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test
RUN mkdir -p build/dependency
WORKDIR build/dependency
RUN jar -xf ../libs/Comu-0.0.1-SNAPSHOT.jar
WORKDIR ../..

# product stage
# FROM openjdk:8-jdk-alpine
FROM openjdk:8-jdk
RUN apt update && apt install ffmpeg -y
RUN apt install youtube-dl -y
ARG DEPENDENCY=build/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app
COPY ./stream.sh /app/lib/stream.sh
ENTRYPOINT ["java","-cp","app:app/lib/*","com.listener.comu.BackendApplication"]