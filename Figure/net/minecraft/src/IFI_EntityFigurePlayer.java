package net.minecraft.src;

public class IFI_EntityFigurePlayer extends EntityMob {

	public String armorPrefix[];

	public IFI_EntityFigurePlayer(World world) {
		super(world);
		armorPrefix = new String[] {"", "", "", ""};
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		for (int i = 0; i < armorPrefix.length; i++) {
			armorPrefix[i] = nbttagcompound.getString(String.format("Armor%d", i));
			if (armorPrefix[i] == null) {
				armorPrefix[i] = "";
			}
		}
		setURLSkin(nbttagcompound.getBoolean("URLSkin"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		for (int i = 0; i < armorPrefix.length; i++) {
			if (armorPrefix[i] == null) {
				armorPrefix[i] = "";
			}
			nbttagcompound.setString(String.format("Armor%d", i), armorPrefix[i]);
		}
		nbttagcompound.setBoolean("URLSkin", skinUrl != null);
	}

	@Override
	public int getMaxHealth() {
		return 20;
	}

	public void setURLSkin(boolean flag) {
		// URLスキンを有効にする
		if (flag) {
			skinUrl = (new StringBuilder()).append("http://s3.amazonaws.com/MinecraftSkins/").append(ModLoader.getMinecraftInstance().session.username).append(".png").toString();
		} else {
			skinUrl = null;
		}
	}

}
