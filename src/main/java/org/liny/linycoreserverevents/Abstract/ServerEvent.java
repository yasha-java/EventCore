package org.liny.linycoreserverevents.Abstract;

public abstract class ServerEvent {

    public abstract void start ();

    public abstract void stop ();

    public abstract Integer getTimeSeconds ();

    public abstract String getName ();

}
