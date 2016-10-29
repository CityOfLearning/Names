package com.dyn.render.hud.dialog;

import java.awt.Color;

import com.rabbit.gui.component.display.Shape;
import com.rabbit.gui.component.display.ShapeType;
import com.rabbit.gui.component.display.TextLabel;
import com.rabbit.gui.component.display.entity.DisplayEntityHead;
import com.rabbit.gui.component.display.entity.EntityComponent;
import com.rabbit.gui.show.Show;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class DialogHud extends Show {

	private TextLabel textArea;
	private EntityLivingBase entity;

	public DialogHud() {
		entity = new DisplayEntityHead(Minecraft.getMinecraft().theWorld);
	}

	public DialogHud(EntityLivingBase entity) {
		this.entity = entity;
	}

	public void setRenderText(String renderText) {
		textArea.setText(renderText);
	}

	@Override
	public void setup() {
		super.setup();

		float zoom = 4.5f;
		registerComponent(
				new EntityComponent((int) (width * .1), (int) (height * .975), width / 3, 100, entity, 0, zoom, false));

		registerComponent(textArea = new TextLabel(width / 3, (int) (height * .7), (int) (width * .6),
				(int) (height * .2), "This is a test of the dialog renderer").setMultilined(true));

		registerComponent(new Shape(0, (int) (height * .66), width, (int) (height * .33), ShapeType.RECT,
				new Color(40, 40, 55, 150)));
	}

}