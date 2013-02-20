package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IFI_ServerFigure_LittleMaid extends IFI_ServerFigure {

	@Override
	public void sendData(IFI_EntityFigure pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		LMM_EntityLittleMaid lentity = (LMM_EntityLittleMaid)pFigure.renderEntity;
		int lf = (lentity.maidWait ? 1 : 0) |
				(lentity.isMaidContract() ? 2 : 0);
		pData.writeByte(lf);
//		lentity.textureIndex = MMM_TextureManager.getStringToIndex(lentity.textureName);
//		lentity.textureArmorIndex = MMM_TextureManager.getStringToIndex(lentity.textureArmorName);
		pData.writeInt(lentity.textureIndex);
		pData.writeInt(lentity.textureArmorIndex);
	}

	@Override
	public void reciveData(IFI_EntityFigure pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		LMM_EntityLittleMaid lentity = (LMM_EntityLittleMaid)pFigure.renderEntity;
		int lf = pData.readByte();
		lentity.setMaidWait((lf & 1) != 0);
		lentity.maidContract = (lf & 2) != 0;
		lentity.setMaidContract(lentity.maidContract);
		lentity.setOwner(lentity.maidContract ? "Figure" : "");
		lentity.setTextureIndex(pData.readInt(), pData.readInt());
		LMM_Client.setTextureValue(lentity);
		
//		lentity.setDominantArm(0);
//		lentity.setEquipItem(0, 0);
//		lentity.setEquipItem(1, 1);
//		lentity.mstatMaskSelect = 16;
//		lentity.checkMaskedMaid();
//		lentity.checkHeadMount();
	}

	@Override
	protected void reciveItem(IFI_EntityFigure pFigure, byte[] pData) {
		super.reciveItem(pFigure, pData);
		LMM_EntityLittleMaid lentity = (LMM_EntityLittleMaid)pFigure.renderEntity;
		lentity.setDominantArm(0);
		lentity.setEquipItem(0, 0);
		lentity.setEquipItem(1, 1);
		lentity.checkMaskedMaid();
		lentity.checkHeadMount();
	}

	@Override
	public void sendItems(IFI_EntityFigure pFigure, boolean pClient) {
		sendItem(1, pFigure, pClient);
		sendItem(2, pFigure, pClient);
		sendItem(3, pFigure, pClient);
		sendItem(5, pFigure, pClient);
		sendItem(6, pFigure, pClient);
		sendItem(21, pFigure, pClient);
		sendItem(22, pFigure, pClient);
	}

}
