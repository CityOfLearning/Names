package com.dyn.render.render;

import org.lwjgl.opengl.GL11;

import com.dyn.achievements.AchievementsMod;
import com.dyn.achievements.handlers.AchievementManager;
import com.rabbit.gui.utils.TextureHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class AchievementPlusNotification extends Gui implements INotification {
	private final Minecraft mc;
	private final String notificationTitle;
	private final String notificationText;
	private int windowWidth;
	private int windowHeight;
	private long notificationTime;

	public AchievementPlusNotification(Minecraft mc, String notificationTitle, String notificationText) {
		this.mc = mc;
		this.notificationTitle = notificationTitle;
		this.notificationText = notificationText;
		notificationTime = Minecraft.getSystemTime();
	}

	@Override
	public void drawNotification(int pos) {
		if (notificationTime != 0L) {
			double d0 = (Minecraft.getSystemTime() - notificationTime) / 3000.0D;

			if ((d0 < 0.0D) || (d0 > 1.0D)) {
				notificationTime = 0L;
			} else {
				updateWindowScale();
				GlStateManager.disableDepth();
				GlStateManager.depthMask(false);
				double d1 = d0 * 2.0D;

				if (d1 > 1.0D) {
					d1 = 2.0D - d1;
				}

				d1 *= 4.0D;
				d1 = 1.0D - d1;

				if (d1 < 0.0D) {
					d1 = 0.0D;
				}

				d1 *= d1;
				d1 *= d1;
				int i = windowWidth - 160;
				int j = 0 - (int) (d1 * 36.0D);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableTexture2D();
				mc.getTextureManager().bindTexture(guiTexture);
				GlStateManager.disableLighting();
				this.drawTexturedModalRect(i, j, 96, 202, 160, 32);

				mc.fontRendererObj.drawString(notificationTitle, i + 6, j + 7, -256);
				mc.fontRendererObj.drawString(notificationText, i + 6, j + 18, -1);

				RenderHelper.enableGUIStandardItemLighting();
				GlStateManager.disableLighting();
				GlStateManager.enableRescaleNormal();
				GlStateManager.enableColorMaterial();
				GlStateManager.enableLighting();
				TextureHelper.bindTexture(AchievementManager.findAchievementByName(notificationTitle).getTextureId());
				drawScaledTexturedRect(i + 2, j + 7, 100, 16, 16);
				GlStateManager.disableLighting();
				GlStateManager.depthMask(true);
				GlStateManager.enableDepth();

			}
		} else {
			AchievementsMod.proxy.getNotificationsManager().removeNotification(this);
		}

	}

	private void drawScaledTexturedRect(int x, int y, int z, int width, int height) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderer.pos(x + width, y + height, z).tex(1, 1).endVertex();
		renderer.pos(x + width, y, z).tex(1, 0).endVertex();
		renderer.pos(x, y, z).tex(0, 0).endVertex();
		renderer.pos(x, y + height, z).tex(0, 1).endVertex();
		tessellator.draw();
	}

	@Override
	public void updateWindowScale() {
		GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		windowWidth = mc.displayWidth;
		windowHeight = mc.displayHeight;
		ScaledResolution scaledresolution = new ScaledResolution(mc);
		windowWidth = scaledresolution.getScaledWidth();
		windowHeight = scaledresolution.getScaledHeight();
		GlStateManager.clear(256);
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, windowWidth, windowHeight, 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.0F, 0.0F, -2000.0F);
	}
}
