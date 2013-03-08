package net.minecraft.src;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;

public class IFI_ItemFigure extends Item {

	public static Map<String, Entity> entityStringMap = new TreeMap<String, Entity>();
	public static IFI_EntityFigure fentityFigure = new IFI_EntityFigure(null);

	public static ItemStack firstPerson;

	public IFI_ItemFigure(int i) {
		super(i);
		maxStackSize = 16;
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
/*
	public static void checkCreateEntity(World world) {
		// Entityのリストを作成
		if (world != null && lasetWorld != world) {
			entityIndexMap.clear();
			entityStringMap.clear();
			for (Map.Entry<String, Class<Entity>> me : mod_IFI_Figure.entityClassMap
					.entrySet()) {
				if (me.getKey() == null)
					continue;
				Entity entity1 = EntityList.createEntityByName(me.getKey(),
						world);
				if (entity1 != null) {
					entityIndexMap.put(
							Integer.valueOf(EntityList.getEntityID(entity1)),
							entity1);
					entityStringMap.put(me.getKey(), entity1);
				}
			}
			lasetWorld = world;
		}
	}
*/
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
			
			if (par1ItemStack.getItemDamage() > 0) {
				if (!par3World.isRemote) {
					// Server
					// 選択なしで設置
					try {
						Constructor<IFI_EntityFigure> lc = mod_IFI_Figure.classFigure.getConstructor(World.class, int.class);
						IFI_EntityFigure ef = lc.newInstance(par3World, par1ItemStack.getItemDamage());
						ef.setPositionAndRotation(x, y, z, lyaw, 0F);
						par3World.spawnEntityInWorld(ef);
						par3World.playSoundAtEntity(par2EntityPlayer, "step.wood",
								0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
					} catch (Exception e) {
					}
				}
			} else {
				if (par3World.isRemote) {
					// Client
					// Guiを表示してフィギュアを選択
					// ここで生成されるEntityは捨てインスタンス
//					IFI_EntityFigure ef = new IFI_EntityFigure(par3World);
					fentityFigure.setWorld(par3World);
					fentityFigure.setPositionAndRotation(x, y, z, lyaw, 0F);
					ModLoader.openGUI(par2EntityPlayer, new IFI_GuiFigureSelect(par3World, fentityFigure));
				}
			}
			par1ItemStack.stackSize--;
		}

		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		return super.onItemRightClick(itemstack, world, entityplayer);
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		if (itemstack.getItemDamage() != 0) {
			String ls = EntityList.getStringFromID(itemstack.getItemDamage());
			if (ls != null) {
				return (new StringBuilder()).append(super.getItemName())
						.append(".").append(ls).toString();
			} else {
				// System.out.println(String.format("figua-e-id lost:%d",
				// itemstack.getItemDamage()));
				itemstack.setItemDamage(0);
			}
		}
		return super.getItemName();
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(mod_IFI_Figure.figure, 1));
		Map<Integer, Class> lmap = null;
		try {
			lmap = (Map<Integer, Class>)ModLoader.getPrivateValue(EntityList.class, null, 2);
		} catch (Exception e) {
			
		}
		if (lmap != null && MMM_Helper.mc != null && MMM_Helper.mc.theWorld != null) {
			for (Entry<Integer, Class> le : lmap.entrySet()) {
				try {
					Entity lentity = EntityList.createEntityByID(le.getKey(), MMM_Helper.mc.theWorld);
					if (lentity instanceof EntityLiving) {
						par3List.add(new ItemStack(mod_IFI_Figure.figure, 1, le.getKey()));
					}
				} catch (Exception e) {
				}
			}
		}
	}

	public EntityLiving getEntityFromID(int pIndex) {
		return (EntityLiving)entityStringMap.get(EntityList.getStringFromID(pIndex));
	}

	public boolean renderItem(EntityLiving pEntity, ItemStack pItemstack, int pIndex) {
		//特殊レンダーへ
		if (pItemstack.getItemDamage() == 0) {
			return false;
		}
		GL11.glPushMatrix();
		if (pEntity != null) {
			if (pItemstack == firstPerson) {
				GL11.glTranslatef(-0.5F, 0.0F, 0.25F);
				GL11.glRotatef(225F, 0F, 1F, 0F);
			} else {
				GL11.glTranslatef(-0.5F, 0.0F, 0.5F);
				GL11.glRotatef(180F, 0F, 1F, 0F);
			}
			GL11.glScalef(2.5F, 2.5F, 2.5F);
		}
		firstPerson = null;
		
		fentityFigure.setRenderEntity(getEntityFromID(pItemstack.getItemDamage()));
		RenderManager.instance.renderEntityWithPosYaw(fentityFigure, 0, 0, 0, 0, 0);
		IFI_Client.callAfterRender(fentityFigure);
		
		GL11.glPopMatrix();
		return true;
	}

	public boolean renderItemInFirstPerson(float pDelta, MMM_IItemRenderer pItemRenderer) {
		// 元のコード丸パクリ
		firstPerson = pItemRenderer.getItemToRender();
		return false;
	}

	public String getRenderTexture() {
		return null;
	}

	public boolean drawItemIntoGui(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int k, int l, int i1) {
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
			fentityFigure.setWorld(MMM_Helper.mc.theWorld);
			fentityFigure.setRenderEntity(getEntityFromID(j));
			RenderManager.instance.renderEntityWithPosYaw(fentityFigure, 0, 0, 0, 0, 0);
			IFI_Client.callAfterRender(fentityFigure);
			
			GL11.glPopMatrix();
			return true;
		} else {
			return false;
		}
	}

}
