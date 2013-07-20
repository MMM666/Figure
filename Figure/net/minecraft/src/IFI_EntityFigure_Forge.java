package net.minecraft.src;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class IFI_EntityFigure_Forge extends IFI_EntityFigure
	implements IEntityAdditionalSpawnData {

	public IFI_EntityFigure_Forge(World world) {
		super(world);
	}
	public IFI_EntityFigure_Forge(World world, Entity entity) {
		super(world, entity);
	}
	public IFI_EntityFigure_Forge(World world, int index) {
		super(world, index);
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
//		data.writeInt(mobIndex);
		data.writeFloat(rotationYaw);
		data.writeFloat(rotationPitch);
		data.writeUTF(mobString);
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		Entity lentity;
//		lentity = EntityList.createEntityByID(data.readInt(), worldObj);
		setRotation(data.readFloat(), data.readFloat());
		lentity = EntityList.createEntityByName(data.readUTF(), worldObj);
		setRenderEntity((EntityLivingBase) lentity);
		IFI_Client.getGui(this);
	}

}
