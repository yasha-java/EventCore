package org.liny.linycoreserverevents.Abstract;

import org.bukkit.plugin.Plugin;

public interface ServerEvent {
    void start ();

    void stop ();

    Integer getTimeSeconds ();

    String getName ();

    Boolean canSpawn ();

    Float getPercent ();

    Boolean isClosed ();

    Plugin getMain ();
}
