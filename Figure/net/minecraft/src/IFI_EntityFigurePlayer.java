package net.minecraft.src;

public class IFI_EntityFigurePlayer extends EntityMob {

	public String skinUser;
	private ThreadDownloadImageData fskinDownload;
	private ResourceLocation fskinResorce;


	public IFI_EntityFigurePlayer(World world) {
		super(world);
		skinUser = null;
		setURLSkin();
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

	public void setURLSkin() {
		// URLƒXƒLƒ“‚ð—LŒø‚É‚·‚é
		if (!MMM_Helper.isClient) return;
		if (skinUser != null && !skinUser.isEmpty()) {
			fskinResorce = IFI_Client.func_110311_f(skinUser);
			fskinDownload = IFI_Client.func_110304_a(this.fskinResorce, skinUser);
		} else {
			fskinResorce = AbstractClientPlayer.field_110314_b;
		}
	}

	public ThreadDownloadImageData func_110309_l() {
		return this.fskinDownload;
	}

	public ResourceLocation func_110306_p() {
		return this.fskinResorce;
	}

}
