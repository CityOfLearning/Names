package com.dyn.render.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.opengl.GL11;

import com.dyn.robot.entity.EntityRobot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class RenderChatBubble {
	private List<List<IChatComponent>> formattedMessages;
	private Map<Long, String> prevMessages;
	private int boxLength;
	private float scale;

	public RenderChatBubble() {
		formattedMessages = new ArrayList<List<IChatComponent>>();
		prevMessages = new TreeMap<Long, String>();
		boxLength = 46;
		scale = 0.5f;
	}

	private void addLine(String text, ChatStyle style, List<IChatComponent> formattedText) {
		ChatComponentText line = new ChatComponentText(text);
		line.setChatStyle(style);
		formattedText.add(line);
	}

	private void drawRect(int par0, int par1, int par2, int par3, int par4, double par5) {
		if (par0 < par2) {
			int j1 = par0;
			par0 = par2;
			par2 = j1;
		}
		if (par1 < par3) {
			int j1 = par1;
			par1 = par3;
			par3 = j1;
		}
		float f = ((par4 >> 24) & 0xFF) / 255.0f;
		float f2 = ((par4 >> 16) & 0xFF) / 255.0f;
		float f3 = ((par4 >> 8) & 0xFF) / 255.0f;
		float f4 = (par4 & 0xFF) / 255.0f;
		WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
		GlStateManager.color(f2, f3, f4, f);
		tessellator.begin(7, DefaultVertexFormats.POSITION);
		tessellator.pos(par0, par3, par5).endVertex();
		tessellator.pos(par2, par3, par5).endVertex();
		tessellator.pos(par2, par1, par5).endVertex();
		tessellator.pos(par0, par1, par5).endVertex();
		Tessellator.getInstance().draw();
	}

	public List<IChatComponent> formatText(String text, int lineWidth, boolean mcFont) {
		List<IChatComponent> formattedText = new ArrayList();
		ChatStyle style = new ChatStyle();
		String line = "";
		text = text.replace("\n", " \n ");
		text = text.replace("\r", " \r ");
		String[] words = text.split(" ");
		FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
		for (String word : words) {
			Label_0235: {
				if (!word.isEmpty()) {
					if (word.length() == 1) {
						char c = word.charAt(0);
						if ((c == '\r') || (c == '\n')) {
							addLine(line, style, formattedText);
							line = "";
							break Label_0235;
						}
					}
					String newLine;
					if (line.isEmpty()) {
						newLine = word;
					} else {
						newLine = line + " " + word;
					}
					if ((mcFont ? font.getStringWidth(newLine)
							: Minecraft.getMinecraft().fontRendererObj.getStringWidth(newLine)) > lineWidth) {
						addLine(line, style, formattedText);
						line = word.trim();
					} else {
						line = newLine;
					}
				}
			}
		}
		if (!line.isEmpty()) {
			addLine(line, style, formattedText);
		}
		return formattedText;
	}

	private void render(double par3, double par5, double par7, float textscale, boolean depth) {
		FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
		float var13 = 1.6f;
		float var14 = 0.016666668f * var13;
		GlStateManager.pushMatrix();
		int size = 0;
		for (List<IChatComponent> block : formattedMessages) {
			size += block.size();
		}
		Minecraft mc = Minecraft.getMinecraft();
		int textYSize = (int) (size * font.FONT_HEIGHT * scale);
		GlStateManager.translate((float) par3 + 0.0f, (float) par5 + (textYSize * textscale * var14), (float) par7);
		GlStateManager.scale(textscale, textscale, textscale);
		GL11.glNormal3f(0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
		GlStateManager.scale(-var14, -var14, var14);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.depthMask(true);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		if (depth) {
			GlStateManager.enableDepth();
		} else {
			GlStateManager.disableDepth();
		}
		int black = depth ? -16777216 : 1426063360;
		int white = depth ? -1140850689 : 1157627903;
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.disableTexture2D();
		drawRect(-boxLength - 2, -2, boxLength + 2, textYSize + 1, white, 0.11);
		drawRect(-boxLength - 1, -3, boxLength + 1, -2, black, 0.1);
		drawRect(-boxLength - 1, textYSize + 2, -1, textYSize + 1, black, 0.1);
		drawRect(3, textYSize + 2, boxLength + 1, textYSize + 1, black, 0.1);
		drawRect(-boxLength - 3, -1, -boxLength - 2, textYSize, black, 0.1);
		drawRect(boxLength + 3, -1, boxLength + 2, textYSize, black, 0.1);
		drawRect(-boxLength - 2, -2, -boxLength - 1, -1, black, 0.1);
		drawRect(boxLength + 2, -2, boxLength + 1, -1, black, 0.1);
		drawRect(-boxLength - 2, textYSize + 1, -boxLength - 1, textYSize, black, 0.1);
		drawRect(boxLength + 2, textYSize + 1, boxLength + 1, textYSize, black, 0.1);
		drawRect(0, textYSize + 1, 3, textYSize + 4, white, 0.11);
		drawRect(-1, textYSize + 4, 1, textYSize + 5, white, 0.11);
		drawRect(-1, textYSize + 1, 0, textYSize + 4, black, 0.1);
		drawRect(3, textYSize + 1, 4, textYSize + 3, black, 0.1);
		drawRect(2, textYSize + 3, 3, textYSize + 4, black, 0.1);
		drawRect(1, textYSize + 4, 2, textYSize + 5, black, 0.1);
		drawRect(-2, textYSize + 4, -1, textYSize + 5, black, 0.1);
		drawRect(-2, textYSize + 5, 1, textYSize + 6, black, 0.1);
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
		GlStateManager.scale(scale, scale, scale);
		int index = 0;
		for (List<IChatComponent> block2 : formattedMessages) {
			for (IChatComponent chat : block2) {
				String message = chat.getFormattedText();
				Minecraft.getMinecraft().fontRendererObj.drawString(message, -font.getStringWidth(message) / 2,
						index * font.FONT_HEIGHT, black, false);
				++index;
			}
		}
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.enableDepth();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();
	}

	public void renderMessages(EntityRobot entity, double x, double y, double z, float textscale, boolean inRange) {
		Map<Long, String> robotmessages = entity.getMessages();
		if (robotmessages.isEmpty()) {
			return;
		}
		if (robotmessages.size() != prevMessages.size()) {
			// either a new message or an old message expired
			prevMessages = robotmessages;
			formattedMessages.clear();
			for (String message : robotmessages.values()) {
				formattedMessages.add(formatText(message, boxLength * 4, true));
			}
		} else if ((prevMessages.size() > 0)
				&& (robotmessages.keySet().iterator().next() != prevMessages.keySet().iterator().next())) {
			// the time tag doesnt match it must be out of sync
			prevMessages = robotmessages;
			formattedMessages.clear();
			for (String message : robotmessages.values()) {
				formattedMessages.add(formatText(message, boxLength * 4, true));
			}
		}

		if (inRange) {
			render(x, y, z, textscale, false);
		}
		render(x, y, z, textscale, true);
	}
}