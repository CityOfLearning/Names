package com.dyn.render.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.Charsets;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import com.dyn.DYNServerMod;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLockIconButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NewMainMenu extends GuiScreen {
	private static final Random RANDOM = new Random();
	private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(
			"textures/gui/title/minecraft.png");
	/** An array of all the paths to the panorama pictures. */
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
			new ResourceLocation("textures/gui/title/background/panorama_0.png"),
			new ResourceLocation("textures/gui/title/background/panorama_1.png"),
			new ResourceLocation("textures/gui/title/background/panorama_2.png"),
			new ResourceLocation("textures/gui/title/background/panorama_3.png"),
			new ResourceLocation("textures/gui/title/background/panorama_4.png"),
			new ResourceLocation("textures/gui/title/background/panorama_5.png") };
	public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here"
			+ EnumChatFormatting.RESET + " for more information.";
	/** Counts the number of screen updates. */
	private float updateCounter;
	/** The splash message. */
	private String splashText;
	/** Timer used to rotate the panorama, increases every tick. */
	private int panoramaTimer;
	/**
	 * Texture allocated for the current viewport of the main menu's panorama
	 * background.
	 */
	private DynamicTexture viewportTexture;
	/**
	 * The Object object utilized as a thread lock when performing non
	 * thread-safe operations
	 */
	private final Object threadLock = new Object();
	/** OpenGL graphics card warning. */
	private String openGLWarning1;
	/** OpenGL graphics card warning. */
	private String openGLWarning2;
	/** Link to the Mojang Support about minimum requirements */
	private String openGLWarningLink;
	private int field_92024_r;
	private int field_92023_s;
	private int field_92022_t;
	private int field_92021_u;
	private int field_92020_v;
	private int field_92019_w;
	private ResourceLocation backgroundTexture;

	private GuiButton singleplayermenu;
	private boolean unlockScreen;
	private GuiButton enterPasswordButton;
	private GuiButton cancelPassswordButton;
	private GuiButton multiplayermenu;
	private GuiLockIconButton lock1;
	private GuiLockIconButton lock2;
	private GuiButton dynServer;
	private GuiButton options;
	private GuiButton quit;
	private GuiTextField passwordField;
	private boolean unlockSingle;
	private boolean unlockMulti;

	public NewMainMenu() {
		openGLWarning2 = field_96138_a;
		splashText = "missingno";
		unlockScreen = false;
		BufferedReader bufferedreader = null;

		try {
			List<String> list = Lists.<String>newArrayList();
			bufferedreader = new BufferedReader(new InputStreamReader(
					Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(),
					Charsets.UTF_8));
			String s;

			while ((s = bufferedreader.readLine()) != null) {
				s = s.trim();

				if (!s.isEmpty()) {
					list.add(s);
				}
			}

			if (!list.isEmpty()) {
				while (true) {
					splashText = list.get(RANDOM.nextInt(list.size()));

					if (splashText.hashCode() != 125780783) {
						break;
					}
				}
			}
		} catch (IOException var12) {
			;
		} finally {
			if (bufferedreader != null) {
				try {
					bufferedreader.close();
				} catch (IOException var11) {
					;
				}
			}
		}

		updateCounter = RANDOM.nextFloat();
		openGLWarning1 = "";

		if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
			openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
			openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
			openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
		}
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed
	 * for buttons)
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
		}

		if (button.id == 1) {
			mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if (button.id == 2) {
			mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if (button.id == 3) {
			unlockSingle = true;
			unlockScreen = true;
			enterPasswordButton.visible = true;
			cancelPassswordButton.visible = true;
			singleplayermenu.visible = false;
			multiplayermenu.visible = false;
			lock1.visible = false;
			lock2.visible = false;
			dynServer.visible = false;
			options.visible = false;
			quit.visible = false;
		}

		if (button.id == 4) {
			mc.shutdown();
		}

		if (button.id == 5) {
			unlockMulti = true;
			unlockScreen = true;
			enterPasswordButton.visible = true;
			cancelPassswordButton.visible = true;
			singleplayermenu.visible = false;
			multiplayermenu.visible = false;
			lock1.visible = false;
			lock2.visible = false;
			dynServer.visible = false;
			options.visible = false;
			quit.visible = false;
		}

		if (button.id == 6) {
			if (unlockSingle && passwordField.getText().equals("UnlockSinglePlayer")) {
				singleplayermenu.enabled = true;
			}

			if (unlockMulti && passwordField.getText().equals("UnlockMultiPlayer")) {
				multiplayermenu.enabled = true;
			}
			unlockSingle = false;
			unlockMulti = false;
			unlockScreen = false;
			enterPasswordButton.visible = false;
			cancelPassswordButton.visible = false;
			singleplayermenu.visible = true;
			multiplayermenu.visible = true;
			lock1.visible = true;
			lock2.visible = true;
			dynServer.visible = true;
			options.visible = true;
			quit.visible = true;
			passwordField.setText("");
		}

		if (button.id == 7) {
			unlockSingle = false;
			unlockMulti = false;
			unlockScreen = false;
			enterPasswordButton.visible = false;
			cancelPassswordButton.visible = false;
			singleplayermenu.visible = true;
			multiplayermenu.visible = true;
			lock1.visible = true;
			lock2.visible = true;
			dynServer.visible = true;
			options.visible = true;
			quit.visible = true;
			passwordField.setText("");
		}

		if (button.id == 10) {
			FMLClientHandler.instance().connectToServerAtStartup("192.210.236.148", 25665);
		}
	}

	@Override
	public void confirmClicked(boolean result, int id) {
		if (id == 13) {
			if (result) {
				try {
					Class<?> oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
					oclass.getMethod("browse", new Class[] { URI.class }).invoke(object,
							new Object[] { new URI(openGLWarningLink) });
				} catch (Throwable throwable) {
					DYNServerMod.logger.error("Couldn\'t open link", throwable);
				}
			}

			mc.displayGuiScreen(this);
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Draws the main menu panorama
	 */
	private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.matrixMode(5889);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableCull();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		int i = 8;

		for (int j = 0; j < (i * i); ++j) {
			GlStateManager.pushMatrix();
			float f = (((float) (j % i) / (float) i) - 0.5F) / 64.0F;
			float f1 = (((float) (j / i) / (float) i) - 0.5F) / 64.0F;
			float f2 = 0.0F;
			GlStateManager.translate(f, f1, f2);
			GlStateManager.rotate((MathHelper.sin((panoramaTimer + p_73970_3_) / 400.0F) * 25.0F) + 20.0F, 1.0F, 0.0F,
					0.0F);
			GlStateManager.rotate(-(panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

			for (int k = 0; k < 6; ++k) {
				GlStateManager.pushMatrix();

				if (k == 1) {
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 2) {
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 3) {
					GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 4) {
					GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (k == 5) {
					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				int l = 255 / (j + 1);
				worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
				worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
				tessellator.draw();
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, false);
		}

		worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.matrixMode(5889);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
	}

	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY,
	 * renderPartialTicks
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GlStateManager.disableAlpha();
		renderSkybox(mouseX, mouseY, partialTicks);
		GlStateManager.enableAlpha();
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getWorldRenderer();
		int i = 274;
		int j = (width / 2) - (i / 2);
		int k = 30;
		drawGradientRect(0, 0, width, height, -2130706433, 16777215);
		drawGradientRect(0, 0, width, height, 0, Integer.MIN_VALUE);
		mc.getTextureManager().bindTexture(minecraftTitleTextures);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if (updateCounter < 1.0E-4D) {
			this.drawTexturedModalRect(j + 0, k + 0, 0, 0, 99, 44);
			this.drawTexturedModalRect(j + 99, k + 0, 129, 0, 27, 44);
			this.drawTexturedModalRect(j + 99 + 26, k + 0, 126, 0, 3, 44);
			this.drawTexturedModalRect(j + 99 + 26 + 3, k + 0, 99, 0, 26, 44);
			this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
		} else {
			this.drawTexturedModalRect(j + 0, k + 0, 0, 0, 155, 44);
			this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
		}

		if (unlockScreen) {
			passwordField.drawTextBox();
			drawCenteredString(fontRendererObj, "Input password to unlock feature", width / 2, (height / 4) + 60, -1);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate((width / 2) + 90, 70.0F, 0.0F);
		GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
		float f = 1.8F - MathHelper
				.abs(MathHelper.sin(((Minecraft.getSystemTime() % 1000L) / 1000.0F) * (float) Math.PI * 2.0F) * 0.1F);
		f = (f * 100.0F) / (fontRendererObj.getStringWidth(splashText) + 32);
		GlStateManager.scale(f, f, f);
		drawCenteredString(fontRendererObj, splashText, 0, -8, -256);
		GlStateManager.popMatrix();
		String s1 = "Copyright Mojang AB. Do not distribute!";
		drawString(fontRendererObj, s1, width - fontRendererObj.getStringWidth(s1) - 2, height - 10, -1);

		if ((openGLWarning1 != null) && (openGLWarning1.length() > 0)) {
			drawRect(field_92022_t - 2, field_92021_u - 2, field_92020_v + 2, field_92019_w - 1, 1428160512);
			drawString(fontRendererObj, openGLWarning1, field_92022_t, field_92021_u, -1);
			drawString(fontRendererObj, openGLWarning2, (width - field_92024_r) / 2, buttonList.get(0).yPosition - 12,
					-1);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);

	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called
	 * when the GUI is displayed and when the window resizes, the buttonList is
	 * cleared beforehand.
	 */
	@Override
	public void initGui() {
		viewportTexture = new DynamicTexture(256, 256);
		backgroundTexture = mc.getTextureManager().getDynamicTextureLocation("background", viewportTexture);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		if (((calendar.get(2) + 1) == 12) && (calendar.get(5) == 24)) {
			splashText = "Merry X-mas!";
		} else if (((calendar.get(2) + 1) == 1) && (calendar.get(5) == 1)) {
			splashText = "Happy new year!";
		} else if (((calendar.get(2) + 1) == 10) && (calendar.get(5) == 31)) {
			splashText = "OOoooOOOoooo! Spooky!";
		}

		int j = (height / 4) + 48;

		buttonList.add(singleplayermenu = new GuiButton(1, (width / 2) - 100, j,
				I18n.format("menu.singleplayer", new Object[0])));

		if (!DYNServerMod.developmentEnvironment) {
			singleplayermenu.enabled = false;
		}

		buttonList.add(lock1 = new GuiLockIconButton(3, (width / 2) + 100, j));
		buttonList.add(multiplayermenu = new GuiButton(2, (width / 2) - 100, j + (24 * 1),
				I18n.format("menu.multiplayer", new Object[0])));
		buttonList.add(lock2 = new GuiLockIconButton(5, (width / 2) + 100, j + 24));

		if (!DYNServerMod.developmentEnvironment) {
			multiplayermenu.enabled = false;
		}

		buttonList.add(dynServer = new GuiButton(10, (width / 2) - 100, j + (48 * 1), "Connect to DYN Server"));
		buttonList.add(options = new GuiButton(0, (width / 2) - 100, j + (72 * 1),
				I18n.format("menu.options", new Object[0])));
		buttonList
				.add(quit = new GuiButton(4, (width / 2) - 100, j + (96 * 1), I18n.format("menu.quit", new Object[0])));

		buttonList.add(enterPasswordButton = new GuiButton(6, (width / 2) - 100, j + 48 + 12, 98, 20, "Enter"));
		buttonList.add(cancelPassswordButton = new GuiButton(7, (width / 2) + 2, j + 48 + 12, 98, 20, "Cancel"));

		enterPasswordButton.visible = false;
		cancelPassswordButton.visible = false;

		passwordField = new GuiTextField(11, fontRendererObj, (width / 2) - 100, j + 24, 200, 20);

		synchronized (threadLock) {
			field_92023_s = fontRendererObj.getStringWidth(openGLWarning1);
			field_92024_r = fontRendererObj.getStringWidth(openGLWarning2);
			int k = Math.max(field_92023_s, field_92024_r);
			field_92022_t = (width - k) / 2;
			field_92021_u = buttonList.get(0).yPosition - 24;
			field_92020_v = field_92022_t + k;
			field_92019_w = field_92021_u + 24;
		}

		mc.setConnectedToRealms(false);
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is
	 * the equivalent of KeyListener.keyTyped(KeyEvent e). Args : character
	 * (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (unlockScreen) {
			passwordField.textboxKeyTyped(typedChar, keyCode);
		}
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (unlockScreen) {
			passwordField.mouseClicked(mouseX, mouseY, mouseButton);
		}
		synchronized (threadLock) {
			if ((openGLWarning1.length() > 0) && (mouseX >= field_92022_t) && (mouseX <= field_92020_v)
					&& (mouseY >= field_92021_u) && (mouseY <= field_92019_w)) {
				GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, openGLWarningLink, 13, true);
				guiconfirmopenlink.disableSecurityWarning();
				mc.displayGuiScreen(guiconfirmopenlink);
			}
		}

	}

	/**
	 * Renders the skybox in the main menu
	 */
	private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
		mc.getFramebuffer().unbindFramebuffer();
		GlStateManager.viewport(0, 0, 256, 256);
		drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		mc.getFramebuffer().bindFramebuffer(true);
		GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
		float f = width > height ? 120.0F / width : 120.0F / height;
		float f1 = (height * f) / 256.0F;
		float f2 = (width * f) / 256.0F;
		int i = width;
		int j = height;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(0.0D, j, zLevel).tex(0.5F - f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(i, j, zLevel).tex(0.5F - f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(i, 0.0D, zLevel).tex(0.5F + f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		worldrenderer.pos(0.0D, 0.0D, zLevel).tex(0.5F + f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();
	}

	/**
	 * Rotate and blurs the skybox view in the main menu
	 */
	private void rotateAndBlurSkybox(float p_73968_1_) {
		mc.getTextureManager().bindTexture(backgroundTexture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.colorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		GlStateManager.disableAlpha();
		int i = 3;

		for (int j = 0; j < i; ++j) {
			float f = 1.0F / (j + 1);
			int k = width;
			int l = height;
			float f1 = (j - (i / 2)) / 256.0F;
			worldrenderer.pos(k, l, zLevel).tex(0.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(k, 0.0D, zLevel).tex(1.0F + f1, 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, 0.0D, zLevel).tex(1.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			worldrenderer.pos(0.0D, l, zLevel).tex(0.0F + f1, 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		++panoramaTimer;
		if (unlockScreen) {
			passwordField.updateCursorCounter();
		}
	}
}