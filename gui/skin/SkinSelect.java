package com.dyn.render.gui.skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import com.dyn.server.database.DBManager;
import com.dyn.server.network.NetworkManager;
import com.dyn.server.network.packets.server.SyncSkinsServerMessage;
import com.rabbit.gui.background.DefaultBackground;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.display.Picture;
import com.rabbit.gui.component.display.TextLabel;
import com.rabbit.gui.component.display.entity.DisplayEntity;
import com.rabbit.gui.component.display.entity.EntityComponent;
import com.rabbit.gui.component.list.DisplayList;
import com.rabbit.gui.component.list.ScrollableDisplayList;
import com.rabbit.gui.component.list.entries.ListEntry;
import com.rabbit.gui.component.list.entries.SelectStringEntry;
import com.rabbit.gui.component.list.entries.StringEntry;
import com.rabbit.gui.render.TextAlignment;
import com.rabbit.gui.show.Show;
import com.rabbit.gui.utils.SkinManager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.AssetsBrowser;

public class SkinSelect extends Show {

	private String root;
	public AssetsBrowser assets;
	private HashSet<String> dataFolder;
	protected HashSet<String> dataTextures;
	private ScrollableDisplayList skinList;
	private SelectStringEntry selectedEntry;
	private DisplayEntity entity;
	private ResourceLocation playerSkin;

	public SkinSelect() {
		setBackground(new DefaultBackground());
		dataFolder = new HashSet<String>();
		dataTextures = new HashSet<String>();
		root = AssetsBrowser.getRoot("dyn:textures/skins/");
		assets = new AssetsBrowser(root, new String[] { "png" });
		title = "Skin Select Gui";
		entity = new DisplayEntity(Minecraft.getMinecraft().theWorld);
		String texture = SkinManager.getSkinTexture(Minecraft.getMinecraft().thePlayer);
		playerSkin = Minecraft.getMinecraft().getNetHandler()
				.getPlayerInfo(Minecraft.getMinecraft().thePlayer.getName()).getLocationSkin();
		if (texture != null) {
			entity.setTexture(new ResourceLocation(texture));
		} else {
			entity.setTexture(playerSkin);
		}
	}

	private void entryClicked(SelectStringEntry entry, DisplayList list, int mouseX, int mouseY) {
		selectedEntry = entry;
		if (dataTextures.contains(entry.getTitle()) && (entry.getTitle() != null)) {
			entity.setTexture(new ResourceLocation(assets.getAsset(entry.getTitle())));
		}
	}

	@Override
	public void onClose() {
		entity.setDead();
		super.onClose();
	}

	@Override
	public void setup() {
		super.setup();

		dataFolder.clear();
		Vector<String> list = new Vector<String>();
		for (String folder : assets.folders) {
			list.add("/" + folder);
			dataFolder.add("/" + folder);
		}
		for (String texture : assets.files) {
			list.add(texture);
			dataTextures.add(texture);
		}
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

		registerComponent(new TextLabel(width / 4, (int) (height * .1), width / 2, 20,
				"Select A Skin - Click and Drag to Rotate", TextAlignment.CENTER));

		// components
		ArrayList<ListEntry> skinAssets = new ArrayList<ListEntry>();

		skinAssets.add(new StringEntry("--Available Skins--"));
		for (String s : list) {
			skinAssets.add(new SelectStringEntry(s, (SelectStringEntry entry, DisplayList dlist, int mouseX,
					int mouseY) -> entryClicked(entry, dlist, mouseX, mouseY)));
		}

		skinList = new ScrollableDisplayList((int) (width * .175), (int) (height * .2), width / 3, 140, 15, skinAssets);
		skinList.setId("skins");
		registerComponent(skinList);

		registerComponent(
				new Button((int) (width * .175), (int) (height * .8), (int) (width / 5.25), 20, "Set as my skin")
						.setClickListener(btn -> {
							if ((selectedEntry != null) && !selectedEntry.getTitle().isEmpty()) {
								SkinManager.setSkinTexture(Minecraft.getMinecraft().thePlayer,
										assets.getAsset(selectedEntry.getTitle()));
								NetworkManager.sendToServer(
										new SyncSkinsServerMessage(Minecraft.getMinecraft().thePlayer.getName(),
												assets.getAsset(selectedEntry.getTitle())));
								DBManager.setPlayerSkin(Minecraft.getMinecraft().thePlayer.getDisplayNameString(),
										assets.getAsset(selectedEntry.getTitle()));
							}
						}));

		registerComponent(
				new Button((int) (width * .38), (int) (height * .8), 60, 20, "Reset Skin").setClickListener(btn -> {
					entity.setTexture(playerSkin);
					SkinManager.removeSkinTexture(Minecraft.getMinecraft().thePlayer);
					NetworkManager.sendToServer(
							new SyncSkinsServerMessage(Minecraft.getMinecraft().thePlayer.getName(), "reset"));
					DBManager.setPlayerSkin(Minecraft.getMinecraft().thePlayer.getDisplayNameString(), "");
				}));

		registerComponent(
				new EntityComponent((int) (width * .7), (int) (height * .85), width / 3, 100, entity, 0, 2.75f));

		// The background
		registerComponent(new Picture(width / 8, (int) (height * .05), (int) (width * (6.0 / 8.0)), (int) (height * .9),
				new ResourceLocation("dyn", "textures/gui/background3.png")));
	}
}
