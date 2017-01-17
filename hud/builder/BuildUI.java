package com.dyn.render.hud.builder;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.dyn.render.hud.Hud;
import com.forgeessentials.commons.selections.AreaBase;
import com.forgeessentials.commons.selections.Point;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

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
	private static final float ALPHA = .25f;

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

	private static void drawName(String name, WorldRenderer wr, FontRenderer fr) {
		float textSize = 1;

		GlStateManager.disableTexture2D();
		String symbol = name.substring(0, 1);
		int s = fr.getStringWidth(symbol) / 2;
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		GlStateManager.color(1, 1, 1, 0.47058824F);
		wr.pos(-5.0, -9.0, 0.0).endVertex();
		wr.pos(-5.0, 0.0, 0.0).endVertex();
		wr.pos(4.0, 0.0, 0.0).endVertex();
		wr.pos(4.0, -9.0, 0.0).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.enableTexture2D();
		fr.drawString(symbol, -s, -8, 553648127);
		fr.drawString(symbol, -s, -8, -1);
		if (Minecraft.getMinecraft().isUnicode()) {
			textSize *= 1.5f;
		}
		GlStateManager.translate(0.0f, 1.0f, 0.0f);
		GlStateManager.scale(textSize / 2.0f, textSize / 2.0f, 1.0f);

		int t = fr.getStringWidth(name) / 2;
		GlStateManager.disableTexture2D();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		GlStateManager.color(0, 0, 0, 0.27450982F);
		wr.pos(-t - 1.0, 0.0, 0.0).endVertex();
		wr.pos(-t - 1.0, 9.0, 0.0).endVertex();
		wr.pos(t, 9.0, 0.0).endVertex();
		wr.pos(t, 0.0, 0.0).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.enableTexture2D();

		fr.drawString(name, -t, 1, 553648127);
		fr.drawString(name, -t, 1, -1);
	}

	public static void drawZone(AreaBase area, String name, float partialTicks) {
		if (area == null) {
			return;
		}

		EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
		if (player == null) {
			return;
		}

		WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		FontRenderer fr = rm.getFontRenderer();

		double renderPosX = TileEntityRendererDispatcher.staticPlayerX;
		double renderPosY = TileEntityRendererDispatcher.staticPlayerY;
		double renderPosZ = TileEntityRendererDispatcher.staticPlayerZ;
		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(-renderPosX + 0.5, -renderPosY + 0.5, -renderPosZ + 0.5);

			GlStateManager.disableTexture2D();
			GlStateManager.enableRescaleNormal();
			GlStateManager.disableLighting();
			GL11.glLineWidth(5);

			boolean seeThrough = true;
			while (true) {
				if (seeThrough) {
					GlStateManager.disableDepth();
					GlStateManager.enableBlend();
					GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
				} else {
					GlStateManager.disableBlend();
					GlStateManager.enableDepth();
				}

				Point p1 = area.getLowPoint();
				Point p2 = area.getHighPoint();
				Point size = area.getSize();
				GlStateManager.pushMatrix();
				{
					GlStateManager.translate((float) (p1.getX() + p2.getX()) / 2, (float) (p1.getY() + p2.getY()) / 2,
							(float) (p1.getZ() + p2.getZ()) / 2);
					GlStateManager.scale(1 + size.getX(), 1 + size.getY(), 1 + size.getZ());
					if (seeThrough) {
						GlStateManager.color(1, 0, 1, ALPHA);
					} else {
						GlStateManager.color(1, 1, 1);
					}
					renderBox(wr);
				}
				GlStateManager.popMatrix();

				if (!seeThrough) {
					break;
				}
				seeThrough = false;
			}

			GlStateManager.enableTexture2D();
		}
		GlStateManager.popMatrix();

		Point center = area.getCenter();
		double d3 = player.lastTickPosX + ((player.posX - player.lastTickPosX) * partialTicks);
		double d4 = player.lastTickPosY + ((player.posY - player.lastTickPosY) * partialTicks);
		double d5 = player.lastTickPosZ + ((player.posZ - player.lastTickPosZ) * partialTicks);
		float offX = (center.getX() - (float) d3) + 0.5f;
		float offY = (center.getY() - (float) d4) + 1.0f;
		float offZ = (center.getZ() - (float) d5) + 0.5f;

		float f = 1.6f;
		float f2 = 0.016666668f * f;
		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(offX, offY, offZ);
			GL11.glNormal3f(0.0f, 1.0f, 0.0f);
			GlStateManager.rotate(-rm.playerViewY, 0.0f, 1.0f, 0.0f);
			GlStateManager.rotate(rm.playerViewX, 1.0f, 0.0f, 0.0f);
			GlStateManager.scale(-f2, -f2, f2);
			GlStateManager.disableLighting();
			GlStateManager.depthMask(false);
			GlStateManager.disableDepth();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			GlStateManager.scale(5.0, 5.0, 1.0);
			drawName(name, wr, fr);
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			GlStateManager.enableDepth();
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		}
		GlStateManager.popMatrix();

	}

	/**
	 * must be translated to proper point before calling
	 */
	private static void renderBox(WorldRenderer wr) {

		wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

		// FRONT
		wr.pos(-0.5, -0.5, -0.5).endVertex();
		wr.pos(-0.5, 0.5, -0.5).endVertex();

		wr.pos(-0.5, 0.5, -0.5).endVertex();
		wr.pos(0.5, 0.5, -0.5).endVertex();

		wr.pos(0.5, 0.5, -0.5).endVertex();
		wr.pos(0.5, -0.5, -0.5).endVertex();

		wr.pos(0.5, -0.5, -0.5).endVertex();
		wr.pos(-0.5, -0.5, -0.5).endVertex();

		// BACK
		wr.pos(-0.5, -0.5, 0.5).endVertex();
		wr.pos(-0.5, 0.5, 0.5).endVertex();

		wr.pos(-0.5, 0.5, 0.5).endVertex();
		wr.pos(0.5, 0.5, 0.5).endVertex();

		wr.pos(0.5, 0.5, 0.5).endVertex();
		wr.pos(0.5, -0.5, 0.5).endVertex();

		wr.pos(0.5, -0.5, 0.5).endVertex();
		wr.pos(-0.5, -0.5, 0.5).endVertex();

		// betweens.
		wr.pos(0.5, 0.5, -0.5).endVertex();
		wr.pos(0.5, 0.5, 0.5).endVertex();

		wr.pos(0.5, -0.5, -0.5).endVertex();
		wr.pos(0.5, -0.5, 0.5).endVertex();

		wr.pos(-0.5, -0.5, -0.5).endVertex();
		wr.pos(-0.5, -0.5, 0.5).endVertex();

		wr.pos(-0.5, 0.5, -0.5).endVertex();
		wr.pos(-0.5, 0.5, 0.5).endVertex();

		Tessellator.getInstance().draw();
	}
}
