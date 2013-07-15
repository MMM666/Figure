package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class IFI_RenderFigurePlayer extends RenderBiped {

	private ModelBiped modelBipedMain;
	private ModelBiped modelArmorChestplate;
	private ModelBiped modelArmor;

	public IFI_RenderFigurePlayer() {
		super(new ModelBiped(0.0F), 0.5F);
		
		modelBipedMain = (ModelBiped) mainModel;
		modelArmorChestplate = new ModelBiped(1.0F);
		modelArmor = new ModelBiped(0.5F);
	}

	protected ResourceLocation func_110856_a(EntityLiving par1EntityLiving) {
		return ((IFI_EntityFigurePlayer)par1EntityLiving).func_110306_p();
	}

	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
		super.preRenderCallback(par1EntityLiving, par2);
		float var3 = 0.9375F;
		GL11.glScalef(var3, var3, var3);
	}

	public void doRenderFigurePlayer(IFI_EntityFigurePlayer entityfigureplayer,
			double d, double d1, double d2, float f, float f1) {
		modelArmorChestplate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak =
				entityfigureplayer.isSneaking();
		modelArmorChestplate.isRiding = modelArmor.isRiding = modelBipedMain.isRiding =
				entityfigureplayer.isRiding();
		double d3 = d1;
		if (entityfigureplayer.isSneaking()) {
			d3 -= 0.125D;
		}
		doRenderLiving(entityfigureplayer, d, d3, d2, f, f1);
		modelArmorChestplate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak = false;
		modelArmorChestplate.isRiding = modelArmor.isRiding = modelBipedMain.isRiding = false;
	}

	@Override
	public void doRenderLiving(EntityLiving par1EntityLiving, double par2,
			double par4, double par6, float par8, float par9) {
		super.doRenderLiving(par1EntityLiving, par2, par4, par6, par8, par9);
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		doRenderFigurePlayer((IFI_EntityFigurePlayer) entity, d, d1, d2, f, f1);
	}

}
