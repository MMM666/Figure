package net.minecraft.src;

import java.io.DataInputStream;

import org.lwjgl.opengl.ARBMultisample;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;

public class IFI_GuiFigurePause extends GuiScreen {

	protected String screenTitle;
	protected IFI_EntityFigure targetEntity;
	protected String button10[] = { "Dismount", "Ride" };
	protected String button11[] = { "Stand", "Sneak" };
	protected static float button13[] = { 1, 2, 4, 6 };
	protected MMM_GuiSlider figureYaw;

	public IFI_GuiFigurePause(IFI_EntityFigure entityfigure) {
		screenTitle = "Figure Pause";
		targetEntity = entityfigure;
	}

	@Override
	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();

		controlList.add(new GuiButton(10, width / 2 + 60, height / 6 + 48 + 12,
				80, 20, stringtranslate
						.translateKey(button10[targetEntity.renderEntity
								.isRiding() ? 1 : 0])));
		controlList.add(new GuiButton(11, width / 2 + 60, height / 6 + 72 + 12,
				80, 20, stringtranslate
						.translateKey(button11[targetEntity.renderEntity
								.isSneaking() ? 1 : 0])));
		controlList.add(new GuiButton(13, width / 2 + 60, height / 6 + 0 + 12,
				80, 20, stringtranslate.translateKey(String.format("1 / %.0f",
						targetEntity.zoom))));
		// controlList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168,
		// 200, 20, stringtranslate.translateKey("gui.done")));
		figureYaw = new MMM_GuiSlider(15, width / 2 - 50, height / 6 + 96 + 12,
				String.format("%.2f", targetEntity.additionalYaw),
				(targetEntity.additionalYaw + 180F) / 360F);
		controlList.add(figureYaw);

		controlList.add(new GuiButton(20, width / 2 + 80, height / 6 + 24 + 12,
				40, 20, String
						.format("%.2f", targetEntity.renderEntity.yOffset)));
		controlList.add(new GuiButton(21, width / 2 + 60, height / 6 + 24 + 12,
				20, 20, stringtranslate.translateKey("+")));
		controlList
				.add(new GuiButton(22, width / 2 + 120, height / 6 + 24 + 12,
						20, 20, stringtranslate.translateKey("-")));
	}

	/**
	 * 特殊なデータ読み込みを実行
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

	/**
	 * 特殊なデータ書き込みを実行
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {}

	/**
	 * 姿勢制御用
	 */
	public void setRotation() {}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 10) {
			if (targetEntity.renderEntity.isRiding()) {
				// 載ってる
				targetEntity.renderEntity.setFlag(2, false);
				guibutton.displayString = button10[0];
			} else {
				// 載ってない
				targetEntity.renderEntity.setFlag(2, true);
				guibutton.displayString = button10[1];
			}
		}
		if (guibutton.id == 11) {
			if (targetEntity.renderEntity.isSneaking()) {
				// しゃがみ
				targetEntity.renderEntity.setFlag(1, false);
				guibutton.displayString = button11[0];
			} else {
				// 立ち
				targetEntity.renderEntity.setFlag(1, true);
				guibutton.displayString = button11[1];
			}
		}
		if (guibutton.id == 13) {
			// 倍率
			int i = 0;
			float z;
			for (int j = 0; j < button13.length; j++) {
				z = button13[j] > 0 ? button13[j] : 1 / -button13[j];
				if (targetEntity.zoom == z) {
					if (j + 1 < button13.length) {
						i = j + 1;
					}
					break;
				}
			}
			z = button13[i] > 0 ? button13[i] : 1 / -button13[i];
			targetEntity.setZoom(z);
			if (z >= 1) {
				guibutton.displayString = String.format("1 / %.0f", targetEntity.zoom);
			} else {
				guibutton.displayString = String.format("%.0f / 1", -button13[i]);
			}
		}
		
		if (guibutton.id == 20) {
			targetEntity.renderEntity.yOffset = 0;
		}
		if (guibutton.id == 21) {
			targetEntity.renderEntity.yOffset += 0.05;
		}
		if (guibutton.id == 22) {
			targetEntity.renderEntity.yOffset -= 0.05;
		}
		for (int k = 0; k < controlList.size(); k++) {
			GuiButton gb = (GuiButton) controlList.get(k);
			if (gb.id == 20) {
				gb.displayString = String.format("%.2f", targetEntity.renderEntity.yOffset);
			}
		}
		
		if (guibutton.id == 200) {
			mc.gameSettings.saveOptions();
			mc.displayGuiScreen(null);
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);
		
		// 基準の向きを変更
		targetEntity.additionalYaw = figureYaw.getSliderValue();
		// キャラ
		int l = width / 2;
		int k = height / 6 + 72;// height / 2;
		EntityLiving elt = (EntityLiving) targetEntity.renderEntity;
		GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(l, k + 30F, 50F);
		// float f1 = 45F;
		float f1 = 80F / elt.height;
		GL11.glScalef(-f1, f1, f1);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		float f2 = elt.renderYawOffset;
		float f5 = (float) (l + 0) - i;
		float f6 = (float) ((k + 30) - 50) - j;
		GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(targetEntity.additionalYaw - 135F, 0.0F, 1.0F, 0.0F);
		// elt.renderYawOffset = getGuiRenderYawOffset();
		elt.renderYawOffset = 0F;
		elt.rotationYaw = (float) Math.atan(f5 / 40F) * 40F;
		elt.rotationPitch = -(float) Math.atan(f6 / 40F) * 20F;
		elt.rotationYawHead = elt.rotationYaw;
		elt.prevRotationYawHead = elt.rotationYaw;
		setRotation();
		GL11.glTranslatef(0.0F, elt.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180F;
		RenderManager.instance.renderEntityWithPosYaw(elt, 0.0D, 0.0D, 0.0D,
				0.0F, 1.0F);
		elt.renderYawOffset = f2;
		elt.prevRotationYaw = elt.rotationYaw + f2;
		elt.prevRotationPitch = elt.rotationPitch;
		elt.rotationYawHead = elt.rotationYaw + f2;
		elt.prevRotationYawHead = elt.rotationYaw + f2;
		setRotation();

		// System.out.println(String.format("f: %f, %f, %f", elt.rotationYaw,
		// elt.prevRotationYaw, elt.renderYawOffset));
		// 影だかバイオームだかの処理?
		GL13.glMultiTexCoord2f(ARBMultitexture.GL_TEXTURE1_ARB, 240.0F, 240.0F);
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);

		super.drawScreen(i, j, f);
	}

	@Override
	public boolean doesGuiPauseGame() {
		// フィギュアをいじっていると時を忘れるよね？
		return false;
	}

	public float getGuiRenderYawOffset() {
		return 0.0F;
	}

	public static void afterRender(IFI_EntityFigure entityfigure) {
		// キャラクターをレンダリングした後の特殊処理
		// staticなので継承するわけではないから自分で作る。
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		// データをサーバーへ送る
		ModLoader.clientSendPacket(
				new Packet250CustomPayload("IFI|Upd", mod_IFI_Figure.sendData(targetEntity)));
		mod_IFI_Figure.Debug("DataSendToServer.");
	}
	
	/**
	 * 固有データを相手へ送る
	 */
	public void sendData(boolean pServer) {
		
	}

	/**
	 * 固有データを受け取ってEntityへ設定する
	 */
	public void reciveData(IFI_EntityFigure pFigure, byte[] pData) {
		
	}
}
