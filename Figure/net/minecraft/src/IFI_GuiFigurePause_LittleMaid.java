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
	private String button104[] = { "Wait", "Active" };
	private String armorsName[];
	private Item armorsItem[][];
	private int armorIndex;
	private int armorDamage;

	public IFI_GuiFigurePause_LittleMaid(IFI_EntityFigure entityfigure) {
		super(entityfigure);
		elm = (LMM_EntityLittleMaid) targetEntity.renderEntity;
	}

	public void initGui() {
		super.initGui();
		try {
			String[] str1 = (String[]) ModLoader.getPrivateValue(
					RenderPlayer.class, null, 3);
			List<String> list = Arrays.asList(str1);
			ArrayList<String> arraylist = new ArrayList<String>();
			arraylist.addAll(list);
			armorsName = arraylist.toArray(new String[0]);

			armorsItem = new Item[str1.length][4];
			for (Item item2 : Item.itemsList) {
				if (item2 instanceof ItemArmor) {
					armorsItem[((ItemArmor) item2).renderIndex][3 - ((ItemArmor) item2).armorType] = item2;
				}
			}
		} catch (Exception exception) {

		}

		armorIndex = 0;
		armorDamage = 0;
		for (int i = 0; i < 4; i++) {
			ItemStack itemstack = elm.maidInventory.armorInventory[i];
			if (itemstack != null) {
				armorIndex = ((ItemArmor) itemstack.getItem()).renderIndex;
				if (itemstack.getMaxDamage() > 0) {
					armorDamage = itemstack.getItemDamage() * 10
							/ itemstack.getMaxDamage();
				}
			}
		}

		bt100 = new GuiButton(100, width / 2 - 120, height / 6 + 120 + 12, 240,
				20, elm.textureName);
		bt101 = new GuiButton(101, width / 2 - 120, height / 6 + 144 + 12, 240,
				20, elm.textureArmorName);
		controlList.add(bt100);
		controlList.add(bt101);
		controlList.add(new GuiButton(102, width / 2 - 140,
				height / 6 + 0 + 12, 80, 20, button102[elm.isMaidContract() ? 0
						: 1]));
		bt103 = new GuiButton(103, width / 2 - 140, height / 6 + 24 + 12, 80,
				20, String.format("Color : %x", elm.getMaidColor()));
		controlList.add(bt103);
		controlList.add(new GuiButton(104, width / 2 + 60,
				height / 6 + 96 + 12, 80, 20, button104[elm.isMaidWait() ? 0
						: 1]));
		controlList.add(new GuiButton(105, width / 2 - 140,
				height / 6 + 48 + 12, 80, 20, armorsName[armorIndex]));

		bt110 = new GuiButton(110, width / 2 - 120, height / 6 + 72 + 12, 40,
				20, String.format("%d", armorDamage));
		controlList.add(bt110);
		controlList.add(new GuiButton(111, width / 2 - 140,
				height / 6 + 72 + 12, 20, 20, "+"));
		controlList.add(new GuiButton(112, width / 2 - 80,
				height / 6 + 72 + 12, 20, 20, "-"));

		controlList.add(new GuiButton(153, width / 2 - 55, height / 6 + 0 + 12,
				20, 20, elm.isMaskedMaid() ? "E" : "N"));
		controlList.add(new GuiButton(152, width / 2 - 55,
				height / 6 + 24 + 12, 20, 20,
				elm.maidInventory.armorInventory[2] != null ? "E" : "N"));
		controlList.add(new GuiButton(151, width / 2 - 55,
				height / 6 + 48 + 12, 20, 20,
				elm.maidInventory.armorInventory[1] != null ? "E" : "N"));
		controlList.add(new GuiButton(150, width / 2 - 55,
				height / 6 + 72 + 12, 20, 20,
				elm.maidInventory.armorInventory[0] != null ? "E" : "N"));

	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 100) {
			if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
				LMM_Client.setPrevTexturePackege(elm, 0);
			} else {
				LMM_Client.setNextTexturePackege(elm, 0);
			}
		}
		if (guibutton.id == 101) {
			if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
				LMM_Client.setPrevTexturePackege(elm, 1);
			} else {
				LMM_Client.setNextTexturePackege(elm, 1);
			}
		}
		if (guibutton.id == 102) {
			// êFÇ™Ç†ÇÈÇ©Çåüçı
			MMM_TextureBox lbox = MMM_TextureManager.getTextureBox(elm.textureName);
			int i = 0;
			int j = !elm.isMaidContract() ? 0 : MMM_TextureManager.tx_wild;
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
				elm.maidContract = !elm.isMaidContract();
				// elm.setMaidContract(!elm.isMaidContract());// óéÇøÇÈ
				// byte byte0 = elm.dataWatcher.getWatchableObjectByte(16);
				// if(!elm.isMaidContract()) {
				// elm.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 |
				// 4)));
				// } else {
				// elm.dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 &
				// -5)));
				// }
				elm.setOwner(elm.isMaidContract() ? "Figure" : "");
				guibutton.displayString = button102[elm.isMaidContract() ? 0 : 1];
				elm.setMaidColor(i);
				bt103.displayString = String.format("Color : %x",
						elm.getMaidColor());
			}
		}
		if (guibutton.id == 103) {
			MMM_TextureBox lbox = MMM_TextureManager.getTextureBox(elm.textureName);

			int i = 0;
			int j = elm.isMaidContract() ? 0 : MMM_TextureManager.tx_wild;
			if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
				for (i = elm.getMaidColor() - 1; i >= 0; i--) {
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
				for (i = elm.getMaidColor() + 1; i < 16; i++) {
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
			bt103.displayString = String.format("Color : %x",
					elm.getMaidColor());
		}
		if (guibutton.id == 104) {
			elm.setMaidWait(!elm.isMaidWait());
			guibutton.displayString = button104[elm.isMaidWait() ? 0 : 1];
		}
		if (guibutton.id == 105) {
			armorIndex++;
			if (armorIndex >= armorsName.length) {
				armorIndex = 0;
			}
			guibutton.displayString = armorsName[armorIndex];
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

		if (guibutton.id == 153) {
			// ItemStack itemstack1 = elm.maidInventory.getStackInSlot(1);
			if (guibutton.displayString == "N") {
				elm.maidInventory.setInventorySlotContents(1, new ItemStack(
						armorsItem[armorIndex][3]));
				guibutton.displayString = "E";
			} else {
				elm.maidInventory.setInventorySlotContents(1, null);
				elm.maidInventory.armorInventory[3] = null;
				guibutton.displayString = "N";
			}
			elm.mstatMaskSelect = 17;
		}
		if (guibutton.id == 152) {
			if (guibutton.displayString == "N") {
				elm.maidInventory.armorInventory[2] = new ItemStack(
						armorsItem[armorIndex][2]);
				guibutton.displayString = "E";
			} else {
				elm.maidInventory.armorInventory[2] = null;
				guibutton.displayString = "N";
			}
		}
		if (guibutton.id == 151) {
			if (guibutton.displayString == "N") {
				elm.maidInventory.armorInventory[1] = new ItemStack(
						armorsItem[armorIndex][1]);
				guibutton.displayString = "E";
			} else {
				elm.maidInventory.armorInventory[1] = null;
				guibutton.displayString = "N";
			}
		}
		if (guibutton.id == 150) {
			if (guibutton.displayString == "N") {
				elm.maidInventory.armorInventory[0] = new ItemStack(
						armorsItem[armorIndex][0]);
				guibutton.displayString = "E";
			} else {
				elm.maidInventory.armorInventory[0] = null;
				guibutton.displayString = "N";
			}
		}
		/*
		 * if (elm.maidInventory.armorInventory[3] != null) {
		 * elm.maidInventory.setInventorySlotContents(1, new
		 * ItemStack(armorsItem[armorIndex][3], 1 ,
		 * armorsItem[armorIndex][3].getMaxDamage() * armorDamage / 10));
		 * elm.setMaskedMaid(); } if (elm.maidInventory.armorInventory[2] !=
		 * null) { elm.maidInventory.armorInventory[2] = new
		 * ItemStack(armorsItem[armorIndex][2], 1 ,
		 * armorsItem[armorIndex][2].getMaxDamage() * armorDamage / 10); } if
		 * (elm.maidInventory.armorInventory[1] != null) {
		 * elm.maidInventory.armorInventory[1] = new
		 * ItemStack(armorsItem[armorIndex][1], 1 ,
		 * armorsItem[armorIndex][1].getMaxDamage() * armorDamage / 10); } if
		 * (elm.maidInventory.armorInventory[0] != null) {
		 * elm.maidInventory.armorInventory[0] = new
		 * ItemStack(armorsItem[armorIndex][0], 1 ,
		 * armorsItem[armorIndex][0].getMaxDamage() * armorDamage / 10); }
		 */

		bt100.displayString = elm.textureName;
		bt101.displayString = elm.textureArmorName;
		LMM_Client.setTextureValue(elm);
	}

}
