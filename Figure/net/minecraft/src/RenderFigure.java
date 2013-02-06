package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderFigure extends Render {
	
	public RenderFigure() {
//		shadowSize = 0.5F;
		shadowSize = 0.0F;
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2,
			float f, float f1) {
		
		EntityFigure ef = (EntityFigure)entity;
		if (ef.renderEntity != null) {

//			ef.setWorld(entity.worldObj);
//			ef.renderEntity.posX = ef.posX * ef.zoom;
//			ef.renderEntity.posY = ef.posY * ef.zoom;
//			ef.renderEntity.posZ = ef.posZ * ef.zoom;
			ef.renderEntity.rotationYaw = ef.rotationYaw - ef.additionalYaw;
//			ef.renderEntity.prevPosX = entity.prevPosX;
//			ef.renderEntity.prevPosY = entity.prevPosY;
//			ef.renderEntity.prevPosZ = entity.prevPosZ;
//			ef.renderEntity.prevRotationYaw = entity.prevRotationYaw;
//			ef.renderEntity.lastTickPosX = ef.lastTickPosX * ef.zoom;
//			ef.renderEntity.lastTickPosY = ef.lastTickPosY * ef.zoom;
//			ef.renderEntity.lastTickPosZ = ef.lastTickPosZ * ef.zoom;
//			ef.renderEntity.entityBrightness = entity.entityBrightness;
//			ef.renderEntity.yOffset = entity.yOffset;
			if (ef.renderEntity instanceof EntityLiving) {
				EntityLiving el = (EntityLiving)ef.renderEntity;
				el.renderYawOffset = el.rotationYaw;
				el.prevRenderYawOffset = el.renderYawOffset;
				el.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
			}
			
//			ef.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
	        GL11.glPushMatrix();
	        GL11.glTranslatef((float)d , (float)d1 + ((ef.renderEntity.isRiding() ? (float)ef.renderEntity.getYOffset() : ef.renderEntity.yOffset) / ef.zoom), (float)d2);
//	        GL11.glRotatef(angle, x, y, z)
	        float fz = 1F / ef.zoom;
//	        float fz = 0.25F;
	        GL11.glScalef(fz, fz, fz);
			
	        renderManager.renderEntityWithPosYaw(ef.renderEntity, 0, 0, 0, 0, 0);
	        GL11.glPopMatrix();
	        ef.callAfterRender();
		}
	}
}
