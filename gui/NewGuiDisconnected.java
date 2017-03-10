package com.dyn.render.gui;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NewGuiDisconnected extends GuiScreen {
	private String reason;
	private List<String> multilineMessage;
	private int field_175353_i;

	public NewGuiDisconnected() {
		reason = "Failed to connect or was disconnected from server";
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed
	 * for buttons)
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			mc.displayGuiScreen(new NewMainMenu());
		}
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY,
	 * renderPartialTicks
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, reason, width / 2,
				(height / 2) - (field_175353_i / 2) - (fontRendererObj.FONT_HEIGHT * 2), 11184810);
		int i = (height / 2) - (field_175353_i / 2);

		if (multilineMessage != null) {
			for (String s : multilineMessage) {
				drawCenteredString(fontRendererObj, s, width / 2, i, 16777215);
				i += fontRendererObj.FONT_HEIGHT;
			}
		}

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
		multilineMessage = fontRendererObj.listFormattedStringToWidth(reason, width - 50);
		field_175353_i = multilineMessage.size() * fontRendererObj.FONT_HEIGHT;
		buttonList.add(new GuiButton(0, (width / 2) - 100,
				(height / 2) + (field_175353_i / 2) + fontRendererObj.FONT_HEIGHT, "Return to Main Menu"));
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is
	 * the equivalent of KeyListener.keyTyped(KeyEvent e). Args : character
	 * (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
	}
}