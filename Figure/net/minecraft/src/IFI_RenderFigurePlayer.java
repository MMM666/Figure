package net.minecraft.src;

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
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		doRenderFigurePlayer((IFI_EntityFigurePlayer) entity, d, d1, d2, f, f1);
	}

}
