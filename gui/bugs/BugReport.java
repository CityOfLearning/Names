package com.dyn.render.gui.bugs;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dyn.DYNServerMod;
import com.google.gson.JsonObject;
import com.rabbit.gui.background.DefaultBackground;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.control.MultiTextbox;
import com.rabbit.gui.component.control.TextBox;
import com.rabbit.gui.component.display.TextLabel;
import com.rabbit.gui.render.TextAlignment;
import com.rabbit.gui.show.Show;

import net.minecraft.client.Minecraft;

public class BugReport extends Show {

	private static File getTimestampedFileForDirectory() {
		File file = new File(Minecraft.getMinecraft().mcDataDir, "/logs/");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
		String s = "bug_report-" + dateFormat.format(new Date()).toString();
		int i = 1;

		while (true) {
			File file1 = new File(file, s + (i == 1 ? "" : "_" + i) + ".json");

			if (!file1.exists()) {
				return file1;
			}

			++i;
		}
	}

	private TextBox requestTitle;

	private MultiTextbox bugDesc;

	private MultiTextbox fix;

	public BugReport() {
		setBackground(new DefaultBackground());
		title = "Bug Reporter";
	}

	@Override
	public void setup() {

		registerComponent(
				new TextLabel(width / 3, (int) (height * .1), width / 3, 20, "Bug Reporter", TextAlignment.CENTER));
		registerComponent(new TextLabel(width / 4, (int) (height * .15), width / 2, 20,
				"What is the bug/issue you encountered?", TextAlignment.CENTER));

		registerComponent(requestTitle = new TextBox(width / 6, (int) (height * .25), (int) (width * .66), 20,
				"Short Description"));

		registerComponent(bugDesc = new MultiTextbox(width / 6, (int) (height * .35), (int) (width * .66),
				(int) (height * .25), "How did it happen?"));

		registerComponent(fix = new MultiTextbox(width / 6, (int) (height * .63), (int) (width * .66),
				(int) (height * .15), "Possible fix?"));

		// GUI main section
		registerComponent(new Button((int) (width * .6), (int) (height * .84), 150, 20, "Send Bug Report")
				.setClickListener(but -> {
					if (!requestTitle.getText().isEmpty() && !requestTitle.getText().equals("Short Description")
							&& !bugDesc.getText().isEmpty() && !bugDesc.getText().equals("How did it happen?")) {
						JsonObject bug = new JsonObject();
						bug.addProperty("location", Minecraft.getMinecraft().thePlayer.getPosition().toString());
						bug.addProperty("desc", requestTitle.getText());
						bug.addProperty("explain", bugDesc.getText());
						bug.addProperty("fix", fix.getText().isEmpty() ? "None" : fix.getText());
						try {
							FileWriter fileWrite = new FileWriter(getTimestampedFileForDirectory());

							fileWrite.write(bug.toString());
							fileWrite.close();
							stage.close();
						} catch (Exception e) {
							DYNServerMod.logger.error("Failed Writing File", e);
						}
					}
				}));
	}
}
