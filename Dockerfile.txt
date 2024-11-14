FROM amazoncorretto:11-alpine-jdk
ARG JAR_FILE=target/*.jar
ARG PROFOLES
ARG ENV
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-jar", "app.jar"]
#여기서 profile 이름을 바꾸면 그걸로 실행됨
#server랑 spring에 있는 D = yml파일 - 도커가 실행될때 이름을 이걸로 바꿈
#매개변수 PROFOLES, ENV