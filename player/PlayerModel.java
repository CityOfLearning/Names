package com.dyn.render.player;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.google.common.collect.Maps;
import com.rabbit.gui.utils.SkinManager;

import api.player.model.ModelPlayerAPI;
import api.player.model.ModelPlayerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PlayerModel extends ModelPlayerBase {

	private ModelBiped bipedModel;
	private Map<EntityPlayer, Integer> textureSize;
	private Map<EntityPlayer, String> textureName;

	public PlayerModel(ModelPlayerAPI modelplayerapi) {
		super(modelplayerapi);
		textureSize = Maps.newHashMap();
		textureName = Maps.newHashMap();
	}

	@Override
	public void afterLocalConstructing(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2,
			boolean paramBoolean) {
		if ((modelBiped == null) || (modelBiped instanceof ModelPlayer)) {
			bipedModel = new ModelBiped();
			bipedModel.isChild = false;
		}
	}

	@Override
	public void render(Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4,
			float paramFloat5, float paramFloat6) {
		if (paramEntity instanceof EntityPlayer) {
			if (SkinManager.hasSkinTexture((EntityPlayer) paramEntity)) {
				if (SkinManager.getSkinTexture((EntityPlayer) paramEntity) == textureName.get(paramEntity)) {
					if (textureSize.get(paramEntity) == 32) {
						bipedModel.render(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5,
								paramFloat6);
					} else {
						super.render(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5,
								paramFloat6);
					}
				} else {
					InputStream inputstream = null;
					try {
						if (textureName.containsKey(paramEntity)) {
							textureName.replace((EntityPlayer) paramEntity,
									SkinManager.getSkinTexture((EntityPlayer) paramEntity));
						} else {
							textureName.put((EntityPlayer) paramEntity,
									SkinManager.getSkinTexture((EntityPlayer) paramEntity));
						}
						IResource iresource = Minecraft.getMinecraft().getResourceManager()
								.getResource(new ResourceLocation(textureName.get(paramEntity)));
						inputstream = iresource.getInputStream();
						BufferedImage bufferedimage = TextureUtil.readBufferedImage(inputstream);
						if (textureSize.containsKey(paramEntity)) {
							textureSize.replace((EntityPlayer) paramEntity, bufferedimage.getHeight());
						} else {
							textureSize.put((EntityPlayer) paramEntity, bufferedimage.getHeight());
						}
						if (textureSize.get(paramEntity) == 32) {
							bipedModel.render(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4,
									paramFloat5, paramFloat6);
						} else {
							super.render(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5,
									paramFloat6);
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						if (inputstream != null) {
							try {
								inputstream.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		} else {
			super.render(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
		}

	}

}
