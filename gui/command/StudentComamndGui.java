package com.dyn.render.gui.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.dyn.fixins.blocks.cmdblock.StudentCommandBlockLogic;
import com.dyn.server.network.NetworkDispatcher;
import com.dyn.server.network.packets.server.StudentCommandBlockMessage;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StudentComamndGui extends GuiScreen {
	/** Text field containing the command block's command. */
	private GuiTextField commandTextField;
	private GuiTextField previousOutputTextField;
	/** Command block being edited. */
	private final StudentCommandBlockLogic localCommandBlock;
	/** "Done" button for the GUI. */
	private GuiButton doneBtn;
	private GuiButton cancelBtn;
	private GuiButton toggleOutputBtn;
	private boolean trackOutput;

	String[] okCmdList = { "/heal", "/say" };
	List<String> approvedCommands = new ArrayList<String>();

	public StudentComamndGui(StudentCommandBlockLogic logic) {
		localCommandBlock = logic;
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed
	 * for buttons)
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			if (button.id == 1) {
				localCommandBlock.setTrackOutput(trackOutput);
				mc.displayGuiScreen((GuiScreen) null);
			} else if (button.id == 0) {
				String[] split = commandTextField.getText().split("\\s+");
				if (approvedCommands.contains(split[0])) {
					NetworkDispatcher.sendToServer(new StudentCommandBlockMessage(localCommandBlock.shouldTrackOutput(),
							localCommandBlock.getPosition(), commandTextField.getText()));

					if (!localCommandBlock.shouldTrackOutput()) {
						localCommandBlock.setLastOutput((IChatComponent) null);
					}

					mc.displayGuiScreen((GuiScreen) null);
				} else {
					previousOutputTextField.setText("That Command is not approved for use");
				}
			} else if (button.id == 4) {
				localCommandBlock.setTrackOutput(!localCommandBlock.shouldTrackOutput());
				toggleTrackOutputGuiElement();
			}
		}
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY,
	 * renderPartialTicks
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, I18n.format("advMode.setCommand", new Object[0]), width / 2, 20, 16777215);
		drawString(fontRendererObj, I18n.format("advMode.command", new Object[0]), (width / 2) - 150, 37, 10526880);
		commandTextField.drawTextBox();
		int i = 75;
		int j = 0;
		drawString(fontRendererObj, I18n.format("advMode.nearestPlayer", new Object[0]), (width / 2) - 150,
				i + (j++ * fontRendererObj.FONT_HEIGHT), 10526880);
		drawString(fontRendererObj, I18n.format("advMode.randomPlayer", new Object[0]), (width / 2) - 150,
				i + (j++ * fontRendererObj.FONT_HEIGHT), 10526880);
		drawString(fontRendererObj, I18n.format("advMode.allPlayers", new Object[0]), (width / 2) - 150,
				i + (j++ * fontRendererObj.FONT_HEIGHT), 10526880);
		drawString(fontRendererObj, I18n.format("advMode.allEntities", new Object[0]), (width / 2) - 150,
				i + (j++ * fontRendererObj.FONT_HEIGHT), 10526880);
		drawString(fontRendererObj, "", (width / 2) - 150, i + (j++ * fontRendererObj.FONT_HEIGHT), 10526880);

		if (previousOutputTextField.getText().length() > 0) {
			i = i + (j * fontRendererObj.FONT_HEIGHT) + 16;
			drawString(fontRendererObj, I18n.format("advMode.previousOutput", new Object[0]), (width / 2) - 150, i,
					10526880);
			previousOutputTextField.drawTextBox();
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
		for (String s : okCmdList) {
			approvedCommands.add(s);
		}
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonList.add(doneBtn = new GuiButton(0, (width / 2) - 4 - 150, (height / 4) + 120 + 12, 150, 20,
				I18n.format("gui.done", new Object[0])));
		buttonList.add(cancelBtn = new GuiButton(1, (width / 2) + 4, (height / 4) + 120 + 12, 150, 20,
				I18n.format("gui.cancel", new Object[0])));
		buttonList.add(toggleOutputBtn = new GuiButton(4, ((width / 2) + 150) - 20, 150, 20, 20, "O"));
		commandTextField = new GuiTextField(2, fontRendererObj, (width / 2) - 150, 50, 300, 20);
		commandTextField.setMaxStringLength(32767);
		commandTextField.setFocused(true);
		commandTextField.setText(localCommandBlock.getCommand());
		previousOutputTextField = new GuiTextField(3, fontRendererObj, (width / 2) - 150, 150, 276, 20);
		previousOutputTextField.setMaxStringLength(32767);
		previousOutputTextField.setEnabled(false);
		previousOutputTextField.setText("-");
		trackOutput = localCommandBlock.shouldTrackOutput();
		toggleTrackOutputGuiElement();
		doneBtn.enabled = commandTextField.getText().trim().length() > 0;
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is
	 * the equivalent of KeyListener.keyTyped(KeyEvent e). Args : character
	 * (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		commandTextField.textboxKeyTyped(typedChar, keyCode);
		previousOutputTextField.textboxKeyTyped(typedChar, keyCode);
		doneBtn.enabled = commandTextField.getText().trim().length() > 0;

		if ((keyCode != 28) && (keyCode != 156)) {
			if (keyCode == 1) {
				actionPerformed(cancelBtn);
			}
		} else {
			actionPerformed(doneBtn);
		}
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		commandTextField.mouseClicked(mouseX, mouseY, mouseButton);
		previousOutputTextField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat
	 * events
	 */
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	private void toggleTrackOutputGuiElement() {
		if (localCommandBlock.shouldTrackOutput()) {
			toggleOutputBtn.displayString = "O";

			if (localCommandBlock.getLastOutput() != null) {
				previousOutputTextField.setText(localCommandBlock.getLastOutput().getUnformattedText());
			}
		} else {
			toggleOutputBtn.displayString = "X";
			previousOutputTextField.setText("-");
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		commandTextField.updateCursorCounter();
	}
}