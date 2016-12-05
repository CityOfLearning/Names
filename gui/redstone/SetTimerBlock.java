package com.dyn.render.gui.redstone;

import java.awt.Color;

import com.dyn.fixins.blocks.redstone.timer.TimerBlockTileEntity;
import com.dyn.server.network.NetworkManager;
import com.dyn.server.network.messages.MessageTimerBlockUpdate;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.control.TextBox;
import com.rabbit.gui.component.display.Picture;
import com.rabbit.gui.component.display.TextLabel;
import com.rabbit.gui.show.Show;

import net.minecraft.util.ResourceLocation;

public class SetTimerBlock extends Show {

	TimerBlockTileEntity block;
	int time;

	public SetTimerBlock(TimerBlockTileEntity block) {
		title = "Set Timer Block Timer";
		this.block = block;
		time = block.getTimer();
	}

	@Override
	public void setup() {

		registerComponent(new TextLabel((int) (width * .3), (int) (height * .35), (int) (width * .4), 15, Color.black,
				"Set Block Timer: (10-12000)"));

		registerComponent(new TextBox((int) (width * .3), (int) (height * .4), 120, 20).setText("" + block.getTimer())
				.setTextChangedListener((TextBox textbox, String previousText) -> {
					if (!previousText.isEmpty()) {
						try {
							time = Math.min(12000, Math.max(10, Integer.parseInt(previousText)));
							if (time != Integer.parseInt(previousText)) {
								textbox.setText("" + time);
							}
						} catch (Exception e) {
							textbox.setText("" + time);
						}
					}
				}));

		registerComponent(new Button((int) (width * .45), (int) (height * .5), 100, 20, "Update Timer Block")
				.setClickListener(btn -> {
					NetworkManager.sendToServer(new MessageTimerBlockUpdate(block.getPos(), time));
					getStage().close();
				}));
		// The background
		registerComponent(new Picture((int) (width * .25), (int) (height * .3), (int) (width * .5), (int) (height * .4),
				new ResourceLocation("dyn", "textures/gui/background.png")));
	}
}
