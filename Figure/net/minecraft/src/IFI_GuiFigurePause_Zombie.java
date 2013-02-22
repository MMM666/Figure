package net.minecraft.src;

public class IFI_GuiFigurePause_Zombie extends IFI_GuiFigurePause {

	public EntityZombie efzombie;


	public IFI_GuiFigurePause_Zombie(IFI_EntityFigure entityfigure) {
		super(entityfigure);
		efzombie = (EntityZombie)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		controlList.add(new GuiButton(100, width / 2 - 140, height / 6 + 0 + 12, 80, 20, efzombie.isVillager() ? "Villager" : "Crafter"));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 100:
			// スキンのロード元
			efzombie.setVillager(!efzombie.isVillager());
			guibutton.displayString = efzombie.isVillager() ? "Villager" : "Crafter";
			break;
		case 16:
			// 幼生態判定
//			efzombie.setChild(!efzombie.isChild());
			efzombie.getDataWatcher().updateObject(12, !efzombie.isChild() ? (byte)1 : (byte)0);
			guibutton.displayString = button16[efzombie.isChild() ? 1 : 0];
			return;
		}
		
		super.actionPerformed(guibutton);
	}

}
