package net.minecraft.src;

public class IFI_GuiFigurePause_Wolf extends IFI_GuiFigurePause {

	private EntityWolf ew;
	private String button102[] = { "Contract", "Wild" };
	private String button103[] = { "Sitting", "Standing" };
	private String button104[] = { "Angry", "Calm" };

	public IFI_GuiFigurePause_Wolf(IFI_EntityFigure entityfigure) {
		super(entityfigure);
		ew = (EntityWolf) targetEntity.renderEntity;
	}

	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(102, width / 2 - 140,
				height / 6 + 0 + 12, 80, 20, button102[ew.isTamed() ? 0 : 1]));
		buttonList
				.add(new GuiButton(103, width / 2 - 140, height / 6 + 24 + 12,
						80, 20, button103[ew.isSitting() ? 0 : 1]));
		buttonList.add(new GuiButton(104, width / 2 - 140,
				height / 6 + 48 + 12, 80, 20, button104[ew.isAngry() ? 0 : 1]));

		buttonList.add(new GuiButton(150, width / 2 - 120,
				height / 6 + 72 + 12, 40, 20, String.format("%d", ew.health)));
		buttonList.add(new GuiButton(151, width / 2 - 140,
				height / 6 + 72 + 12, 20, 20, "+"));
		buttonList.add(new GuiButton(152, width / 2 - 80,
				height / 6 + 72 + 12, 20, 20, "-"));
	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 102:
			ew.setTamed(!ew.isTamed());
			ew.setOwner(ew.isTamed() ? "Figure" : "");
			guibutton.displayString = button102[ew.isTamed() ? 0 : 1];
			break;

		case 103:
			ew.setSitting(!ew.isSitting());
			guibutton.displayString = button103[ew.isSitting() ? 0 : 1];
			break;

		case 104:
			ew.setAngry(!ew.isAngry());
			guibutton.displayString = button104[ew.isAngry() ? 0 : 1];
			break;
		}

		if (guibutton.id == 150) {
			ew.health = 10;
		}
		if (guibutton.id == 151) {
			if (ew.health < ew.getMaxHealth())
				ew.health++;
		}
		if (guibutton.id == 152) {
			if (ew.health > 0)
				ew.health--;
		}
		for (int k = 0; k < buttonList.size(); k++) {
			GuiButton gb = (GuiButton) buttonList.get(k);
			if (gb.id == 150) {
				ew.dataWatcher.updateObject(18, Integer.valueOf(ew.health));
				gb.displayString = String.format("%d", ew.health);
			}
		}

	}

}
