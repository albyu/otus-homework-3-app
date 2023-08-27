FROM eclipse-temurin:8-jre
ARG APP_HOME=/opt/otus-homework
RUN mkdir $APP_HOME
WORKDIR $APP_HOME
COPY build/libs/otus-homework.jar $APP_HOME/otus-homework.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","otus-homework.jar"]
