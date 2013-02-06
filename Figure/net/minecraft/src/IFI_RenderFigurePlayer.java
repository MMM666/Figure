package net.minecraft.src;

public class IFI_RenderFigurePlayer extends RenderLiving {

	private ModelBiped modelBipedMain;
	private ModelBiped modelArmorChestplate;
	private ModelBiped modelArmor;

	public IFI_RenderFigurePlayer() {
		super(new ModelBiped(0.0F), 0.5F);
		
		modelBipedMain = (ModelBiped) mainModel;
		modelArmorChestplate = new ModelBiped(1.0F);
		modelArmor = new ModelBiped(0.5F);
	}

	protected int setArmorModel(IFI_EntityFigurePlayer entityfigureplayer, int i, float f) {
		String prefix = entityfigureplayer.armorPrefix[3 - i];
		if (prefix != null && !prefix.isEmpty()) {
			loadTexture((new StringBuilder()).append("/armor/").append(prefix)
					.append("_").append(i != 2 ? 1 : 2).append(".png").toString());
			ModelBiped modelbiped = i != 2 ? modelArmorChestplate : modelArmor;
			modelbiped.bipedHead.showModel = i == 0;
			modelbiped.bipedHeadwear.showModel = i == 0;
			modelbiped.bipedBody.showModel = i == 1 || i == 2;
			modelbiped.bipedRightArm.showModel = i == 1;
			modelbiped.bipedLeftArm.showModel = i == 1;
			modelbiped.bipedRightLeg.showModel = i == 2 || i == 3;
			modelbiped.bipedLeftLeg.showModel = i == 2 || i == 3;
			setRenderPassModel(modelbiped);
			return 1;
		}
		return -1;
	}

	public void renderFigurePlayer(IFI_EntityFigurePlayer entityfigureplayer,
			double d, double d1, double d2, float f, float f1) {
		modelArmorChestplate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak =
				entityfigureplayer.isSneaking();
		modelArmorChestplate.isRiding = modelArmor.isRiding = modelBipedMain.isRiding =
				entityfigureplayer.isRiding();
		double d3 = d1;
		if (entityfigureplayer.isSneaking()) {
			d3 -= 0.125D;
		}
		super.doRenderLiving(entityfigureplayer, d, d3, d2, f, f1);
		modelArmorChestplate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak = false;
		modelArmorChestplate.isRiding = modelArmor.isRiding = modelBipedMain.isRiding = false;
	}

	@Override
	protected int shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return setArmorModel((IFI_EntityFigurePlayer) entityliving, i, f);
	}

	@Override
	public void doRenderLiving(EntityLiving entityliving,
			double d, double d1, double d2, float f, float f1) {
		renderFigurePlayer((IFI_EntityFigurePlayer) entityliving, d, d1, d2, f, f1);
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		renderFigurePlayer((IFI_EntityFigurePlayer) entity, d, d1, d2, f, f1);
	}

}
