package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IFI_ServerFigure_FigurePlayer extends IFI_ServerFigure {

	@Override
	public void sendData(IFI_EntityFigure pFigure, DataOutput pData)
			throws IOException {
		IFI_EntityFigurePlayer lentity = (IFI_EntityFigurePlayer)pFigure.renderEntity;
		pData.writeBoolean(lentity.skinUrl == null);
	}

	@Override
	public void reciveData(IFI_EntityFigure pFigure, DataInput pData)
			throws IOException {
		IFI_EntityFigurePlayer lentity = (IFI_EntityFigurePlayer)pFigure.renderEntity;
		lentity.setURLSkin(pData.readBoolean());
	}

}