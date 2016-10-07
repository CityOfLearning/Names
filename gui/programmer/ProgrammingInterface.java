package com.dyn.render.gui.programmer;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.dyn.DYNServerMod;
import com.dyn.render.RenderMod;
import com.dyn.utils.FileUtils;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.control.MultiTextbox;
import com.rabbit.gui.component.control.PictureButton;
import com.rabbit.gui.component.control.TextBox;
import com.rabbit.gui.component.display.Panel;
import com.rabbit.gui.component.display.Picture;
import com.rabbit.gui.component.display.TextLabel;
import com.rabbit.gui.show.Show;

import mobi.omegacentauri.raspberryjammod.process.RJMProcessCode;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ProgrammingInterface extends Show {

	private String termText;
	private String fileName;

	public ProgrammingInterface() {
		title = "Python Programming Interface";
		termText = "#Welcome to the progamming interface!";
	}

	@Override
	public void setup() {
		super.setup();

		Panel savePanel = new Panel((int) (width * .33), (int) (height * .33), (int) (width * .45),
				(int) (height * .33)).setVisible(false);

		registerComponent(savePanel);

		savePanel.registerComponent(new Picture(0, 0, (savePanel.getWidth()), (savePanel.getHeight()),
				new ResourceLocation("dyn", "textures/gui/background.png")));

		savePanel.registerComponent(new TextLabel((int) (savePanel.getWidth() * .1), (int) (savePanel.getHeight() * .1),
				(int) (savePanel.getWidth() * .8), 15, "Save File As:"));

		savePanel.registerComponent(new TextBox((int) (savePanel.getWidth() * .1), (int) (savePanel.getHeight() * .25),
				(int) (savePanel.getWidth() * .8), 15, "File Name")
						.setTextChangedListener((TextBox textbox, String previousText) -> {
							fileName = previousText;
						}));

		savePanel
				.registerComponent(new Button(savePanel.getWidth() / 3 - 23, savePanel.getHeight() - 35, 45, 15, "Save")
						.setClickListener(btn -> {
							if (fileName != null && !fileName.isEmpty()) {
								try {
									FileUtils.writeFile(new File(DYNServerMod.scriptsLoc, fileName + ".py"), termText);
								} catch (Exception e) {
									DYNServerMod.logger.error("Could not create script file", e);
								}
							}
							fileName = "";
							savePanel.setVisible(false);
						}));

		savePanel.registerComponent(
				new Button(savePanel.getWidth() / 3 * 2 - 23, savePanel.getHeight() - 35, 45, 15, "Cancel")
						.setClickListener(btn -> {
							fileName = "";
							savePanel.setVisible(false);
						}));

		Panel panel = new Panel((int) (width * .55), 0, (int) (width * .45), height);

		registerComponent(panel);
		// The Panel background
		panel.registerComponent(new Picture(0, 0, (panel.getWidth()), (panel.getHeight()),
				new ResourceLocation("dyn", "textures/gui/background2.png")));

		panel.registerComponent(new MultiTextbox(10, 15, panel.getWidth() - 20, panel.getHeight() - 35)
				.setText(termText).setBackgroundVisibility(false).setDrawUnicode(true)
				.setTextChangedListener((TextBox textbox, String previousText) -> {
					termText = previousText;
				}));

		panel.registerComponent(new PictureButton(panel.getWidth() - 15, 0, 15, 15,
				new ResourceLocation("dyn", "textures/gui/exit.png")).setClickListener(btn -> {
					RenderMod.proxy.toggleRenderProgramInterface(false);
					Minecraft.getMinecraft().setIngameFocus();
				}));

		panel.registerComponent(new Button(0, 0, 45, 15, "<<Game").setClickListener(btn -> {
			RenderMod.proxy.toggleRenderProgramInterface(true);
			Minecraft.getMinecraft().setIngameFocus();
		}));

		panel.registerComponent(
				new Button(panel.getWidth() - 50, panel.getHeight() - 20, 45, 15, "Run").setClickListener(btn -> {
					RJMProcessCode.run(Arrays.asList(termText.split(Pattern.quote("\n"))), Minecraft.getMinecraft().thePlayer);
				}));

		panel.registerComponent(new Button(panel.getWidth() / 2 - 23, panel.getHeight() - 20, 45, 15, "Compile")
				.setClickListener(btn -> {

				}));

		panel.registerComponent(new Button(5, panel.getHeight() - 20, 45, 15, "Save").setClickListener(btn -> {
			savePanel.setVisible(true);
		}));
	}

}
