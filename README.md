## Dunice Java Intensive

В данном репозитории будут храниться решения практических задач из интенсива Java
Каждая выполненная задача будет подкреплена ссылкой на её соответсвующее решение
<br/>
<br/>
#### Практические задания:
1. Java Basics - Core - JVM
   1. [Команда](java-basic-core-jvm/src/main/resources/scripts/archive_and_run.sh) для запуска Java-приложения myapp.jar с аргументами JVM
   2. Построение [строки](java-basic-core-jvm/src/main/java/net/dunice/intensive/basics/StringConcatenation.java) с числами от 1 до 100, используя StringBuilder
   3. Удаление [дубликатов](java-basic-core-jvm/src/main/java/net/dunice/intensive/basics/RemoveDuplicates.java) целых значений из списка без изменения последовательности
   4. [Поиск](java-basic-core-jvm/src/main/java/net/dunice/intensive/basics/FindMax.java) максимального значения в списке без использования sort() и max()
   5. Создание собственных [исключений](java-basic-core-jvm/src/main/java/net/dunice/intensive/basics/UserAgeChecker.java) для валидации возраста
   6. [Фильтрация](java-basic-core-jvm/src/main/java/net/dunice/intensive/basics/StreamApi.java) строк с длиной < 5 и преобразование оставшихся
   7. [Поиск](java-basic-core-jvm/src/main/java/net/dunice/intensive/basics/spring/UserService.java) пользователя в Spring UserService
2. Java Concurrency
   1. Создание потокобезопасного [сета](java-basic-core-jvm/src/main/java/net/dunice/intensive/core/ThreadSafeSets.java) из 32 элементов 
   2. [Запуск](java-concurrency/src/main/java/net/dunice/intensive/concurrency/ForkJoinPools.java) двух [задач](java-concurrency/src/main/java/net/dunice/intensive/concurrency/ParallelStreamRecursiveTask.java) в ForkJoinPool 
   3. Разблокирующийся [демон поток](java-concurrency/src/main/java/net/dunice/intensive/concurrency/DaemonTask.java)
3. Spring, Spring Boot
   1. Создание своего [Scope](spring_spring_boot/src/main/java/net/dunice/intensive/spring_boot/tasks/CachedPoolScope.java)
   2. [Поиск](spring_spring_boot/src/main/java/net/dunice/intensive/spring_boot/tasks/GetServices.java) всех Service бинов без использования рефлексии
