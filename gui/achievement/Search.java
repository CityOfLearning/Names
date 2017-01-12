package com.dyn.render.gui.achievement;

import java.util.ArrayList;
import java.util.List;

import com.dyn.DYNServerConstants;
import com.dyn.achievements.achievement.AchievementPlus;
import com.dyn.achievements.handlers.AchievementManager;
import com.dyn.betterachievements.gui.GuiBetterAchievements;
import com.rabbit.gui.background.DefaultBackground;
import com.rabbit.gui.component.control.PictureButton;
import com.rabbit.gui.component.control.TextBox;
import com.rabbit.gui.component.display.Picture;
import com.rabbit.gui.component.display.TextLabel;
import com.rabbit.gui.component.grid.Grid;
import com.rabbit.gui.component.grid.ScrollableGrid;
import com.rabbit.gui.component.grid.entries.GridEntry;
import com.rabbit.gui.component.grid.entries.PictureButtonGridEntry;
import com.rabbit.gui.render.TextAlignment;
import com.rabbit.gui.show.Show;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Search extends Show {

	private ScrollableGrid achievementGrid;

	public Search() {
		setBackground(new DefaultBackground());
		title = "Achievement Gui";
	}

	@Override
	public void setup() {
		super.setup();

		registerComponent(
				new TextLabel(width / 3, (int) (height * .1), width / 3, 20, "Achievements", TextAlignment.CENTER));

		// the side buttons
		registerComponent(new PictureButton((int) (width * .03), (int) (height * .2), 30, 30,
				DYNServerConstants.ACHIEVEMENT_IMAGE).setIsEnabled(true).addHoverText("Achievement Maps")
						.setDoesDrawHoverText(true).setClickListener(but -> Minecraft.getMinecraft()
								.displayGuiScreen(new GuiBetterAchievements(getStage(), 0))));

		List<GridEntry> entries = new ArrayList<>();

		for (AchievementPlus a : AchievementManager.getAllAchievements()) {
			List<String> hoverText = new ArrayList<>();
			hoverText.add(a.getName());
			hoverText.add(a.getDescription());
			if (a.getTextureId() == null) {
				entries.add(new PictureButtonGridEntry(25, 25,
						new ResourceLocation("minecraft", "textures/items/experience_bottle.png"))
								.setDoesDrawHoverText(true).setHoverText(hoverText)
								.setClickListener((PictureButtonGridEntry entry, Grid grid, int mouseX,
										int mouseY) -> getStage().display(new Info(a))));
			} else {
				entries.add(new PictureButtonGridEntry(25, 25, a.getTextureId()).setDoesDrawHoverText(true)
						.setHoverText(hoverText).setClickListener((PictureButtonGridEntry entry, Grid grid, int mouseX,
								int mouseY) -> getStage().display(new Info(a))));
			}

		}

		achievementGrid = new ScrollableGrid((int) (width / 5.8), (int) (height * .25), (int) (width * .65),
				(int) (height * .62), 45, 45, entries);
		achievementGrid.setVisibleBackground(false);
		registerComponent(achievementGrid);

		registerComponent(new TextBox((int) (width * .2), (int) (height * .15), width / 2, 20, "Search for Achievement")
				.setId("achsearch")
				.setTextChangedListener((TextBox textbox, String previousText) -> textChanged(textbox, previousText)));
		// The background
		registerComponent(new Picture(width / 8, (int) (height * .05), (int) (width * (6.0 / 8.0)), (int) (height * .9),
				new ResourceLocation("dyn", "textures/gui/background3.png")));
	}

	private void textChanged(TextBox textbox, String previousText) {
		if (textbox.getId() == "achsearch") {
			achievementGrid.clear();
			if (textbox.getText().isEmpty()) {
				for (AchievementPlus a : AchievementManager.getAllAchievements()) {
					List<String> hoverText = new ArrayList<>();
					hoverText.add(a.getName());
					hoverText.add(a.getDescription());
					if (a.getTextureId() == null) {
						achievementGrid.add(new PictureButtonGridEntry(25, 25,
								new ResourceLocation("minecraft", "textures/items/experience_bottle.png"))
										.setDoesDrawHoverText(true).setHoverText(hoverText)
										.setClickListener((PictureButtonGridEntry entry, Grid grid, int mouseX,
												int mouseY) -> getStage().display(new Info(a))));
					} else {
						achievementGrid
								.add(new PictureButtonGridEntry(25, 25, a.getTextureId()).setDoesDrawHoverText(true)
										.setHoverText(hoverText).setClickListener((PictureButtonGridEntry entry,
												Grid grid, int mouseX, int mouseY) -> getStage().display(new Info(a))));
					}

				}
			} else {
				for (AchievementPlus a : AchievementManager.findAchievementsByName(textbox.getText())) {
					List<String> hoverText = new ArrayList<>();
					hoverText.add(a.getName());
					hoverText.add(a.getDescription());
					if (a.getTextureId() == null) {
						achievementGrid.add(new PictureButtonGridEntry(25, 25,
								new ResourceLocation("minecraft", "textures/items/experience_bottle.png"))
										.setDoesDrawHoverText(true).setHoverText(hoverText)
										.setClickListener((PictureButtonGridEntry entry, Grid grid, int mouseX,
												int mouseY) -> getStage().display(new Info(a))));
					} else {
						achievementGrid
								.add(new PictureButtonGridEntry(25, 25, a.getTextureId()).setDoesDrawHoverText(true)
										.setHoverText(hoverText).setClickListener((PictureButtonGridEntry entry,
												Grid grid, int mouseX, int mouseY) -> getStage().display(new Info(a))));
					}
				}
			}
		}
	}
}
