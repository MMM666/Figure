package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IFI_ServerFigure_Wolf extends IFI_ServerFigure {

	@Override
	public void sendData(IFI_EntityFigure pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityWolf lentity = (EntityWolf)pFigure.renderEntity;
		pData.writeFloat(lentity.func_110143_aJ());
		int lf = (lentity.isTamed() ? 1 : 0) |
				(lentity.isAngry() ? 2 : 0);
		pData.writeByte(lf);
	}

	@Override
	public void reciveData(IFI_EntityFigure pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityWolf lentity = (EntityWolf)pFigure.renderEntity;
		lentity.setEntityHealth(pData.readFloat());
		int lf = pData.readByte();
		lentity.setTamed((lf & 1) != 0);
		lentity.setAngry((lf & 2) != 0);
	}

}
