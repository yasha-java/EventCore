package org.liny.linycoreserverevents.Data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.liny.linycoreserverevents.Abstract.ServerEvent;

import java.util.Optional;

public class EventData {
    private ServerEvent previousEvent = null;
    public @NotNull Optional<ServerEvent> getPreviousEvent() {
        return Optional.ofNullable(this.previousEvent);
    }
    public void setPreviousEvent(@Nullable ServerEvent previousEvent) {
        this.previousEvent = previousEvent;
    }

    private ServerEvent currentEvent = null;
    public @NotNull Optional<ServerEvent> getCurrentEvent() {
        return Optional.ofNullable(this.currentEvent);
    }
    public void setCurrentEvent(@Nullable ServerEvent currentEvent) {
        this.currentEvent = currentEvent;
    }

    private ServerEvent futureEvent = null;
    public @NotNull Optional<ServerEvent> getFutureEvent() {
        return Optional.ofNullable(this.futureEvent);
    }
    public void setFutureEvent(@Nullable ServerEvent futureEvent) {
        this.futureEvent = futureEvent;
    }
}
