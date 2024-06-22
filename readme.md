# y2dnl - скачивание видео, преобразование в аудио, выкладывание преобразованных в объектное хранилище

## Описание
`y2dnl` — это сервис для взаимодействия с API из репозитория [`y2rss_api`](https://github.com/GePi/y2rss_api).
Так как преобразование файлов — затратная операция, сервис спроектирован таким образом, что можно запускать любое количество его инстанций приложения, на разных серверах, не делая никаких дополнительных действий для их синхронизации.

## Функциональность
- **Загрузка мультимедийных файлов**: Сервис использует `yt-dlp` для загрузки файлов.
- **Перекодирование файлов**: С помощью `ffmpeg` сервис перекодирует загруженные файлы в аудио формат.
- **Интеракция с API**: Сервис взаимодействует с `y2rss_api` для получения списка файлов которые необходимо закачать и перекодировать и отправки ссылок на закаченные в хранилище файлы обратно.

## Сборка и запуск проекта с использованием Docker

### Сборка Docker-образа
Для сборки Docker-образа выполните следующую команду в корневой директории проекта:
```sh
docker build -t docker.io/gepi/y2dnl:0.0.2 .
```
### Для локального использования 
Добавьте следующие файлы в каталог y2dlp:
- ffmpeg.exe;
- ffprobe.exe;
- yt-dlp.exe.

И пропишите переменные пути к файлу загрузки:
```
  TARGET_ABSOLUTE_PATH=<рабочий каталог для yt-dlp>\y2files
  LOADER_FILE_NAME=yt-dlp.exe
  LOADER_ABSOLUTE_PATH=<путь к файлам (yt-dlp.ext...)>\ytdlp
```