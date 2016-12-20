package com.dyn.render.hud.path;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;

public class EntityPathRenderer {

	private static List<EntityLiving> entities = Lists.newArrayList();

	public static void addEntityForPathRendering(EntityLiving entity) {
		entities.add(entity);
	}

	public static void removeEntityForPathRendering(EntityLiving entity) {
		entities.remove(entity);
	}

	private static void renderBox() {
		WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();

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

	public static void renderEntityPaths() {
		for (EntityLiving entity : entities) {
			if (entity.isDead) {
				removeEntityForPathRendering(entity);
			} else {
				renderPath(entity);
			}
		}
	}

	public static void renderPath(EntityLiving entity) {
		// System.out.println(entity+": "+entity.getNavigator() +", " +
		// ((PathNavigateRobot) entity.getNavigator()).getEntityPath());
		if (!entity.getNavigator().noPath()) {
			PathEntity path = entity.getNavigator().getPath();

			double renderPosX = TileEntityRendererDispatcher.staticPlayerX;
			double renderPosY = TileEntityRendererDispatcher.staticPlayerY;
			double renderPosZ = TileEntityRendererDispatcher.staticPlayerZ;
			GL11.glPushMatrix();
			GL11.glTranslated(-renderPosX + 0.5, -renderPosY + 0.5, -renderPosZ + 0.5);

			GlStateManager.resetColor();

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glLineWidth(3);
			for (int i = path.getCurrentPathIndex(); i < path.getCurrentPathLength(); ++i) {
				PathPoint point = path.getPathPointFromIndex(i);
				boolean seeThrough = true;
				while (true) {
					if (seeThrough) {
						GL11.glDisable(GL11.GL_DEPTH_TEST);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					} else {
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_DEPTH_TEST);
					}

					GL11.glPushMatrix();
					GL11.glTranslated(point.xCoord, point.yCoord, point.zCoord);
					GL11.glScalef(0.96F, 0.96F, 0.96F);
					if (seeThrough) {
						GL11.glColor4f(1, 0, 0, .25f);
					} else {
						GL11.glColor3f(1, 0, 0);
					}
					renderBox();
					GL11.glPopMatrix();

					if (!seeThrough) {
						break;
					}
					seeThrough = false;
				}
			}
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
		}
	}

}
