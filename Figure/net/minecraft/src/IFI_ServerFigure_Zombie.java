package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IFI_ServerFigure_Zombie extends IFI_ServerFigure {

	@Override
	public void sendData(IFI_EntityFigure pFigure, DataOutput pData)
			throws IOException {
		EntityZombie lentity = (EntityZombie)pFigure.renderEntity;
		int lf = (lentity.isVillager() ? 1 : 0) |
				(lentity.isChild() ? 2 : 0);
		pData.writeByte(lf);
	}

	@Override
	public void reciveData(IFI_EntityFigure pFigure, DataInput pData)
			throws IOException {
		EntityZombie lentity = (EntityZombie)pFigure.renderEntity;
		int lf = pData.readByte();
		lentity.setVillager((lf & 1) != 0);
//		lentity.setChild((lf & 2) != 0);
		lentity.getDataWatcher().updateObject(12, ((lf & 2) != 0) ? (byte)1 : (byte)0);

	}

}
