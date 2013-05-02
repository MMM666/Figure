package net.minecraft.src;

import static net.minecraft.src.IFI_Statics.IFI_Packet_Data;
import static net.minecraft.src.IFI_Statics.IFI_Packet_UpadteItem;
import static net.minecraft.src.IFI_Statics.IFI_Server_UpadteFigure;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.lwjgl.opengl.GL11;

/**
 * Client側専用の処理
 */
public class IFI_Client {

	public static void setZoomRate() {
		String s[] = mod_IFI_Figure.zoomRate.split(",");
		if (s.length > 0) {
			float az[] = new float[s.length];
			for (int i = 0; i < s.length; i++) {
				az[i] = Float.valueOf(s[i].trim());
			}
			IFI_GuiFigurePause.button13 = az;
		}
	}

	public static void clientCustomPayload(NetClientHandler var1, Packet250CustomPayload var2) {
		// 独自パケットチャンネルの受信
		World lworld = MMM_Helper.mc.theWorld;
		Entity lentity = null;
		IFI_EntityFigure lfigure = null;
		IFI_ServerFigure lserver = null;
		if ((var2.data[0] & 0x80) != 0) {
			int leid = MMM_Helper.getInt(var2.data, 1);
			lentity = lworld.getEntityByID(leid);
			if (lentity instanceof IFI_EntityFigure) {
				lfigure = (IFI_EntityFigure)lentity;
				lserver = mod_IFI_Figure.getServerFigure(lfigure);
			}
		}
		switch (var2.data[0]) {
		case IFI_Packet_Data:
			// サーバーから姿勢制御データ等を受信
			lserver.setData((IFI_EntityFigure)lentity, var2.data);
			mod_IFI_Figure.Debug("DataSet ID:%d Client.", lentity.entityId);
			break;
			
		case IFI_Packet_UpadteItem:
			// サーバーからItemStackを受信
			lserver.reciveItem(lfigure, var2.data);
			break;
		}
	}

	public static void sendToServer(Packet pPacket) {
		ModLoader.clientSendPacket(pPacket);
	}

	/**
	 * 独自のGUIを獲得する。
	 */
	public static IFI_GuiFigurePause getGui(IFI_EntityFigure pEntity) {
		Class<IFI_GuiFigurePause> cl = mod_IFI_Figure.guiClassMap.get(pEntity.mobString);
		IFI_GuiFigurePause g = null;
		pEntity.afterrender = null;
		if (cl != null) {
			try {
				Constructor<IFI_GuiFigurePause> cn = cl
						.getConstructor(new Class[] { IFI_EntityFigure.class });
				g = cn.newInstance(new Object[] { pEntity });

				pEntity.afterrender = g.getClass().getMethod("afterRender",
						new Class[] { IFI_EntityFigure.class });
			} catch (Exception exception) {
				System.out.println("can't constract Gui.");
			}
		}
		if (g == null) {
			g = new IFI_GuiFigurePause(pEntity);
		}
		
		return g;
	}

	public static boolean callAfterRender(IFI_EntityFigure pEntity) {
		if (pEntity.afterrender == null) return false;
		
		try {
			pEntity.afterrender.invoke(null, new Object[] { pEntity });
			return true;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static void initEntitys() {
		new IFI_GuiFigureSelect(MMM_Helper.mc.theWorld, null);
	}

	/**
	 * サーバーにフィギュア固有データを要求する。
	 */
	public static void getFigureData(Entity pEntity) {
		// サーバーへ姿勢データの要求をする
		byte ldata[] = new byte[5];
		ldata[0] = IFI_Server_UpadteFigure;
		MMM_Helper.setInt(ldata, 1, pEntity.entityId);
		ModLoader.clientSendPacket(new Packet250CustomPayload("IFI|Upd", ldata));
	}

	public static void openGuiSelect(EntityPlayer pEntity, World pWorld) {
		// Guiを表示してフィギュアを選択
		ModLoader.openGUI(pEntity, new IFI_GuiFigureSelect(pWorld, IFI_ItemFigure.fentityFigure));
	}

	public static void openGuiPause(EntityPlayer pPlayer, IFI_EntityFigure pFigure) {
		ModLoader.openGUI(pPlayer, IFI_Client.getGui(pFigure));
	}

	public static boolean renderItem(EntityLiving pEntity, ItemStack pItemstack, int pIndex) {
		//特殊レンダーへ
//		if (pItemstack.getItemDamage() == 0 || IFI_ItemFigure.fentityFigure.renderEntity == null) {
		if (pItemstack.getItemDamage() == 0) {
			return false;
		}
		GL11.glPushMatrix();
		if (pEntity != null) {
			if (pItemstack == IFI_ItemFigure.firstPerson) {
				GL11.glTranslatef(-0.5F, 0.0F, 0.25F);
				GL11.glRotatef(225F, 0F, 1F, 0F);
			} else {
				GL11.glTranslatef(-0.5F, 0.0F, 0.5F);
				GL11.glRotatef(180F, 0F, 1F, 0F);
			}
			GL11.glScalef(2.5F, 2.5F, 2.5F);
		}
		IFI_ItemFigure.firstPerson = null;
		
		IFI_ItemFigure.fentityFigure.setWorld(MMM_Helper.mc.theWorld);
		IFI_ItemFigure.fentityFigure.setPositionAndRotation(0, 0, 0, 0F, 0F);
		IFI_ItemFigure.fentityFigure.setRenderEntity(IFI_ItemFigure.getEntityFromID(pItemstack.getItemDamage()));
		IFI_ItemFigure.fentityFigure.renderEntity.setPositionAndRotation(0, 0, 0, 0F, 0F);
		IFI_ItemFigure.fentityFigure.renderEntity.prevRotationYawHead =
				IFI_ItemFigure.fentityFigure.renderEntity.rotationYawHead = 0F;
		RenderManager.instance.renderEntityWithPosYaw(IFI_ItemFigure.fentityFigure, 0, 0, 0, 0, 0);
		IFI_Client.callAfterRender(IFI_ItemFigure.fentityFigure);
		
//		mod_IFI_Figure.Debug("Entity:%s, World:%b", fentityFigure.renderEntity.getClass().getSimpleName(), fentityFigure.worldObj != null);
		
		GL11.glPopMatrix();
		return true;
	}

	public static boolean renderItemInFirstPerson(float pDelta, MMM_IItemRenderer pItemRenderer) {
		// 元のコード丸パクリ
		return false;
	}

	public static boolean drawItemIntoGui(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int k, int l, int i1) {
		if  (j != 0) {
			// 特殊レンダーGUI内部
			GL11.glPushMatrix();
			GL11.glTranslatef(l - 2, i1 + 3, -3F);
			GL11.glScalef(10F, 10F, 10F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1F);
			GL11.glRotatef(210F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
			
//			IFI_EntityFigure ef = new IFI_EntityFigure(MMM_Helper.mc.theWorld);
			IFI_ItemFigure.fentityFigure.setWorld(MMM_Helper.mc.theWorld);
			IFI_ItemFigure.fentityFigure.setRenderEntity(IFI_ItemFigure.getEntityFromID(j));
			RenderManager.instance.renderEntityWithPosYaw(IFI_ItemFigure.fentityFigure, 0, 0, 0, 0, 0);
			IFI_Client.callAfterRender(IFI_ItemFigure.fentityFigure);
			
			GL11.glPopMatrix();
			return true;
		} else {
			return false;
		}
	}

}
