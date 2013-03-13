package net.minecraft.src;

import static net.minecraft.src.IFI_Statics.IFI_Packet_Data;
import static net.minecraft.src.IFI_Statics.IFI_Packet_UpadteItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * サーバー側の受信処理。 GUIを作成した際はこちらも作ること。
 */
public class IFI_ServerFigure {

	protected byte[] getData(IFI_EntityFigure pFigure) {
		try {
			ByteArrayOutputStream lba = new ByteArrayOutputStream();
			DataOutputStream lds = new DataOutputStream(lba);
			EntityLiving lentity = pFigure.renderEntity;
			lds.writeByte(IFI_Packet_Data);
			lds.writeInt(pFigure.entityId);
			lds.writeByte(0); // UpdateCount
			lds.writeFloat(pFigure.additionalYaw);
			lds.writeFloat(pFigure.zoom);
			int lf = (lentity.isRiding() ? 1 : 0) |
					(lentity.isSneaking() ? 2 : 0) |
					(lentity.isChild() ? 4 : 0);
			lds.writeByte(lf); // Flags
			lds.writeFloat(lentity.rotationYaw);
			lds.writeFloat(lentity.rotationPitch);
			lds.writeFloat(pFigure.fyOffset);
			sendData(pFigure, lds);
			return lba.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}

	protected void setData(IFI_EntityFigure pFigure, byte[] pData) {
		if (pFigure.hasRenderEntity()) {
			try {
				ByteArrayInputStream lba = new ByteArrayInputStream(pData);
				DataInputStream lds = new DataInputStream(lba);
				EntityLiving lel = pFigure.renderEntity;
				lds.readByte(); // IFI_Packet_Data);
				lds.readInt(); // pFigure.entityId;
				lds.readByte(); // UpdateCount
				pFigure.additionalYaw = lds.readFloat();
				pFigure.zoom = lds.readFloat();
				int lf = lds.readByte(); // Flags
				lel.setFlag(2, (lf & 1) != 0);
				lel.setSneaking((lf & 2) != 0);
				if (lel instanceof EntityAgeable) {
					((EntityAgeable)lel).setGrowingAge(-(lf & 4));
				}
				lel.rotationYaw = lel.prevRotationYaw =
						lel.rotationYawHead = lel.prevRotationYawHead = lds.readFloat();
				lel.rotationPitch = lel.prevRotationPitch = lds.readFloat();
				pFigure.fyOffset = lds.readFloat();
				reciveData(pFigure, lds);
				pFigure.setZoom(pFigure.zoom);
			} catch (Exception e) {
			}
		}
	}

	protected void sendItem(int pIndex, IFI_EntityFigure pFigure, boolean pClient) {
		// ItemStackを送信
		ItemStack litemstack = pFigure.renderEntity.getCurrentItemOrArmor(pIndex);
		try {
			ByteArrayOutputStream lbo = new ByteArrayOutputStream();
			DataOutputStream ldo = new DataOutputStream(lbo);
			ldo.writeByte(IFI_Packet_UpadteItem);
			ldo.writeInt(pFigure.entityId);
			ldo.writeByte(pIndex);
			Packet.writeItemStack(litemstack, ldo);
			Packet250CustomPayload lpacket = new Packet250CustomPayload("IFI|Upd", lbo.toByteArray());
			if (pClient) {
				IFI_Client.sendToServer(lpacket);
				mod_IFI_Figure.Debug("ItemSet ID:%d, Slot:%d, Item:%s Client.",
						pFigure.entityId, pIndex, litemstack.toString());
			} else {
				((WorldServer)pFigure.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(pFigure, lpacket);
				mod_IFI_Figure.Debug("ItemSetToAllClient.");
			}
		} catch (Exception e) {
		}
	}

	protected void reciveItem(IFI_EntityFigure pFigure, byte[] pData) {
		int lslotid3 = 0;
		ItemStack lis3 = null;
		try {
			ByteArrayInputStream lbi3 = new ByteArrayInputStream(pData);
			DataInputStream ldi3 = new DataInputStream(lbi3);
			ldi3.readByte();
			ldi3.readInt();
			lslotid3 = ldi3.readByte();
			lis3 = Packet.readItemStack(ldi3);
			pFigure.renderEntity.setCurrentItemOrArmor(lslotid3, lis3);
			mod_IFI_Figure.Debug("ItemSet ID:%d, Slot:%d, Item:%s", pFigure.entityId, lslotid3, lis3.toString());
		} catch (Exception e) {
		}
	}


	/**
	 * 固有データを相手へ送る。 
	 * 継承はこちら。
	 */
	public void sendData(IFI_EntityFigure pFigure, DataOutput pData) throws IOException {}

	/**
	 * 固有データを受け取ってEntityへ設定する。
	 * 継承はこちら。
	 */
	public void reciveData(IFI_EntityFigure pFigure, DataInput pData) throws IOException {}

	/**
	 * 特殊なデータ読み込みを実行
	 */
	public void readEntityFromNBT(IFI_EntityFigure pFigure, NBTTagCompound nbttagcompound) {}

	/**
	 * 特殊なデータ書き込みを実行
	 */
	public void writeEntityToNBT(IFI_EntityFigure pFigure, NBTTagCompound nbttagcompound) {}

	/**
	 * 姿勢制御用
	 */
	public void setRotation(IFI_EntityFigure pFigure) {}
	
	/**
	 * サーバーへ設定されたアイテムを送信。
	 */
	public void sendItems(IFI_EntityFigure pFigure, boolean pClient) {
		sendItem(4, pFigure, pClient);
		sendItem(3, pFigure, pClient);
		sendItem(2, pFigure, pClient);
		sendItem(1, pFigure, pClient);
		sendItem(0, pFigure, pClient);
	}

}
