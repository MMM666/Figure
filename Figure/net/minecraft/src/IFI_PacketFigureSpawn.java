package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Figure用スポーンパケット。 Modloader専用。
 */
public class IFI_PacketFigureSpawn extends Packet23VehicleSpawn {
	
	public int figureID;

	public IFI_PacketFigureSpawn(Entity par1Entity, int var2, int pFigureID) {
		super(par1Entity, var2);
		figureID = pFigureID;
	}

	public void readPacketData(DataInputStream par1DataInputStream)
			throws IOException {
		this.entityId = par1DataInputStream.readInt();
		this.type = par1DataInputStream.readByte();
		this.xPosition = par1DataInputStream.readInt();
		this.yPosition = par1DataInputStream.readInt();
		this.zPosition = par1DataInputStream.readInt();
		this.pitch = par1DataInputStream.readByte();
		this.yaw = par1DataInputStream.readByte();
		this.figureID = par1DataInputStream.readInt();
	}

	public void writePacketData(DataOutputStream par1DataOutputStream)
			throws IOException {
		par1DataOutputStream.writeInt(this.entityId);
		par1DataOutputStream.writeByte(this.type);
		par1DataOutputStream.writeInt(this.xPosition);
		par1DataOutputStream.writeInt(this.yPosition);
		par1DataOutputStream.writeInt(this.zPosition);
		par1DataOutputStream.writeByte(this.pitch);
		par1DataOutputStream.writeByte(this.yaw);
		par1DataOutputStream.writeInt(this.figureID);
	}

	public int getPacketSize() {
		return 23;
	}

	public void processPacket(NetHandler par1NetHandler) {
		// 独自スポーンでtypeとかTrackerのIDのとか無視。
		if (par1NetHandler instanceof NetClientHandler) {
			double lx = (double)xPosition / 32.0D;
			double ly = (double)yPosition / 32.0D;
			double lz = (double)zPosition / 32.0D;
			
			IFI_EntityFigure lentity = new IFI_EntityFigure(MMM_Helper.mc.theWorld, figureID);
			lentity.serverPosX = xPosition;
			lentity.serverPosY = yPosition;
			lentity.serverPosZ = zPosition;
			lentity.rotationPitch = (float) (pitch * 360) / 256.0F;
			lentity.rotationYaw = (float) (yaw * 360) / 256.0F;
			lentity.setPositionAndRotation2(lx, ly, lz, lentity.rotationYaw, lentity.rotationPitch, 1);
			Entity[] var13 = lentity.getParts();
			
			if (var13 != null) {
				int var14 = entityId - lentity.entityId;
				
				for (int var11 = 0; var11 < var13.length; ++var11) {
					var13[var11].entityId += var14;
				}
			}
			
			lentity.entityId = entityId;
			MMM_Helper.mc.theWorld.addEntityToWorld(entityId, lentity);
//			IFI_Client.getFigureData(lentity);
			mod_IFI_Figure.Debug("SpawnFigure %s, %f, %f, %f Client.",
					lentity.mobString, lentity.posX, lentity.posY, lentity.posZ);
		}
	}
}
