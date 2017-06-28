package com.dyn.render.proxy;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.dyn.DYNServerMod;
import com.dyn.betterachievements.gui.GuiBetterAchievements;
import com.dyn.betterachievements.handler.GuiOpenHandler;
import com.dyn.fixins.blocks.decision.DecisionBlockTileEntity;
import com.dyn.fixins.blocks.dialog.DialogBlockTileEntity;
import com.dyn.fixins.blocks.redstone.proximity.ProximityBlockTileEntity;
import com.dyn.fixins.blocks.redstone.timer.TimerBlockTileEntity;
import com.dyn.render.RenderMod;
import com.dyn.render.gui.NewGuiDisconnected;
import com.dyn.render.gui.NewMainMenu;
import com.dyn.render.gui.achievement.Search;
import com.dyn.render.gui.bugs.BugReport;
import com.dyn.render.gui.decision.EditDecisionBlock;
import com.dyn.render.gui.dialog.EditDialogBlock;
import com.dyn.render.gui.plots.PlotBuySell;
import com.dyn.render.gui.programmer.ProgrammingInterface;
import com.dyn.render.gui.redstone.SetProximityBlock;
import com.dyn.render.gui.redstone.SetTimerBlock;
import com.dyn.render.gui.skin.SkinSelect;
import com.dyn.render.hud.DynOverlay;
import com.dyn.render.hud.builder.BuildUI;
import com.dyn.render.hud.decision.DecisionHud;
import com.dyn.render.hud.dialog.DialogHud;
import com.dyn.render.hud.frozen.Freeze;
import com.dyn.render.hud.path.EntityPathRenderer;
import com.dyn.render.manager.NotificationsManager;
import com.dyn.render.player.PlayerModel;
import com.dyn.render.player.PlayerRenderer;
import com.dyn.render.reference.Reference;
import com.dyn.server.network.NetworkManager;
import com.dyn.server.network.packets.server.RequestWorldZonesMessage;
import com.dyn.student.StudentUI;
import com.dyn.utils.PlayerAccessLevel;
import com.rabbit.gui.RabbitGui;

