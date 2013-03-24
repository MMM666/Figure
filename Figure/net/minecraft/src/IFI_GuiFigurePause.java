package net.minecraft.src;

import static net.minecraft.src.IFI_Statics.IFI_Packet_UpadteItem;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.lwjgl.opengl.ARBMultisample;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;

public class IFI_GuiFigurePause extends GuiScreen {

	protected String screenTitle;
	protected IFI_EntityFigure targetEntity;
	protected String button10[] = { "Dismount", "Ride" };
	protected String button11[] = { "Stand", "Sneak" };
	protected static float button13[] = { 1, 2, 4, 6 };
	protected String button16[] = { "A", "C" };
	protected MMM_GuiSlider figureYaw;

	public IFI_GuiFigurePause(IFI_EntityFigure entityfigure) {
		screenTitle = "Figure Pause";
		targetEntity = entityfigure;
	}

	@Override
	public boolean doesGuiPauseGame() {
		// フィギュアをいじっていると時を忘れるよね？
		return false;
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (par2 == 1) {
			// データをサーバーへ送る
			ModLoader.clientSendPacket(new Packet250CustomPayload("IFI|Upd",
					mod_IFI_Figure.getServerFigure(targetEntity).getData(targetEntity)));
			mod_IFI_Figure.Debug("DataSendToServer.");
			mod_IFI_Figure.getServerFigure(targetEntity).sendItems(targetEntity, true);
//			targetEntity.setPosition(targetEntity.posX, targetEntity.posY, targetEntity.posZ);
		}
		super.keyTyped(par1, par2);
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
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
		mod_IFI_Figure.getServerFigure(targetEntity).setRotation(targetEntity);
		GL11.glTranslatef(0.0F, elt.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180F;
		RenderManager.instance.renderEntityWithPosYaw(elt, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		elt.renderYawOffset = f2;
		elt.prevRotationYaw = elt.rotationYaw + f2;
		elt.prevRotationPitch = elt.rotationPitch;
		elt.rotationYawHead = elt.rotationYaw + f2;
		elt.prevRotationYawHead = elt.rotationYaw + f2;
		mod_IFI_Figure.getServerFigure(targetEntity).setRotation(targetEntity);

		// System.out.println(String.format("f: %f, %f, %f", elt.rotationYaw,
		// elt.prevRotationYaw, elt.renderYawOffset));
		// 影だかバイオームだかの処理?
//		GL13.glMultiTexCoord2f(ARBMultitexture.GL_TEXTURE1_ARB, 240.0F, 240.0F);
		GL11.glPopMatrix();
//		RenderHelper.disableStandardItemLighting();
//		GL11.glDisable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		super.drawScreen(i, j, f);
	}


	// 継承して独自処理を書く必要がある物

	@Override
	public void initGui() {
		if (targetEntity.renderEntity != null && IFI_GuiItemSelect.isChange) {
			setItems();
		}
		IFI_GuiItemSelect.isChange = false;
		
		StringTranslate stringtranslate = StringTranslate.getInstance();
		
		buttonList.add(new GuiButton(10, width / 2 + 60, height / 6 + 48 + 12, 80, 20,
				stringtranslate.translateKey(button10[targetEntity.renderEntity.isRiding() ? 1 : 0])));
		buttonList.add(new GuiButton(11, width / 2 + 60, height / 6 + 72 + 12, 80, 20,
				stringtranslate.translateKey(button11[targetEntity.renderEntity.isSneaking() ? 1 : 0])));
		buttonList.add(new GuiButton(13, width / 2 + 60, height / 6 + 0 + 12, 60, 20,
				stringtranslate.translateKey(String.format("1 / %.0f", targetEntity.zoom))));
		buttonList.add(new GuiButton(16, width / 2 + 120, height / 6 + 0 + 12, 20, 20,
				stringtranslate.translateKey(button16[targetEntity.renderEntity.isChild() ? 1 : 0])));
		buttonList.add(new GuiButton(17, width / 2 - 140, height / 6 + 96 + 12, 80, 20,
				stringtranslate.translateKey("EquipSelect")));
		figureYaw = new MMM_GuiSlider(15, width / 2 - 50, height / 6 + 96 + 12,
				String.format("%.2f", targetEntity.additionalYaw), (targetEntity.additionalYaw + 180F) / 360F);
		buttonList.add(figureYaw);
		
		buttonList.add(new GuiButton(20, width / 2 + 80, height / 6 + 24 + 12,
				40, 20, String.format("%.2f", targetEntity.fyOffset)));
		buttonList.add(new GuiButton(21, width / 2 + 60, height / 6 + 24 + 12,
				20, 20, stringtranslate.translateKey("+")));
		buttonList.add(new GuiButton(22, width / 2 + 120, height / 6 + 24 + 12,
				20, 20, stringtranslate.translateKey("-")));
	}

	public float getGuiRenderYawOffset() {
		return 0.0F;
	}

	/**
	 * GUIの操作をした時の処理
	 */
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
		if (guibutton.id == 16) {
			// 幼生態判定
			if (targetEntity.renderEntity instanceof EntityAgeable) {
				EntityAgeable lentity = (EntityAgeable)targetEntity.renderEntity;
				lentity.setGrowingAge(lentity.isChild() ? 0 : -1);
				guibutton.displayString = button16[lentity.isChild() ? 1 : 0];
			}
		}
		if (guibutton.id == 17) {
			// 装備品の設定
			IFI_GuiItemSelect.clearInventory();
			getItems();
			mc.displayGuiScreen(new IFI_GuiItemSelect(this, targetEntity.renderEntity));
		}
		
		if (guibutton.id == 20) {
			targetEntity.fyOffset = 0;
		}
		if (guibutton.id == 21) {
			targetEntity.fyOffset += 0.05;
		}
		if (guibutton.id == 22) {
			targetEntity.fyOffset -= 0.05;
		}
		for (int k = 0; k < buttonList.size(); k++) {
			GuiButton gb = (GuiButton) buttonList.get(k);
			if (gb.id == 20) {
				gb.displayString = String.format("%.2f", targetEntity.fyOffset);
			}
		}
		
	}

	/**
	 * アイテムを設定する
	 */
	public void setItems() {
		targetEntity.renderEntity.setCurrentItemOrArmor(4, IFI_GuiItemSelect.inventoryItem.getStackInSlot(0));
		targetEntity.renderEntity.setCurrentItemOrArmor(3, IFI_GuiItemSelect.inventoryItem.getStackInSlot(1));
		targetEntity.renderEntity.setCurrentItemOrArmor(2, IFI_GuiItemSelect.inventoryItem.getStackInSlot(2));
		targetEntity.renderEntity.setCurrentItemOrArmor(1, IFI_GuiItemSelect.inventoryItem.getStackInSlot(3));
		targetEntity.renderEntity.setCurrentItemOrArmor(0, IFI_GuiItemSelect.inventoryItem.getStackInSlot(4));
	}

	/**
	 * 現在のアイテムをGUIへ移す。
	 */
	public void getItems() {
		IFI_GuiItemSelect.inventoryItem.setInventorySlotContents(0, targetEntity.renderEntity.getCurrentArmor(3));
		IFI_GuiItemSelect.inventoryItem.setInventorySlotContents(1, targetEntity.renderEntity.getCurrentArmor(2));
		IFI_GuiItemSelect.inventoryItem.setInventorySlotContents(2, targetEntity.renderEntity.getCurrentArmor(1));
		IFI_GuiItemSelect.inventoryItem.setInventorySlotContents(3, targetEntity.renderEntity.getCurrentArmor(0));
		IFI_GuiItemSelect.inventoryItem.setInventorySlotContents(4, targetEntity.renderEntity.getHeldItem());
	}

	public static void afterRender(IFI_EntityFigure entityfigure) {
		// キャラクターをレンダリングした後の特殊処理
		// staticなので継承するわけではないから自分で作る。
	}

}
