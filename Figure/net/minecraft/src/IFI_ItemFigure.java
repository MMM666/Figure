package net.minecraft.src;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.lwjgl.opengl.GL11;

public class IFI_ItemFigure extends Item {

	public static Map<String, Entity> entityStringMap = new TreeMap<String, Entity>();
	public static IFI_EntityFigure fentityFigure;

	public static ItemStack firstPerson;

	public IFI_ItemFigure(int i) {
		super(i);
		maxStackSize = 16;
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World,
			int par4, int par5, int par6, int par7,
			float par8, float par9, float par10) {
		// こっちでやれば座標検出要らないと思う
		if (par3World.getBlockMaterial(par4, par5 + 1, par6) == Material.air || par9 <= 0.5D) {
			// 方向ぎめはここに入れる
			mod_IFI_Figure.Debug("x:%f, y:%f, z:%f, dir:%d", par8, par9, par10, par7);
			double x, y, z;
			if (par7 == 1) {
				// 天辺を選択した場合詳細な配置ができる
				x = par4 + par8;
				y = par5 + par9;
				z = par6 + par10;
			} else {
				x = par4 + 0.5D;
				y = par5 + 1.0D;
				z = par6 + 0.5D;
			}
			float lyaw = (180F - par2EntityPlayer.rotationYaw) % 360F;
			EntityLivingBase lelb = getEntityFromItemStack(par1ItemStack);
			
			if (par1ItemStack.getItemDamage() > 0) {
				if (!par3World.isRemote) {
					// Server
					// 選択なしで設置
					try {
						Constructor<IFI_EntityFigure> lc = mod_IFI_Figure.classFigure.getConstructor(World.class, int.class);
						IFI_EntityFigure lef = lc.newInstance(par3World, par1ItemStack.getItemDamage());
						lef.setPositionAndRotation(x, y, z, lyaw, 0F);
						par3World.spawnEntityInWorld(lef);
						par3World.playSoundAtEntity(par2EntityPlayer, "step.wood",
								0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				if (par3World.isRemote) {
					// Client
					// Guiを表示してフィギュアを選択
					// ここで生成されるEntityは捨てインスタンス
					fentityFigure.setWorld(par3World);
					fentityFigure.setPositionAndRotation(x, y, z, lyaw, 0F);
					IFI_Client.openGuiSelect(par2EntityPlayer, par3World);
				}
			}
			par1ItemStack.stackSize--;
		}
		return false;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if (itemstack.getItemDamage() != 0) {
			String ls;
			if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("FigureName")) {
				ls = itemstack.getTagCompound().getString("FigureName");
			} else {
				ls = EntityList.getStringFromID(itemstack.getItemDamage());
			}
			if (ls != null) {
				return (new StringBuilder()).append(super.getUnlocalizedName())
						.append(".").append(ls).toString();
			} else {
				mod_IFI_Figure.Debug("figua-e-id lost:%d", itemstack.getItemDamage());
				itemstack.setItemDamage(0);
			}
		}
		return super.getUnlocalizedName();
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		// Creativeタブに追加するアイテム
		par3List.add(new ItemStack(mod_IFI_Figure.figure, 1));
		Map<Integer, Class> lmap = null;
		try {
			lmap = (Map<Integer, Class>)ModLoader.getPrivateValue(EntityList.class, null, 2);
		} catch (Exception e) {
			
		}
		if (lmap != null) {
			for (Entry<Integer, Class> le : lmap.entrySet()) {
				Class lcl = le.getValue();
				if (!Modifier.isAbstract(lcl.getModifiers()) && EntityLivingBase.class.isAssignableFrom(lcl)) {
					par3List.add(new ItemStack(mod_IFI_Figure.figure, 1, le.getKey()));
				}
			}
		}
	}

//	public static EntityLivingBase getEntityFromID(int pIndex) {
//		IFI_Client.initEntitys();
//		return (EntityLivingBase)entityStringMap.get(EntityList.getStringFromID(pIndex));
//	}

	/**
	 * ItemStackに関連付けられているEntityを返す。<br>
	 * 対象となるEntityが存在しない場合はItemDamageを０に設定し適当なEntityを返す。
	 * @param pItemStack
	 * @return
	 */
	public static EntityLivingBase getEntityFromItemStack(ItemStack pItemStack) {
		IFI_Client.initEntitys();
		String ls = EntityList.getStringFromID(pItemStack.getItemDamage());
		if (ls == null || !entityStringMap.containsKey(ls)) {
			if (pItemStack.hasTagCompound()) {
				if (pItemStack.getTagCompound().hasKey("FigureName")) {
					ls = pItemStack.getTagCompound().getString("FigureName");
					if (entityStringMap.containsKey(ls)) {
						EntityLivingBase le = (EntityLivingBase)entityStringMap.get(ls);
						pItemStack.setItemDamage(EntityList.getEntityID(le));
						return le;
					}
				}
			}
			pItemStack.setItemDamage(0);
			return (EntityLivingBase)entityStringMap.values().iterator().next();
		}
		
		return (EntityLivingBase)entityStringMap.get(ls);
	}

}
