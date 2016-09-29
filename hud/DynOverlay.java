package com.dyn.render.hud;

import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.dyn.DYNServerMod;
import com.dyn.render.RenderMod;
import com.dyn.utils.PlayerLevel;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;

public class DynOverlay extends Hud {

	private static final ResourceLocation TABS = new ResourceLocation("dyn", "textures/gui/sm_notification2.png");

	public static boolean isHidden = false;

	// this techincally overwrites the super class
	public static void draw() {
		updateWindowScale();
		Map<String, KeyBinding> keys = RenderMod.instance.getKeyBindings();
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		// int xPos = windowWidth - 120;
		// int yPos = windowHeight - 32;
		int xPos = 0;
		int yPos = 0;
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.disableLighting();

		if (!isHidden) {
			xPos += drawTab(xPos, yPos, "Achiev", "ements",
					"(" + Keyboard.getKeyName(keys.get("achievement").getKeyCode()) + ")");
			if (DYNServerMod.status == PlayerLevel.ADMIN) {
				xPos += drawTab(xPos, yPos, "Admin", "GUI",
						"(" + Keyboard.getKeyName(keys.get("admin").getKeyCode()) + ")");
			} else if (DYNServerMod.status == PlayerLevel.MENTOR) {
				xPos += drawTab(xPos, yPos, "Mentor", "GUI",
						"(" + Keyboard.getKeyName(keys.get("mentor").getKeyCode()) + ")");
			} else if (DYNServerMod.status == PlayerLevel.STUDENT) {
				xPos += drawTab(xPos, yPos, "Student", "GUI",
						"(" + Keyboard.getKeyName(keys.get("student").getKeyCode()) + ")");
			}
			xPos += drawTab(xPos, yPos, "Select", "Skin",
					"(" + Keyboard.getKeyName(keys.get("skin").getKeyCode()) + ")");
			drawTab(xPos, yPos, "Hide", "GUI", "(" + Keyboard.getKeyName(keys.get("hide").getKeyCode()) + ")");
		} else {
			xPos += drawHiddenTab(xPos, yPos, "Achiev", "ements",
					"(" + Keyboard.getKeyName(keys.get("achievement").getKeyCode()) + ")");
			if (DYNServerMod.status == PlayerLevel.ADMIN) {
				xPos += drawHiddenTab(xPos, yPos, "Admin", "GUI",
						"(" + Keyboard.getKeyName(keys.get("admin").getKeyCode()) + ")");
			} else if (DYNServerMod.status == PlayerLevel.MENTOR) {
				xPos += drawHiddenTab(xPos, yPos, "Mentor", "GUI",
						"(" + Keyboard.getKeyName(keys.get("mentor").getKeyCode()) + ")");
			} else if (DYNServerMod.status == PlayerLevel.STUDENT) {
				xPos += drawHiddenTab(xPos, yPos, "Student", "GUI",
						"(" + Keyboard.getKeyName(keys.get("student").getKeyCode()) + ")");
			}
			xPos += drawHiddenTab(xPos, yPos, "Select", "Skin",
					"(" + Keyboard.getKeyName(keys.get("skin").getKeyCode()) + ")");
			drawHiddenTab(xPos, yPos, "Hide", "GUI", "(" + Keyboard.getKeyName(keys.get("hide").getKeyCode()) + ")");
		}
		GlStateManager.disableLighting();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
	}

	private static int drawHiddenTab(int xPos, int yPos, String... lines) {
		int tabwidth = Math.max(mc.fontRendererObj.getStringWidth(lines[0]) + 8, 35);
		tabwidth = Math.max(mc.fontRendererObj.getStringWidth(lines[1]) + 8, tabwidth);
		tabwidth = Math.max(mc.fontRendererObj.getStringWidth(lines[2]) + 8, tabwidth);
		mc.getTextureManager().bindTexture(TABS);
		drawScaledTexturedRect(xPos, yPos <= 0 ? yPos - 28 : yPos + 28, 1, tabwidth, 32);
		return tabwidth;
	}

	private static int drawTab(int xPos, int yPos, String... lines) {
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
}
