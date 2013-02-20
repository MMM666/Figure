package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IFI_ServerFigure_Enderman extends IFI_ServerFigure {

	@Override
	public void sendData(IFI_EntityFigure pFigure, DataOutput pData)
			throws IOException {
		super.sendData(pFigure, pData);
		EntityEnderman lentity = (EntityEnderman)pFigure.renderEntity;
		pData.writeInt(lentity.getCarried());
		pData.writeBoolean(lentity.isScreaming());
	}

	@Override
	public void reciveData(IFI_EntityFigure pFigure, DataInput pData)
			throws IOException {
		super.reciveData(pFigure, pData);
		EntityEnderman lentity = (EntityEnderman)pFigure.renderEntity;
		lentity.setCarried(pData.readInt());
		lentity.setScreaming(pData.readBoolean());
	}

	@Override
	public void readEntityFromNBT(IFI_EntityFigure pFigure, NBTTagCompound nbttagcompound) {
		// 特殊なデータ読み込みを実行
		((EntityEnderman)pFigure.renderEntity).setScreaming(nbttagcompound.getBoolean("Attacking"));
	}

	public void writeEntityToNBT(IFI_EntityFigure pFigure, NBTTagCompound nbttagcompound) {
		// 特殊なデータ書き込みを実行
		nbttagcompound.setBoolean("Attacking", ((EntityEnderman)pFigure.renderEntity).isScreaming());
	}

}
