package org.liny.linycoreserverevents.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Vector2 implements Vector {

    public @NotNull Double x = 0.0D, z = 0.0D;
    public @Nullable String stringContainer;

    public Vector2 () {}
    public Vector2 (@NotNull Double x, @NotNull Double z) {
        this.x = x; this.z = z;
    }
    public Vector2 (@NotNull Double x, @NotNull Double z, @Nullable String stringValue) {
        this.x = x; this.z = z; this.stringContainer = stringValue;
    }

    public boolean equals (@NotNull Vector2 value) {
        return Objects.equals(this, value);
    }

}
