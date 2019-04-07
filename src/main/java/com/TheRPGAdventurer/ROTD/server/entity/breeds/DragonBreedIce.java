/*
 ** 2013 October 24
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package com.TheRPGAdventurer.ROTD.server.entity.breeds;

import com.TheRPGAdventurer.ROTD.DragonMountsLootTables;
import com.TheRPGAdventurer.ROTD.server.initialization.ModItems;
import com.TheRPGAdventurer.ROTD.client.sound.SoundEffectNames;
import com.TheRPGAdventurer.ROTD.server.entity.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.server.entity.helper.EnumDragonLifeStage;
import com.TheRPGAdventurer.ROTD.server.entity.helper.breath.BreathNode;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class DragonBreedIce extends DragonBreed {

    private static final Block FOOTPRINT = Blocks.SNOW_LAYER;
    private static final float FOOTPRINT_CHANCE = 0.2f;

    public DragonBreedIce() {
        super("ice", 0Xffffff);

        setImmunity(DamageSource.MAGIC);
        setImmunity(DamageSource.HOT_FLOOR);
        setImmunity(DamageSource.LIGHTNING_BOLT);
        setImmunity(DamageSource.WITHER);

        setHabitatBlock(Blocks.SNOW);
        setHabitatBlock(Blocks.SNOW_LAYER);
        setHabitatBlock(Blocks.ICE);
        setHabitatBlock(Blocks.PACKED_ICE);
        setHabitatBlock(Blocks.FROSTED_ICE);

        setHabitatBiome(Biomes.FROZEN_OCEAN);
        setHabitatBiome(Biomes.FROZEN_RIVER);
        setHabitatBiome(Biomes.ICE_MOUNTAINS);
        setHabitatBiome(Biomes.ICE_PLAINS);
    }

    @Override
    public void onUpdate(EntityTameableDragon dragon) {
        // place some snow footprints where the dragon walks
        if (dragon.isEgg() && !dragon.isFlying()) {
            World world = dragon.world;
            for (int i = 0; i < 4; i++) {
                if (world.rand.nextFloat() < FOOTPRINT_CHANCE) {
                    continue;
                }

                double bx = dragon.posX + (i % 2 * 2 - 1) * 0.25;
                double by = dragon.posY + 0.5;
                double bz = dragon.posZ + (i / 2 % 2 * 2 - 1) * 0.25;
                BlockPos blockPos = new BlockPos(bx, by, bz);
                IBlockState state = world.getBlockState(blockPos);
                // from EntitySnowman.onLivingUpdate, with slight tweaks
                if (world.getBlockState(blockPos).getMaterial() == Material.AIR
                        && world.getBiomeForCoordsBody(blockPos).getFloatTemperature(blockPos) <= 0.8F
                        && FOOTPRINT.canPlaceBlockAt(world, blockPos)) {
                    world.setBlockState(blockPos, FOOTPRINT.getDefaultState());
                }
            }
        }
    }

	@Override
	public void onEnable(com.TheRPGAdventurer.ROTD.server.entity.EntityTameableDragon dragon) {
		
	}

	@Override
	public void onDisable(com.TheRPGAdventurer.ROTD.server.entity.EntityTameableDragon dragon) {
		
	}

	@Override
	public void onDeath(com.TheRPGAdventurer.ROTD.server.entity.EntityTameableDragon dragon) {
		
	}
	
	@Override
    public boolean useColdSound() {
    	return true;
    }
	
	@Override
    public void continueAndUpdateBreathing(World world, Vec3d origin, Vec3d endOfLook, BreathNode.Power power, EntityTameableDragon dragon) {
		dragon.getBreathHelper().getBreathAffectedAreaIce().continueBreathing(world, origin, endOfLook, power);
		dragon.getBreathHelper().getBreathAffectedAreaIce().updateTick(world);
    }
    
	@Override
    public void spawnBreathParticles(World world, BreathNode.Power power, int tickCounter, Vec3d origin, Vec3d endOfLook, EntityTameableDragon dragon) {
		dragon.getBreathHelper().getEmitter().setBeamEndpoints(origin, endOfLook);
		dragon.getBreathHelper().getEmitter().spawnBreathParticlesforIceDragon(world, power, tickCounter);
    }
	
	@Override
	public SoundEffectNames[] getBreathWeaponSoundEffects(EnumDragonLifeStage stage) {
    	final SoundEffectNames hatchling[] = {SoundEffectNames.ADULT_BREATHE_ICE_START,
                SoundEffectNames.ADULT_BREATHE_ICE_LOOP,
                SoundEffectNames.ADULT_BREATHE_ICE_STOP};

        final SoundEffectNames juvenile[] = {SoundEffectNames.ADULT_BREATHE_ICE_START,
                SoundEffectNames.ADULT_BREATHE_ICE_LOOP,
                SoundEffectNames.ADULT_BREATHE_ICE_STOP};

        final SoundEffectNames adult[] = {SoundEffectNames.ADULT_BREATHE_ICE_START,
            SoundEffectNames.ADULT_BREATHE_ICE_LOOP,
            SoundEffectNames.ADULT_BREATHE_ICE_STOP};
    	
    	switch(stage) {
		case ADULT:
			soundEffectNames = adult;
			break;
		case EGG:
			break;
		case HATCHLING:
			soundEffectNames = hatchling;
			break;
		case JUVENILE:
			soundEffectNames = juvenile;       
			break;
		default:
			break;    	
    	}
    	
		return soundEffectNames;
    }   
	
	@Override
	public ResourceLocation getLootTable(EntityTameableDragon dragon) {
		return DragonMountsLootTables.ENTITIES_DRAGON_ICE;
	}
	
	@Override
	public void onLivingUpdate(EntityTameableDragon dragon) {
		if(dragon.isOverWater()) { 
			freezeNearby(dragon, dragon.world, new BlockPos(dragon),  1);
		}
	}
	
	public static void freezeNearby(EntityLivingBase living, World worldIn, BlockPos pos, int level) {
        if (living.onGround) {
            float f = (float)Math.min(16, 2 + level);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(0, 0, 0);

            for (BlockPos.MutableBlockPos blockpos$mutableblockpos1 : BlockPos.getAllInBoxMutable(pos.add((double)(-f), -1.0D, (double)(-f)), pos.add((double)f, -1.0D, (double)f))) {
                if (blockpos$mutableblockpos1.distanceSqToCenter(living.posX, living.posY, living.posZ) <= (double)(f * f)) {
                    blockpos$mutableblockpos.setPos(blockpos$mutableblockpos1.getX(), blockpos$mutableblockpos1.getY() + 1, blockpos$mutableblockpos1.getZ());
                    IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos);

                    if (iblockstate.getMaterial() == Material.AIR) {
                        IBlockState iblockstate1 = worldIn.getBlockState(blockpos$mutableblockpos1);

                        if (iblockstate1.getMaterial() == Material.WATER && (iblockstate1.getBlock() == net.minecraft.init.Blocks.WATER || iblockstate1.getBlock() == net.minecraft.init.Blocks.FLOWING_WATER) && iblockstate1.getValue(BlockLiquid.LEVEL).intValue() == 0 && worldIn.mayPlace(Blocks.FROSTED_ICE, blockpos$mutableblockpos1, false, EnumFacing.DOWN, null)) {
                            worldIn.setBlockState(blockpos$mutableblockpos1, Blocks.FROSTED_ICE.getDefaultState());
                            worldIn.scheduleUpdate(blockpos$mutableblockpos1.toImmutable(), Blocks.FROSTED_ICE, MathHelper.getInt(living.getRNG(), 60, 120));
                        }
                    }
                }
            }
        }
    }
	
	@Override
	public Item getShearDropitem(EntityTameableDragon dragon) {
		return ModItems.IceDragonScales;
	}
	
	@Override
	public EnumParticleTypes getSneezeParticle() {
		return null;
	}
	
}
