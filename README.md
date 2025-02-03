## Dunice Java Intensive

В данном репозитории будут храниться решения практических задач из интенсива Java
Каждая выполненная задача будет подкреплена ссылкой на её соответсвующее решение
<br/>
<br/>
#### Практические задания:
#### 1. Java Basics - Core - JVM
   - [Команда](java-basic-core-jvm/src/main/resources/scripts/archive_and_run.sh) для запуска Java-приложения myapp.jar с аргументами JVM
   - Построение [строки](java-basic-core-jvm/src/main/java/net/dunice/intensive/basics/StringConcatenation.java) с числами от 1 до 100, используя StringBuilder
   - Удаление [дубликатов](java-basic-core-jvm/src/main/java/net/dunice/intensive/basics/RemoveDuplicates.java) целых значений из списка без изменения последовательности
   - [Поиск](java-basic-core-jvm/src/main/java/net/dunice/intensive/basics/FindMax.java) максимального значения в списке без использования sort() и max()
   - Создание собственных [исключений](java-basic-core-jvm/src/main/java/net/dunice/intensive/basics/UserAgeChecker.java) для валидации возраста
   - [Фильтрация](java-basic-core-jvm/src/main/java/net/dunice/intensive/basics/StreamApi.java) строк с длиной < 5 и преобразование оставшихся
   - [Поиск](java-basic-core-jvm/src/main/java/net/dunice/intensive/basics/spring/UserService.java) пользователя в Spring UserService
#### 2. Java Concurrency
   - Создание потокобезопасного [сета](java-basic-core-jvm/src/main/java/net/dunice/intensive/core/ThreadSafeSets.java) из 32 элементов 
   - [Запуск](java-concurrency/src/main/java/net/dunice/intensive/concurrency/ForkJoinPools.java) двух [задач](java-concurrency/src/main/java/net/dunice/intensive/concurrency/ParallelStreamRecursiveTask.java) в ForkJoinPool 
   - Разблокирующийся [демон поток](java-concurrency/src/main/java/net/dunice/intensive/concurrency/DaemonTask.java)
#### 3. Spring, Spring Boot
   - Создание своего [Scope](spring_spring_boot/src/main/java/net/dunice/intensive/spring_boot/tasks/CachedPoolScope.java)
   - [Поиск](spring_spring_boot/src/main/java/net/dunice/intensive/spring_boot/tasks/GetServices.java) всех Service бинов без использования рефлексии
   - Перехват запроса и [добавление](spring_spring_boot/src/main/java/net/dunice/intensive/spring_boot/tasks/AddInfoHeaderFilter.java) произвольного заголовка
   - Запуск вложенной [транзакции](spring_spring_boot/src/main/java/net/dunice/intensive/spring_boot/tasks/SelfTransactionCallsService.java) (REQUIRES_NEW) внутри одного сервиса
#### 4. [Quiz Spring Boot Web Project](spring_spring_boot): 
- Простой REST API сервис для игры в викторины
#### 5. DBMS (Data Base Managing Systems)
> Вставьте содержимое следующих файлов в консоль mongosh после авторизации
   1. Поиска книги с названием ["1984"](dbms/src/main/resources/mongo_tasks.txt)
   2. Поиска книг, опубликованных [раньше 1950 года](dbms/src/main/resources/mongo_tasks.txt)
