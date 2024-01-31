package org.liny.linycoreserverevents.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Vector3 implements Vector {

    public @NotNull Double x = 0.0D, z = 0.0D, y = 0.0D;
    public @Nullable String stringContainer;

    public Vector3() {}
    public Vector3(@NotNull Double x, @NotNull Double y, @NotNull Double z) {
        this.x = x; this.z = z; this.y = y;
    }
    public Vector3(@NotNull Double x, @NotNull Double y, @NotNull Double z, @Nullable String stringValue) {
        this.x = x; this.z = z; this.stringContainer = stringValue; this.y = y;
    }

    public boolean equals (@NotNull Vector3 value) {
        return Objects.equals(this, value);
    }

}
