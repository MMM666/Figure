package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IFI_ServerFigure_Sheep extends IFI_ServerFigure {

	@Override
	public void sendData(IFI_EntityFigure pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntitySheep lentity = (EntitySheep)pFigure.renderEntity;
		pData.writeByte(lentity.getFleeceColor());
		pData.writeBoolean(lentity.getSheared());
	}

	@Override
	public void reciveData(IFI_EntityFigure pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntitySheep lentity = (EntitySheep)pFigure.renderEntity;
		lentity.setFleeceColor(pData.readByte());
		lentity.setSheared(pData.readBoolean());
	}

}
