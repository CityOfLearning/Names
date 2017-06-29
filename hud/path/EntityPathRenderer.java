package com.dyn.render.hud.path;

import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;

public class EntityPathRenderer {

	private static Map<EntityLiving, PathEntity> entities = Maps.newHashMap();

	public static void addEntityForPathRendering(EntityLiving entity, PathEntity path) {
		if (entities.containsKey(entity)) {
			entities.replace(entity, path);
		} else {
			entities.put(entity, path);
		}
	}

	public static void removeEntityForPathRendering(EntityLiving entity) {
		entities.remove(entity);
	}

	public static void renderEntityPaths() {
		Iterator<EntityLiving> it = entities.keySet().iterator();
		while (it.hasNext()) {
			EntityLiving entity = it.next();
			PathPoint point = entities.get(entity).getFinalPathPoint();
			if ((entity == null) || entity.isDead
					|| (entity.getPosition().distanceSq(point.xCoord, point.yCoord, point.zCoord) < 1)) {
				it.remove();
			} else {
				renderPath(entities.get(entity));
			}
		}
	}

	// pathing only ever happens on the server side... how do we get it client
	// side to render
	public static void renderPath(PathEntity path) {

		double renderPosX = TileEntityRendererDispatcher.staticPlayerX;
		double renderPosY = TileEntityRendererDispatcher.staticPlayerY;
		double renderPosZ = TileEntityRendererDispatcher.staticPlayerZ;
		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(-renderPosX + 0.5, -renderPosY + 0.5, -renderPosZ + 0.5);

			GlStateManager.resetColor();

			if (path == null) {
				return;
			}

			// might have to disable this
			GlStateManager.depthMask(false);
			GlStateManager.disableDepth();
			GlStateManager.disableCull();
			GlStateManager.enableBlend();
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableTexture2D();

			GL11.glLineWidth((float) (5.0F - Math.min(4.5, (path.getPathPointFromIndex(0).distanceTo(
					new PathPoint((int) (-renderPosX + 0.5), (int) (-renderPosY + 0.5), (int) (-renderPosZ + 0.5)))
					/ 10.0))));

			GlStateManager.pushMatrix();
			{
				// GlStateManager.translate(0, -1D, 0);

				for (int i = 1; i < path.getCurrentPathLength(); i++) {

					GL11.glColor4d(1, 1, 1, .5);

					PathPoint lastPoint = path.getPathPointFromIndex(i - 1);
					PathPoint pathPoint = path.getPathPointFromIndex(i);

					// tess.startDrawing(GL11.GL_LINE_STRIP);
					WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();

					wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);

					wr.pos(lastPoint.xCoord, lastPoint.yCoord, lastPoint.zCoord).endVertex();
					;
					wr.pos((lastPoint.xCoord + pathPoint.xCoord) / 2D, Math.max(lastPoint.yCoord, pathPoint.yCoord),
							(lastPoint.zCoord + pathPoint.zCoord) / 2D).endVertex();
					;
					wr.pos(pathPoint.xCoord, pathPoint.yCoord, pathPoint.zCoord).endVertex();
					;

					Tessellator.getInstance().draw();

				}

			}
			GlStateManager.popMatrix();
			GlStateManager.enableCull();
			GlStateManager.enableDepth();
			GlStateManager.disableBlend();
			GlStateManager.depthMask(true);
			GL11.glDisable(GL11.GL_LINE_STIPPLE);
			GlStateManager.enableTexture2D();
		}
		GlStateManager.popMatrix();
	}

}
