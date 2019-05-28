package com.TheRPGAdventurer.ROTD.client.render.dragon.layer;

import com.TheRPGAdventurer.ROTD.client.model.dragon.DragonModel;
import com.TheRPGAdventurer.ROTD.client.render.dragon.DragonRenderer;
import com.TheRPGAdventurer.ROTD.client.render.dragon.breeds.DefaultDragonBreedRenderer;
import com.TheRPGAdventurer.ROTD.entity.entitytameabledragon.EntityTameableDragon;
import com.TheRPGAdventurer.ROTD.util.math.Interpolation;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class LayerRendererDragonBanner extends LayerRendererDragon {

    private final ModelBanner bannerModel=new ModelBanner();

    public LayerRendererDragonBanner(DragonRenderer renderer, DefaultDragonBreedRenderer breedRenderer, DragonModel model) {
        super(renderer, breedRenderer, model);

    }

    @Override
    public void doRenderLayer(EntityTameableDragon dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Minecraft mc=Minecraft.getMinecraft();
        ItemStack itemstack1=dragon.getBanner1();
        ItemStack itemstack2=dragon.getBanner2();
        ItemStack itemstack3=dragon.getBanner3();
        ItemStack itemstack4=dragon.getBanner4();

        // create it a fucking workaround this thing acts more of a getter than something else both paras are null
        ResourceLocation resourcelocation1=this.getBannerResourceLocation((TileEntityBanner) Block.getBlockFromItem(itemstack1.getItem()).createTileEntity(null, null));
        ResourceLocation resourcelocation2=this.getBannerResourceLocation((TileEntityBanner) Block.getBlockFromItem(itemstack2.getItem()).createTileEntity(null, null));
        ResourceLocation resourcelocation3=this.getBannerResourceLocation((TileEntityBanner) Block.getBlockFromItem(itemstack3.getItem()).createTileEntity(null, null));
        ResourceLocation resourcelocation4=this.getBannerResourceLocation((TileEntityBanner) Block.getBlockFromItem(itemstack4.getItem()).createTileEntity(null, null));


        GlStateManager.pushMatrix();

        if (itemstack1!=null) {
            model.body.postRender(0.0625F);
            GlStateManager.translate(1.0F, 0.4F, -0.5F);
            GlStateManager.translate(0.0F, 0.0, Interpolation.smoothStep(-2.5F, 0.0F, dragon.getAnimator().getSpeed()));
            GlStateManager.translate(0, Interpolation.smoothStep(0.3F, dragon.getAnimator().getModelOffsetY() + 1.5F, dragon.getAnimator().getSpeed()), 0);
            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-dragon.getBodyPitch(), 0.0F, 0.0F, 1.0F);
            GlStateManager.scale(0.625F, -0.625F, -0.625F);
//            mc.getItemRenderer().renderItem(dragon, itemstack1, ItemCameraTransforms.TransformType.HEAD);
            renderer.renderBanner(resourcelocation1, bannerModel);

        }

        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();

        if (itemstack2!=null) {
            model.body.postRender(0.0625F);
            GlStateManager.translate(-1.0F, 0.4, -0.5F);
            GlStateManager.translate(0.0F, 0.0, Interpolation.smoothStep(-2.5F, 0.0F, dragon.getAnimator().getSpeed()));
            GlStateManager.translate(0, Interpolation.smoothStep(0.3F, dragon.getAnimator().getModelOffsetY() + 1.5F, dragon.getAnimator().getSpeed()), 0);
            GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(dragon.getBodyPitch(), 0.0F, 0.0F, 1.0F);
            GlStateManager.scale(0.625F, -0.625F, -0.625F);
//            mc.getItemRenderer().renderItem(dragon, itemstack2, ItemCameraTransforms.TransformType.HEAD);
            renderer.renderBanner(resourcelocation2, bannerModel);
        }

        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();

        if (itemstack3!=null) {
            model.body.postRender(0.0625F);
            GlStateManager.translate(-0.4F, -1.7F, 1.7F);
            GlStateManager.translate(0.0F, 0.0, Interpolation.smoothStep(0F, 0.0F, dragon.getAnimator().getSpeed()));
            GlStateManager.translate(0, Interpolation.smoothStep(3.2F, dragon.getAnimator().getModelOffsetY() + 1.5F, dragon.getAnimator().getSpeed()), 0);
            GlStateManager.translate(0, 0, Interpolation.smoothStep(-1.9F, dragon.getAnimator().getModelOffsetZ() + 1.5F, dragon.getAnimator().getSpeed()));
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-dragon.getBodyPitch() - 5, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(0.525F, -0.625F, -0.625F);
//            mc.getItemRenderer().renderItem(dragon, itemstack3, ItemCameraTransforms.TransformType.HEAD);
            renderer.renderBanner(resourcelocation3, bannerModel);
        }

        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();

        if (itemstack4!=null) {
            model.body.postRender(0.0625F);
            GlStateManager.translate(0.4F, -1.7F, 1.7F);
            GlStateManager.translate(0, Interpolation.smoothStep(3.2F, dragon.getAnimator().getModelOffsetY() + 1.5F, dragon.getAnimator().getSpeed()), 0);
            GlStateManager.translate(0, 0, Interpolation.smoothStep(-1.9F, dragon.getAnimator().getModelOffsetZ() + 1.5F, dragon.getAnimator().getSpeed()));

            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-dragon.getBodyPitch() - 5, 1.0F, 0.0F, 0.0F);

            GlStateManager.scale(0.525F, -0.625F, -0.625F);
//            mc.getItemRenderer().renderItem(dragon, itemstack4, ItemCameraTransforms.TransformType.HEAD);
            renderer.renderBanner(resourcelocation4, bannerModel);
        }

        GlStateManager.popMatrix();
    }

    @Nullable
    private ResourceLocation getBannerResourceLocation(TileEntityBanner bannerObj) {
        return BannerTextures.BANNER_DESIGNS.getResourceLocation(bannerObj.getPatternResourceLocation(), bannerObj.getPatternList(), bannerObj.getColorList());
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}