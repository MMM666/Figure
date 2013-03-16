package net.minecraft.src;

public class IFI_GuiFigurePause_Ozelot extends IFI_GuiFigurePause {

	private EntityOcelot eo;
	private String button102[] = { "Contract", "Wild" };
	private String button103[] = { "Sitting", "Standing" };
	private String button104[] = { "ozelot", "black", "red", "siamese" };

	public IFI_GuiFigurePause_Ozelot(IFI_EntityFigure entityfigure) {
		super(entityfigure);
		eo = (EntityOcelot) targetEntity.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(102, width / 2 - 140,
				height / 6 + 0 + 12, 80, 20, button102[eo.isTamed() ? 0 : 1]));
		buttonList
				.add(new GuiButton(103, width / 2 - 140, height / 6 + 24 + 12,
						80, 20, button103[eo.isSitting() ? 0 : 1]));
		buttonList.add(new GuiButton(104, width / 2 - 140,
				height / 6 + 48 + 12, 80, 20, button104[eo.getTameSkin()]));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 102:
			eo.setTamed(!eo.isTamed());
			eo.setOwner(eo.isTamed() ? "Figure" : "");
			guibutton.displayString = button102[eo.isTamed() ? 0 : 1];
			break;

		case 103:
			eo.setSitting(!eo.isSitting());
			guibutton.displayString = button103[eo.isSitting() ? 0 : 1];
			break;

		case 104:
			eo.setTameSkin((eo.getTameSkin() + 1) % button104.length);
			guibutton.displayString = button104[(int) eo.getTameSkin()];
			break;
		}
	}

}
