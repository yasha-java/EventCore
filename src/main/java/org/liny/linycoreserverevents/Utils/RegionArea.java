package org.liny.linycoreserverevents.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class RegionArea<R extends Vector> {

    public @Nullable R fPoint, sPoint;
    public @Nullable List<String> container = new ArrayList<>();

    public RegionArea () {}
    public RegionArea (@NotNull R fPoint, @NotNull R sPoint) {

        this.fPoint = fPoint; this.sPoint = sPoint;

        if (fPoint instanceof Vector2) {

            container.add(((Vector2)fPoint).stringContainer);
            container.add(((Vector2)sPoint).stringContainer);

            Vector2 fPointTemp = new Vector2(java.lang.Math.max(((Vector2) fPoint).x, ((Vector2) sPoint).x),
                    java.lang.Math.max(((Vector2) fPoint).z, ((Vector2) sPoint).z));
            Vector2 sPointTemp = new Vector2(java.lang.Math.min(((Vector2) fPoint).x, ((Vector2) sPoint).x),
                    java.lang.Math.min(((Vector2) fPoint).z, ((Vector2) sPoint).z));

            this.fPoint = (R) fPointTemp; this.sPoint = (R) sPointTemp;

        } else {

            container.add(((Vector3)fPoint).stringContainer);
            container.add(((Vector3)sPoint).stringContainer);

            Vector3 fPointTemp = new Vector3(java.lang.Math.max(((Vector3) fPoint).x, ((Vector3) sPoint).x),
                    java.lang.Math.max(((Vector3) fPoint).y, ((Vector3) sPoint).y),
                    java.lang.Math.max(((Vector3) fPoint).z, ((Vector3) sPoint).z));
            Vector3 sPointTemp = new Vector3(java.lang.Math.min(((Vector3) fPoint).x, ((Vector3) sPoint).x),
                    java.lang.Math.min(((Vector3) fPoint).y, ((Vector3) sPoint).y),
                    java.lang.Math.min(((Vector3) fPoint).z, ((Vector3) sPoint).z));

            this.fPoint = (R) fPointTemp; this.sPoint = (R) sPointTemp;

        }

    }
    public RegionArea (@NotNull Vector clazz, @NotNull Location location, @NotNull Double xOffset, @Nullable Double yOffset, @NotNull Double zOffset) {

        if (clazz instanceof Vector2) {

            this.fPoint = (R) new Vector2(location.getX() + xOffset, location.getZ() + zOffset);
            this.sPoint = (R) new Vector2(location.getX() - xOffset, location.getZ() - zOffset);

        } else {

            if (yOffset == null) throw new IllegalArgumentException("Can't create RegionArea of Vector3 with null yOffset.");

            this.fPoint = (R) new Vector3(location.getX() + xOffset, location.getY() + yOffset,  location.getZ() + zOffset);
            this.sPoint = (R) new Vector3(location.getX() - xOffset, location.getY() - yOffset,  location.getZ() - zOffset);

        }

    }

    public @NotNull Collection<Player> getContainedPlayers () {
        return Bukkit.getOnlinePlayers()
                .parallelStream()
                .filter(player -> this.checkPoint(player.getLocation()))
                .collect(Collectors.toList());
    }

    public @NotNull Boolean checkPoint (@NotNull Double x, @NotNull Double y, @Nullable Double z) {

        if (this.fPoint instanceof Vector2) {

            return (((Vector2)this.fPoint).x <= x && x >= ((Vector2) Objects.requireNonNull(this.sPoint)).x) &&
                    (((Vector2)this.fPoint).z <= y && y >= ((Vector2)this.sPoint).z);

        } else {

            if (z == null) throw new IllegalArgumentException("Can't check RegionArea of Vector3 with null parameter 'z'. Use RegionArea.checkPoint(@NotNull x, @NotNull y, @NotNull z) for RegionArea of Vector3");

            return (((Vector3)Objects.requireNonNull(this.fPoint)).x <= x && x >= ((Vector3) Objects.requireNonNull(this.sPoint)).x) &&
                    (((Vector3)this.fPoint).y <= y && y >= ((Vector3)this.sPoint).y) &&
                    (((Vector3)this.fPoint).z <= z && z >= ((Vector3)this.sPoint).z);

        }

    }
    public @NotNull Boolean checkPoint(@NotNull Location location) {
        if (this.fPoint instanceof Vector2) {
            double x = location.getX();
            double z = location.getZ();

            return (((Vector2) this.fPoint).x >= x && x >= ((Vector2) Objects.requireNonNull(this.sPoint)).x) &&
                    (((Vector2) this.fPoint).z >= z && z >= ((Vector2) this.sPoint).z);
         } else {
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            return (((Vector3) Objects.requireNonNull(this.fPoint)).x >= x && x >= ((Vector3) Objects.requireNonNull(this.sPoint)).x) &&
                    (((Vector3) this.fPoint).y >= y && y >= ((Vector3) this.sPoint).y) &&
                    (((Vector3) this.fPoint).z >= z && z >= ((Vector3) this.sPoint).z);
        }
    }


    public @NotNull Vector randomPoint () {

        if (fPoint instanceof Vector2) {

            return new Vector2(

                    ((((Vector2) Objects.requireNonNull(this.sPoint))).x + (java.lang.Math.random() * ((((Vector2)this.fPoint)).x - (((Vector2)this.sPoint)).x))),

                    ((((Vector2)this.sPoint)).z + (java.lang.Math.random() * ((((Vector2)this.fPoint)).z - (((Vector2)this.sPoint)).z)))

            );

        }

        return new Vector3(

                ((((Vector3) Objects.requireNonNull(this.sPoint))).x + (java.lang.Math.random() * ((((Vector3) Objects.requireNonNull(this.fPoint))).x - (((Vector3)this.sPoint)).x))),

                ((((Vector3)this.sPoint)).y + (java.lang.Math.random() * ((((Vector3)this.fPoint)).y - (((Vector3)this.sPoint)).y))),

                ((((Vector3)this.sPoint)).z + (java.lang.Math.random() * ((((Vector3)this.fPoint)).z - (((Vector3)this.sPoint)).z)))

        );

    }

}
