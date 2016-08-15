package com.dyn.render.proxy;

import org.lwjgl.input.Keyboard;

import com.dyn.render.gui.SkinSelect;
import com.dyn.render.gui.achievement.Search;
import com.dyn.render.manager.NotificationsManager;
import com.dyn.render.reference.Reference;
import com.dyn.render.render.DynOverlay;
import com.dyn.render.render.PlayerRenderer;
import com.dyn.student.StudentUI;
import com.rabbit.gui.RabbitGui;

import api.player.render.RenderPlayerAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Client implements Proxy {

	private KeyBinding skinKey;
	private KeyBinding hideGuiKey;
	private KeyBinding achievementKey;

	private boolean hideGui = false;

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
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if ((Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
			return;
		}
		if (skinKey.isPressed()) {
			RabbitGui.proxy.display(new SkinSelect());
		}
		if (achievementKey.isPressed()) {
			RabbitGui.proxy.display(new Search());
		}

		if (hideGuiKey.isPressed()) {
			hideGui = !hideGui;
		}
	}

	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		if (Minecraft.getMinecraft().inGameHasFocus) {
			if (StudentUI.frozen.getFlag()) {
				hudOverlay.drawFrozenOverlay();
			}
			hudOverlay.drawOverlay(hideGui);
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

}