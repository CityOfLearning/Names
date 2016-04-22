package com.dyn.names.proxy;

import org.lwjgl.opengl.GL11;

import com.dyn.names.manager.NamesManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraftforge.client.event.RenderLivingEvent.Specials;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Client implements Proxy {

	static final class DYNSwitchEnumVisible {
		static final int[] playerVisibleEnum = new int[Team.EnumVisible.values().length];

		static {
			try {
				playerVisibleEnum[Team.EnumVisible.ALWAYS.ordinal()] = 1;
			} catch (NoSuchFieldError var4) {
				;
			}

			try {
				playerVisibleEnum[Team.EnumVisible.NEVER.ordinal()] = 2;
			} catch (NoSuchFieldError var3) {
				;
			}

			try {
				playerVisibleEnum[Team.EnumVisible.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
			} catch (NoSuchFieldError var2) {
				;
			}

			try {
				playerVisibleEnum[Team.EnumVisible.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
			} catch (NoSuchFieldError var1) {
				;
			}
		}
	}

	private boolean canRenderName(RenderManager renderManager, EntityLivingBase targetEntity) {
		EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;

		if ((targetEntity instanceof EntityPlayer) && (targetEntity != entityplayersp)) {
			Team team = targetEntity.getTeam();
			Team team1 = entityplayersp.getTeam();

			if (team != null) {
				Team.EnumVisible enumvisible = team.func_178770_i();

				switch (DYNSwitchEnumVisible.playerVisibleEnum[enumvisible.ordinal()]) {
				case 1:
					return true;
				case 2:
					return false;
				case 3:
					return (team1 == null) || team.isSameTeam(team1);
				case 4:
					return (team1 == null) || !team.isSameTeam(team1);
				default:
					return true;
				}
			}
		}

		return Minecraft.isGuiEnabled() && (targetEntity != renderManager.livingPlayer)
				&& !targetEntity.isInvisibleToPlayer(entityplayersp) && (targetEntity.riddenByEntity == null);
	}

	@Override
	public void init() {
		FMLCommonHandler.instance().bus().register(this);

		MinecraftForge.EVENT_BUS.register(this);

		NamesManager.init();
	}

	@SubscribeEvent
	public void NameRenderEvent(Specials.Pre event) {
		if (event.entity instanceof EntityPlayer) {
			event.setCanceled(true);
			if (canRenderName(event.renderer.func_177068_d(), event.entity)) {
				double d3 = event.entity.getDistanceSqToEntity(event.renderer.func_177068_d().livingPlayer);
				float f = event.entity.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK
						: RendererLivingEntity.NAME_TAG_RANGE;

				if (d3 < (f * f)) {
					String s = event.entity.getDisplayName().getFormattedText();
					if ((NamesManager.getDYNUsername(event.entity.getName()) != null)
							&& !NamesManager.getDYNUsername(event.entity.getName()).isEmpty()) {
						s = s.replace(event.entity.getName(), NamesManager.getDYNUsername(event.entity.getName()));
					}
					GlStateManager.alphaFunc(516, 0.1F);

					if (event.entity.isSneaking()) {
						FontRenderer fontrenderer = event.renderer.getFontRendererFromRenderManager();
						GlStateManager.pushMatrix();
						GlStateManager.translate((float) event.x,
								((float) event.y + event.entity.height + 0.5F)
										- (event.entity.isChild() ? event.entity.height / 2.0F : 0.0F),
								(float) event.z);
						GL11.glNormal3f(0.0F, 1.0F, 0.0F);
						GlStateManager.rotate(-event.renderer.func_177068_d().playerViewY, 0.0F, 1.0F, 0.0F);
						GlStateManager.rotate(event.renderer.func_177068_d().playerViewX, 1.0F, 0.0F, 0.0F);
						GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
						GlStateManager.translate(0.0F, 9.374999F, 0.0F);
						GlStateManager.disableLighting();
						GlStateManager.depthMask(false);
						GlStateManager.enableBlend();
						GlStateManager.disableTexture2D();
						GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
						Tessellator tessellator = Tessellator.getInstance();
						WorldRenderer worldrenderer = tessellator.getWorldRenderer();
						worldrenderer.startDrawingQuads();
						int i = fontrenderer.getStringWidth(s) / 2;
						worldrenderer.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
						worldrenderer.addVertex(-i - 1, -1.0D, 0.0D);
						worldrenderer.addVertex(-i - 1, 8.0D, 0.0D);
						worldrenderer.addVertex(i + 1, 8.0D, 0.0D);
						worldrenderer.addVertex(i + 1, -1.0D, 0.0D);
						tessellator.draw();
						GlStateManager.enableTexture2D();
						GlStateManager.depthMask(true);
						fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
						GlStateManager.enableLighting();
						GlStateManager.disableBlend();
						GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
						GlStateManager.popMatrix();
					} else {
						renderLivingLabel(event.renderer.func_177068_d(), event.entity, s, event.x,
								event.y - (event.entity.isChild() ? (double) (event.entity.height / 2.0F) : 0.0D),
								event.z);
					}
				}
			}
		}
	}

	@Override
	public void renderGUI() {
		// TODO Auto-generated method stub

	}

	private void renderLivingLabel(RenderManager renderManager, Entity entity, String s, double x, double y, double z) {
		double d3 = entity.getDistanceSqToEntity(renderManager.livingPlayer);

		if (d3 <= (64 * 64)) {
			FontRenderer fontrenderer = renderManager.getFontRenderer();
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x + 0.0F, (float) y + entity.height + 0.5F, (float) z);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(-f1, -f1, f1);
			GlStateManager.disableLighting();
			GlStateManager.depthMask(false);
			GlStateManager.disableDepth();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			byte b0 = 0;

			if (s.equals("deadmau5")) {
				b0 = -10;
			}

			GlStateManager.disableTexture2D();
			worldrenderer.startDrawingQuads();
			int j = fontrenderer.getStringWidth(s) / 2;
			worldrenderer.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
			worldrenderer.addVertex(-j - 1, -1 + b0, 0.0D);
			worldrenderer.addVertex(-j - 1, 8 + b0, 0.0D);
			worldrenderer.addVertex(j + 1, 8 + b0, 0.0D);
			worldrenderer.addVertex(j + 1, -1 + b0, 0.0D);
			tessellator.draw();
			GlStateManager.enableTexture2D();
			fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, b0, 553648127);
			GlStateManager.enableDepth();
			GlStateManager.depthMask(true);
			fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, b0, -1);
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.popMatrix();
		}
	}
}