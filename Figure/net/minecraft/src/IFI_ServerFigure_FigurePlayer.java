package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IFI_ServerFigure_FigurePlayer extends IFI_ServerFigure {

	@Override
	public void sendData(IFI_EntityFigure pFigure, DataOutput pData)
			throws IOException {
		IFI_EntityFigurePlayer lentity = (IFI_EntityFigurePlayer)pFigure.renderEntity;
		if (lentity.skinUser != null) {
			pData.writeUTF(lentity.skinUser);
		} else {
			pData.writeUTF("");
		}
	}

	@Override
	public void reciveData(IFI_EntityFigure pFigure, DataInput pData)
			throws IOException {
		IFI_EntityFigurePlayer lentity = (IFI_EntityFigurePlayer)pFigure.renderEntity;
		lentity.skinUser = pData.readUTF();
		lentity.setURLSkin();
	}

}
