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
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

public class DialogHud extends Show {

	private TextLabel textArea;
	private EntityLivingBase entity;
	private EntityComponent entityElement;
	private Shape bg;

	public DialogHud() {
		entity = new DisplayEntityHead(Minecraft.getMinecraft().theWorld);
	}

	public DialogHud(EntityLivingBase entity) {
		this.entity = entity;
	}

	public void setEntity(EntityLivingBase entity) {
		if (entity != null) {
			this.entity = entity;
			entityElement.setEntity(this.entity);
			entityElement.setRotation((float) ((Math.PI / 4) * 40.0F));
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

		registerComponent(entityElement = new EntityComponent((int) (width * .1), (int) (height * .685), 0, 0, entity,
				0, zoom, false));

		entityElement.setRotation((float) ((Math.PI / 4) * 40.0F));
		
		registerComponent(textArea = new TextLabel(width / 3, (int) (height * .7), (int) (width * .6),
				(int) (height * .2), "This is a test of the dialog renderer").setMultilined(true));

		registerComponent(bg = new Shape(0, (int) (height * .66), width, (int) (height * .33), ShapeType.RECT,
				new Color(40, 40, 55, 150)));
	}
	
	@Override
	public void onUpdate() {
		ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
		width = scaledresolution.getScaledWidth();
		height = scaledresolution.getScaledHeight();
		
		entityElement.setX((int) (width * .1));
		entityElement.setY((int) (height * .685));
		
		bg.setY((int) (height * .66));
		bg.setWidth(width);
		bg.setHeight((int) (height * .33));
		
		textArea.setX(width / 3);
		textArea.setY((int) (height * .7));
		textArea.setWidth((int) (width * .6));
		textArea.setHeight((int) (height * .2));
	}

}
