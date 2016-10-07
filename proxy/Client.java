package com.dyn.render.proxy;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.dyn.DYNServerMod;
import com.dyn.betterachievements.gui.GuiBetterAchievements;
import com.dyn.betterachievements.handler.GuiOpenHandler;
import com.dyn.render.gui.achievement.Search;
import com.dyn.render.gui.programmer.ProgrammingInterface;
import com.dyn.render.gui.skin.SkinSelect;
import com.dyn.render.hud.DynOverlay;
import com.dyn.render.hud.builder.BuildUI;
import com.dyn.render.hud.frozen.Freeze;
import com.dyn.render.manager.NotificationsManager;
import com.dyn.render.player.PlayerModel;
import com.dyn.render.player.PlayerRenderer;
import com.dyn.render.reference.Reference;
import com.dyn.student.StudentUI;
import com.dyn.utils.PlayerLevel;
import com.rabbit.gui.RabbitGui;

import api.player.model.ModelPlayerAPI;
import api.player.render.RenderPlayerAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Client implements Proxy {

	private ProgrammingInterface programInterface = new ProgrammingInterface();

	private boolean showProgrammer = false;
	
	private KeyBinding skinKey;
	private KeyBinding hideGuiKey;
	private KeyBinding achievementKey;
	private KeyBinding scriptKey;
	private KeyBinding buildKey;

	@Override
	public Map<String, ?> getKeyBindings() {
		Map<String, KeyBinding> keys = new HashMap();
		keys.put("achievement", achievementKey);
		keys.put("skin", skinKey);
		keys.put("hide", hideGuiKey);
		keys.put("build", buildKey);
		keys.put("script", scriptKey);
		return keys;
	}

	@Override
	public void init() {
		RenderPlayerAPI.register(Reference.MOD_ID, PlayerRenderer.class);
		ModelPlayerAPI.register(Reference.MOD_ID, PlayerModel.class);
		MinecraftForge.EVENT_BUS.register(this);
		skinKey = new KeyBinding("key.toggle.skinui", Keyboard.KEY_J, "key.categories.toggle");
		hideGuiKey = new KeyBinding("key.toggle.achievementgui", Keyboard.KEY_H, "key.categories.toggle");
		achievementKey = new KeyBinding("key.toggle.hideui", Keyboard.KEY_N, "key.categories.toggle");
		buildKey = new KeyBinding("key.toggle.buildui", Keyboard.KEY_B, "key.categories.toggle");
		scriptKey = new KeyBinding("key.toggle.scriptui", Keyboard.KEY_P, "key.categories.toggle");

		ClientRegistry.registerKeyBinding(achievementKey);
		ClientRegistry.registerKeyBinding(hideGuiKey);
		ClientRegistry.registerKeyBinding(skinKey);
		ClientRegistry.registerKeyBinding(buildKey);
		ClientRegistry.registerKeyBinding(scriptKey);
	}

	// @SubscribeEvent
	// public void renderPass(RenderGameOverlayEvent event) {
	// // need to determine when the game renders an achievement overlay...
	// // we might have to mixin this since it's not actively bussed
	// }

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		// stop unintended command block manipulations
		if ((event.gui instanceof GuiCommandBlock) && !(DYNServerMod.status == PlayerLevel.ADMIN)) {
			event.setCanceled(true);
			return;
		}

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

		if ((DYNServerMod.status == PlayerLevel.ADMIN) && buildKey.isPressed() && !Keyboard.isRepeatEvent()) {
			DYNServerMod.logger.info("Build Gui Opened");
			BuildUI.isOpen = !BuildUI.isOpen;
		}

		if (BuildUI.isOpen) {
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			String type = BuildUI.expandArea ? "expand" : "contract";
			Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
			EnumFacing enumfacing = entity.getHorizontalFacing();
			String dirf = "";
			String dirb = "";
			String dirl = "";
			String dirr = "";
			switch (enumfacing) {
			case NORTH:
				dirf = "n";
				dirb = "s";
				dirl = "w";
				dirr = "e";
				break;
			case SOUTH:
				dirf = "s";
				dirb = "n";
				dirl = "e";
				dirr = "w";
				break;
			case WEST:
				dirf = "e";
				dirb = "w";
				dirl = "n";
				dirr = "s";
				break;
			case EAST:
				dirf = "w";
				dirb = "e";
				dirl = "s";
				dirr = "n";
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				player.sendChatMessage(String.format("//%s %d %s", type, BuildUI.expandSize, dirf));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				player.sendChatMessage(String.format("//%s %d %s", type, BuildUI.expandSize, dirb));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				player.sendChatMessage(String.format("//%s %d %s", type, BuildUI.expandSize, dirl));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				player.sendChatMessage(String.format("//%s %d %s", type, BuildUI.expandSize, dirr));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR)) {
				player.sendChatMessage(String.format("//%s %d u", type, BuildUI.expandSize));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_NEXT)) {
				player.sendChatMessage(String.format("//%s %d d", type, BuildUI.expandSize));
			} else if (Keyboard.isKeyDown(Keyboard.KEY_HOME)) {
				player.sendChatMessage("//pos1");
			} else if (Keyboard.isKeyDown(Keyboard.KEY_END)) {
				player.sendChatMessage("//pos2");
			} else if (Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
				BuildUI.expandArea = !BuildUI.expandArea;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_EQUALS)) {
				BuildUI.expandSize++;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_MINUS)) {
				if (BuildUI.expandSize > 1) {
					BuildUI.expandSize--;
				}
			}
		}

		if (hideGuiKey.isPressed()) {
			DYNServerMod.logger.info("Gui Toggled");
			DynOverlay.isHidden = !DynOverlay.isHidden;
		}
		
		if (scriptKey.isPressed()) {
			DYNServerMod.logger.info("Program Gui Toggled");
			RabbitGui.proxy.display(programInterface);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			showProgrammer = false;
			BuildUI.isOpen = false;
		}
	}

	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		if (Minecraft.getMinecraft().inGameHasFocus || ((RabbitGui.proxy.getCurrentStage() != null)
				&& (RabbitGui.proxy.getCurrentStage().getShow() instanceof ProgrammingInterface))) {
			if (StudentUI.frozen.getFlag()) {
				Freeze.draw();
			}

			BuildUI.draw();

			DynOverlay.draw();
			
			if (Minecraft.getMinecraft().inGameHasFocus && showProgrammer) {
				programInterface.onDraw(0, 0, event.renderTickTime);
			}
		}
		if (!(Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu)) {
			NotificationsManager.renderNotifications();
		}
	}

	

	@Override
	public void renderGUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public void toggleRenderProgramInterface(boolean state) {
		showProgrammer = state;
	}
	

}