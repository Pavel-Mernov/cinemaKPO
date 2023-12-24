Данное приложение позволяет кинотеатру:
1. Добавлять фильмы в базу данных (add new movie).
2. Удалять существующий фильм из базы  (remove movie).
3. Добавлять сеансы для конкретного фильма и назначать сеансу конкретное время (add movie show).
4. Удалять уже существующие сеансы (remove movie show).
5. Продавать билеты на конкретный сеанс, если этот сеанс ещё не начался и свободные места на этот сеанс есть (Sell ticket).
6. Возвращать билеты за конкретное место на конкретном сеансе, если сеанс ещё начался и освобождаемое место занято в принципе.

Возможность редактирования данных о фильмах и о сеансах реализована в виде добавления и удаления фильмов и сеансов.
Данные о фильмах и о сеансах (для каждого сеанса хранится также информация о проданных билетах) хранятся в файлах формата .csv (самый простой и читаемый формат для хранения данных). При этом, данных формат не требует подключения библиотек сериализации.

Интерфейс приложения интуитивно понятен людям, знающим английский язык на уровне B1 и выше. Все запросы к пользователю выводятся в консоль на английском языке.
