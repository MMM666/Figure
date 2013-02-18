package net.minecraft.src;

public class IFI_GuiFigurePause_Ghast extends IFI_GuiFigurePause {

	public IFI_GuiFigurePause_Ghast(IFI_EntityFigure entityfigua) {
		super(entityfigua);
    	eg = (EntityGhast)targetEntity.renderEntity;
	}
	
    public void initGui()
    {
    	super.initGui();
        controlList.add(new GuiButton(102, width / 2 - 140, height / 6 + 0 + 12, 80, 20, button102[eg.dataWatcher.getWatchableObjectByte(16) != 1 ? 0 : 1]));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    	// ����ȃf�[�^�ǂݍ��݂����s
		eg.dataWatcher.updateObject(16, nbttagcompound.getByte("dw16"));
        eg.texture = eg.dataWatcher.getWatchableObjectByte(16) != 1 ? "/mob/ghast.png" : "/mob/ghast_fire.png";
    }

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		// ����ȃf�[�^�������݂����s
		byte b = eg.dataWatcher.getWatchableObjectByte(16);
		nbttagcompound.setByte("dw16", b);
	}

    protected void actionPerformed(GuiButton guibutton)
    {
    	super.actionPerformed(guibutton);
    	
    	if(!guibutton.enabled)
        {
            return;
        }
    	switch (guibutton.id) {
    		case 102:
    			if (eg.dataWatcher.getWatchableObjectByte(16) == 1) {
    				eg.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
    			} else {
    				eg.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
    			}
            	guibutton.displayString = button102[eg.dataWatcher.getWatchableObjectByte(16) != 1 ? 0 : 1];
                eg.texture = eg.dataWatcher.getWatchableObjectByte(16) != 1 ? "/mob/ghast.png" : "/mob/ghast_fire.png";
            	break;
    	}
    }

    private EntityGhast eg;
    private String button102[] = {"Charge", "Fire"};


}