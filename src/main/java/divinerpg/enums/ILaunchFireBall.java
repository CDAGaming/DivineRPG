package divinerpg.enums;

import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;

import javax.annotation.*;

@FunctionalInterface
public interface ILaunchFireBall {

    /**
     * Creates fireball
     *
     * @param world            - current world
     * @param parent           - parent entity
     * @param x                - x velocity
     * @param y                - y velocity
     * @param z                - z velocity
     * @param fireballStrength - strength of fireball (random value 0..5)
     * @return
     */
    @Nonnull
    Entity createFireball(Level world, LivingEntity parent, double x, double y, double z, int fireballStrength);


    /**
     * Is taken from ghast shoot task
     */
    default Entity createFireball(LivingEntity parent, Entity target) {
        Vec3 vec3d = parent.getEyePosition(1.0F);

        double d2 = target.xo - (parent.xo + vec3d.x * 4.0D);
        double d3 = target.getBoundingBox().minY + (target.getEyeY() / 2.0F) - (0.5D + parent.yo + (parent.getEyeY() / 2.0F));
        double d4 = target.zo - (parent.zo + vec3d.z * 4.0D);

        Entity fireball = createFireball(parent.level(), parent, d2, d3, d4, parent.getRandom().nextInt(6));

        fireball.xo = parent.xo + vec3d.x * 4.0D;
        fireball.yo = parent.yo + (parent.getEyeY() / 2.0F) + 0.5D;
        fireball.zo = parent.zo + vec3d.z * 4.0D;

        return fireball;
    }
}