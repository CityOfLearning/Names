package com.dyn.render.proxy;

import org.lwjgl.input.Keyboard;

import com.dyn.DYNServerMod;
import com.dyn.betterachievements.gui.GuiBetterAchievements;
import com.dyn.betterachievements.handler.GuiOpenHandler;
import com.dyn.render.gui.SkinSelect;
import com.dyn.render.gui.achievement.Search;
import com.dyn.render.gui.turtle.ProgrammingInterface;
import com.dyn.render.hud.DynOverlay;
import com.dyn.render.hud.PlayerRenderer;
import com.dyn.render.manager.NotificationsManager;
import com.dyn.render.reference.Reference;
import com.dyn.student.StudentUI;
import com.dyn.utils.PlayerLevel;
import com.rabbit.gui.RabbitGui;

import api.player.render.RenderPlayerAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Client implements Proxy {

	private KeyBinding skinKey;
	private KeyBinding hideGuiKey;
	private KeyBinding achievementKey;
	private ProgrammingInterface programInterface = new ProgrammingInterface();

	private boolean hideGui = false;
	private boolean showTurtleProgrammer = false;

	private final DynOverlay hudOverlay = new DynOverlay();

	@Override
	public void init() {
		RenderPlayerAPI.register(Reference.MOD_ID, PlayerRenderer.class);
		MinecraftForge.EVENT_BUS.register(this);
		skinKey = new KeyBinding("key.toggle.skinui", Keyboard.KEY_B, "key.categories.toggle");
		hideGuiKey = new KeyBinding("key.toggle.achievementgui", Keyboard.KEY_H, "key.categories.toggle");
		achievementKey = new KeyBinding("key.toggle.achievementui", Keyboard.KEY_N, "key.categories.toggle");

		ClientRegistry.registerKeyBinding(achievementKey);
		ClientRegistry.registerKeyBinding(hideGuiKey);
		ClientRegistry.registerKeyBinding(skinKey);
	}

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		// stop unintended command block manipulations
		if ((event.gui instanceof GuiCommandBlock) && !(DYNServerMod.status == PlayerLevel.ADMIN)) {
			event.setCanceled(true);
			return;
		}

		// a work in progress
		/*
		 * if (event.gui instanceof
		 * dan200.computercraftedu.client.gui.GuiTurtleRemote) { // annoyingly
		 * this will spit out a bunch of NPE's because of the // closed
		 * container event.setCanceled(true); // use the same interface
		 * RabbitGui.proxy.display(programInterface); }
		 */

		if (event.gui instanceof GuiAchievements) {
			event.setCanceled(true);
			try {
				Minecraft.getMinecraft().displayGuiScreen(
						new GuiBetterAchievements((GuiScreen) GuiOpenHandler.prevScreen.get(event.gui),
								(Integer) GuiOpenHandler.currentPage.get(event.gui) + 1));
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if ((Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
			return;
		}
		if (skinKey.isPressed()) {
			DYNServerMod.logger.info("Skin Gui Opened");
			RabbitGui.proxy.display(new SkinSelect());
		}
		if (achievementKey.isPressed()) {
			DYNServerMod.logger.info("Achievement Gui Opened");
			RabbitGui.proxy.display(new Search());
		}

		if (hideGuiKey.isPressed()) {
			hideGui = !hideGui;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			showTurtleProgrammer = false;
		}
	}

	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		if (Minecraft.getMinecraft().inGameHasFocus || ((RabbitGui.proxy.getCurrentStage() != null)
				&& (RabbitGui.proxy.getCurrentStage().getShow() instanceof ProgrammingInterface))) {
			if (StudentUI.frozen.getFlag()) {
				hudOverlay.drawFrozenOverlay();
			}
			hudOverlay.drawOverlay(hideGui);

			if (Minecraft.getMinecraft().inGameHasFocus && showTurtleProgrammer) {
				programInterface.onDraw(0, 0, event.renderTickTime);
			}
		}
		if (!(Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu)) {
			NotificationsManager.renderNotifications();
		}
	}

	// @SubscribeEvent
	// public void renderPass(RenderGameOverlayEvent event) {
	// // need to determine when the game renders an achievement overlay...
	// // we might have to mixin this since it's not actively bussed
	// }

	@Override
	public void renderGUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public void toggleRenderProgramInterface(boolean state) {
		showTurtleProgrammer = state;
	}

}