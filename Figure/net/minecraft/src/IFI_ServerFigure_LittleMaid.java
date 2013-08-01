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
				(lentity.isContract() ? 2 : 0) |
				(lentity.mstatAimeBow ? 4 : 0);
		pData.writeByte(lf);
		pData.writeByte(lentity.getColor());
		if (pFigure.worldObj.isRemote) {
			lentity.textureData.textureIndex[0] = MMM_TextureManager.instance.getIndexTextureBoxServerIndex((MMM_TextureBox)lentity.textureData.textureBox[0]);
			lentity.textureData.textureIndex[1] = MMM_TextureManager.instance.getIndexTextureBoxServerIndex((MMM_TextureBox)lentity.textureData.textureBox[1]);
//			lentity.textureIndex[1] = MMM_TextureManager.instance.getIndexTextureBoxServer(lentity, lentity.textureBox[1].textureName);
		}
		pData.writeInt(lentity.textureData.textureIndex[0]);
		pData.writeInt(lentity.textureData.textureIndex[1]);
		mod_IFI_Figure.Debug("tex-s(%s): %d,  %d : %d", pFigure.worldObj.isRemote ? "CL->SV" : "SV->CL", 
				lentity.textureData.textureIndex[0], lentity.textureData.textureIndex[1], lentity.textureData.getColor());
	}

	@Override
	public void reciveData(IFI_EntityFigure pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		LMM_EntityLittleMaid lentity = (LMM_EntityLittleMaid)pFigure.renderEntity;
		int lf = pData.readByte();
		lentity.setMaidWait((lf & 1) != 0);
		lentity.textureData.setContract((lf & 2) != 0);
		lentity.setContract(lentity.textureData.isContract());
		lentity.setOwner(lentity.textureData.isContract() ? "Figure" : "");
		lentity.mstatAimeBow = (lf & 4) != 0;
		lentity.updateAimebow();
		lentity.setColor(pData.readByte());
		lentity.textureData.textureIndex[0] = pData.readInt();
		lentity.textureData.textureIndex[1] = pData.readInt();
		if (pFigure.worldObj.isRemote) {
			// Client
			MMM_TextureManager.instance.postGetTexturePack(lentity, lentity.textureData.getTextureIndex());
		} else {
			// Server
			lentity.setTexturePackIndex(lentity.textureData.getColor(), lentity.textureData.getTextureIndex());
		}
		mod_IFI_Figure.Debug("tex-r(%s): %d,  %d : %d",
				pFigure.worldObj.isRemote ? "SV->CL" : "CL->SV",
				lentity.textureData.textureIndex[0], lentity.textureData.textureIndex[1], lentity.textureData.color);
		
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
//		lentity.setTextureNames();
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

	@Override
	public void readEntityFromNBT(IFI_EntityFigure pFigure, NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(pFigure, nbttagcompound);
		((LMM_EntityLittleMaid)pFigure.renderEntity).mstatAimeBow = nbttagcompound.getBoolean("Aimbow");
		((LMM_EntityLittleMaid)pFigure.renderEntity).updateAimebow();
	}

	@Override
	public void writeEntityToNBT(IFI_EntityFigure pFigure, NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(pFigure, nbttagcompound);
		nbttagcompound.setBoolean("Aimbow", ((LMM_EntityLittleMaid)pFigure.renderEntity).isAimebow());
	}

}
