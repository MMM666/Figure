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
			float lhealthScale = BossStatus.healthScale;
			int lstatusBarLength = BossStatus.statusBarLength;
			String lbossName = BossStatus.bossName;
			boolean lfield_82825_d = BossStatus.field_82825_d;
			
			GL11.glPushMatrix();
			GL11.glTranslatef(
					(float) d,
					(float) d1 + ef.fyOffset + ((ef.renderEntity.isRiding()
							? (float) ef.renderEntity.getYOffset()
									: ef.renderEntity.yOffset) / ef.zoom),
					(float) d2);
			// GL11.glRotatef(angle, x, y, z)
			float fz = 1F / ef.zoom;
			// float fz = 0.25F;
			GL11.glScalef(fz, fz, fz);
			GL11.glRotatef(ef.rotationYaw + ef.additionalYaw, 0F, 1F, 0F);
			renderManager.renderEntityWithPosYaw(ef.renderEntity, 0, 0, 0, 0, 0);
			GL11.glPopMatrix();
			IFI_Client.callAfterRender(ef);
			
			// GUIで表示した分のボスのステータスを表示しない
			BossStatus.healthScale = lhealthScale;
			BossStatus.statusBarLength = lstatusBarLength;
			BossStatus.bossName = lbossName;
			BossStatus.field_82825_d = lfield_82825_d;
		}
	}

	@Override
	protected ResourceLocation func_110775_a(Entity var1) {
		return null;
	}

}
