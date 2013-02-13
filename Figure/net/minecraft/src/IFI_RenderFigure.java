package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class IFI_RenderFigure extends Render {

	public IFI_RenderFigure() {
		shadowSize = 0.0F;
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		IFI_EntityFigure ef = (IFI_EntityFigure) entity;
		if (ef.renderEntity != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(
					(float) d,
					(float) d1
							+ ((ef.renderEntity.isRiding()
									? (float) ef.renderEntity.getYOffset()
									: ef.renderEntity.yOffset) / ef.zoom),
					(float) d2);
			// GL11.glRotatef(angle, x, y, z)
			float fz = 1F / ef.zoom;
			// float fz = 0.25F;
			GL11.glScalef(fz, fz, fz);
			GL11.glRotatef(ef.rotationYaw - ef.additionalYaw, 0F, 1F, 0F);
			renderManager.renderEntityWithPosYaw(ef.renderEntity, 0, 0, 0, 0, 0);
			GL11.glPopMatrix();
			ef.callAfterRender();
		}
	}

}
