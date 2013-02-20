package net.minecraft.src;

import static net.minecraft.src.IFI_Statics.IFI_Packet_Data;
import static net.minecraft.src.IFI_Statics.IFI_Packet_UpadteItem;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Client����p�̏���
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
		// �Ǝ��p�P�b�g�`�����l���̎�M
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
			// �T�[�o�[����p������f�[�^������M
			lserver.setData((IFI_EntityFigure)lentity, var2.data);
			mod_IFI_Figure.Debug("DataSet ID:%d Client.", lentity.entityId);
			break;
			
		case IFI_Packet_UpadteItem:
			// �T�[�o�[����ItemStack����M
			lserver.reciveItem(lfigure, var2.data);
			break;
		}
	}

	public static void sendToServer(Packet pPacket) {
		ModLoader.clientSendPacket(pPacket);
	}

	/**
	 * �Ǝ���GUI���l������B
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

}
