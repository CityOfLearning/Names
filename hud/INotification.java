package com.dyn.render.hud;

import net.minecraft.util.ResourceLocation;

public interface INotification {
	public static final ResourceLocation guiTexture = new ResourceLocation(
			"textures/gui/achievement/achievement_background.png");

	public void drawNotification(int pos);

	public void updateWindowScale();
}
