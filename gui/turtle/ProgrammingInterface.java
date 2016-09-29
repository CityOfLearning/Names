package com.dyn.render.gui.turtle;

import com.dyn.render.RenderMod;
import com.dyn.render.gui.widgets.TerminalInterface;
//import com.dyn.robot.api.IDYNRobotAccess;
//import com.dyn.robot.entity.EntityRobot;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.control.MultiTextbox;
import com.rabbit.gui.component.control.PictureButton;
import com.rabbit.gui.component.control.TextBox;
import com.rabbit.gui.component.display.Panel;
import com.rabbit.gui.component.display.Picture;
import com.rabbit.gui.show.Show;

import dan200.computercraft.shared.computer.core.IComputer;
import dan200.computercraft.shared.computer.core.IComputerContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ProgrammingInterface extends Show {

	private String termText;
//	protected final IDYNRobotAccess m_robot;
	protected final IComputer m_computer;
	
	public ProgrammingInterface() {
		title = "Turtle Programmer";
		termText = "Welcome to the progamming interface!";
		
//		m_robot = null;
		m_computer = null;
	}
	
//	public ProgrammingInterface(EntityRobot robot) {
//		title = "Turtle Programmer";
//		termText = "Welcome to the progamming interface!";
//		
//		m_robot = (IDYNRobotAccess) robot.getAccess();
//		m_computer = robot.getComputer();
//
//		if (!m_computer.isOn()) {
//			System.out.println("Turning Computer On");
//			m_computer.turnOn();
//		}
//	}

	@Override
	public void setup() {
		super.setup();

		Panel panel = new Panel((int) (width * .55), 0, (int) (width * .45), height);

		registerComponent(panel);
		// The Panel background
		panel.registerComponent(new Picture(0, 0, (panel.getWidth()), (panel.getHeight()),
				new ResourceLocation("dyn", "textures/gui/background2.png")));

		// this works as far as persistence is concerned
//		panel.registerComponent(new MultiTextbox(10, 15, panel.getWidth() - 20, panel.getHeight() - 25)
//				.setText(termText).setBackgroundVisibility(false).setDrawUnicode(true)
//				.setTextChangedListener((TextBox textbox, String previousText) -> {
//					termText = previousText;
//				}));

//		panel.registerComponent(
//				new TerminalInterface(10, 15, panel.getWidth() - 20, panel.getHeight() - 30, m_computer));

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
	
//	public IDYNRobotAccess getRobot(){
//		return m_robot;
//	}

}
