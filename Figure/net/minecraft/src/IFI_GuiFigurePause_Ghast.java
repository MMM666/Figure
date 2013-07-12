package net.minecraft.src;

public class IFI_GuiFigurePause_Ghast extends IFI_GuiFigurePause {

	private EntityGhast eg;
	private String button102[] = { "Charge", "Fire" };


	public IFI_GuiFigurePause_Ghast(IFI_EntityFigure entityfigua) {
		super(entityfigua);
		eg = (EntityGhast) targetEntity.renderEntity;
	}

	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(102, width / 2 - 140, height / 6 + 0 + 12, 80, 20,
				button102[eg.dataWatcher.getWatchableObjectByte(16) != 1 ? 0 : 1]));
	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 102:
			if (eg.dataWatcher.getWatchableObjectByte(16) == 1) {
				eg.dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
			} else {
				eg.dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
			}
			guibutton.displayString = button102[eg.dataWatcher
					.getWatchableObjectByte(16) != 1 ? 0 : 1];
			break;
		}
	}

}
