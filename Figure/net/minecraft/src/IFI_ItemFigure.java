package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;

public class IFI_ItemFigure extends Item implements MMM_IItemRender {

	public static World lasetWorld;
	public static Map<Integer, Entity> entityIndexMap = new TreeMap<Integer, Entity>();
	public static Map<String, Entity> entityStringMap = new TreeMap<String, Entity>();


	public IFI_ItemFigure(int i) {
		super(i);
		maxStackSize = 16;
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

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

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		// 初期チェック
		checkCreateEntity(world);

		// 設置
		float f = 1.0F;
		float f1 = entityplayer.prevRotationPitch
				+ (entityplayer.rotationPitch - entityplayer.prevRotationPitch)
				* f;
		float f2 = entityplayer.prevRotationYaw
				+ (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * f;
		double d = entityplayer.prevPosX
				+ (entityplayer.posX - entityplayer.prevPosX) * (double) f;
		double d1 = (entityplayer.prevPosY
				+ (entityplayer.posY - entityplayer.prevPosY) * (double) f + 1.6200000000000001D)
				- (double) entityplayer.yOffset;
		double d2 = entityplayer.prevPosZ
				+ (entityplayer.posZ - entityplayer.prevPosZ) * (double) f;
		Vec3 vec3d = Vec3.createVectorHelper(d, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.01745329F - 3.141593F);
		float f4 = MathHelper.sin(-f2 * 0.01745329F - 3.141593F);
		float f5 = -MathHelper.cos(-f1 * 0.01745329F);
		float f6 = MathHelper.sin(-f1 * 0.01745329F);
		float f7 = f4 * f5;
		float f8 = f6;
		float f9 = f3 * f5;
		double d3 = 5D;
		Vec3 vec3d1 = Vec3.createVectorHelper((double) f7 * d3, (double) f8 * d3, (double) f9 * d3);
		MovingObjectPosition movingobjectposition = world.rayTraceBlocks_do(vec3d, vec3d1, true);
		if (movingobjectposition == null) {
			return itemstack;
		}
		if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
			int i = movingobjectposition.blockX;
			int j = movingobjectposition.blockY;
			int k = movingobjectposition.blockZ;
			double my = Block.blocksList[world.getBlockId(i, j, k)].maxY;
			if (world.getBlockMaterial(i, j + 1, k) == Material.air
					|| my <= 0.5D) {
				if (!world.isRemote) {
					Entity se = null;
					if (itemstack.getItemDamage() != 0) {
						se = EntityList.createEntityByID(
								itemstack.getItemDamage(), world);
					}
					if (se == null) {
						int l = (Integer) (entityIndexMap.keySet().toArray())[entityplayer.rand
								.nextInt(entityIndexMap.size())];
						se = EntityList.createEntityByID(l, world);
						System.out.println("Selection null.");
					}
					EntityFigure ef = new EntityFigure(world, se);
					if (itemstack.getItemDamage() == 0) {
						if (entityplayer instanceof EntityPlayerSP) {
							ModLoader.openGUI(entityplayer, new GuiFigureSelect(ef));
						}
					}
					// 方向ぎめはここに入れる
					System.out.println(String.format("dir:%d",
							movingobjectposition.sideHit));
					double x, y, z;
					if (movingobjectposition.sideHit == 1) {
						// 天辺を選択した場合詳細な配置ができる
						double dd = (j + my - vec3d.yCoord) / (vec3d1.yCoord - vec3d.yCoord);
						x = (vec3d1.xCoord - vec3d.xCoord) * dd + vec3d.xCoord;
						y = j + my;
						z = (vec3d1.zCoord - vec3d.zCoord) * dd + vec3d.zCoord;
						System.out
								.println(String.format("%f, %f, %f", x, y, z));
					} else {
						x = i + 0.5D;
						y = j + 1.0D;
						z = k + 0.5D;
					}
					
					ef.setPositionAndRotation2(x, y, z,
							(entityplayer.rotationYaw + 180F) % 360F, 0, 1);
					se.setPositionAndRotation(0, 0, 0, ef.rotationYaw, 0);
					if (se instanceof EntityLiving) {
						EntityLiving sel = (EntityLiving) se;
						sel.prevRotationYawHead = sel.rotationYawHead = ef.rotationYaw;
					}
					ef.getGui().setRotation();
					world.spawnEntityInWorld(ef);
				}
				world.playSoundAtEntity(entityplayer, "step.wood", 0.5F,
						0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
				itemstack.stackSize--;
			}
		}
		
		return itemstack;
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		if (itemstack.getItemDamage() != 0) {
			checkCreateEntity(mod_IFI_Figure.mc.theWorld);
			Entity e = entityIndexMap.get(itemstack.getItemDamage());
			if (e != null) {
				return (new StringBuilder()).append(super.getItemName())
						.append(".").append(EntityList.getEntityString(e))
						.toString();
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
		if (!entityIndexMap.isEmpty()) {
			for (Map.Entry<Integer, Entity> ei : entityIndexMap.entrySet()) {
				par3List.add(new ItemStack(mod_IFI_Figure.figure, 1, ei.getKey()));
			}
		}
	}

	@Override
	public boolean renderItem(EntityLiving pEntity, ItemStack pItemstack, int pIndex) {
		//特殊レンダーへ
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.5F, 0.0F, 0.5F);
		GL11.glRotatef(180F, 0F, 1F, 0F);
		GL11.glScalef(2.5F, 2.5F, 2.5F);
		
		EntityFigure ef = new EntityFigure(pEntity.worldObj, IFI_ItemFigure.entityIndexMap.get(pItemstack.getItemDamage()));
		RenderManager.instance.renderEntityWithPosYaw(ef, 0, 0, 0, 0, 0);
		ef.callAfterRender();
//		RenderManager.instance.renderEntityWithPosYaw(new EntityFigure(entityliving.worldObj, ItemFigure.entityIndexMap.get(itemstack.getItemDamage())), 0, 0, 0, 0, 0);
		
		GL11.glPopMatrix();
		return true;
	}

	@Override
	public boolean renderItemInFirstPerson(float pDelta) {
		// 元のコード丸パクリ
		/*
		EntityPlayerSP entityplayersp = MMM_Helper.mc.thePlayer;
		float f1 = prevEquippedProgress + (equippedProgress - prevEquippedProgress) * pDelta;
		float f2 = ((EntityPlayer) (entityplayersp)).prevRotationPitch + (((EntityPlayer) (entityplayersp)).rotationPitch - ((EntityPlayer) (entityplayersp)).prevRotationPitch) * pDelta;
		// 影だかバイオームだかの処理?影がおかしくなるのを補正
		int i = entityplayersp.getBrightnessForRender(pDelta);
		int j = i % 0x10000;
		int k = i / 0x10000;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
		
		GL11.glPushMatrix();
		GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(((EntityPlayer) (entityplayersp)).prevRotationYaw + (((EntityPlayer) (entityplayersp)).rotationYaw - ((EntityPlayer) (entityplayersp)).prevRotationYaw) * pDelta, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		ItemStack itemstack = itemToRender;
		float f3 = MMM_Helper.mc.theWorld.getLightBrightness(
				MathHelper.floor_double(((EntityPlayer) (entityplayersp)).posX),
				MathHelper.floor_double(((EntityPlayer) (entityplayersp)).posY),
				MathHelper.floor_double(((EntityPlayer) (entityplayersp)).posZ));
		GL11.glColor4f(f3, f3, f3, 1.0F);
		
		GL11.glPushMatrix();
		float f5 = 0.8F;
		float f9 = entityplayersp.getSwingProgress(pDelta);
		float f13 = MathHelper.sin(f9 * 3.141593F);
		float f17 = MathHelper.sin(MathHelper.sqrt_float(f9) * 3.141593F);
		GL11.glTranslatef(-f17 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f9) * 3.141593F * 2.0F) * 0.2F, -f13 * 0.2F);
		GL11.glTranslatef(0.7F * f5, -0.65F * f5 - (1.0F - f1) * 0.6F, -0.9F * f5);
		GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
		GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
		f9 = entityplayersp.getSwingProgress(pDelta);
		f13 = MathHelper.sin(f9 * f9 * 3.141593F);
		f17 = MathHelper.sin(MathHelper.sqrt_float(f9) * 3.141593F);
		GL11.glRotatef(-f13 * 20F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-f17 * 20F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(-f17 * 80F, 1.0F, 0.0F, 0.0F);
		f9 = 0.5F;//0.4F;
		GL11.glScalef(f9, f9, f9);
		GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.25F, 0.0F, -0.5F);
		
		renderItem(entityplayersp, itemstack, 0);
		GL11.glPopMatrix();
		
		RenderHelper.disableStandardItemLighting();
		*/
		return true;
	}

	@Override
	public String getRenderTexture() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean renderItem() {
		/*
//      renderManager.renderEntityWithPosYaw(new EntityFigure(entityitem.worldObj, entityitem.item.getItemDamage()), d, d1, d2, f, f1);
		EntityFigure ef = new EntityFigure(entityitem.worldObj, IFI_ItemFigure.entityIndexMap.get(entityitem.item.getItemDamage()));
		renderManager.renderEntityWithPosYaw(ef, 0, 0, 0, 0, f1);
		ef.callAfterRender();
*/
		return true;
	}

	public boolean drawItemIntoGui(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int k, int l, int i1)
	{
		if  (j != 0) {
			// 特殊レンダーGUI内部
			GL11.glPushMatrix();
			GL11.glTranslatef(l - 2, i1 + 3, -3F);
			GL11.glScalef(10F, 10F, 10F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1F);
			GL11.glRotatef(210F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
			
//            renderManager.renderEntityWithPosYaw(new EntityFigure(ModLoader.getMinecraftInstance().theWorld, j), 0, 0, 0, 0, 0);
			EntityFigure ef = new EntityFigure(MMM_Helper.mc.theWorld, IFI_ItemFigure.entityIndexMap.get(j));
			RenderManager.instance.renderEntityWithPosYaw(ef, 0, 0, 0, 0, 0);
			ef.callAfterRender();
			
			GL11.glPopMatrix();
			return true;
		} else {
			return false;
		}
	}

}
