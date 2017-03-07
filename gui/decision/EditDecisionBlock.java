package com.dyn.render.gui.decision;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.dyn.fixins.blocks.decision.DecisionBlockTileEntity;
import com.dyn.fixins.blocks.decision.DecisionBlockTileEntity.Choice;
import com.dyn.server.network.NetworkManager;
import com.dyn.server.network.messages.MessageDecisionUpdate;
import com.google.common.collect.Maps;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.control.CheckBox;
import com.rabbit.gui.component.control.DropDown;
import com.rabbit.gui.component.control.MultiTextbox;
import com.rabbit.gui.component.control.TextBox;
import com.rabbit.gui.component.display.Picture;
import com.rabbit.gui.component.display.TextLabel;
import com.rabbit.gui.show.Show;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.client.AssetsBrowser;
import noppes.npcs.entity.EntityNPCInterface;

public class EditDecisionBlock extends Show {

	DecisionBlockTileEntity block;
	String text;
	String entity = "";
	String entitySkin = "";

	private int x1, y1, z1, x2, y2, z2;

	DropDown<String> textureList;
	DropDown<String> entityTypes;

	private String root;
	public AssetsBrowser assets;
	protected HashSet<String> dataTextures;
	private TextBox cmdtx1;
	private TextBox cmdtx2;
	private TextBox cmdtx3;
	private TextBox cmdtx4;
	private TextBox chTitle4;
	private TextBox chTitle3;
	private TextBox chTitle2;
	private TextBox chTitle1;
	private CheckBox chBx1;
	private CheckBox chBx2;
	private CheckBox chBx3;
	private CheckBox chBx4;
	private DropDown dMenu1;
	private DropDown dMenu2;
	private DropDown dMenu3;
	private DropDown dMenu4;

	public EditDecisionBlock(DecisionBlockTileEntity block) {
		title = "Edit Decision Block";
		this.block = block;
		x1 = block.getCorner1().getX();
		y1 = block.getCorner1().getY();
		z1 = block.getCorner1().getZ();
		x2 = block.getCorner2().getX();
		y2 = block.getCorner2().getY();
		z2 = block.getCorner2().getZ();
		text = block.getText();

		root = AssetsBrowser.getRoot("dyn:textures/skins/");
		assets = new AssetsBrowser(root, new String[] { "png" });
	}

	private Map<String, Choice> buildChoiceMap() {
		Map<String, Choice> retVal = Maps.newHashMap();
		List<Choice> choices = new ArrayList<>(block.getChoices().values());
		if (chBx1.isChecked()) {
			int index = dMenu1.getSelectedElement() != null ? dMenu1.getSelectedElement().getItemIndex()
					: choices.size() > 0 ? choices.get(0).getId() : Choice.NONE.getId();
			retVal.put(chTitle1.getText(), new Choice(index, index >= 2 ? cmdtx1.getText() : "none"));
		}
		if (chBx2.isChecked()) {
			int index = dMenu2.getSelectedElement() != null ? dMenu2.getSelectedElement().getItemIndex()
					: choices.size() > 1 ? choices.get(1).getId() : Choice.NONE.getId();
			retVal.put(chTitle2.getText(), new Choice(index, index >= 2 ? cmdtx2.getText() : "none"));
		}
		if (chBx3.isChecked()) {
			int index = dMenu3.getSelectedElement() != null ? dMenu3.getSelectedElement().getItemIndex()
					: choices.size() > 2 ? choices.get(2).getId() : Choice.NONE.getId();
			retVal.put(chTitle3.getText(), new Choice(index, index >= 2 ? cmdtx3.getText() : "none"));
		}
		if (chBx4.isChecked()) {
			int index = dMenu4.getSelectedElement() != null ? dMenu4.getSelectedElement().getItemIndex()
					: choices.size() > 3 ? choices.get(3).getId() : Choice.NONE.getId();
			retVal.put(chTitle4.getText(), new Choice(index, index >= 2 ? cmdtx4.getText() : "none"));
		}
		return retVal;
	}

