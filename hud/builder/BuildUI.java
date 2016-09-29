package com.dyn.render.hud.builder;

import java.awt.Color;

import com.dyn.render.hud.Hud;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class BuildUI extends Hud {

	// texture
	private static final ResourceLocation KEY_UP = new ResourceLocation("dyn", "textures/gui/key/up.png");
	private static final ResourceLocation KEY_DN = new ResourceLocation("dyn", "textures/gui/key/down.png");
	private static final ResourceLocation KEY_LT = new ResourceLocation("dyn", "textures/gui/key/left.png");
	private static final ResourceLocation KEY_RG = new ResourceLocation("dyn", "textures/gui/key/right.png");
	private static final ResourceLocation KEY_PU = new ResourceLocation("dyn", "textures/gui/key/page up.png");
	private static final ResourceLocation KEY_PD = new ResourceLocation("dyn", "textures/gui/key/page down.png");

	public static boolean isOpen = false;
	public static boolean expandArea = true;
	public static int expandSize = 1;

	public static void draw() {
		if (isOpen) {
			updateWindowScale();
			GlStateManager.disableDepth();
			GlStateManager.depthMask(false);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableTexture2D();
			GlStateManager.disableLighting();

			mc.fontRendererObj.drawString((expandArea ? "Expanding " : "Contracting ") + expandSize + " blocks",
					(int) (windowWidth * .65), (int) (windowHeight * .05), Color.white.getRGB());

			mc.getTextureManager().bindTexture(KEY_UP);
			drawScaledTexturedRect((int) (windowWidth * .9), (int) (windowHeight * .125), -1, 25, 25);
			mc.fontRendererObj.drawString("Forward", (int) (windowWidth * .75), (int) (windowHeight * .15),
					Color.white.getRGB());
			mc.getTextureManager().bindTexture(KEY_DN);
			drawScaledTexturedRect((int) (windowWidth * .9), (int) (windowHeight * .225), -1, 25, 25);
			mc.fontRendererObj.drawString("Backwards", (int) (windowWidth * .75), (int) (windowHeight * .25),
					Color.white.getRGB());
			mc.getTextureManager().bindTexture(KEY_LT);
			drawScaledTexturedRect((int) (windowWidth * .9), (int) (windowHeight * .325), -1, 25, 25);
			mc.fontRendererObj.drawString("Left", (int) (windowWidth * .75), (int) (windowHeight * .35),
					Color.white.getRGB());
			mc.getTextureManager().bindTexture(KEY_RG);
			drawScaledTexturedRect((int) (windowWidth * .9), (int) (windowHeight * .425), -1, 25, 25);
			mc.fontRendererObj.drawString("Right", (int) (windowWidth * .75), (int) (windowHeight * .45),
					Color.white.getRGB());
			mc.getTextureManager().bindTexture(KEY_PU);
			drawScaledTexturedRect((int) (windowWidth * .9), (int) (windowHeight * .525), -1, 25, 25);
			mc.fontRendererObj.drawString("Up", (int) (windowWidth * .75), (int) (windowHeight * .55),
					Color.white.getRGB());
			mc.getTextureManager().bindTexture(KEY_PD);
			drawScaledTexturedRect((int) (windowWidth * .9), (int) (windowHeight * .625), -1, 25, 25);
			mc.fontRendererObj.drawString("Down", (int) (windowWidth * .75), (int) (windowHeight * .65),
					Color.white.getRGB());

			GlStateManager.disableLighting();
			GlStateManager.depthMask(true);
			GlStateManager.enableDepth();
		}
	}
}