import api.player.model.ModelPlayerAPI;
import api.player.render.RenderPlayerAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Client implements Proxy {

	private ProgrammingInterface programInterface;
	private DialogHud dialog;

	private boolean showProgrammer = false;
	private boolean showDialog = false;
	private boolean hasShown = false;

	private KeyBinding skinKey;
	private KeyBinding hideGuiKey;
	private KeyBinding achievementKey;
	private KeyBinding buildKey;
	private KeyBinding plotKey;
	private KeyBinding bugKey;
	private int dialogDuration = 0;

	@Override
	public Map<String, ?> getKeyBindings() {
		Map<String, KeyBinding> keys = new HashMap();
		keys.put("achievement", achievementKey);
		keys.put("skin", skinKey);
		keys.put("hide", hideGuiKey);
		keys.put("build", buildKey);
		keys.put("plots", plotKey);
		keys.put("bugs", bugKey);
		return keys;
	}

	// @SubscribeEvent
	// public void renderPass(RenderGameOverlayEvent event) {
	// // need to determine when the game renders an achievement overlay...
	// // we might have to mixin this since it's not actively bussed
	// }

	@Override
	public void handleErrorMessage(String error, String code, int line) {
		if (showProgrammer) {
			programInterface.handleErrorMessage(error, code, line);
		}
	}

	@Override
	public void init() {
		programInterface = new ProgrammingInterface();
		dialog = new DialogHud();
		RenderPlayerAPI.register(Reference.MOD_ID, PlayerRenderer.class);
		ModelPlayerAPI.register(Reference.MOD_ID, PlayerModel.class);
		MinecraftForge.EVENT_BUS.register(this);
		skinKey = new KeyBinding("key.toggle.skinui", Keyboard.KEY_J, "key.categories.toggle");
		hideGuiKey = new KeyBinding("key.toggle.achievementgui", Keyboard.KEY_H, "key.categories.toggle");
		achievementKey = new KeyBinding("key.toggle.hideui", Keyboard.KEY_N, "key.categories.toggle");
		buildKey = new KeyBinding("key.toggle.buildui", Keyboard.KEY_COMMA, "key.categories.toggle");
		plotKey = new KeyBinding("key.toggle.plotui", Keyboard.KEY_PERIOD, "key.categories.toggle");
		bugKey = new KeyBinding("key.toggle.bugui", Keyboard.KEY_B, "key.categories.toggle");

		ClientRegistry.registerKeyBinding(achievementKey);
		ClientRegistry.registerKeyBinding(hideGuiKey);
		ClientRegistry.registerKeyBinding(skinKey);
		ClientRegistry.registerKeyBinding(buildKey);
		ClientRegistry.registerKeyBinding(plotKey);
		ClientRegistry.registerKeyBinding(bugKey);
	}

	@Override
	public boolean isDialogInterfaceOpen() {
		return showDialog;
	}

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		// stop unintended command block manipulations

		if ((event.gui instanceof GuiMainMenu)) {
			event.setCanceled(true);
			Minecraft.getMinecraft().displayGuiScreen(new NewMainMenu());
		}

		if ((event.gui instanceof GuiDisconnected)) {
			event.setCanceled(true);
			Minecraft.getMinecraft().displayGuiScreen(new NewGuiDisconnected());
		}

		if ((event.gui instanceof GuiCommandBlock) && !(DYNServerMod.accessLevel == PlayerAccessLevel.ADMIN)) {
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

		if ((DYNServerMod.accessLevel == PlayerAccessLevel.ADMIN) && buildKey.isPressed()
				&& !Keyboard.isRepeatEvent()) {
			DYNServerMod.logger.info("Build Gui Opened");
			if (!BuildUI.isOpen) {
				NetworkManager
						.sendToServer(new RequestWorldZonesMessage(Minecraft.getMinecraft().thePlayer.dimension, true));
			}
			BuildUI.isOpen = !BuildUI.isOpen;
		}

		if (plotKey.isPressed()) {
			RabbitGui.proxy.display(new PlotBuySell());
		}

		if (bugKey.isPressed()) {
			RabbitGui.proxy.display(new BugReport());
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

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			showProgrammer = false;
			BuildUI.isOpen = false;
		}
	}

	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		if (Minecraft.getMinecraft().inGameHasFocus) {
			if (StudentUI.frozen.getFlag()) {
				Freeze.draw();
			}

			BuildUI.draw();

			DynOverlay.draw();

			if (showDialog) {
				if (dialogDuration <= 0) {
					dialogDuration = 0;
					showDialog = false;
				} else {
					dialog.onUpdate();
					dialog.onDraw(0, 0, event.renderTickTime);
					dialogDuration--;
				}
			}

			if (showProgrammer) {
				programInterface.onDraw(0, 0, event.renderTickTime);
			}
		}
		if (!(Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu)) {
			NotificationsManager.renderNotifications();
		}
	}

	@Override
	public void openDecisionGui(EntityLivingBase entity, DecisionBlockTileEntity decisionBlockTileEntity) {
		RabbitGui.proxy.display(new DecisionHud(entity, decisionBlockTileEntity));
	}

	@Override
	public void openEditDecisionBlock(DecisionBlockTileEntity decisionBlock) {
		RabbitGui.proxy.display(new EditDecisionBlock(decisionBlock));
	}

	@Override
	public void openEditDialogInterface(DialogBlockTileEntity block) {
		RabbitGui.proxy.display(new EditDialogBlock(block));
	}

	@Override
	public void openSetProximityInterface(ProximityBlockTileEntity block) {
		RabbitGui.proxy.display(new SetProximityBlock(block));
	}

	@Override
	public void openSetTimerInterface(TimerBlockTileEntity block) {
		RabbitGui.proxy.display(new SetTimerBlock(block));
	}

	@SubscribeEvent
	public void render(RenderWorldLastEvent event) {
		EntityPathRenderer.renderEntityPaths();

		if (BuildUI.isOpen) {
			for (String key : RenderMod.zoneAreas.keySet()) {
				BuildUI.drawZone(RenderMod.zoneAreas.get(key), key, event.partialTicks);
			}
		}
	}

	@Override
	public void setPlayerSkinTextureName(EntityPlayer player, String texture) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toggleDialogHud(EntityLivingBase entity, boolean state, String text, int duration, boolean interupt) {
		if (interupt) {
			if (entity == null) {
				RabbitGui.proxy.display(new DialogHud(text, true));
			} else {
				RabbitGui.proxy.display(new DialogHud(entity, text, true));
			}
		} else {
			showDialog = state;
			if (state) {
				if (!hasShown) {
					if (entity == null) {
						RabbitGui.proxy.display(dialog = new DialogHud());
					} else {
						RabbitGui.proxy.display(dialog = new DialogHud(entity, false));
					}
					RabbitGui.proxy.getCurrentStage().close();
					hasShown = true;
				}
				dialog.setRenderText(text);
				dialog.setEntity(entity);
				dialogDuration = duration;
			}
		}

	}

	@Override
	public void toggleRenderProgramInterface(boolean state) {
		showProgrammer = state;
	}
}