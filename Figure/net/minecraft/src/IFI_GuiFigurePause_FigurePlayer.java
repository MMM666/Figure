package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.openmbean.ArrayType;

public class IFI_GuiFigurePause_FigurePlayer extends IFI_GuiFigurePause {

	public IFI_EntityFigurePlayer efplayer;
	private String[] armorsName;
	private int[] armorsSelection;

	public IFI_GuiFigurePause_FigurePlayer(IFI_EntityFigure entityfigure) {
		super(entityfigure);
		efplayer = (IFI_EntityFigurePlayer) entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		try {
			String[] str1 = (String[]) ModLoader.getPrivateValue(
					RenderPlayer.class, null, 3);
			List<String> list = Arrays.asList(str1);
			ArrayList<String> arraylist = new ArrayList<String>();
			arraylist.addAll(list);
			armorsName = arraylist.toArray(new String[0]);
		} catch (Exception exception) {
		}
		armorsSelection = new int[] { -1, -1, -1, -1 };
		
		for (int i = 0; i < armorsSelection.length; i++) {
			for (int j = 0; j < armorsName.length; j++) {
				if (efplayer.armorPrefix[i] != null
						&& armorsName[j].compareTo(efplayer.armorPrefix[i]) == 0) {
					armorsSelection[i] = j;
					break;
				}
			}
		}
		
//		controlList.add(new GuiButton(153, width / 2 - 140, height / 6 + 0 + 12,
//				80, 20, getArmorName(armorsSelection[3])));
//		controlList.add(new GuiButton(152, width / 2 - 140, height / 6 + 24 + 12,
//				80, 20, getArmorName(armorsSelection[2])));
//		controlList.add(new GuiButton(151, width / 2 - 140, height / 6 + 48 + 12,
//				80, 20, getArmorName(armorsSelection[1])));
//		controlList.add(new GuiButton(150, width / 2 - 140, height / 6 + 72 + 12,
//				80, 20, getArmorName(armorsSelection[0])));
		controlList.add(new GuiButton(100, width / 2 - 140, height / 6 + 0 + 12, 80, 20, getSkinMode()));
		
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 150:
		case 151:
		case 152:
		case 153:
			// アーマーの変更
			int index = guibutton.id - 150;
			if (armorsSelection[index] < 0) {
				armorsSelection[index] = 0;
			} else if (++armorsSelection[index] == armorsName.length) {
				armorsSelection[index] = -1;
			}
			efplayer.armorPrefix[index] = getArmorName(armorsSelection[index]);
			guibutton.displayString = efplayer.armorPrefix[index];
			break;
			
		case 100:
			// スキンのロード元
			efplayer.setURLSkin(efplayer.skinUrl == null);
			guibutton.displayString = getSkinMode();
			break;
		}
	}

	public String getArmorName(int index) {
		if (index < 0) return "";
		return armorsName[index];
	}

	public String getSkinMode() {
		return efplayer.skinUrl != null ? "URL Skin" : "Local Skin";
	}

}
