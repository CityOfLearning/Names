package com.dyn.render.proxy;

import org.lwjgl.input.Keyboard;

import com.dyn.render.gui.SkinSelect;
import com.dyn.render.reference.Reference;
import com.dyn.render.render.PlayerRenderer;
import com.rabbit.gui.RabbitGui;

import api.player.render.RenderPlayerAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class Client implements Proxy {

	private KeyBinding skinKey;

	@Override
	public void init() {
		RenderPlayerAPI.register(Reference.MOD_ID, PlayerRenderer.class);
		MinecraftForge.EVENT_BUS.register(this);
		skinKey = new KeyBinding("key.toggle.skinui", Keyboard.KEY_B, "key.categories.toggle");
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
	}

	@Override
	public void renderGUI() {
		// TODO Auto-generated method stub

	}

	@SubscribeEvent
	public void renderPass(RenderGameOverlayEvent event) {
		// need to determine when the game renders an achievement overlay...
		// we might have to mixin this since it's not actively bussed
	}

}