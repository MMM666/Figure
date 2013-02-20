package net.minecraft.src;

public class IFI_GuiFigurePause_EnderDragon extends IFI_GuiFigurePause {

	public EntityDragon edragon;

	public IFI_GuiFigurePause_EnderDragon(IFI_EntityFigure entityfigua) {
		super(entityfigua);
		edragon = (EntityDragon) targetEntity.renderEntity;
	}
/*
	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		edragon.rotationYaw = edragon.prevRotationYaw;
		// System.out.println(String.format("r: %f, %f, %f", ed.rotationYaw,
		// ed.prevRotationYaw, ed.renderYawOffset));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		// System.out.println(String.format("w: %f, %f, %f", ed.rotationYaw,
		// ed.prevRotationYaw, ed.renderYawOffset));
	}

	@Override
	public void setRotation() {
		// 姿勢制御用
		// System.out.println(String.format("1:%f, %f, %f", edragon.rotationYaw,
		// edragon.prevRotationYaw, edragon.renderYawOffset));
		for (; edragon.rotationYaw >= 180F; edragon.rotationYaw -= 360F) {
		}
		for (; edragon.rotationYaw < -180F; edragon.rotationYaw += 360F) {
		}

		for (int i = 0; i < edragon.ringBuffer.length; i++) {
			edragon.ringBuffer[i][0] = edragon.rotationYaw
					+ edragon.renderYawOffset + 180F;
			edragon.ringBuffer[i][1] = edragon.posY;
		}
		// System.out.println(String.format("2:%f, %f, %f", edragon.rotationYaw,
		// edragon.prevRotationYaw, edragon.renderYawOffset));

		edragon.dragonPartHead.width = edragon.dragonPartHead.height = 3F;
		edragon.dragonPartTail1.width = edragon.dragonPartTail1.height = 2.0F;
		edragon.dragonPartTail2.width = edragon.dragonPartTail2.height = 2.0F;
		edragon.dragonPartTail3.width = edragon.dragonPartTail3.height = 2.0F;
		edragon.dragonPartBody.height = 3F;
		edragon.dragonPartBody.width = 5F;
		edragon.dragonPartWing1.height = 2.0F;
		edragon.dragonPartWing1.width = 4F;
		edragon.dragonPartWing2.height = 3F;
		edragon.dragonPartWing2.width = 4F;
		float f3 = (((float) (edragon.getMovementOffsets(5, 1.0F)[1] - edragon
				.getMovementOffsets(10, 1.0F)[1]) * 10F) / 180F) * 3.141593F;
		float f5 = MathHelper.cos(f3);
		float f6 = -MathHelper.sin(f3);
		float f7 = (edragon.rotationYaw * 3.141593F) / 180F;
		float f8 = MathHelper.sin(f7);
		float f9 = MathHelper.cos(f7);
		// edragon.field_40171_aq.onUpdate();
		edragon.dragonPartBody.setLocationAndAngles(edragon.posX
				+ (double) (f8 * 0.5F), edragon.posY, edragon.posZ
				- (double) (f9 * 0.5F), 0.0F, 0.0F);
		// edragon.field_40175_au.onUpdate();
		edragon.dragonPartWing1.setLocationAndAngles(edragon.posX
				+ (double) (f9 * 4.5F), edragon.posY + 2D, edragon.posZ
				+ (double) (f8 * 4.5F), 0.0F, 0.0F);
		// edragon.field_40174_av.onUpdate();
		edragon.dragonPartWing2.setLocationAndAngles(edragon.posX
				- (double) (f9 * 4.5F), edragon.posY + 2D, edragon.posZ
				- (double) (f8 * 4.5F), 0.0F, 0.0F);
		double ad[] = edragon.getMovementOffsets(5, 1.0F);
		double ad1[] = edragon.getMovementOffsets(0, 1.0F);
		float f11 = MathHelper.sin((edragon.rotationYaw * 3.141593F) / 180F
				- edragon.randomYawVelocity * 0.01F);
		float f12 = MathHelper.cos((edragon.rotationYaw * 3.141593F) / 180F
				- edragon.randomYawVelocity * 0.01F);
		// edragon.field_40177_ap.onUpdate();
		edragon.dragonPartHead.setLocationAndAngles(edragon.posX
				+ (double) (f11 * 5.5F * f5), edragon.posY + (ad1[1] - ad[1])
				* 1.0D + (double) (f6 * 5.5F), edragon.posZ
				- (double) (f12 * 5.5F * f5), 0.0F, 0.0F);
		for (int i = 0; i < 3; i++) {
			EntityDragonPart dragonpart = null;
			if (i == 0) {
				dragonpart = edragon.dragonPartTail1;
			}
			if (i == 1) {
				dragonpart = edragon.dragonPartTail2;
			}
			if (i == 2) {
				dragonpart = edragon.dragonPartTail3;
			}
			double ad2[] = edragon.getMovementOffsets(12 + i * 2, 1.0F);
			float f13 = (edragon.rotationYaw * 3.141593F) / 180F
					+ ((func_40159_b(ad2[0] - ad[0]) * 3.141593F) / 180F)
					* 1.0F;
			float f14 = MathHelper.sin(f13);
			float f15 = MathHelper.cos(f13);
			float f16 = 1.5F;
			float f17 = (float) (i + 1) * 2.0F;
			// dragonpart.onUpdate();
			dragonpart
					.setLocationAndAngles(
							edragon.posX
									- (double) ((f8 * f16 + f14 * f17) * f5),
							((edragon.posY + (ad2[1] - ad[1]) * 1.0D) - (double) ((f17 + f16) * f6)) + 1.5D,
							edragon.posZ
									+ (double) ((f9 * f16 + f15 * f17) * f5),
							0.0F, 0.0F);
		}
		// System.out.println(String.format("3:%f, %f, %f", edragon.rotationYaw,
		// edragon.prevRotationYaw, edragon.renderYawOffset));

		// elt.onUpdate();
	}
*/
	private float func_40159_b(double d) {
		for (; d >= 180D; d -= 360D) {
		}
		for (; d < -180D; d += 360D) {
		}
		return (float) d;
	}

	public float getGuiRenderYawOffset() {
		return 180F;
	}

	public static void afterRender(IFI_EntityFigure entityfigure) {
		// キャラクターをレンダリングした後の特殊処理
//		RenderDragon.entityDragon = null;
		// TODO:ステータスバーの削除をするにはBossStatusの値を書き戻してやる。
	}

}
