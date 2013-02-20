package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IFI_ServerFigure_Ozelot extends IFI_ServerFigure {

	@Override
	public void sendData(IFI_EntityFigure pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityOcelot lentity = (EntityOcelot)pFigure.renderEntity;
//		pData.writeInt(lentity.getHealth());
		pData.writeByte(lentity.getTameSkin());
		int lf = (lentity.isTamed() ? 1 : 0);
		pData.writeByte(lf);
	}

	@Override
	public void reciveData(IFI_EntityFigure pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityOcelot lentity = (EntityOcelot)pFigure.renderEntity;
//		lentity.setEntityHealth(pData.readInt());
		lentity.setTameSkin(pData.readByte());
		int lf = pData.readByte();
		lentity.setTamed((lf & 1) != 0);
	}

}
