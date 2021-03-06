package net.minecraft.src;

public class IFI_GuiFigurePause_Sheep extends IFI_GuiFigurePause {

	private EntitySheep es;
	private String button102[] = { "Fullfrontal", "Sheared" };


	public IFI_GuiFigurePause_Sheep(IFI_EntityFigure entityfigua) {
		super(entityfigua);
		es = (EntitySheep) targetEntity.renderEntity;
	}

	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(102, width / 2 - 140, height / 6 + 0 + 12, 80, 20, 
				button102[es.getSheared() ? 0 : 1]));
		buttonList.add(new GuiButton(103, width / 2 - 140, height / 6 + 24 + 12, 80, 20,
				String.format("Color : %x", es.getFleeceColor())));
	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);

		if (!guibutton.enabled) {
			return;
		}
		switch (guibutton.id) {
		case 102:
			es.setSheared(!es.getSheared());
			guibutton.displayString = button102[es.getSheared() ? 0 : 1];
			break;

		case 103:
			if (es.getFleeceColor() == 15) {
				es.setFleeceColor(0);
			} else {
				es.setFleeceColor(es.getFleeceColor() + 1);
			}
			guibutton.displayString = String.format("Color : %x",
					es.getFleeceColor());
			break;
		}
	}

}
