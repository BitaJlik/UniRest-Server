# UniRest-Server
### [Main developer](https://t.me/BitaJl1k)
### [Second developer](https://t.me/lisowsky)

## Опис проекту
UniRest-Server - це проект, який створено для керування БД та запитами API з інших клієнтів для управлінням гуртожитків та автоматизації різних процесів 

### Запуск
- Запустіть сервер: `java -jar UniRest-Server.jar`

### (API-BASE) `object` = `[dormitory,floor,room,user,request,cooker]`
Є загальні запити для основних моделей інші запити будуть нижче:

- `GET` URL::/`object`/get - Повертає об'єкт за параметром {id}
- `GET` URL::/`object`/all - Повертає всі об'єкти з таблиці <object>
- `GET` URL::/`object`/add - Повертає id збереженого об'єкта переданого у body у вигляді json
- `GET` URL::/`object`/remove - Повертає code=202 якщо видалення успішне 400 якщо такого {id} не знайдено 

### (API) Реалізовані методи
- `GET` =   
- `GET`
- `GET`

### Методи в плані\прогресі
- `NO-OP`

# Реалізовано для головного проекту

[![Readme Card](https://github-readme-stats.vercel.app/api/pin/?username=BitaJlik&repo=UniRest-Android)](https://github.com/BitaJlik/UniRest-Android)
