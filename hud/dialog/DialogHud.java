package com.dyn.render.hud.dialog;

import java.awt.Color;

import com.dyn.fixins.entity.ghost.GhostEntity;
import com.rabbit.gui.component.display.Shape;
import com.rabbit.gui.component.display.ShapeType;
import com.rabbit.gui.component.display.TextLabel;
import com.rabbit.gui.component.display.entity.DisplayEntity;
import com.rabbit.gui.component.display.entity.DisplayEntityHead;
import com.rabbit.gui.component.display.entity.EntityComponent;
import com.rabbit.gui.show.Show;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class DialogHud extends Show {

	private TextLabel textArea;
	private EntityLivingBase entity;
	private EntityComponent entityElement;

	public DialogHud() {
		entity = new DisplayEntityHead(Minecraft.getMinecraft().theWorld);
	}

	public DialogHud(EntityLivingBase entity) {
		this.entity = entity;
	}

	public void setEntity(EntityLivingBase entity) {
		if (entity != null) {
			this.entity = entity;
			this.entity.renderYawOffset = (float) ((-Math.PI / 4) * 20.0F);
			this.entity.rotationYaw = (float) ((-Math.PI / 4) * 40.0F);
			this.entity.rotationYawHead = this.entity.rotationYaw;
			this.entity.prevRotationYawHead = this.entity.rotationYaw;
			entityElement.setEntity(this.entity);
			if (entity instanceof DisplayEntityHead) {
				entityElement.setZoom(4.5f);
				((DisplayEntity) entityElement.getEntity()).setTexture(((DisplayEntity) entity).getTexture());
			} else {
				entityElement.setZoom(1 * (2.2f / entity.height));
			}
			if (entity instanceof GhostEntity) {
				((GhostEntity) this.entity).setAlpha(1);
				entityElement.setZoom(1.5f);
			}
		}
	}

	public void setRenderText(String renderText) {
		textArea.setText(renderText);
	}

	public void setRotation(float rotation) {
		entityElement.setRotation(rotation);
	}

	@Override
	public void setup() {
		super.setup();

		float zoom = 1;
		if (entity instanceof DisplayEntityHead) {
			zoom = 4.5f;
		} else {
			zoom = 1 * (2.2f / entity.height);
		}
		if (entity instanceof GhostEntity) {
			((GhostEntity) entity).setAlpha(1);
			zoom = 1.5f;
		}

		entity.renderYawOffset = (float) ((-Math.PI / 4) * 20.0F);
		entity.rotationYaw = (float) ((-Math.PI / 4) * 40.0F);
		entity.rotationYawHead = entity.rotationYaw;
		entity.prevRotationYawHead = entity.rotationYaw;

		registerComponent(entityElement = new EntityComponent((int) (width * .1), (int) (height * .685), 0, 0, entity,
				0, zoom, false));

		registerComponent(textArea = new TextLabel(width / 3, (int) (height * .7), (int) (width * .6),
				(int) (height * .2), "This is a test of the dialog renderer").setMultilined(true));

		registerComponent(new Shape(0, (int) (height * .66), width, (int) (height * .33), ShapeType.RECT,
				new Color(40, 40, 55, 150)));
	}

}
