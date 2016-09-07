package com.dyn.render.gui.turtle;

import com.dyn.render.RenderMod;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.control.MultiTextbox;
import com.rabbit.gui.component.control.PictureButton;
import com.rabbit.gui.component.control.TextBox;
import com.rabbit.gui.component.display.Panel;
import com.rabbit.gui.component.display.Picture;
import com.rabbit.gui.show.Show;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ProgrammingInterface extends Show {

	private String termText;

	public ProgrammingInterface() {
		title = "Turtle Programmer";
		termText = "Welcome to the progamming interface!";
	}

	@Override
	public void setup() {
		super.setup();

		Panel panel = new Panel((int) (width * .6), 0, (int) (width * .4), height);

		registerComponent(panel);
		// The Panel background
		panel.registerComponent(new Picture(0, 0, (int) (panel.getWidth()), (int) (panel.getHeight()),
				new ResourceLocation("dyn", "textures/gui/background2.png")));

		// this works as far as persistence is concerned
		panel.registerComponent(new MultiTextbox(10, 15, panel.getWidth() - 20, panel.getHeight() - 25)
				.setText(termText).setBackgroundVisibility(false).setDrawUnicode(true)
				.setTextChangedListener((TextBox textbox, String previousText) -> {
					termText = previousText;
				}));

//		panel.registerComponent(
//				new TerminalInterface(10, 15, panel.getWidth() - 20, panel.getHeight() - 30, new IComputerContainer() {
//					public IComputer getComputer() {
//						return m_computer;
//					}
//				}));

		panel.registerComponent(new PictureButton(panel.getWidth() - 15, 0, 15, 15,
				new ResourceLocation("dyn", "textures/gui/exit.png")).setClickListener(btn -> {
					RenderMod.proxy.toggleRenderProgramInterface(false);
					Minecraft.getMinecraft().setIngameFocus();
				}));

		panel.registerComponent(new Button(0, 0, 45, 15, "<<Game").setClickListener(btn -> {
			RenderMod.proxy.toggleRenderProgramInterface(true);
			Minecraft.getMinecraft().setIngameFocus();
		}));
	}

}
