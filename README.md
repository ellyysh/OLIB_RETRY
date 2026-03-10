# OLIB Works App

## Описание

Android приложение для поиска книг через Open Library API.  
Приложение позволяет искать книги, просматривать список результатов, открывать детали книги и добавлять книги в избранное.

## Используемые технологии

- Jetpack Compose
- ViewModel
- Repository
- Retrofit
- Coroutines
- Navigation Compose

## API

Open Library API

Поиск книг:
https://openlibrary.org/search.json?q={query}

Детали книги:
https://openlibrary.org/works/{id}.json

API не требует ключа.

## Функционал

- экран списка книг
- экран деталей книги
- избранное
- состояния UI: Loading / Error / Empty / Success
- кнопка Retry при ошибке сети
- retry policy для сетевых запросов

## Как запустить

1. Открыть проект в Android Studio
2. Выполнить Gradle Sync
3. Запустить эмулятор
4. Нажать Run

## Скриншоты
