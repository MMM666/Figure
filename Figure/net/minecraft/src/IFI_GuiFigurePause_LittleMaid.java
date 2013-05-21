package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.input.Keyboard;

public class IFI_GuiFigurePause_LittleMaid extends IFI_GuiFigurePause {

	private LMM_EntityLittleMaid elm;
	private GuiButton bt100;
	private GuiButton bt101;
	private GuiButton bt103;
	private GuiButton bt110;
	private String button102[] = { "Contract", "Wild" };
	private String button104[] = { "Wait", "Active", "Aim" };
	private int armorIndex;
	private int armorDamage;


	public IFI_GuiFigurePause_LittleMaid(IFI_EntityFigure entityfigure) {
		super(entityfigure);
		elm = (LMM_EntityLittleMaid) targetEntity.renderEntity;
	}

	public void initGui() {
		super.initGui();
		
		armorIndex = 0;
		armorDamage = 0;
		for (int i = 0; i < 4; i++) {
			ItemStack itemstack = elm.maidInventory.armorInventory[i];
			if (itemstack != null) {
				armorIndex = ((ItemArmor) itemstack.getItem()).renderIndex;
				if (itemstack.getMaxDamage() > 0) {
					armorDamage = itemstack.getItemDamage() * 10 / itemstack.getMaxDamage();
				}
			}
		}
		
		bt100 = new GuiButton(100, width / 2 - 120, height / 6 + 120 + 12, 240, 20, elm.textureBox[0].textureName);
		bt101 = new GuiButton(101, width / 2 - 120, height / 6 + 144 + 12, 240, 20, elm.textureBox[1].textureName);
		buttonList.add(bt100);
		buttonList.add(bt101);
		buttonList.add(new GuiButton(160, width / 2 - 140, height / 6 + 0 + 12, 80, 20, "TextureSelect"));
		buttonList.add(new GuiButton(102, width / 2 - 140, height / 6 + 24 + 12, 80, 20, button102[elm.maidContract ? 0 : 1]));
		bt103 = new GuiButton(103, width / 2 - 140, height / 6 + 48 + 12, 80, 20, String.format("Color : %x", elm.maidColor));
		buttonList.add(bt103);
		buttonList.add(new GuiButton(104, width / 2 + 60, height / 6 + 96 + 12, 80, 20, button104[elm.isMaidWait() ? 0 : elm.mstatAimeBow ? 2 : 1]));
		
		bt110 = new GuiButton(110, width / 2 - 120, height / 6 + 72 + 12, 40,
				20, String.format("%d", armorDamage));
		buttonList.add(bt110);
		buttonList.add(new GuiButton(111, width / 2 - 140, height / 6 + 72 + 12, 20, 20, "+"));
		buttonList.add(new GuiButton(112, width / 2 - 80, height / 6 + 72 + 12, 20, 20, "-"));
		
		elm.mstatMaskSelect = 16;
		elm.setDominantArm(0);
		elm.setEquipItem(0, 0);
		elm.setEquipItem(1, 1);
		elm.checkMaskedMaid();
		elm.checkHeadMount();
		elm.sendTextureToServer();
//		elm.setTexturePackName(elm.textureBox);
	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 100) {
			MMM_TextureBox lbox[] = new MMM_TextureBox[2];
			if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
				elm.setPrevTexturePackege(0);
				lbox[0] = (MMM_TextureBox)elm.textureBox[0];
				lbox[1] = (MMM_TextureBox)elm.textureBox[1];
				elm.setPrevTexturePackege(0);
			} else {
				elm.setNextTexturePackege(0);
				lbox[0] = (MMM_TextureBox)elm.textureBox[0];
				lbox[1] = (MMM_TextureBox)elm.textureBox[1];
				elm.setPrevTexturePackege(0);
			}
			MMM_TextureManager.checkTextureBoxServer((MMM_TextureBox)elm.textureBox[0]);
			elm.textureBox[0] = lbox[0];
			elm.textureBox[1] = lbox[1];
			elm.textureIndex[0] = MMM_TextureManager.getIndexTextureBoxServerIndex(lbox[0]);
			elm.textureIndex[1] = MMM_TextureManager.getIndexTextureBoxServerIndex(lbox[1]);
		}
		if (guibutton.id == 101) {
			MMM_TextureBox lbox[] = new MMM_TextureBox[2];
			if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
				elm.setPrevTexturePackege(1);
				lbox[0] = (MMM_TextureBox)elm.textureBox[0];
				lbox[1] = (MMM_TextureBox)elm.textureBox[1];
				elm.setPrevTexturePackege(1);
			} else {
				elm.setNextTexturePackege(1);
				lbox[0] = (MMM_TextureBox)elm.textureBox[0];
				lbox[1] = (MMM_TextureBox)elm.textureBox[1];
				elm.setPrevTexturePackege(1);
			}
			MMM_TextureManager.checkTextureBoxServer((MMM_TextureBox)elm.textureBox[1]);
			elm.textureBox[0] = lbox[0];
			elm.textureBox[1] = lbox[1];
			elm.textureIndex[0] = MMM_TextureManager.getIndexTextureBoxServerIndex(lbox[0]);
			elm.textureIndex[1] = MMM_TextureManager.getIndexTextureBoxServerIndex(lbox[1]);
		}
		if (guibutton.id == 102) {
			// êFÇ™Ç†ÇÈÇ©Çåüçı
			MMM_TextureBox lbox = MMM_TextureManager.getTextureBox(elm.textureBox[0].textureName);
			int i = 0;
			int j = !elm.maidContract ? 0 : MMM_TextureManager.tx_wild;
			for (i = elm.maidColor; i < 16; i++) {
				if (lbox.hasColor(i + j)) {
					break;
				}
			}
			if (i > 15) {
				for (i = 0; i < 16; i++) {
					if (lbox.hasColor(i + j)) {
						break;
					}
				}
			}
			if (i < 16) {
				// å_ñÒèÛë‘ê›íË
				elm.maidContract = !elm.maidContract;
				elm.setOwner(elm.maidContract ? "Figure" : "");
				guibutton.displayString = button102[elm.maidContract ? 0 : 1];
				elm.setMaidColor(i);
				bt103.displayString = String.format("Color : %x",
						elm.getMaidColor());
			}
		}
		if (guibutton.id == 103) {
			MMM_TextureBox lbox = MMM_TextureManager.getTextureBox(elm.textureBox[0].textureName);
			
			int i = 0;
			int j = elm.maidContract ? 0 : MMM_TextureManager.tx_wild;
			if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
				for (i = elm.maidColor - 1; i >= 0; i--) {
					if (lbox.hasColor(i + j)) {
						break;
					}
				}
				if (i < 0) {
					for (i = 15; i >= 0; i--) {
						if (lbox.hasColor(i + j)) {
							break;
						}
					}
				}
			} else {
				for (i = elm.maidColor + 1; i < 16; i++) {
					if (lbox.hasColor(i + j)) {
						break;
					}
				}
				if (i > 15) {
					for (i = 0; i < 16; i++) {
						if (lbox.hasColor(i + j)) {
							break;
						}
					}
				}
			}
			elm.setMaidColor(i);
			elm.setTextureNames();
			bt103.displayString = String.format("Color : %x", elm.maidColor);
		}
		if (guibutton.id == 104) {
			if (elm.isMaidWait()) {
				elm.setMaidWait(false);
				elm.mstatAimeBow = true;
				elm.updateAimebow();
			} else if (elm.mstatAimeBow) {
				elm.mstatAimeBow = false;
				elm.updateAimebow();
			} else {
				elm.setMaidWait(true);
			}
//			elm.setMaidWait(!elm.isMaidWait());
			guibutton.displayString = button104[elm.isMaidWait() ? 0 : elm.mstatAimeBow ? 2 : 1];
		}

		if (guibutton.id == 110) {
			armorDamage = 0;
		}
		if (guibutton.id == 111) {
			if (armorDamage < 10)
				armorDamage++;
		}
		if (guibutton.id == 112) {
			if (armorDamage > 0)
				armorDamage--;
		}
		bt110.displayString = String.format("%d", armorDamage);
		
		if (guibutton.id == 160) {
			mc.displayGuiScreen(new LMM_GuiTextureSelect(this, elm, 0xffff, false));
		}
		
		bt100.displayString = elm.textureBox[0].textureName;
		bt101.displayString = elm.textureBox[1].textureName;
		elm.setTextureNames();
	}

	@Override
	public void onGuiClosed() {
//		elm.textureIndex = MMM_TextureManager.getStringToIndex(elm.textureName);
//		elm.textureArmorIndex = MMM_TextureManager.getStringToIndex(elm.textureArmorName);
		elm.setMaidContract(elm.maidContract);
		super.onGuiClosed();
	}

	@Override
	public void setItems() {
		elm.setCurrentItemOrArmor(3, IFI_GuiItemSelect.inventoryItem.getStackInSlot(1));
		elm.setCurrentItemOrArmor(2, IFI_GuiItemSelect.inventoryItem.getStackInSlot(2));
		elm.setCurrentItemOrArmor(1, IFI_GuiItemSelect.inventoryItem.getStackInSlot(3));
		elm.setCurrentItemOrArmor(5, IFI_GuiItemSelect.inventoryItem.getStackInSlot(4));
		elm.setCurrentItemOrArmor(6, IFI_GuiItemSelect.inventoryItem.getStackInSlot(5));
		elm.setCurrentItemOrArmor(21, IFI_GuiItemSelect.inventoryItem.getStackInSlot(0));
		elm.setCurrentItemOrArmor(22, IFI_GuiItemSelect.inventoryItem.getStackInSlot(8));
		elm.setEquipItem(0, 0);
		elm.setEquipItem(1, 1);
	}

	@Override
	public void getItems() {
		IFI_GuiItemSelect.inventoryItem.setInventorySlotContents(0, elm.getCurrentItemOrArmor(21));
		IFI_GuiItemSelect.inventoryItem.setInventorySlotContents(1, elm.getCurrentItemOrArmor(3));
		IFI_GuiItemSelect.inventoryItem.setInventorySlotContents(2, elm.getCurrentItemOrArmor(2));
		IFI_GuiItemSelect.inventoryItem.setInventorySlotContents(3, elm.getCurrentItemOrArmor(1));
		IFI_GuiItemSelect.inventoryItem.setInventorySlotContents(4, elm.getCurrentItemOrArmor(5));
		IFI_GuiItemSelect.inventoryItem.setInventorySlotContents(5, elm.getCurrentItemOrArmor(6));
		IFI_GuiItemSelect.inventoryItem.setInventorySlotContents(8, elm.getCurrentItemOrArmor(22));
	}

}
