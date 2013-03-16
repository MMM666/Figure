package net.minecraft.src;

public class IFI_GuiFigurePause_Villager extends IFI_GuiFigurePause {

	public EntityVillager efvillager;
	protected static String sb100[] = { "farmer", "librarian", "priest", "smith", "butcher", "villager" };


	public IFI_GuiFigurePause_Villager(IFI_EntityFigure entityfigure) {
		super(entityfigure);
		efvillager = (EntityVillager)entityfigure.renderEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		
		buttonList.add(new GuiButton(100, width / 2 - 140, height / 6 + 0 + 12, 80, 20, sb100[efvillager.getProfession()]));
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
			int li = efvillager.getProfession() + 1;
			if (li >= sb100.length) {
				li = 0;
			}
			efvillager.setProfession(li);
			guibutton.displayString = sb100[li];
			break;
		}
	}

}
