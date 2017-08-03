package com.dyn.render.player;

import com.rabbit.gui.utils.SkinManager;

import api.player.model.ModelPlayerAPI;
import api.player.model.ModelPlayerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerModel extends ModelPlayerBase {

	private ModelBiped bipedModel;

	public PlayerModel(ModelPlayerAPI modelplayerapi) {
		super(modelplayerapi);
	}

	@Override
	public void afterLocalConstructing(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2,
			boolean paramBoolean) {
		if ((modelBiped == null) || (modelBiped instanceof ModelPlayer)) {
			bipedModel = new ModelBiped();
			bipedModel.isChild = false;
		} else {
			bipedModel = modelBiped;
		}
	}

	@Override
	public void render(Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4,
			float paramFloat5, float paramFloat6) {
		if (paramEntity instanceof EntityPlayer) {
			if (SkinManager.hasSkinTexture((EntityPlayer) paramEntity)) {
				if ((SkinManager.getSkinTextureHeight((EntityPlayer) paramEntity) == 32)
						&& (modelPlayerArmor == null)) {
					bipedModel.render(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5,
							paramFloat6);
				} else {
					modelPlayerAPI.localRender(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4,
							paramFloat5, paramFloat6);
				}
			} else {
				modelPlayerAPI.localRender(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5,
						paramFloat6);
			}
		} else {
			modelPlayerAPI.localRender(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5,
					paramFloat6);
		}
	}

	@Override
	public void renderRightArm() {
		if (SkinManager.getSkinTextureHeight(Minecraft.getMinecraft().thePlayer) == 32) {
			bipedModel.bipedRightArm.render(0.0625F);
		} else {
			super.renderRightArm();
		}

	}
}
