package net.minecraft.src;

import static net.minecraft.src.IFI_Statics.IFI_Packet_Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Client側専用の処理
 */
public class IFI_Client {

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
			// クライアントから姿勢制御データ等を受信
			lserver.reciveData((IFI_EntityFigure)lentity, var2.data);
			mod_IFI_Figure.Debug("DataSet ID:%d Client.", lentity.entityId);
			break;
		}
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


}
