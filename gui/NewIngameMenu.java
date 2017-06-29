package com.dyn.render.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NewIngameMenu extends GuiScreen {

	GuiIngameMenu guiIngameMenu;

	public NewIngameMenu(GuiIngameMenu guiIngameMenu) {
		this.guiIngameMenu = guiIngameMenu;
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed
	 * for buttons)
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 0:
			mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
			break;
		case 1:
			boolean flag = mc.isIntegratedServerRunning();
			boolean flag1 = mc.isConnectedToRealms();
			button.enabled = false;
			mc.theWorld.sendQuittingDisconnectingPacket();
			mc.loadWorld((WorldClient) null);

			if (flag) {
				mc.displayGuiScreen(new NewMainMenu());
			} else if (flag1) {
				RealmsBridge realmsbridge = new RealmsBridge();
				realmsbridge.switchToRealms(new NewMainMenu());
			} else {
				mc.displayGuiScreen(new NewMainMenu());
			}

		case 2:
		case 3:
		default:
			break;
		case 4:
			mc.displayGuiScreen((GuiScreen) null);
			mc.setIngameFocus();
			break;
		case 5:
			if (mc.thePlayer != null) {
				mc.displayGuiScreen(new GuiAchievements(this, mc.thePlayer.getStatFileWriter()));
			}
			break;
		case 6:
			if (mc.thePlayer != null) {
				mc.displayGuiScreen(new GuiStats(this, mc.thePlayer.getStatFileWriter()));
			}
			break;
		case 7:
			mc.displayGuiScreen(new GuiShareToLan(this));
			break;
		case 12:
			net.minecraftforge.fml.client.FMLClientHandler.instance().showInGameModOptions(guiIngameMenu);
			break;
		}
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY,
	 * renderPartialTicks
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, I18n.format("menu.game", new Object[0]), width / 2, 40, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called
	 * when the GUI is displayed and when the window resizes, the buttonList is
	 * cleared beforehand.
	 */
	@Override
	public void initGui() {
		buttonList.clear();
		int i = -16;
		buttonList.add(new GuiButton(1, (width / 2) - 100, (height / 4) + 120 + i,
				I18n.format("menu.returnToMenu", new Object[0])));

		if (!mc.isIntegratedServerRunning()) {
			buttonList.get(0).displayString = I18n.format("menu.disconnect", new Object[0]);
		}

		buttonList.add(new GuiButton(4, (width / 2) - 100, (height / 4) + 24 + i,
				I18n.format("menu.returnToGame", new Object[0])));
		buttonList.add(new GuiButton(0, (width / 2) - 100, (height / 4) + 96 + i, 98, 20,
				I18n.format("menu.options", new Object[0])));
		buttonList.add(
				new GuiButton(12, (width / 2) + 2, (height / 4) + 96 + i, 98, 20, I18n.format("fml.menu.modoptions")));
		GuiButton guibutton;
		buttonList.add(guibutton = new GuiButton(7, (width / 2) - 100, (height / 4) + 72 + i, 200, 20,
				I18n.format("menu.shareToLan", new Object[0])));
		buttonList.add(new GuiButton(5, (width / 2) - 100, (height / 4) + 48 + i, 98, 20,
				I18n.format("gui.achievements", new Object[0])));
		buttonList.add(new GuiButton(6, (width / 2) + 2, (height / 4) + 48 + i, 98, 20,
				I18n.format("gui.stats", new Object[0])));
		guibutton.enabled = mc.isSingleplayer() && !mc.getIntegratedServer().getPublic();
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();
	}
}