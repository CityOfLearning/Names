package com.dyn.render.gui.dialog;

import java.awt.Color;

import com.dyn.fixins.blocks.dialog.DialogBlockTileEntity;
import com.dyn.server.network.NetworkDispatcher;
import com.dyn.server.network.messages.MessageDialogUpdate;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.control.MultiTextbox;
import com.rabbit.gui.component.control.TextBox;
import com.rabbit.gui.component.display.Picture;
import com.rabbit.gui.component.display.TextLabel;
import com.rabbit.gui.show.Show;

import net.minecraft.util.ResourceLocation;

public class EditDialogBlock extends Show {

	DialogBlockTileEntity block;
	int blockX;
	int blockY;
	int blockZ;
	String text;

	public EditDialogBlock(DialogBlockTileEntity block) {
		title = "Edit Dialog Block";
		this.block = block;
		blockX = block.getXRadius();
		blockY = block.getYRadius();
		blockZ = block.getZRadius();
		text = block.getText();
	}

	@Override
	public void setup() {

		registerComponent(new TextLabel((int) (width * .2), (int) (height * .15), 100, 15, Color.black,
				"Set Block Area:\n\nX:      Y:      Z:").setMultilined(true));

		registerComponent(new TextBox((int) (width * .225), (int) (height * .2), 45, 20).setText("" + blockX)
				.setTextChangedListener((TextBox textbox, String previousText) -> {
					if (!previousText.isEmpty()) {
						try {
							blockX = Integer.parseInt(previousText);
						} catch (Exception e) {
							textbox.setText("" + block.getXRadius());
						}
					}
				}));

		registerComponent(new TextBox((int) (width * .375), (int) (height * .2), 45, 20).setText("" + blockY)
				.setTextChangedListener((TextBox textbox, String previousText) -> {
					if (!previousText.isEmpty()) {
						try {
							blockY = Integer.parseInt(previousText);
						} catch (Exception e) {
							textbox.setText("" + block.getYRadius());
						}
					}
				}));

		registerComponent(new TextBox((int) (width * .525), (int) (height * .2), 45, 20).setText("" + blockZ)
				.setTextChangedListener((TextBox textbox, String previousText) -> {
					if (!previousText.isEmpty()) {
						try {
							blockZ = Integer.parseInt(previousText);
						} catch (Exception e) {
							textbox.setText("" + block.getZRadius());
						}
					}
				}));

		registerComponent(
				new TextLabel((int) (width * .2), (int) (height * .3), 100, 15, Color.black, "Set Block Dialog:"));

		registerComponent(new MultiTextbox((int) (width * .2), (int) (height * .35), width / 2, (int) (height * .4))
				.setText(text).setTextChangedListener((TextBox textbox, String previousText) -> {
					text = previousText;
				}));

		registerComponent(new Button((int) (width * .55), (int) (height * .8), 120, 20, "Update Dialog Block")
				.setClickListener(btn -> {
					NetworkDispatcher
							.sendToServer(new MessageDialogUpdate(block.getPos(), text, blockX, blockY, blockZ));
					getStage().close();
				}));
		// The background
		registerComponent(new Picture(width / 8, (int) (height * .05), (int) (width * (6.0 / 8.0)), (int) (height * .9),
				new ResourceLocation("dyn", "textures/gui/background.png")));
	}
}
