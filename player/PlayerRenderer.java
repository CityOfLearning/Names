package com.dyn.render.player;

import org.lwjgl.opengl.GL11;

import com.dyn.render.manager.NamesManager;
import com.dyn.server.database.DBManager;
import com.rabbit.gui.utils.SkinManager;

import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class PlayerRenderer extends RenderPlayerBase {

	public PlayerRenderer(RenderPlayerAPI api) {
		super(api);
	}

	@Override
	public boolean bindEntityTexture(AbstractClientPlayer player) {
		if (SkinManager.hasSkinTexture(player)) {
			// attach our texture
			if ((SkinManager.getSkinTexture(player) != null) && !(SkinManager.getSkinTexture(player).isEmpty())) {
				if (SkinManager.bindSkinTexture(player)) {
					return true;
				} else {
					return super.bindEntityTexture(player);
				}

			} else {
				return super.bindEntityTexture(player);
			}
		} else {
			Runnable task = () -> {
				// this blocks and so we gotta thread it
				String texture = DBManager.getPlayerSkin(player.getDisplayNameString()).trim();
				if ((texture != null) && !texture.isEmpty()) {
					SkinManager.setSkinTexture(player, texture);
				}
			};
			// set it to the current texture the thread will run and overwrite
			// this if it isn't empty
			// SkinManager.setSkinTexture(player,
			// SkinManager.setSkinTexture(player, "");
			SkinManager.setSkinTexture(player,
					Minecraft.getMinecraft().getNetHandler().getPlayerInfo(player.getName()).getLocationSkin());

			new Thread(task).start();
			return super.bindEntityTexture(player);
		}
	}

	@Override
	public void renderName(AbstractClientPlayer player, double d1, double d2, double d3) {
		// the local can render name is wonky
		if (renderPlayerAPI.localCanRenderName(player)) {
			double d4 = player.getDistanceSqToEntity(renderPlayerAPI.localGetRenderManager().livingPlayer);
			float f = player.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK
					: RendererLivingEntity.NAME_TAG_RANGE;

			if (d4 < (f * f)) {
				String s = player.getDisplayName().getFormattedText();
				// if its null we havent grabbed the name yet
				if (NamesManager.getDYNUsername(player.getName()) == null) {
					NamesManager.addUsername(player.getName(),
							DBManager.getDisplayNameFromMCUsername(player.getName()));
				} else if (!NamesManager.getDYNUsername(player.getName()).isEmpty()) {
					s = s.replace(player.getName(), NamesManager.getDYNUsername(player.getName()));
				}
				GlStateManager.alphaFunc(516, 0.1F);

				if (player.isSneaking()) {
					FontRenderer fontrenderer = renderPlayerAPI.localGetFontRendererFromRenderManager();
					GlStateManager.pushMatrix();
					{
						GlStateManager.translate((float) d1,
								((float) d2 + player.height + 0.5F) - (player.isChild() ? player.height / 2.0F : 0.0F),
								(float) d3);
						GL11.glNormal3f(0.0F, 1.0F, 0.0F);
						GlStateManager.rotate(-renderPlayerAPI.localGetRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
						GlStateManager.rotate(renderPlayerAPI.localGetRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
						GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
						GlStateManager.translate(0.0F, 9.374999F, 0.0F);
						GlStateManager.disableLighting();
						GlStateManager.depthMask(false);
						GlStateManager.enableBlend();
						GlStateManager.disableTexture2D();
						GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
						int i = fontrenderer.getStringWidth(s) / 2;
						Tessellator tessellator = Tessellator.getInstance();
						WorldRenderer worldrenderer = tessellator.getWorldRenderer();
						worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
						worldrenderer.pos(-i - 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
						worldrenderer.pos(-i - 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
						worldrenderer.pos(i + 1, 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
						worldrenderer.pos(i + 1, -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
						tessellator.draw();
						GlStateManager.enableTexture2D();
						GlStateManager.depthMask(true);
						fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
						GlStateManager.enableLighting();
						GlStateManager.disableBlend();
						GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					}
					GlStateManager.popMatrix();
				} else {
					renderPlayerAPI.localRenderLivingLabel(player, s, d1,
							d2 - (player.isChild() ? (double) (player.height / 2.0F) : 0.0D), d3, 64);
					// maybe the int is distance? not really clear...
				}
			}
		}
	}

	@Override
	public void renderRightArm(AbstractClientPlayer player) {
		if (SkinManager.hasSkinTexture(player)) {
			// attach our texture
			SkinManager.bindSkinTexture(player);
		}
		super.renderRightArm(player);
	}
}
