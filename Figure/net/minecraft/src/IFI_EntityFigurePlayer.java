package net.minecraft.src;

public class IFI_EntityFigurePlayer extends EntityMob {

	public IFI_EntityFigurePlayer(World world) {
		super(world);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		setURLSkin(nbttagcompound.getBoolean("URLSkin"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("URLSkin", skinUrl != null);
	}

	@Override
	public int getMaxHealth() {
		return 20;
	}

	public void setURLSkin(boolean flag) {
		// URLƒXƒLƒ“‚ð—LŒø‚É‚·‚é
		if (flag) {
			skinUrl = (new StringBuilder()).append("http://s3.amazonaws.com/MinecraftSkins/").append(ModLoader.getMinecraftInstance().session.username).append(".png").toString();
		} else {
			skinUrl = null;
		}
	}

}
