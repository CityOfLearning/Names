package com.dyn.render.gui.dialog;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.dyn.fixins.blocks.dialog.DialogBlockTileEntity;
import com.dyn.server.network.NetworkManager;
import com.dyn.server.network.messages.MessageDialogUpdate;
import com.rabbit.gui.component.control.Button;
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
import noppes.npcs.client.AssetsBrowser;

public class EditDialogBlock extends Show {

	DialogBlockTileEntity block;
	String text;
	String entity = "";
	String entitySkin = "";

	private int x1, y1, z1, x2, y2, z2;

	DropDown<String> textureList;
	DropDown<Integer> entityTypes;

	private String root;
	public AssetsBrowser assets;
	protected HashSet<String> dataTextures;

	public EditDialogBlock(DialogBlockTileEntity block) {
		title = "Edit Dialog Block";
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

	@Override
	public void setup() {

		registerComponent(
				new TextLabel((int) (width * .625), (int) (height * .1), 100, 15, Color.black, "Display Entity:"));

		registerComponent(entityTypes = new DropDown<Integer>((int) (width * .625), (int) (height * .15), 90, 15,
				block.getEntity() != null ? block.getEntity().getName() : "").setDrawUnicode(true)
						.setItemSelectedListener((DropDown<Integer> dropdown, String selected) -> {
							if (dropdown.getElement(selected).getValue() != 90) {
								entity = "" + dropdown.getElement(selected).getValue();
							} else {
								entity = selected;
							}
						}));

		for (String entityname : EntityList.getEntityNameList()) {
			// we probably dont want npc characters since they don't play by the
			// same rules
			if (!entityname.contains("npc") && !entityname.contains("LightningBolt")
					&& !entityname.contains("ArmorStand")) {
				if (EntityLivingBase.class
						.isAssignableFrom(EntityList.getClassFromID(EntityList.getIDFromString(entityname)))) {
					entityTypes.add(entityname, EntityList.getIDFromString(entityname));
				}
			}
		}

		entityTypes.add("DisplayEntity", 90);
		entityTypes.add("DisplayHead", 90);

		registerComponent(
				new TextLabel((int) (width * .625), (int) (height * .25), 100, 15, Color.black, "Display Skin:"));

		registerComponent(textureList = new DropDown<String>((int) (width * .625), (int) (height * .3), 90, 15)
				.setDrawUnicode(true).setItemSelectedListener((DropDown<String> dropdown, String selected) -> {
					entitySkin = assets.getAsset(selected);
				}));

		List<String> list = new ArrayList<>();
		for (String texture : assets.files) {
			list.add(texture);
		}
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

		for (String skin : list) {
			// we probably dont want npc characters since they don't play by the
			// same rules
			textureList.add(skin);
		}

		registerComponent(
				new TextLabel((int) (width * .14), (int) (height * .1), 100, 60, Color.black, "Set Block Area:"));

		registerComponent(
				new TextBox((int) (width * .18), (int) (height * .15), 45, 20).setText("" + block.getCorner1().getX())
						.setTextChangedListener((TextBox textbox, String previousText) -> {
							if (!previousText.isEmpty()) {
								try {
									x1 = Integer.parseInt(previousText);
								} catch (Exception e) {
									textbox.setText("" + x1);
								}
							}
						}));

		registerComponent(new TextLabel((int) (width * .15), (int) (height * .175), 30, 20, Color.black, "W:"));

		registerComponent(
				new TextBox((int) (width * .33), (int) (height * .15), 45, 20).setText("" + block.getCorner1().getY())
						.setTextChangedListener((TextBox textbox, String previousText) -> {
							if (!previousText.isEmpty()) {
								try {
									y1 = Integer.parseInt(previousText);
								} catch (Exception e) {
									textbox.setText("" + y1);
								}
							}
						}));

		registerComponent(new TextLabel((int) (width * .29), (int) (height * .175), 30, 20, Color.black, "Dn:"));

		registerComponent(
				new TextBox((int) (width * .48), (int) (height * .15), 45, 20).setText("" + block.getCorner1().getZ())
						.setTextChangedListener((TextBox textbox, String previousText) -> {
							if (!previousText.isEmpty()) {
								try {
									z1 = Integer.parseInt(previousText);
								} catch (Exception e) {
									textbox.setText("" + z1);
								}
							}
						}));

		registerComponent(new TextLabel((int) (width * .45), (int) (height * .175), 30, 20, Color.black, "N:")
				.setMultilined(false));

		registerComponent(
				new TextBox((int) (width * .18), (int) (height * .25), 45, 20).setText("" + block.getCorner2().getX())
						.setTextChangedListener((TextBox textbox, String previousText) -> {
							if (!previousText.isEmpty()) {
								try {
									x2 = Integer.parseInt(previousText);
								} catch (Exception e) {
									textbox.setText("" + x2);
								}
							}
						}));

		registerComponent(new TextLabel((int) (width * .15), (int) (height * .275), 30, 20, Color.black, "E:"));

		registerComponent(
				new TextBox((int) (width * .33), (int) (height * .25), 45, 20).setText("" + block.getCorner2().getY())
						.setTextChangedListener((TextBox textbox, String previousText) -> {
							if (!previousText.isEmpty()) {
								try {
									y2 = Integer.parseInt(previousText);
								} catch (Exception e) {
									textbox.setText("" + y2);
								}
							}
						}));

		registerComponent(new TextLabel((int) (width * .29), (int) (height * .275), 30, 20, Color.black, "Up:"));

		registerComponent(
				new TextBox((int) (width * .48), (int) (height * .25), 45, 20).setText("" + block.getCorner2().getZ())
						.setTextChangedListener((TextBox textbox, String previousText) -> {
							if (!previousText.isEmpty()) {
								try {
									z2 = Integer.parseInt(previousText);
								} catch (Exception e) {
									textbox.setText("" + z2);
								}
							}
						}));

		registerComponent(new TextLabel((int) (width * .45), (int) (height * .275), 30, 20, Color.black, "S:"));

		registerComponent(
				new TextLabel((int) (width * .15), (int) (height * .375), 100, 15, Color.black, "Set Block Dialog:"));

		registerComponent(
				new MultiTextbox((int) (width * .15), (int) (height * .425), (int) (width * .65), (int) (height * .35))
						.setText(text).setTextChangedListener((TextBox textbox, String previousText) -> {
							text = previousText;
						}));

		registerComponent(new Button((int) (width * .5), (int) (height * .8125), 120, 20, "Update Dialog Block")
				.setClickListener(btn -> {
					NetworkManager.sendToServer(new MessageDialogUpdate(entity, entitySkin, block.getPos(), text,
							new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2)));
					getStage().close();
				}));
		// The background
		registerComponent(new Picture((int) (width * .1125), (int) (height * .05), (int) (width * (6.0 / 8.0)),
				(int) (height * .9), new ResourceLocation("dyn", "textures/gui/background.png")));
	}
}
