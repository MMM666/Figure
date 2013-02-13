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
		controlList.add(new GuiButton(102, width / 2 - 140,
				height / 6 + 0 + 12, 80, 20, button102[eo.isTamed() ? 0 : 1]));
		controlList
				.add(new GuiButton(103, width / 2 - 140, height / 6 + 24 + 12,
						80, 20, button103[eo.isSitting() ? 0 : 1]));
		controlList.add(new GuiButton(104, width / 2 - 140,
				height / 6 + 48 + 12, 80, 20, button104[eo.getTameSkin()]));
		/*
		 * controlList.add(new GuiButton(150, width / 2 - 120, height / 6 + 72 +
		 * 12, 40, 20, String.format("%d", eo.health))); controlList.add(new
		 * GuiButton(151, width / 2 - 140, height / 6 + 72 + 12, 20, 20, "+"));
		 * controlList.add(new GuiButton(152, width / 2 - 80, height / 6 + 72 +
		 * 12, 20, 20, "-"));
		 */
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		// 特殊なデータ読み込みを実行
		// eo.dataWatcher.updateObject(18, (byte)Integer.valueOf(eo.health));
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
		/*
		 * if (guibutton.id == 150) { eo.health = 10; } if (guibutton.id == 151)
		 * { if (eo.health < eo.getMaxHealth()) eo.health++; } if (guibutton.id
		 * == 152) { if (eo.health > 0) eo.health--; } for(int k = 0; k <
		 * controlList.size(); k++) { GuiButton gb =
		 * (GuiButton)controlList.get(k); if (gb.id == 150) {
		 * eo.dataWatcher.updateObject(18, (byte)Integer.valueOf(eo.health));
		 * gb.displayString = String.format("%d", eo.health); } }
		 */

	}

}
