package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.openmbean.ArrayType;

public class IFI_GuiFigurePause_FigurePlayer extends IFI_GuiFigurePause {

	public IFI_EntityFigurePlayer efplayer;
	protected List<String> playerList;
	protected int playerIndex;

	public IFI_GuiFigurePause_FigurePlayer(IFI_EntityFigure entityfigure) {
		super(entityfigure);
		efplayer = (IFI_EntityFigurePlayer) entityfigure.renderEntity;
		playerList = new ArrayList<String>();
		playerList.add("Local Skin");
		for (Object lo : MMM_Helper.mc.theWorld.playerEntities) {
			EntityPlayer lep = (EntityPlayer)lo;
			if (lep.username != null && !lep.username.isEmpty()) {
				playerList.add(lep.username);
			}
		}
		if (efplayer.skinUser != null && !efplayer.skinUser.isEmpty()) {
			if (playerList.contains(efplayer.skinUser)) {
				playerIndex = playerList.indexOf(efplayer.skinUser);
			} else {
				playerIndex = -1;
			}
		} else {
			playerIndex = 0;
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		String ls = playerIndex == 0 ? playerList.get(0) : efplayer.skinUser;
		buttonList.add(new GuiButton(100, width / 2 - 140, height / 6 + 0 + 12, 80, 20, ls));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 100:
			// スキンのロード元
			
			if (playerList.size() <= ++playerIndex) {
				playerIndex = 0;
			}
			guibutton.displayString = playerList.get(playerIndex);
			efplayer.skinUser = playerIndex == 0 ? "" : guibutton.displayString;
			efplayer.setURLSkin();
			break;
		}
	}

}
