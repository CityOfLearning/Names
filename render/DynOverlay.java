package com.dyn.render.render;

import org.lwjgl.opengl.GL11;

import com.dyn.DYNServerMod;
import com.dyn.utils.PlayerLevel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class DynOverlay {

	private static final ResourceLocation TABS = new ResourceLocation("dyn", "textures/gui/sm_notification2.png");
	private static final ResourceLocation FREEZE = new ResourceLocation("dyn", "textures/gui/frozenBorder.png");

	private final Minecraft mc;
	private int windowWidth;
	private int windowHeight;

	public DynOverlay() {
		mc = Minecraft.getMinecraft();
	}

	public void drawFrozenOverlay() {

		updateWindowScale();
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.disableLighting();

		mc.getTextureManager().bindTexture(FREEZE);
		drawScaledTexturedRect(0, 0, -1, windowWidth, windowHeight);

		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableColorMaterial();
		GlStateManager.enableLighting();
		GlStateManager.disableLighting();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
	}

	private int drawHiddenTab(int xPos, int yPos, String... lines) {
		int tabwidth = Math.max(mc.fontRendererObj.getStringWidth(lines[0]) + 8, 35);
		tabwidth = Math.max(mc.fontRendererObj.getStringWidth(lines[1]) + 8, tabwidth);
		tabwidth = Math.max(mc.fontRendererObj.getStringWidth(lines[2]) + 8, tabwidth);
		mc.getTextureManager().bindTexture(TABS);
		drawScaledTexturedRect(xPos, yPos <= 0 ? yPos - 28 : yPos + 28, 1, tabwidth, 32);
		return tabwidth;
	}

	public void drawOverlay(boolean hidden) {

		updateWindowScale();
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		// int xPos = windowWidth - 120;
		// int yPos = windowHeight - 32;
		int xPos = 0;
		int yPos = 0;
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.disableLighting();

		if (!hidden) {
			xPos += drawTab(xPos, yPos, "Achiev", "ements", "(N)");
			if (DYNServerMod.status == PlayerLevel.ADMIN) {
				xPos += drawTab(xPos, yPos, "Admin", "GUI", "(M)");
			} else if (DYNServerMod.status == PlayerLevel.MENTOR) {
				xPos += drawTab(xPos, yPos, "Mentor", "GUI", "(M)");
			} else if (DYNServerMod.status == PlayerLevel.STUDENT) {
				xPos += drawTab(xPos, yPos, "Student", "GUI", "(M)");
			}
			xPos += drawTab(xPos, yPos, "Select", "Skin", "(B)");
			drawTab(xPos, yPos, "Hide", "GUI", "(H)");
		} else {
			xPos += drawHiddenTab(xPos, yPos, "Achiev", "ements", "(N)");
			if (DYNServerMod.status == PlayerLevel.ADMIN) {
				xPos += drawHiddenTab(xPos, yPos, "Admin", "GUI", "(M)");
			} else if (DYNServerMod.status == PlayerLevel.MENTOR) {
				xPos += drawHiddenTab(xPos, yPos, "Mentor", "GUI", "(M)");
			} else if (DYNServerMod.status == PlayerLevel.STUDENT) {
				xPos += drawHiddenTab(xPos, yPos, "Student", "GUI", "(M)");
			}
			xPos += drawHiddenTab(xPos, yPos, "Select", "Skin", "(B)");
			drawHiddenTab(xPos, yPos, "Hide", "GUI", "(H)");
		}

		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableColorMaterial();
		GlStateManager.enableLighting();
		GlStateManager.disableLighting();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
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

	private int drawTab(int xPos, int yPos, String... lines) {
		int tabwidth = Math.max(mc.fontRendererObj.getStringWidth(lines[0]) + 8, 35);
		tabwidth = Math.max(mc.fontRendererObj.getStringWidth(lines[1]) + 8, tabwidth);
		tabwidth = Math.max(mc.fontRendererObj.getStringWidth(lines[2]) + 8, tabwidth);
		mc.getTextureManager().bindTexture(TABS);
		drawScaledTexturedRect(xPos, yPos, 1, tabwidth, 32);
		mc.fontRendererObj.drawString(lines[0],
				(int) (xPos + ((tabwidth / 2.0) - (mc.fontRendererObj.getStringWidth(lines[0]) / 2.0))), yPos + 3,
				-256);
		mc.fontRendererObj.drawString(lines[1],
				(int) (xPos + ((tabwidth / 2.0) - (mc.fontRendererObj.getStringWidth(lines[1]) / 2.0))), yPos + 11,
				-256);
		mc.fontRendererObj.drawString(lines[2],
				(int) (xPos + ((tabwidth / 2.0) - (mc.fontRendererObj.getStringWidth(lines[2]) / 2.0))), yPos + 19, -1);
		return tabwidth;
	}

	private void updateWindowScale() {
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
