package org.liny.linycoreserverevents.Abstract;

public interface ServerEvent {
    void start ();

    void stop ();

    Integer getTimeSeconds ();

    String getName ();

    Boolean canSpawn ();

    Float getPercent ();

    Boolean isClosed ();
}
