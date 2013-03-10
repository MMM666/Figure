package net.minecraft.src;

public class IFI_EntityFigurePlayer extends EntityMob {

	public String skinUser;

	public IFI_EntityFigurePlayer(World world) {
		super(world);
		skinUser = null;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		skinUser = nbttagcompound.getString("SkinUser");
//		setURLSkin();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		if (skinUser == null || skinUser.isEmpty()) {
			nbttagcompound.setString("SkinUser", "");
		} else {
			nbttagcompound.setString("SkinUser", skinUser);
		}
	}

	@Override
	public int getMaxHealth() {
		return 20;
	}

	public void setURLSkin() {
		// URLƒXƒLƒ“‚ð—LŒø‚É‚·‚é
		if (!MMM_Helper.isClient) return;
		if (skinUser == null || skinUser.isEmpty()) {
			skinUrl = null;
		} else {
			skinUrl = "http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(skinUser) + ".png";
//			skinUrl = (new StringBuilder()).append("http://s3.amazonaws.com/MinecraftSkins/").append(ModLoader.getMinecraftInstance().session.username).append(".png").toString();
		}
	}

}
