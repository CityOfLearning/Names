package com.dyn.render.hud.dialog;

import java.awt.Color;

import com.dyn.fixins.entity.ghost.GhostEntity;
import com.dyn.robot.entity.EntityRobot;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.display.Picture;
import com.rabbit.gui.component.display.ScissorPanel;
import com.rabbit.gui.component.display.Shape;
import com.rabbit.gui.component.display.ShapeType;
import com.rabbit.gui.component.display.TextLabel;
import com.rabbit.gui.component.display.entity.DisplayEntity;
import com.rabbit.gui.component.display.entity.DisplayEntityHead;
import com.rabbit.gui.component.display.entity.EntityComponent;
import com.rabbit.gui.show.Show;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;

public class DialogHud extends Show {

	private TextLabel textArea;
	private EntityLivingBase entity;
	private EntityComponent entityElement;
	private Shape bg;
	private String text = "This is a test of the dialog renderer";
	private boolean doesInterupt = false;

	public DialogHud() {
		entity = new DisplayEntityHead(Minecraft.getMinecraft().theWorld);
	}

	public DialogHud(String text, boolean interupt) {
		entity = new DisplayEntityHead(Minecraft.getMinecraft().theWorld);
		this.text = text;
		this.doesInterupt = interupt;
	}

	public DialogHud(EntityLivingBase entity, boolean interupt) {
		this.entity = entity;
		this.doesInterupt = interupt;
	}

	public DialogHud(EntityLivingBase entity, String text, boolean interupt) {
		this.entity = entity;
		this.text = text;
		this.doesInterupt = interupt;
	}

	@Override
	public void onUpdate() {
		if (!doesInterupt) {
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

		if (doesInterupt) {
			float zoom = 1;
			double entheight = .25;
			double entwidth = .3;
			if (entity instanceof DisplayEntityHead) {
				zoom = 5f;
				entheight = .1;
				entwidth = .1;
			} else {
				zoom = 1 * Math.min((3.5f / entity.height), 4);
			}
			if (entity instanceof GhostEntity) {
				((GhostEntity) entity).setAlpha(1);
				zoom = 3.5f;
				entheight = .1;
			}
			if (entity instanceof EntitySpider) {
				entheight = .35;
				entwidth = .05;
			}
			if (entity instanceof EntityChicken) {
				entheight = .5;
				entwidth = .1;
			}
			
			if (entity instanceof EntityRobot){
				entwidth = .175;
			}

			ScissorPanel panel = new ScissorPanel((int) (width * .185), (int) (height * .2), (int) (width * .25),
					(int) (width * .225), true);

			panel.registerComponent(entityElement = new EntityComponent((int) (panel.getWidth() * entwidth),
					(int) (panel.getHeight() * entheight), 0, 0, entity, 0, zoom, false));

			entityElement.setRotation((float) ((Math.PI / 4) * 40.0F));

			registerComponent(textArea = new TextLabel((int) (width * .45), (int) (height * .25), (int) (width * .35),
					(int) (height * .6), text).setMultilined(true));

			if (!(entity instanceof DisplayEntityHead)) {
				panel.registerComponent(bg = new Shape(0, 0, panel.getWidth(), panel.getHeight(), ShapeType.RECT,
						new Color(40, 40, 40, 255)));
			}

			registerComponent(panel);

			registerComponent(new Button((int) (width * .25), (int) (height * .65), 60, 20, "Close")
					.setClickListener(btn -> {
						getStage().close();
					}));

			registerComponent(new Picture((int) (getWidth() * .15), (int) (getHeight() * .15), (int) (getWidth() * .7),
					(int) (getHeight() * .7), new ResourceLocation("dyn", "textures/gui/background3.png")));

		} else {
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
			if (entity instanceof EntitySpider) {
				zoom *= .8;
			}

			registerComponent(entityElement = new EntityComponent((int) (width * .1), (int) (height * .685), 0, 0,
					entity, 0, zoom, false));

			entityElement.setRotation((float) ((Math.PI / 4) * 40.0F));

			registerComponent(textArea = new TextLabel(width / 3, (int) (height * .7), (int) (width * .6),
					(int) (height * .2), text).setMultilined(true));

			registerComponent(bg = new Shape(0, (int) (height * .66), width, (int) (height * .33), ShapeType.RECT,
					new Color(40, 40, 55, 150)));
		}
	}

}
