# SynchronizedServerEvents

Создавайте синхронизированные события с помощью SynchronizedServerEvents, что бы они не стартовались одновременно!
SynchronizedServerEvents простейшая API, для создания эвентов.

# Техническая часть

Скопируйте репо в свой Intellij Idea и сделайте компиляцию package. Перенесите ```original-LinyCoreServerEvents-1.0.jar``` в любое удобное место и запустите команду в консоли Windows:
```
mvn install:install-file -Dfile="Путь_к_original-LinyCoreServerEvents-1.0.jar" -DgroupId=org.liny -DartifactId=LinyCoreServerEvents -Dversion=1.0
```
!!! ДЛЯ ТЕХ У КОГО НЕ ПРОПИСАНА MAVEN В PATH КОНСОЛИ !!!:
```
"<ПУТЬ К ПАПКЕ УСТАНОВКИ>\IntelliJ IDEA Community Edition 2023.2\plugins\maven\lib\maven3\bin\mvn.cmd" install:install-file -Dfile="Путь_к_original-LinyCoreServerEvents-1.0.jar" -DgroupId=org.liny -DartifactId=LinyCoreServerEvents -Dversion=1.0
```

maven: 
```
<dependency>
    <groupId>org.liny</groupId>
    <artifactId>LinyCoreServerEvents</artifactId>
    <version>1.0</version>
    <scope>provided</scope>
</dependency>
```

Для бесперебойного доступа к API советую каждый раз кастовать класс в своем плагине:
```
public @Nullable LinyCoreServerEvents cast () {
    @Nullable LinyCoreServerEvents EventCore = LinyCoreServerEvents.getPlugin(LinyCoreServerEvents.class);
    if (EventCore != null)
        if (!EventCore.isEnabled()) return null;
    return EventCore;
}
```

Для создания своего синхронизированного эвента необходимо создать класс наследованный от ```org.liny.linycoreserverevents.Abstract.ServerEvent```:
```
public class MyEvent extends ServerEvent {

  @Override
  public void start() {} //Вызывается когда эвент должен стартовать
  
  @Override
  public void stop() {} //Вызывается когда эвент должен ПРИНУДИТЕЛЬНО закончится (не используется)
  
  @Override
  public void getTimeSeconds() {} //Вызывается когда необходимо получить время для запуска эвента
  
  @Override
  public void getName() {} //Вызывается когда необходимо получить название эвента

}
```

Теперь нам необходимо зарегистрировать наш синхронный эвент:
```
Objects.requireNonNull(cast()).getEventSpawnManager().registerEvent(new MyEvent(), "myEvent"); //Стоит учитывать то, что вторым аргументом идет название события для использования В КОМАНДЕ /events, а getName() в ServerEvent содержит визуальное название Эвента
```

# Команды:
```/events``` - Останавливает запуск эвентов по таймеру (таймер не останавливается)
```/events start <название>``` - Принудительно запускает зарегистрированный эвент, если такого нет, то отображает список загруженных эвентов


