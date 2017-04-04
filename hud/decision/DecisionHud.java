package com.dyn.render.hud.decision;

import java.awt.Color;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.dyn.fixins.blocks.decision.DecisionBlockTileEntity;
import com.dyn.fixins.blocks.decision.DecisionBlockTileEntity.Choice;
import com.dyn.fixins.entity.ghost.GhostEntity;
import com.dyn.robot.entity.EntityRobot;
import com.dyn.server.network.NetworkManager;
import com.dyn.server.network.messages.MessageBlockRedstoneSignalUpdate;
import com.dyn.server.network.packets.server.ServerCommandMessage;
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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;

public class DecisionHud extends Show {

	private EntityLivingBase entity;
	private EntityComponent entityElement;
	private DecisionBlockTileEntity block;
	private boolean reactivate = true;

	public DecisionHud(EntityLivingBase entity, DecisionBlockTileEntity block) {
		this.entity = entity;
		this.block = block;
	}

	@Override
	public void onClose() {
		super.onClose();
		if (block.isQuiz() && reactivate) {
			block.setActive(true);
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

	public void setRotation(float rotation) {
		entityElement.setRotation(rotation);
	}

	@Override
	public void setup() {
		super.setup();

		reactivate = true;

		float zoom = 1 * Math.min((3.5f / entity.height), 4);
		double entheight = .25;
		double entwidth = .3;
		if (entity instanceof DisplayEntity) {
			zoom = 3.125f;
			entwidth = .075;
			entheight = .1;
			if (entity instanceof DisplayEntityHead) {
				zoom = 6f;
				entheight = .1;
				entwidth = .1;
			}
		} else if (entity instanceof GhostEntity) {
			((GhostEntity) entity).setAlpha(1);
			zoom = 3.5f;
			entheight = .1;
		} else if (entity instanceof EntitySpider) {
			entheight = .35;
			entwidth = .05;
		} else if (entity instanceof EntityChicken) {
			entheight = .5;
			entwidth = .1;
		} else if (entity instanceof EntityRobot) {
			entwidth = .175;
		}

		ScissorPanel panel = new ScissorPanel((int) (width * .185), (int) (height * .2), (int) (width * .25),
				(int) (height * .3875), true);

		panel.registerComponent(entityElement = new EntityComponent((int) (panel.getWidth() * entwidth),
				(int) (panel.getHeight() * entheight), 0, 0, entity, 0, zoom, false));

		entityElement.setRotation((float) ((Math.PI / 4) * 40.0F));

		registerComponent(new TextLabel((int) (width * .45), (int) (height * .25), (int) (width * .35),
				(int) (height * .6), block.getText()).setMultilined(true));

		if (!(entity instanceof DisplayEntityHead)) {
			panel.registerComponent(
					new Shape(0, 0, panel.getWidth(), panel.getHeight(), ShapeType.RECT, new Color(40, 40, 40, 255)));
		}

		registerComponent(panel);

		int index = 0;

		for (Entry<String, Choice> choice : block.getChoices().entrySet()) {
			registerComponent(new Button((int) ((width * .225) + (width * .3 * (index % 2))),
					(int) ((height * .61) + (height * .1 * (index / 2))), (int) (width * .25), 20, choice.getKey())
							.setClickListener(btn -> {
								if (block.isQuiz() && choice.getValue().isCorrect()) {
									reactivate = false;
								}
								if (choice.getValue().equals(Choice.REDSTONE)) {
									NetworkManager
											.sendToServer(new MessageBlockRedstoneSignalUpdate(block.getPos(), true));
									getStage().close();
								} else if (!(choice.getValue().equals(Choice.NONE))) {
									String command = choice.getValue().getValue();
									// TODO: Right Now it can only do simple
									// player commands, doing all or entity
									// commands requires world and position data
									// in an ICommandSender class so we would
									// need like a fake player with op status
									if (command.contains(Pattern.quote("@"))) {
										String cmdSubStr = command.substring(command.indexOf("@"),
												command.indexOf(" ", command.indexOf("@")));
										switch (cmdSubStr) {
										case "P":
											command = command.replace("@P",
													Minecraft.getMinecraft().thePlayer.getName());
											break;
										case "p":
											command = command.replace("@p",
													Minecraft.getMinecraft().thePlayer.getName());
											break;
										}
									}
									NetworkManager.sendToServer(new ServerCommandMessage(command));
									getStage().close();
								} else {
									NetworkManager
											.sendToServer(new MessageBlockRedstoneSignalUpdate(block.getPos(), false));
									getStage().close();
								}
							}));
			index++;
		}

		registerComponent(new Picture((int) (getWidth() * .15), (int) (getHeight() * .15), (int) (getWidth() * .7),
				(int) (getHeight() * .7), new ResourceLocation("dyn", "textures/gui/background3.png")));
	}

}
