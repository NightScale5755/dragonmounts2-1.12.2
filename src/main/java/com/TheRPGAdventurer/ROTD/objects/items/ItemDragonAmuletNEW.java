package com.TheRPGAdventurer.ROTD.objects.items;

import com.TheRPGAdventurer.ROTD.DragonMounts;
import com.TheRPGAdventurer.ROTD.inits.ModItems;
import com.TheRPGAdventurer.ROTD.objects.entity.entitytameabledragon.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.objects.items.entity.EntityDragonAmulet;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Dragon Amulet Item for the use of carrying dragons in an item
 *
 * @author WolfShotz
 */
public class ItemDragonAmuletNEW extends Item {

    private EnumItemBreedTypes type;

    public ItemDragonAmuletNEW() {
        String name = "dragon_amulet";
        this.setRegistryName(DragonMounts.MODID, name);
        this.setUnlocalizedName(name);
        this.setMaxStackSize(1);
        this.setCreativeTab(DragonMounts.mainTab);

        ModItems.ITEMS.add(this);
    }

    private boolean containsDragonEntity(ItemStack stack) {
        return !stack.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().hasKey("breed");
    }

    /**
     * Called when the player has right clicked an entity with the itemstack
     * <p> Writes the entity NBT data to the item stack, and then sets dead
     */
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if (containsDragonEntity(stack) || !target.isEntityAlive() || !target.isServerWorld()) return false;
        if (target instanceof EntityTameableDragon) {
            EntityTameableDragon dragon = (EntityTameableDragon) target;
            if (dragon.isTamedFor(player)) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("breed", dragon.getBreedType().toString().toLowerCase());
                this.type = EnumItemBreedTypes.valueOf(dragon.getBreedType().toString());
                tag.setString("Name", type.color + dragon.getDisplayName().getFormattedText());
                tag.setString("OwnerName", dragon.getOwner().getName());
                target.writeToNBT(tag);
                stack.setTagCompound(tag);
                player.setHeldItem(hand, stack);
                if (dragon.getLeashed()) dragon.clearLeashed(true, true); // Fix Lead Dupe exploit
                stack.setStackDisplayName(type.color + stack.getDisplayName());
                player.world.playSound((EntityPlayer) null, player.getPosition(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.NEUTRAL, 1, 1);

                target.setDead();
                return true;
            }
            return true;
        } else player.sendStatusMessage(new TextComponentTranslation("dragon.notOwned"), true);
        return false;
    }

    /**
     * Called when the player has right clicked the ItemStack on a block
     * <p> Spawns an entity in the world with the given NBT data the ItemStack was storing
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote || !player.isServerWorld()) return EnumActionResult.FAIL;
        ItemStack stack = player.getHeldItem(hand);
        if (!containsDragonEntity(stack) || !stack.hasTagCompound()) return EnumActionResult.FAIL;

        EntityTameableDragon dragon = new EntityTameableDragon(world);
        dragon.readFromNBT(stack.getTagCompound());

        if (dragon.isTamedFor(player)) {
            BlockPos blockPos = pos.offset(facing);
            dragon.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            stack.setTagCompound(null);
            player.setHeldItem(hand, stack);
            world.spawnEntity(dragon);
            player.world.playSound((EntityPlayer) null, player.getPosition(), SoundEvents.ENTITY_ILLAGER_MIRROR_MOVE, SoundCategory.NEUTRAL, 1, 1);
            stack.clearCustomName();
            return EnumActionResult.SUCCESS;
        } else player.sendStatusMessage(new TextComponentTranslation("dragon.notOwned"), true);
        return EnumActionResult.FAIL;
    }

    /* Item Extras */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn) {
        TextFormatting t = null;
        if (containsDragonEntity(stack)) {
            tooltip.add("Name: " + stack.getTagCompound().getString("Name"));
            tooltip.add("Health: " + t.GREEN + Math.round(stack.getTagCompound().getDouble("Health")));
            tooltip.add("Owner: " + t.GOLD + stack.getTagCompound().getString("OwnerName"));
            String gender = stack.getTagCompound().getBoolean("IsMale") ? t.BLUE + "M" : t.RED + "FM";
            tooltip.add("Gender: " + gender);
        } else {
            tooltip.add(t.GREEN + new TextComponentTranslation("item.dragonamulet.info").getUnformattedText());
            stack.setStackDisplayName(TextFormatting.RESET + stack.getDisplayName());
        }
    }

    @Nonnull
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        EntityItem entity = new EntityDragonAmulet(world, location.posX, location.posY, location.posZ, itemstack);
        if (location instanceof EntityItem) {
            // workaround for private access on that field >_>
            NBTTagCompound tag = new NBTTagCompound();
            location.writeToNBT(tag);
            entity.setPickupDelay(tag.getShort("PickupDelay"));
        }
        entity.motionX = location.motionX;
        entity.motionY = location.motionY;
        entity.motionZ = location.motionZ;
        return entity;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }
}