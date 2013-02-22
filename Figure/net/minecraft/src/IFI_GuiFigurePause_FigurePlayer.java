package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.openmbean.ArrayType;

public class IFI_GuiFigurePause_FigurePlayer extends IFI_GuiFigurePause {

	public IFI_EntityFigurePlayer efplayer;

	public IFI_GuiFigurePause_FigurePlayer(IFI_EntityFigure entityfigure) {
		super(entityfigure);
		efplayer = (IFI_EntityFigurePlayer) entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		
		controlList.add(new GuiButton(100, width / 2 - 140, height / 6 + 0 + 12, 80, 20, getSkinMode()));
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
			efplayer.setURLSkin(efplayer.skinUrl == null);
			guibutton.displayString = getSkinMode();
			break;
		}
	}

	public String getSkinMode() {
		return efplayer.skinUrl != null ? "URL Skin" : "Local Skin";
	}

}
