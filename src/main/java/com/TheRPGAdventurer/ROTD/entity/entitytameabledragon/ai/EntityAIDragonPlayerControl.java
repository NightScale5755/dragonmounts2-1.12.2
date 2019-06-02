/*
 ** 2012 March 18
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package com.TheRPGAdventurer.ROTD.entity.entitytameabledragon.ai;

import com.TheRPGAdventurer.ROTD.entity.entitytameabledragon.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.entity.entitytameabledragon.breeds.EnumDragonBreed;
import com.TheRPGAdventurer.ROTD.util.math.MathX;
import com.TheRPGAdventurer.ROTD.util.reflection.PrivateAccessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;

/**
 * Abstract "AI" for player-controlled movements.
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class EntityAIDragonPlayerControl extends EntityAIDragonBase implements PrivateAccessor {

    private Vec3d inates;

    public EntityAIDragonPlayerControl(EntityTameableDragon dragon) {
        super(dragon);
        setMutexBits(0xffffffff);
    }

    @Override
    public boolean shouldExecute() {
        rider = dragon.getControllingPlayer();
        return rider != null;
    }

    @Override
    public void startExecuting() {
        dragon.getNavigator().clearPathEntity();
    }

    private void updateIntendedRideRotation(EntityPlayer rider) {
        boolean hasRider = dragon.hasControllingPlayer(rider);
        if (hasRider && rider.moveStrafing == 0) {
            dragon.rotationYaw = rider.rotationYaw;
            dragon.rotationPitch = rider.rotationPitch;
        }
    }

    @Override
    public void updateTask() {
        Vec3d wp = rider.getLook(1.0F);

        double x = dragon.posX;
        double y = dragon.posY;
        double z = dragon.posZ;

        if (dragon.getBreedType() == EnumDragonBreed.SYLPHID) {
            PotionEffect watereffect = new PotionEffect(MobEffects.WATER_BREATHING, 200);
            if (!rider.isPotionActive(watereffect.getPotion()) && rider.isInWater()) { // If the Potion isn't currently active,
                rider.addPotionEffect(watereffect); // Apply a copy of the PotionEffect to the player
            }
        }

        // control direction with movement keys
        if (rider.moveStrafing != 0 || rider.moveForward != 0) {
            if (rider.moveForward < 0) {
                wp = wp.rotateYaw(MathX.PI_F);
            } else if (rider.moveStrafing > 0) {
                wp = wp.rotateYaw(MathX.PI_F * 0.5f);
            } else if (rider.moveStrafing < 0) {
                wp = wp.rotateYaw(MathX.PI_F * -0.5f);
            }

            x += wp.x * 10;
            if(!dragon.isYLocked()) y += wp.y * 10;
            z += wp.z * 10;
        }

        // lift off from a jump
        if (!dragon.isFlying()) {
            if (entityIsJumping(rider)) {
                dragon.liftOff();
            }
        }

        if ((!dragon.isUsingBreathWeapon() && rider.moveStrafing == 0) || dragon.followYaw() && dragon.isFlying()) {
            dragon.rotationYaw = rider.rotationYaw;
//            dragon.rotationPitch = rider.rotationPitch;
        }
        dragon.getMoveHelper().setMoveTo(x, y, z, 1.2);
    }
}