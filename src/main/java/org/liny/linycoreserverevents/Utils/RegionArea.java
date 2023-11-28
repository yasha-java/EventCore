package org.liny.linycoreserverevents.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegionArea {

    public @NotNull Vector fPoint = new Vector2(), sPoint = new Vector2();
    public @Nullable List<String> container = new ArrayList<>();

    public RegionArea () {}
    public RegionArea (@NotNull Vector fPoint, @NotNull Vector sPoint) {

        if (!Objects.equals(fPoint.getClass(), sPoint.getClass())) throw new IllegalArgumentException(" Can't create RegionArea with " + fPoint.getClass() + ", " + sPoint.getClass());

        this.fPoint = fPoint; this.sPoint = sPoint;

        if (fPoint instanceof Vector2) {

            container.add(((Vector2)fPoint).stringContainer);
            container.add(((Vector2)sPoint).stringContainer);

            Vector2 fPointTemp = new Vector2(java.lang.Math.max(((Vector2) fPoint).x, ((Vector2) sPoint).x),
                    java.lang.Math.max(((Vector2) fPoint).z, ((Vector2) sPoint).z));
            Vector2 sPointTemp = new Vector2(java.lang.Math.min(((Vector2) fPoint).x, ((Vector2) sPoint).x),
                    java.lang.Math.min(((Vector2) fPoint).z, ((Vector2) sPoint).z));

            this.fPoint = fPointTemp; this.sPoint = sPointTemp;

        } else {

            container.add(((Vector3)fPoint).stringContainer);
            container.add(((Vector3)sPoint).stringContainer);

            Vector3 fPointTemp = new Vector3(java.lang.Math.max(((Vector3) fPoint).x, ((Vector3) sPoint).x),
                    java.lang.Math.max(((Vector3) fPoint).y, ((Vector3) sPoint).y),
                    java.lang.Math.max(((Vector3) fPoint).z, ((Vector3) sPoint).z));
            Vector3 sPointTemp = new Vector3(java.lang.Math.min(((Vector3) fPoint).x, ((Vector3) sPoint).x),
                    java.lang.Math.min(((Vector3) fPoint).y, ((Vector3) sPoint).y),
                    java.lang.Math.min(((Vector3) fPoint).z, ((Vector3) sPoint).z));

            this.fPoint = fPointTemp; this.sPoint = sPointTemp;

        }

    }
    public RegionArea (@NotNull Vector clazz, @NotNull Location location, @NotNull Double xOffset, @Nullable Double yOffset, @NotNull Double zOffset) {

        if (clazz instanceof Vector2) {

            /*
            [19:07:13 INFO]:fpoint x 1569.500000 : spoint x -591.500000
                            fpoint z 1529.500000 : spoint z -631.500000
                            vX 1546.149737 : vZ -610.779321

                            first -> false
                            second -> false
                            all -> false
             */

            this.fPoint = new Vector2(location.getX() + xOffset, location.getZ() + zOffset);
            this.sPoint = new Vector2(location.getX() - xOffset, location.getZ() - zOffset);

        } else {

            if (yOffset == null) throw new IllegalArgumentException("Can't create RegionArea of Vector3 with null yOffset.");

            this.fPoint = new Vector3(location.getX() + xOffset, location.getY() + yOffset,  location.getZ() + zOffset);
            this.sPoint = new Vector3(location.getX() - xOffset, location.getY() - yOffset,  location.getZ() - zOffset);

        }

    }

    public @NotNull Boolean checkPoint (@NotNull Double x, @NotNull Double y, @Nullable Double z) {

        if (this.fPoint instanceof Vector2) {

            return (((Vector2)this.fPoint).x <= x && x >= ((Vector2)this.sPoint).x) &&
                    (((Vector2)this.fPoint).z <= y && y >= ((Vector2)this.sPoint).z);

        } else {

            if (z == null) throw new IllegalArgumentException("Can't check RegionArea of Vector3 with null parameter 'z'. Use RegionArea.checkPoint(@NotNull x, @NotNull y, @NotNull z) for RegionArea of Vector3");

            return (((Vector3)this.fPoint).x <= x && x >= ((Vector3)this.sPoint).x) &&
                    (((Vector3)this.fPoint).y <= y && y >= ((Vector3)this.sPoint).y) &&
                    (((Vector3)this.fPoint).z <= z && z >= ((Vector3)this.sPoint).z);

        }

    }
    public @NotNull Boolean checkPoint(@NotNull Location location) {
        if (this.fPoint instanceof Vector2) {
            double x = location.getX();
            double z = location.getZ();

            /*
            Bukkit.getConsoleSender().sendMessage("fpoint x %f : fpoint x %f\nspoint z %f : spoint z %f\nvX %f : vZ %f"
                    .formatted(((Vector2) this.fPoint).x,
                            ((Vector2) this.fPoint).z,
                            ((Vector2) this.sPoint).x,
                            ((Vector2) this.sPoint).z,
                            x, z));
            Bukkit.getConsoleSender().sendMessage("first -> %s\nsecond -> %s\nall -> %s"
                    .formatted((((Vector2) this.fPoint).x <= x && x <= ((Vector2) this.sPoint).x),
                            (((Vector2) this.fPoint).z <= z && z <= ((Vector2) this.sPoint).z),
                            (((Vector2) this.fPoint).x <= x && x <= ((Vector2) this.sPoint).x) && (((Vector2) this.fPoint).z <= z && z <= ((Vector2) this.sPoint).z)));

            [19:07:13 INFO]:fpoint x 1569.500000 : fpoint z -591.500000
                            spoint x 1529.500000 : spoint z -631.500000
                            vX 1546.149737 : vZ -610.779321

                            first -> false
                            second -> false
                            all -> false
             */

            return (((Vector2) this.fPoint).x >= x && x >= ((Vector2) this.sPoint).x) && // 1569 >= 1546(true) && 1546 >= 1529(true) - true
                    (((Vector2) this.fPoint).z >= z && z >= ((Vector2) this.sPoint).z); // -591 >= -610(true) && -610 >= -631(true) - true   | true
         } else {
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            return (((Vector3) this.fPoint).x >= x && x >= ((Vector3) this.sPoint).x) &&
                    (((Vector3) this.fPoint).y >= y && y >= ((Vector3) this.sPoint).y) &&
                    (((Vector3) this.fPoint).z >= z && z >= ((Vector3) this.sPoint).z);
        }
    }


    public @NotNull Vector randomPoint () {

        if (fPoint instanceof Vector2) {

            return new Vector2(

                    ((((Vector2)this.sPoint)).x + (java.lang.Math.random() * ((((Vector2)this.fPoint)).x - (((Vector2)this.sPoint)).x))),

                    ((((Vector2)this.sPoint)).z + (java.lang.Math.random() * ((((Vector2)this.fPoint)).z - (((Vector2)this.sPoint)).z)))

            );

        }

        return new Vector3(

                ((((Vector3)this.sPoint)).x + (java.lang.Math.random() * ((((Vector3)this.fPoint)).x - (((Vector3)this.sPoint)).x))),

                ((((Vector3)this.sPoint)).y + (java.lang.Math.random() * ((((Vector3)this.fPoint)).y - (((Vector3)this.sPoint)).y))),

                ((((Vector3)this.sPoint)).z + (java.lang.Math.random() * ((((Vector3)this.fPoint)).z - (((Vector3)this.sPoint)).z)))

        );

    }

    public @NotNull Object type () { return fPoint.getClass(); }

}
