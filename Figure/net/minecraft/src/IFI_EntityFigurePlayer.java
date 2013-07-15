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
			fskinResorce = func_110311_f(skinUser);
			fskinDownload = func_110304_a(this.fskinResorce, skinUser);
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

	public static ThreadDownloadImageData func_110304_a(
			ResourceLocation par0ResourceLocation, String par1Str) {
		return func_110301_a(par0ResourceLocation, func_110300_d(par1Str),
				AbstractClientPlayer.field_110314_b, new ImageBufferDownload());
	}

	private static ThreadDownloadImageData func_110301_a(
			ResourceLocation par0ResourceLocation, String par1Str,
			ResourceLocation par2ResourceLocation, IImageBuffer par3IImageBuffer) {
		TextureManager var4 = Minecraft.getMinecraft().func_110434_K();
		Object var5 = var4.func_110581_b(par0ResourceLocation);
		
		if (var5 == null) {
			var5 = new ThreadDownloadImageData(par1Str, par2ResourceLocation,
					par3IImageBuffer);
			var4.func_110579_a(par0ResourceLocation, (TextureObject) var5);
		}
		
		return (ThreadDownloadImageData) var5;
	}

	public static String func_110300_d(String par0Str) {
		return String.format(
				"http://skins.minecraft.net/MinecraftSkins/%s.png",
				new Object[] { StringUtils.stripControlCodes(par0Str) });
	}

	public static ResourceLocation func_110311_f(String par0Str) {
		return new ResourceLocation(
				"skins/" + StringUtils.stripControlCodes(par0Str));
	}

}
