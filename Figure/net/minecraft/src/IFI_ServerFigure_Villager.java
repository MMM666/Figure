package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IFI_ServerFigure_Villager extends IFI_ServerFigure {

	@Override
	public void sendData(IFI_EntityFigure pFigure, DataOutput pData)
			throws IOException {
		EntityVillager lentity = (EntityVillager)pFigure.renderEntity;
		pData.writeInt(lentity.getProfession());
	}

	@Override
	public void reciveData(IFI_EntityFigure pFigure, DataInput pData)
			throws IOException {
		EntityVillager lentity = (EntityVillager)pFigure.renderEntity;
		lentity.setProfession(pData.readInt());
	}

}
