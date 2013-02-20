package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IFI_ServerFigure_Ghast extends IFI_ServerFigure {

	@Override
	public void sendData(IFI_EntityFigure pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityGhast lentity = (EntityGhast)pFigure.renderEntity;
		pData.writeByte(lentity.dataWatcher.getWatchableObjectByte(16));
	}

	@Override
	public void reciveData(IFI_EntityFigure pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityGhast lentity = (EntityGhast)pFigure.renderEntity;
		lentity.dataWatcher.updateObject(16, (byte)pData.readByte());
		lentity.texture = lentity.dataWatcher.getWatchableObjectByte(16) != 1
				? "/mob/ghast.png" : "/mob/ghast_fire.png";
	}

	@Override
	public void readEntityFromNBT(IFI_EntityFigure pFigure, NBTTagCompound nbttagcompound) {
		// 特殊なデータ読み込みを実行
		pFigure.renderEntity.dataWatcher.updateObject(16, nbttagcompound.getByte("dw16"));
	}

	@Override
	public void writeEntityToNBT(IFI_EntityFigure pFigure, NBTTagCompound nbttagcompound) {
		// 特殊なデータ書き込みを実行
		byte b = pFigure.renderEntity.dataWatcher.getWatchableObjectByte(16);
		nbttagcompound.setByte("dw16", b);
	}

}
