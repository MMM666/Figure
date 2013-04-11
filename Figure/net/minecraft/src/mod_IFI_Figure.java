package net.minecraft.src;

import static net.minecraft.src.IFI_Statics.IFI_Packet_Data;
import static net.minecraft.src.IFI_Statics.IFI_Packet_UpadteItem;
import static net.minecraft.src.IFI_Statics.IFI_Server_SpawnFigure;
import static net.minecraft.src.IFI_Statics.IFI_Server_UpadteFigure;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class mod_IFI_Figure extends BaseMod {

	@MLProp(info = "ItemID(shiftIndex = ItemID - 256)", min = 256, max = 32000)
	public static int ItemID = 22203;
	@MLProp(info = "Zoom rate.")
	public static String zoomRate = "1, 2, 4, 6";
	@MLProp(info = "default Zoom rate.")
	public static float defaultZoomRate = 4F;
	@MLProp()
	public static boolean isDebugMessage = true;
	@MLProp(info = "use Player Figure.")
	public static boolean isFigurePlayer = true;
	@MLProp(info = "EntityID. 0 is auto assigned.")
	public static int UniqueEntityIdFigurePlayer = 224;
	
	public static Item figure;
	public static Class classFigure;
	public static Map<String, Class> guiClassMap = new HashMap<String, Class>();
	public static Map<String, IFI_ServerFigure> serverMap = new HashMap<String, IFI_ServerFigure>();
	public static IFI_ServerFigure defServerFigure;


	public static void Debug(String pText, Object... pData) {
		// デバッグメッセージ
		if (isDebugMessage) {
			System.out.println(String.format("Figure-" + pText, pData));
		}
	}

	@Override
	public String getName() {
		return "Figure";
	}

	@Override
	public String getPriorities() {
		return "required-after:mod_MMM_MMMLib";
	}

	@Override
	public String getVersion() {
		return "1.5.1-2";
	}

	@Override
	public void load() {
		// MMMLibのRevisionチェック
		MMM_Helper.checkRevision("4");
		
		figure = new IFI_ItemFigure(ItemID - 256).setUnlocalizedName("Figure");
		int lentityid = MMM_Helper.getNextEntityID(false);
		classFigure = MMM_Helper.getForgeClass(this, "IFI_EntityFigure");
		ModLoader.registerEntityID(classFigure, "Figure", lentityid);
		// これはForge用ID的な意味で。
		ModLoader.addEntityTracker(this, classFigure, lentityid, 64, 10, false);
		try {
			IFI_ItemFigure.fentityFigure = getEntityFigure(null);
		} catch (Exception e) {
		}
		
		// レシピの追加
		ModLoader.addShapelessRecipe(new ItemStack(figure, 1, 0), new Object[] {Item.stick, Item.clay});
		ModLoader.addShapelessRecipe(new ItemStack(Item.clay), new Object[] { figure });
		if (MMM_Helper.isClient) {
			// 名称変換テーブルの追加
			ModLoader.addName(figure, "Figure");
			// 倍率設定
			IFI_Client.setZoomRate();
		}
		
		// プレーヤースキン表示用MOBの追加
		if (isFigurePlayer) {
			UniqueEntityIdFigurePlayer = UniqueEntityIdFigurePlayer == 0 ?
					MMM_Helper.getNextEntityID(true) : UniqueEntityIdFigurePlayer;
			ModLoader.registerEntityID(IFI_EntityFigurePlayer.class, "FigurePlayer", UniqueEntityIdFigurePlayer);
		}
		
		// パケットチャンネル追加
		ModLoader.registerPacketChannel(this, "IFI|Upd");
	}

	@Override
	public void addRenderer(Map map) {
		map.put(classFigure, new IFI_RenderFigure());
		if (isFigurePlayer) {
			map.put(IFI_EntityFigurePlayer.class, new IFI_RenderFigurePlayer());
		}
	}

	@Override
	public void modsLoaded() {
		// 全部のEntityが追加された？
		try {
			Map<String, Class<Entity>> sc = (Map<String, Class<Entity>>) ModLoader
					.getPrivateValue(EntityList.class, null, 0);
			Map<Class<Entity>, Integer> ci = (Map<Class<Entity>, Integer>) ModLoader
					.getPrivateValue(EntityList.class, null, 3);
			Package packege1 = mod_IFI_Figure.class.getPackage();
			String strpackege = "";
			if (packege1 != null) {
				strpackege = packege1.getName();
			}
			if (!strpackege.isEmpty()) {
				strpackege += ".";
			}
			if (sc != null && ci != null) {
				for (Map.Entry<String, Class<Entity>> me : sc.entrySet()) {
					Class<Entity> cl = (Class<Entity>) me.getValue();
					if (EntityLiving.class.isAssignableFrom(cl)
							&& !cl.isAssignableFrom(EntityLiving.class)
							&& !cl.isAssignableFrom(EntityMob.class)) {
						// 名称の追加
						ModLoader.addLocalization(
								(new StringBuilder())
										.append(figure.getUnlocalizedName())
										.append(".").append(me.getKey())
										.append(".name").toString(),
								(new StringBuilder()).append("Figure ")
										.append(me.getKey()).toString());
						// オートでGUIを追加
						addGui(strpackege, me.getKey());
					}
				}
			}
			defServerFigure = new IFI_ServerFigure();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		// 分解レシピの追加
		ModLoader.addShapelessRecipe(new ItemStack(Item.clay),
				new Object[] { new ItemStack(figure, 1, Short.MAX_VALUE) });
	}

	public void addGui(String pPackegeName, String pName) {
		if (pName != null && pName.length() > 0) {
			ClassLoader classloader1 = mod_IFI_Figure.class.getClassLoader();
			String lcs1, lcs2;
			Class lclass1 = null, lclass2 = null;
			IFI_ServerFigure lserver = null;
			if (MMM_Helper.isClient) {
				try {
					lcs1 = (new StringBuilder()).append(pPackegeName)
							.append("IFI_GuiFigurePause_").append(pName)
							.toString();
					lclass1 = classloader1.loadClass(lcs1);
				} catch (Exception e) {
				}
			}
			try {
				lcs2 = (new StringBuilder()).append(pPackegeName)
						.append("IFI_ServerFigure_").append(pName)
						.toString();
				lclass2 = classloader1.loadClass(lcs2);
				lserver = (IFI_ServerFigure)lclass2.newInstance();
			} catch (Exception e) {
			}
			if ((MMM_Helper.isClient || lclass1 != null) && lserver != null) {
				if (MMM_Helper.isClient) {
					guiClassMap.put(pName, lclass1);
				}
				serverMap.put(pName, lserver);
				Debug("success:%s", pName);
				return;
			}
			Debug("fali:%s", pName);
		}
	}

	@Override
	public Packet23VehicleSpawn getSpawnPacket(Entity var1, int var2) {
		// Modloader用
		return new IFI_PacketFigureSpawn(var1, var2, ((IFI_EntityFigure)var1).mobIndex);
	}

	@Override
	public Entity spawnEntity(int var1, World var2, double var3, double var5, double var7) {
		// Forge用
		if (!MMM_Helper.isForge) return null;
		try {
			IFI_EntityFigure lentity = getEntityFigure(var2);
			lentity.entityId = var1;
			lentity.setPosition(var3, var5, var7);
			return lentity;
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public void serverCustomPayload(NetServerHandler var1, Packet250CustomPayload var2) {
		// 独自パケットチャンネルの受信
		WorldServer lworld = (WorldServer)var1.playerEntity.worldObj;
		Entity lentity = null;
		IFI_EntityFigure lfigure = null;
		IFI_ServerFigure lserver = null;
		int leid = 0;
		if ((var2.data[0] & 0x80) != 0) {
			leid = MMM_Helper.getInt(var2.data, 1);
			lentity = lworld.getEntityByID(leid);
			if (lentity instanceof IFI_EntityFigure) {
				lfigure = (IFI_EntityFigure)lentity;
				lserver = mod_IFI_Figure.getServerFigure(lfigure);
			}
		}
		switch (var2.data[0]) {
		case IFI_Server_SpawnFigure:
			double lx = (double)MMM_Helper.getFloat(var2.data, 1);
			double ly = (double)MMM_Helper.getFloat(var2.data, 5);
			double lz = (double)MMM_Helper.getFloat(var2.data, 9);
			float lyaw = MMM_Helper.getFloat(var2.data, 13);
			if (var2.data.length == 17) {
				// 未選択でGUI閉じたのでアイテムをドロップ
				EntityItem leitem = new EntityItem(lworld, lx, ly + 0.25D, lz, new ItemStack(figure));
				leitem.delayBeforeCanPickup = 10;
				lworld.spawnEntityInWorld(leitem);
				Debug("SpawnItem: %f, %f, %f Server.", lx, ly, lz);
			} else {
				// 指定値にフィギュアをスポーン
				String lname = MMM_Helper.getStr(var2.data, 17);
				try {
					lfigure = getEntityFigure(lworld);
					lentity = EntityList.createEntityByName(lname, lworld);
					lfigure.setRenderEntity((EntityLiving)lentity);
					lfigure.setPositionAndRotation(lx, ly, lz, lyaw, 0F);
					lworld.spawnEntityInWorld(lfigure);
					lworld.playSoundAtEntity(var1.playerEntity, "step.wood",
							0.5F, 0.4F / ((new Random()).nextFloat() * 0.4F + 0.8F));
					Debug("SpawnFigure: %s, %f, %f, %f Server.", lname, lx, ly, lz);
				} catch (Exception e) {
					Debug("SpawnFigure: failed.");
				}
			}
			break;
			
		case IFI_Server_UpadteFigure:
			// クライアントから姿勢制御データ要求を受信
			ModLoader.serverSendPacket(var1, 
					new Packet250CustomPayload("IFI|Upd", lserver.getData(lfigure)));
			Debug("DataSendToClient.");
			lserver.sendItems(lfigure, false);
			break;
			
		case IFI_Packet_Data:
			// クライアントから姿勢制御データ等を受信
			lserver.setData(lfigure, var2.data);
			Debug("DataSet ID:%d Server.", lentity.entityId);
			lworld.getEntityTracker().sendPacketToAllPlayersTrackingEntity(
					lentity, new Packet250CustomPayload("IFI|Upd", lserver.getData(lfigure)));
			Debug("DataSendToAllClient.");
			break;
			
		case IFI_Packet_UpadteItem:
			// クライアントからItemStackを受信
			lserver.reciveItem(lfigure, var2.data);
			// クライアントへItemStackを送信
			lserver.sendItem(var2.data[5], lfigure, false);
			break;
		}
	}

	@Override
	public void clientCustomPayload(NetClientHandler var1, Packet250CustomPayload var2) {
		IFI_Client.clientCustomPayload(var1, var2);
	}

	/**
	 * 独自通信用処理を獲得する。
	 */
	public static IFI_ServerFigure getServerFigure(IFI_EntityFigure pEntity) {
		if (pEntity.renderEntity == null) {
			return null;
		}
		String ls = pEntity.mobString;
		if (serverMap.containsKey(ls)) {
			return serverMap.get(ls);
		}
		return defServerFigure;
	}

	@Override
	public void clientConnect(NetClientHandler var1) {
		// コネクト時にリストを作成。
		IFI_Client.initEntitys();
	}

	/**
	 * EntityFigureのインスタンスを返す
	 */
	public static IFI_EntityFigure getEntityFigure(World pWorld) throws Exception {
		Constructor<IFI_EntityFigure> lc = classFigure.getConstructor(World.class);
		return lc.newInstance(pWorld);
	}

}
