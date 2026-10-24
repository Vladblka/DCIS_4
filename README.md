# Лабораторная работа: работа с базой данных в Spring

Лабораторная работа по теме «Работа с базой данных PostgreSQL в Spring Framework».
Вариант: «Музыкальный инструмент».

## Цель работы

Освоить подключение Spring Boot-приложения к реляционной базе данных PostgreSQL
и выполнение CRUD-операций при помощи `JdbcTemplate` на примере сущности
«Музыкальный инструмент». Научиться разделять приложение на слои
(модель, репозиторий, сервис, консольный интерфейс), внедрять зависимости
через конструктор и использовать декларативные транзакции (`@Transactional`).

## Структура проекта

```
test_harness/
├── pom.xml
└── src/main/
    ├── java/ru/example/instruments/
    │   ├── MusicalInstrumentsApplication.java        — точка входа, запуск Spring Boot
    │   ├── model/
    │   │   └── MusicalInstrument.java                — сущность (товар)
    │   ├── repository/
    │   │   └── MusicalInstrumentRepository.java      — SQL-запросы через JdbcTemplate
    │   ├── service/
    │   │   └── MusicalInstrumentService.java         — слой бизнес-логики, транзакции
    │   └── console/
    │       └── ConsoleRunner.java                    — консольное меню, ввод и проверка данных
    └── resources/
        ├── application.properties                    — параметры подключения к БД
        └── schema.sql                                — создание таблицы
```

## Как реализованы требования задания

| № | Требование | Где реализовано |
|---|------------|------------------|
| 1 | Сущность с идентификатором | `MusicalInstrument.id`, в БД `id BIGSERIAL PRIMARY KEY` |
| 2 | Не менее четырёх текстовых полей | `name`, `brand`, `instrumentType`, `material` в `MusicalInstrument` |
| 3 | Числовые поля | `price` (`BigDecimal`), `quantity` (`Integer`) |
| 4 | Добавление записи | `MusicalInstrumentRepository.save()` (`INSERT ... RETURNING id`), пункт меню 1 |
| 5 | Просмотр всех записей | `MusicalInstrumentRepository.findAll()`, пункт меню 2 |
| 6 | Изменение записи по id | `MusicalInstrumentRepository.update()`, пункт меню 3 |
| 7 | Удаление записи по id | `MusicalInstrumentRepository.deleteById()`, пункт меню 4 |
| 8 | Поиск по части значения поля | `MusicalInstrumentRepository.findByBrand()` (`LOWER(brand) LIKE LOWER(?)`), пункт меню 5 |
| 9 | Проверка вводимых данных | `ConsoleRunner.readText()`, `readInt()`, `readDecimal()` и ограничения `CHECK` в `schema.sql` |

## Описание конфигурации

Приложение построено на Spring Boot, поэтому отдельный класс-конфигурация не нужен:
аннотация `@SpringBootApplication` включает сканирование компонентов и автонастройку.
Spring сам создаёт следующие бины:

- `DataSource` и `JdbcTemplate` — автоматически, на основе `spring-boot-starter-jdbc` и параметров из `application.properties`;
- `MusicalInstrumentRepository` — слой доступа к данным (`@Repository`), получает `JdbcTemplate` через конструктор;
- `MusicalInstrumentService` — слой бизнес-логики (`@Service`, `@Transactional`), получает репозиторий через конструктор;
- `ConsoleRunner` — консольный интерфейс (`@Component`, `CommandLineRunner`), получает сервис через конструктор.

Файл `application.properties` настраивает подключение. Значения берутся из переменных
окружения, а если они не заданы, используются значения по умолчанию:

```
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/music_store}
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
```

Файл `schema.sql` выполняется при каждом запуске (`spring.sql.init.mode=always`).
Таблица создаётся командой `CREATE TABLE IF NOT EXISTS`, поэтому существующие данные не теряются:

```sql
CREATE TABLE IF NOT EXISTS musical_instruments (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    brand VARCHAR(120) NOT NULL,
    instrument_type VARCHAR(80) NOT NULL,
    material VARCHAR(120) NOT NULL,
    price NUMERIC(12, 2) NOT NULL CHECK (price >= 0),
    quantity INTEGER NOT NULL CHECK (quantity >= 0)
);
```

## Как запустить

1. Установить PostgreSQL и создать базу данных:

   ```sql
   CREATE DATABASE music_store;
   ```

2. При необходимости задать параметры подключения переменными окружения
   `DB_URL`, `DB_USER`, `DB_PASSWORD`.
3. Запустить приложение: `mvn spring-boot:run`

Либо собрать jar и запустить его:

```bash
mvn clean package
java -jar target/musical-instruments-1.0.0.jar
```

Требования: Java 17, Maven, запущенный PostgreSQL.

## Что выводит программа

После старта контекста Spring выполняется `ConsoleRunner`: он выводит меню и в цикле
обрабатывает команды пользователя. Все ошибки (неверный ввод, нарушение ограничений
в БД) перехватываются, выводится сообщение «Ошибка: ...», и работа продолжается.

Меню:

| Команда | Действие |
|---------|----------|
| 1 | Добавить инструмент (ввод всех полей, кроме id) |
| 2 | Показать все записи |
| 3 | Изменить запись по id |
| 4 | Удалить запись по id |
| 5 | Найти записи по части названия бренда (без учёта регистра) |
| 0 | Выйти |

Для цены допускается десятичный разделитель `.` или `,`.

Пример вывода:

```
=== Учет музыкальных инструментов ===

1 — добавить  2 — показать все  3 — изменить по id
4 — удалить по id  5 — поиск по бренду  0 — выход
Выберите действие: 1
Название: Stratocaster
Бренд: Fender
Тип инструмента (гитара, пианино...): гитара
Материал: ольха
Цена: 85000,50
Количество: 3
Добавлено: №1 | Stratocaster | бренд: Fender | тип: гитара | материал: ольха | цена: 85000.50 | количество: 3

Выберите действие: 5
Введите часть названия бренда: fen
№1 | Stratocaster | бренд: Fender | тип: гитара | материал: ольха | цена: 85000.50 | количество: 3

Выберите действие: 4
Введите id удаляемой записи: 7
Запись не найдена.

Выберите действие: 0
Работа завершена.
```

Для изменения и удаления метод репозитория возвращает число затронутых строк,
а сервис преобразует его в `boolean`: если запись с указанным id не найдена,
выводится сообщение «Запись не найдена».
