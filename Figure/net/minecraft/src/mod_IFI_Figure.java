package net.minecraft.src;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;

public class mod_IFI_Figure extends BaseMod {

	@MLProp(info = "ItemID(shiftIndex = ItemID - 256)", min = 256, max = 32000)
	public static int itemID = 22203;
//	@MLProp(info = "Override RenderItem.")
	public static boolean renderHacking = false;
	@MLProp(info = "Override Icon.(false = Icon:GoldenApple)")
	public static boolean useIcon = true;
	@MLProp(info = "Zoom rate.")
	public static String zoomRate = "1, 2, 4, 6";

	public static RenderItem renderItem; // 元のRenderItem
	public static int iconIndex;
	public static Item figure;
	public static Minecraft mc;
	public static Map<String, Class<Entity>> entityClassMap = new TreeMap<String, Class<Entity>>();
	public static Map<String, Class> guiClassMap = new HashMap<String, Class>();


	@Override
	public String getVersion() {
		return "1.4.7-1";
	}

	@Override
	public String getName() {
		return "Figure";
	}

	@Override
	public void load() {
		mc = ModLoader.getMinecraftInstance();
		iconIndex = ModLoader.addOverride("/gui/items.png", "/item/figure.png");
		if (useIcon) {
			figure = new IFI_ItemFigure(itemID - 256).setIconIndex(iconIndex).setItemName("Figure");
		} else {
			figure = new IFI_ItemFigure(itemID - 256).setIconCoord(11, 0).setItemName("Figure");
		}
		ModLoader.registerEntityID(net.minecraft.src.EntityFigure.class,
				"Figure", ModLoader.getUniqueEntityId());
		
		// 名称変換テーブルの追加
		ModLoader.addLocalization(
				(new StringBuilder()).append(figure.getItemName())
						.append(".name").toString(), (new StringBuilder())
						.append("Figure").toString());
		// レシピの追加
		ModLoader.addShapelessRecipe(new ItemStack(figure, 1, 0), new Object[] {Item.stick, Item.clay});
		ModLoader.addShapelessRecipe(new ItemStack(Item.clay), new Object[] { figure });
		
		// 倍率設定
		String s[] = zoomRate.split(",");
		if (s.length > 0) {
			float az[] = new float[s.length];
			for (int i = 0; i < s.length; i++) {
				az[i] = Float.valueOf(s[i].trim());
			}
			GuiFigurePause.button13 = az;
		}
		
		// プレーヤースキン表示用MOBの追加
		ModLoader.registerEntityID(IFI_EntityFigurePlayer.class, "FigurePlayer",
				ModLoader.getUniqueEntityId());
		
	}

	@Override
	public void addRenderer(Map map) {
		map.put(EntityFigure.class, new RenderFigure());
		map.put(IFI_EntityFigurePlayer.class, new IFI_RenderFigurePlayer());
	}

	@Override
	public void modsLoaded() {
		// CreativeAPI用
		boolean isCreativeAPI = false;
		Method mes = null;
		try {
			Package pac = this.getClass().getPackage();
			String s;
			if (pac == null)
				s = "CreativeAPI";
			else
				s = pac.getName().concat(".CreativeAPI");
			mes = Class.forName(s).getMethod("addItem",
					new Class[] { Item.class, int.class });
			isCreativeAPI = true;
		} catch (Exception e) {
			ModLoader.getLogger().fine(
					(new StringBuilder("Creative API not found! ")).toString());
		}
		
		// 全部のEntityが追加された？
		try {
			Map<String, Class<Entity>> sc = (Map<String, Class<Entity>>) ModLoader
					.getPrivateValue(EntityList.class, null, 0);
			Map<Class<Entity>, Integer> ci = (Map<Class<Entity>, Integer>) ModLoader
					.getPrivateValue(EntityList.class, null, 3);
			ClassLoader classloader1 = mod_IFI_Figure.class.getClassLoader();
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
						entityClassMap.put(me.getKey(), me.getValue());
						// 分解レシピと名称の追加
						ModLoader.addLocalization(
								(new StringBuilder())
										.append(figure.getItemName())
										.append(".").append(me.getKey())
										.append(".name").toString(),
								(new StringBuilder()).append("Figure ")
										.append(me.getKey()).toString());
						// オートでGUIを追加
						String cls = (new StringBuilder()).append(strpackege)
								.append("GuiFigurePause_").append(me.getKey())
								.toString();
						try {
							Class class1 = classloader1.loadClass(cls);
							addGui(me.getKey(), class1);
							System.out.println("success:" + cls);
						} catch (Exception e) {
							System.out.println("fali:" + cls);
						}
						
						// CreativeAPI用
						if (isCreativeAPI) {
							try {
								mes.invoke(null, figure, ci.get(cl));
							} catch (Exception e) {
							}
						}
						
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		// 分解レシピの追加
		ModLoader.addShapelessRecipe(new ItemStack(Item.clay),
				new Object[] { new ItemStack(figure, 1, -1) });
	}

	public void addGui(String name, Class cl) {
		if (name != null && name.length() > 0) {
			guiClassMap.put(name, cl);
		}
	}

}
