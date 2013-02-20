package net.minecraft.src;

public class IFI_GuiFigurePause_Enderman extends IFI_GuiFigurePause {

	private EntityEnderman eenderman;
	private int carringid;
	private GuiButton bt150;
	private String button102[] = { "Attack", "Nomal" };
	private String button103[] = { "Carried", "Free" };


	public IFI_GuiFigurePause_Enderman(IFI_EntityFigure entityfigua) {
		super(entityfigua);
		eenderman = (EntityEnderman) targetEntity.renderEntity;
	}

	public void initGui() {
		super.initGui();
		
		carringid = eenderman.getCarried();
		if (carringid == 0) {
			carringid = 2;
		}
		controlList.add(new GuiButton(102, width / 2 - 140,
				height / 6 + 0 + 12, 80, 20,
				button102[eenderman.isScreaming() ? 0 : 1]));
		controlList.add(new GuiButton(103, width / 2 - 140,
				height / 6 + 24 + 12, 80, 20,
				button103[eenderman.getCarried() > 0 ? 0 : 1]));
		
		bt150 = new GuiButton(150, width / 2 - 120, height / 6 + 48 + 12, 40,
				20, String.format("%d", carringid));
		controlList.add(bt150);
		controlList.add(new GuiButton(151, width / 2 - 140,
				height / 6 + 48 + 12, 20, 20, "+"));
		controlList.add(new GuiButton(152, width / 2 - 80,
				height / 6 + 48 + 12, 20, 20, "-"));
		
	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		
		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 150) {
			carringid = 2;
		}
		if (guibutton.id == 151) {
			try {
				boolean canpic[] = (boolean[]) ModLoader.getPrivateValue(
						EntityEnderman.class, null, 0);
				int i = (carringid + 1) & 0xff;
				while (i != carringid) {
					if (canpic[i]) {
						carringid = i;
						break;
					}
					i = (i + 1) & 0xff;
				}
			} catch (Exception exception) {
			}
		}
		if (guibutton.id == 152) {
			try {
				boolean canpic[] = (boolean[]) ModLoader.getPrivateValue(
						EntityEnderman.class, null, 0);
				int i = (carringid - 1) & 0xff;
				while (i != carringid) {
					if (canpic[i]) {
						carringid = i;
						break;
					}
					i = (i - 1) & 0xff;
				}
			} catch (Exception exception) {
			}
		}
		switch (guibutton.id) {
		case 102:
			eenderman.setScreaming(!eenderman.isScreaming());
			guibutton.displayString = button102[eenderman.isScreaming() ? 0 : 1];
			break;
			
		case 103:
			eenderman.setCarried(eenderman.getCarried() > 0 ? 0 : carringid);
			guibutton.displayString = button103[eenderman.getCarried() > 0 ? 0 : 1];
			break;
			
		case 150:
		case 151:
		case 152:
			bt150.displayString = String.format("%d", carringid);
			if (eenderman.getCarried() > 0) {
				eenderman.setCarried(carringid);
			}
			break;
		}
	}

}