	@Override
	public void setup() {

		registerComponent(
				new TextLabel((int) (width * .43), (int) (height * .1), 80, 15, Color.black, "Display Entity:"));

		registerComponent(entityTypes = new DropDown<String>((int) (width * .43), (int) (height * .15), 80, 15,
				block.getEntity() != null ? block.getEntity().getName() : "").setDrawUnicode(true)
						.setItemSelectedListener((DropDown<String> dropdown, String selected) -> {
							entity = "" + dropdown.getElement(selected).getValue();
						}));

		for (String entity : EntityList.stringToClassMapping.keySet()) {
			Class cl = EntityList.stringToClassMapping.get(entity);
			if (entity.contains("LightningBolt")) {
				continue;
			}
			if (EntityNPCInterface.class.isAssignableFrom(cl)) {
				continue;
			}
			if (!EntityLivingBase.class.isAssignableFrom(cl)) {
				continue;
			}
			entityTypes.add(StatCollector.translateToLocal("entity." + entity + ".name"), entity);
		}

		entityTypes.add("Fake Player", "DisplayEntity");
		entityTypes.add("Fake Player Head", "DisplayHead");

		registerComponent(
				new TextLabel((int) (width * .65), (int) (height * .1), 80, 15, Color.black, "Display Skin:"));

		registerComponent(textureList = new DropDown<String>((int) (width * .65), (int) (height * .15), 80, 15)
				.setDrawUnicode(true).setItemSelectedListener((DropDown<String> dropdown, String selected) -> {
					entitySkin = assets.getAsset(dropdown.getElement(selected).getValue());
				}));

		List<String> list = new ArrayList<>();
		for (String texture : assets.files) {
			list.add(texture);
		}
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

		for (String skin : list) {
			textureList.add(skin.replace(".png", ""), skin);
		}

		registerComponent(
				new TextLabel((int) (width * .14), (int) (height * .1), 100, 60, Color.black, "Set Block Area:"));

		registerComponent(new TextLabel((int) (width * .15), (int) (height * .175), 30, 20, Color.black, "W:"));

		registerComponent(new TextBox((int) (width * .18), (int) (height * .15), (int) (width * .05), 20)
				.setText("" + block.getCorner1().getX())
				.setTextChangedListener((TextBox textbox, String previousText) -> {
					if (!previousText.isEmpty()) {
						try {
							x1 = Integer.parseInt(previousText);
						} catch (Exception e) {
							textbox.setText("" + x1);
						}
					}
				}));

		registerComponent(new TextLabel((int) (width * .24), (int) (height * .175), 30, 20, Color.black, "Dn:"));

		registerComponent(new TextBox((int) (width * .28), (int) (height * .15), (int) (width * .05), 20)
				.setText("" + block.getCorner1().getY())
				.setTextChangedListener((TextBox textbox, String previousText) -> {
					if (!previousText.isEmpty()) {
						try {
							y1 = Integer.parseInt(previousText);
						} catch (Exception e) {
							textbox.setText("" + y1);
						}
					}
				}));

		registerComponent(new TextLabel((int) (width * .34), (int) (height * .175), 30, 20, Color.black, "N:"));

		registerComponent(new TextBox((int) (width * .37), (int) (height * .15), (int) (width * .05), 20)
				.setText("" + block.getCorner1().getZ())
				.setTextChangedListener((TextBox textbox, String previousText) -> {
					if (!previousText.isEmpty()) {
						try {
							z1 = Integer.parseInt(previousText);
						} catch (Exception e) {
							textbox.setText("" + z1);
						}
					}
				}));

		registerComponent(new TextLabel((int) (width * .15), (int) (height * .275), 30, 20, Color.black, "E:"));

		registerComponent(new TextBox((int) (width * .18), (int) (height * .25), (int) (width * .05), 20)
				.setText("" + block.getCorner2().getX())
				.setTextChangedListener((TextBox textbox, String previousText) -> {
					if (!previousText.isEmpty()) {
						try {
							x2 = Integer.parseInt(previousText);
						} catch (Exception e) {
							textbox.setText("" + x2);
						}
					}
				}));

		registerComponent(new TextLabel((int) (width * .24), (int) (height * .275), 30, 20, Color.black, "Up:"));

		registerComponent(new TextBox((int) (width * .28), (int) (height * .25), (int) (width * .05), 20)
				.setText("" + block.getCorner2().getY())
				.setTextChangedListener((TextBox textbox, String previousText) -> {
					if (!previousText.isEmpty()) {
						try {
							y2 = Integer.parseInt(previousText);
						} catch (Exception e) {
							textbox.setText("" + y2);
						}
					}
				}));

		registerComponent(new TextLabel((int) (width * .34), (int) (height * .275), 30, 20, Color.black, "S:"));

		registerComponent(new TextBox((int) (width * .37), (int) (height * .25), (int) (width * .05), 20)
				.setText("" + block.getCorner2().getZ())
				.setTextChangedListener((TextBox textbox, String previousText) -> {
					if (!previousText.isEmpty()) {
						try {
							z2 = Integer.parseInt(previousText);
						} catch (Exception e) {
							textbox.setText("" + z2);
						}
					}
				}));

		registerComponent(
				new TextLabel((int) (width * .15), (int) (height * .375), 100, 15, Color.black, "Set Block Question:"));

		registerComponent(
				new MultiTextbox((int) (width * .15), (int) (height * .425), (int) (width * .3), (int) (height * .35))
						.setText(text).setTextChangedListener((TextBox textbox, String previousText) -> {
							text = previousText;
						}));

		List<String> choiceTexts = new ArrayList<>(block.getChoices().keySet());
		List<Choice> choices = new ArrayList<>(block.getChoices().values());

		registerComponent(
				chBx1 = new CheckBox((int) (width * .47), (int) (height * .275), 15, 15, "", choices.size() > 0));

		registerComponent(
				chBx2 = new CheckBox((int) (width * .47), (int) (height * .425), 15, 15, "", choices.size() > 1));

		registerComponent(
				chBx3 = new CheckBox((int) (width * .47), (int) (height * .575), 15, 15, "", choices.size() > 2));

		registerComponent(
				chBx4 = new CheckBox((int) (width * .47), (int) (height * .725), 15, 15, "", choices.size() > 3));

		registerComponent(dMenu1 = new DropDown<String>((int) (width * .52), (int) (height * .275), (int) (width * .15),
				15, "Options").addAll("NONE", "REDSTONE", "COMMAND")
						.setDefaultItem(choices.size() > 0 ? choices.get(0).getType() : Choice.NONE.getType())
						.setItemSelectedListener((DropDown<String> dropdown, String selected) -> {
							if (selected == "COMMAND") {
								cmdtx1.setIsVisible(true);
							} else {
								cmdtx1.setIsVisible(false);
							}
						}));

		registerComponent(dMenu2 = new DropDown<String>((int) (width * .52), (int) (height * .425), (int) (width * .15),
				15, "Options").addAll("NONE", "REDSTONE", "COMMAND")
						.setDefaultItem(choices.size() > 1 ? choices.get(1).getType() : Choice.NONE.getType())
						.setItemSelectedListener((DropDown<String> dropdown, String selected) -> {
							if (selected == "COMMAND") {
								cmdtx2.setIsVisible(true);
							} else {
								cmdtx2.setIsVisible(false);
							}
						}));

		registerComponent(dMenu3 = new DropDown<String>((int) (width * .52), (int) (height * .575), (int) (width * .15),
				15, "Options").addAll("NONE", "REDSTONE", "COMMAND")
						.setDefaultItem(choices.size() > 2 ? choices.get(2).getType() : Choice.NONE.getType())
						.setItemSelectedListener((DropDown<String> dropdown, String selected) -> {
							if (selected == "COMMAND") {
								cmdtx3.setIsVisible(true);
							} else {
								cmdtx3.setIsVisible(false);
							}
						}));

		registerComponent(dMenu4 = new DropDown<String>((int) (width * .52), (int) (height * .725), (int) (width * .15),
				15, "Options").addAll("NONE", "REDSTONE", "COMMAND")
						.setDefaultItem(choices.size() > 3 ? choices.get(3).getType() : Choice.NONE.getType())
						.setItemSelectedListener((DropDown<String> dropdown, String selected) -> {
							if (selected == "COMMAND") {
								cmdtx4.setIsVisible(true);
							} else {
								cmdtx4.setIsVisible(false);
							}
						}));

		registerComponent(chTitle1 = new TextBox((int) (width * .69), (int) (height * .275), (int) (width * .15), 15,
				choiceTexts.size() > 0 ? choiceTexts.get(0) : "Choice Title"));

		registerComponent(chTitle2 = new TextBox((int) (width * .69), (int) (height * .425), (int) (width * .15), 15,
				choiceTexts.size() > 1 ? choiceTexts.get(1) : "Choice Title"));

		registerComponent(chTitle3 = new TextBox((int) (width * .69), (int) (height * .575), (int) (width * .15), 15,
				choiceTexts.size() > 2 ? choiceTexts.get(2) : "Choice Title"));

		registerComponent(chTitle4 = new TextBox((int) (width * .69), (int) (height * .725), (int) (width * .15), 15,
				choiceTexts.size() > 3 ? choiceTexts.get(3) : "Choice Title"));

		registerComponent(cmdtx1 = new TextBox((int) (width * .52), (int) (height * .35), (int) (width * .3), 15,
				choices.size() > 0 ? choices.get(0).getValue() : "")
						.setIsVisible(choices.size() > 0 ? choices.get(0).getType().equals("COMMAND") : false));

		registerComponent(cmdtx2 = new TextBox((int) (width * .52), (int) (height * .5), (int) (width * .3), 15,
				choices.size() > 1 ? choices.get(1).getValue() : "")
						.setIsVisible(choices.size() > 1 ? choices.get(1).getType().equals("COMMAND") : false));

		registerComponent(cmdtx3 = new TextBox((int) (width * .52), (int) (height * .65), (int) (width * .3), 15,
				choices.size() > 2 ? choices.get(2).getValue() : "")
						.setIsVisible(choices.size() > 2 ? choices.get(2).getType().equals("COMMAND") : false));

		registerComponent(cmdtx4 = new TextBox((int) (width * .52), (int) (height * .8), (int) (width * .3), 15,
				choices.size() > 3 ? choices.get(3).getValue() : "")
						.setIsVisible(choices.size() > 3 ? choices.get(3).getType().equals("COMMAND") : false));

		registerComponent(new Button((int) (width * .1625), (int) (height * .825), 120, 20, "Update Decision Block")
				.setClickListener(btn -> {
					NetworkManager.sendToServer(new MessageDecisionUpdate(entity, entitySkin, block.getPos(), text,
							new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2), buildChoiceMap()));
					final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
					executor.schedule(() -> block.clearList(), 2, TimeUnit.SECONDS);

					getStage().close();
				}));

		// The background
		registerComponent(new Picture((int) (width * .1125), (int) (height * .05), (int) (width * (6.0 / 8.0)),
				(int) (height * .9), new ResourceLocation("dyn", "textures/gui/background.png")));
	}
}
