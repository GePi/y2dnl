FROM bellsoft/liberica-openjdk-alpine:17-cds

RUN apk update && apk add --no-cache ffmpeg
RUN apk -U add yt-dlp

ENV TZ=Europe/Moscow
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /app/tmp
WORKDIR /app

COPY target/y2dnl-0.0.2.jar y2dnl-0.0.2.jar

ENTRYPOINT ["java", "-jar", "y2dnl-0.0.2.jar"]