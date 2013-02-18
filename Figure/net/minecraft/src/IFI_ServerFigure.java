package net.minecraft.src;

import static net.minecraft.src.IFI_Statics.IFI_Packet_Data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataOutput;

/**
 * サーバー側の受信処理。
 * GUIを作成した際はこちらも作ること。
 */
public class IFI_ServerFigure {

	public byte[] getData(IFI_EntityFigure pFigure) {
		try {
			ByteArrayOutputStream lba = new ByteArrayOutputStream();
			DataOutputStream lds = new DataOutputStream(lba);
			lds.writeByte(IFI_Packet_Data);
			lds.writeInt(pFigure.entityId);
			lds.writeByte(0);	// UpdateCount
			lds.writeFloat(pFigure.additionalYaw);
			lds.writeFloat(pFigure.zoom);
			lds.writeByte(0);	// Flags
			lds.writeFloat(pFigure.renderEntity.rotationYawHead);
			lds.writeFloat(pFigure.renderEntity.rotationPitch);
			sendData(pFigure, lds);
			return lba.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 固有データを相手へ送る
	 */
	public void sendData(IFI_EntityFigure pFigure, DataOutput pData) throws IOException {
		
	}

	/**
	 * 固有データを受け取ってEntityへ設定する
	 */
	public void reciveData(IFI_EntityFigure pFigure, byte[] pData) {
		if (pFigure.hasRenderEntity()) {
			EntityLiving lel = pFigure.renderEntity;
			pFigure.additionalYaw = MMM_Helper.getFloat(pData, 6);
			pFigure.zoom = MMM_Helper.getFloat(pData, 10);
			lel.prevRotationYaw = lel.rotationYawHead = lel.prevRotationYawHead = MMM_Helper.getFloat(pData, 15);
			lel.prevRotationPitch = lel.rotationPitch = MMM_Helper.getFloat(pData, 19);
		}
	}

}
