package com.TheRPGAdventurer.ROTD.server.blocks.tileentities;

import com.TheRPGAdventurer.ROTD.DragonMounts;
import com.TheRPGAdventurer.ROTD.client.inventory.ContainerDragonShulker;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Dragon Core TileEntity
 * 
 * @author WolfShotz
 */

public class TileEntityDragonShulker extends TileEntityLockableLoot implements ITickable
{
	private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(72, ItemStack.EMPTY);
	public int numPlayersUsing, ticksSinceSync;
	public float progress;
	public float progressOld;
	public float lidAngle, prevLidAngle;
	private TileEntityDragonShulker.AnimationStatus animationStatus;
	private int openCount;
	
	@Override
	public int getSizeInventory()
	{
		return 1;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public boolean isEmpty()
	{
		for (ItemStack stack : this.chestContents)
		{
			if (stack.isEmpty()) return false;
		}
		
		return true;
	}
	
	@Override
	public String getName()
	{
		return this.hasCustomName() ? this.customName : "container.dragon_shulker";
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.chestContents = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		
		if (!this.checkLootAndRead(compound)) ItemStackHelper.loadAllItems(compound, chestContents);
		if (compound.hasKey("CustomName", 8)) this.customName = compound.getString("CustomName");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		if(!this.checkLootAndWrite(compound)) ItemStackHelper.saveAllItems(compound, chestContents);
		if(compound.hasKey("CustomName", 8)) compound.setString("CustomName", this.customName);
		
		return compound;
	}
	
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerDragonShulker(playerInventory, this, playerIn);
	}
	
	@Override
	public String getGuiID()
	{
		return DragonMounts.MODID + ":dragon_shulker";
	}
	
	@Override
	protected NonNullList<ItemStack> getItems()
	{
		return this.chestContents;
	}
	
/*	@Override
	public void tick()
	{
		if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + pos.getX() + pos.getY() + pos.getZ()) % 200 == 0)
        {
            this.numPlayersUsing = 0;
            float f = 5.0F;

            for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double)((float)pos.getX() - 5.0F), (double)((float)pos.getY() - 5.0F), (double)((float)pos.getZ() - 5.0F), (double)((float)(pos.getX() + 1) + 5.0F), (double)((float)(pos.getY() + 1) + 5.0F), (double)((float)(pos.getZ() + 1) + 5.0F))))
            {
                if (entityplayer.openContainer instanceof ContainerDragonShulker)
                {
                    if (((ContainerDragonShulker)entityplayer.openContainer).getShulkerInventory() == this)
                    {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }
		
        this.prevLidAngle = this.lidAngle;
        float f1 = 0.1F;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
        {
            double d1 = (double)pos.getX() + 0.5D;
            double d2 = (double)pos.getZ() + 0.5D;
            this.world.playSound((EntityPlayer)null, d1, (double)pos.getY() + 0.5D, d2, SoundEvents.BLOCK_ENDERCHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            this.world.playSound((EntityPlayer)null, d1, (double)pos.getY() + 0.5D, d2, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.HOSTILE, 0.5F, this.world.rand.nextFloat() * 0.04F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float f2 = this.lidAngle;

            if (this.numPlayersUsing > 0)
            {
                this.lidAngle += 0.1F;
            }
            else
            {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float f3 = 0.5F;

            if (this.lidAngle < 0.5F && f2 >= 0.5F)
            {
                double d3 = (double)pos.getX() + 0.5D;
                double d0 = (double)pos.getZ() + 0.5D;
                this.world.playSound((EntityPlayer)null, d3, (double)pos.getY() + 0.5D, d0, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
                this.world.playSound((EntityPlayer)null, d3, (double)pos.getY() + 0.5D, d0, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.09F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }		
	} */
	
	@Override
	public void tick()
	{
        this.updateAnimation();
    }

    protected void updateAnimation()
    {
        this.progressOld = this.progress;

        switch (this.animationStatus)
        {
            case CLOSED:
                this.progress = 0.0F;
                break;
            case OPENING:
                this.progress += 0.1F;
                if (this.progress >= 1.0F)
                {
                    this.animationStatus = TileEntityDragonShulker.AnimationStatus.OPENED;
                    this.progress = 1.0F;
                }

                break;
            case CLOSING:
                this.progress -= 0.1F;

                if (this.progress <= 0.0F)
                {
                    this.animationStatus = TileEntityDragonShulker.AnimationStatus.CLOSED;
                    this.progress = 0.0F;
                }

                break;
            case OPENED:
                this.progress = 1.0F;
        }
    }

    public TileEntityDragonShulker.AnimationStatus getAnimationStatus()
    {
        return this.animationStatus;
    }
    
	@Override
	public void openInventory(EntityPlayer player)
	{
		++this.numPlayersUsing;
		this.world.addBlockEvent(pos, this.getBlockType(), 1, this.numPlayersUsing);
		this.world.notifyNeighborsOfStateChange(pos, this.getBlockType(), false);
		if (numPlayersUsing == 1)
		{
			double d1 = (double)pos.getX() + 0.5D;
			double d2 = (double)pos.getZ() + 0.5D;
			this.world.playSound((EntityPlayer)null, d1, (double)pos.getY() + 0.5D, d2, SoundEvents.BLOCK_ENDERCHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
			this.world.playSound((EntityPlayer)null, d1, (double)pos.getY() + 0.5D, d2, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.HOSTILE, 0.04F, this.world.rand.nextFloat() * 0.1F + 0.8F);
		}
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
		--this.numPlayersUsing;
		this.world.addBlockEvent(pos, this.getBlockType(), 1, this.numPlayersUsing);
		this.world.notifyNeighborsOfStateChange(pos, this.getBlockType(), false);
		if (numPlayersUsing <= 0)
		{
			double d3 = (double)pos.getX() + 0.5D;
			double d0 = (double)pos.getZ() + 0.5D;
			this.world.playSound((EntityPlayer)null, d3, (double)pos.getY() + 0.5D, d0, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
			this.world.playSound((EntityPlayer)null, d3, (double)pos.getY() + 0.5D, d0, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 0.3F, this.world.rand.nextFloat() * 0.1F + 0.9F);
		}
	}
	
	
	//ANYTHING BELOW THIS LINE COULD BE TEMPORARY! Anything associated with it is unoptimized and will be re-written later
	public static enum AnimationStatus
	{
		CLOSED,
		CLOSING,
		OPENED,
		OPENING,
	}
	
    public float getProgress(float p_190585_1_)
    {
        return this.progressOld + (this.progress - this.progressOld) * p_190585_1_;
    }
}
