# [Курс BaseJava (обновленный и переработанный)](http://javaops.ru/reg/basejava)

## Разработка web-приложения "База данных резюме"

Веб-приложение для хранения, просмотра и редактирования резюме.  
Проект основан на курсе [basejava](https://github.com/JavaOPs/basejava) от Григория Кислина (urise), полностью докеризован и готов к запуску одной командой.

**Мои улучшения**:
- Проект изначально не использовал Maven — я самостоятельно мигрировал его на Maven (pom.xml, сборка WAR).
- Заменил стандартные JSP + JSTL include на **Freemarker** для более удобного и современного шаблонирования страниц.
- Добавил Docker & Docker Compose с multi-stage сборкой — запуск одной командой, без локальной установки Java/Maven/Tomcat.

## Технологии

- **Java 17** — основной язык
- **Jakarta Servlet 5.0 + Freemarker** — веб-слой и шаблоны
- **PostgreSQL** — база данных (хранение резюме)
- **Tomcat 10.1** — веб-сервер
- **Gson / Jackson / JAXB** — сериализация и работа с данными
- **JUnit 5** — тесты
- **Docker & Docker Compose** — контейнеризация (полный стек в контейнерах)

Я успешно окончил курс "Java-разработчик веб-приложений" с разработкой этого проекта.

![Сертификат с отличием](certificate.jpg)

## Требования

**Только Docker!**  
Ничего больше устанавливать не нужно.

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (для Windows/Mac)  
  или Docker Engine (для Linux)

## Как запустить

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/AlexLight44/basejava.git
   cd basejava