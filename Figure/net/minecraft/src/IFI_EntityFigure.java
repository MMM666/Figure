package net.minecraft.src;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class IFI_EntityFigure extends Entity {

	public EntityLiving renderEntity;
	public float zoom = 4F;
	public String mobString;
	public int mobIndex;
	private int health;
	private Method afterrender;
	public float additionalYaw;
	public byte changeCount;

	public IFI_EntityFigure(World world) {
		super(world);
		health = 5;
		additionalYaw = 0.0F;
		changeCount = -1;
	}

	public IFI_EntityFigure(World world, Entity entity) {
		this(world);
		
		if (!(entity instanceof EntityLiving)) {
			entity = EntityList.createEntityByName("Zombie", world);
		}
		setRenderEntity((EntityLiving) entity);
		getGui();
	}

	public IFI_EntityFigure(World world, int index) {
		this(world, EntityList.createEntityByID(index, world));
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(2, (byte)-1);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		String s = nbttagcompound.getString("mobString");
		zoom = nbttagcompound.getFloat("zoom");
		health = nbttagcompound.getShort("Health");
		additionalYaw = nbttagcompound.getFloat("additionalYaw");
		if (s != null) {
			Entity r = EntityList.createEntityByName(
					nbttagcompound.getString("mobString"), worldObj);
			if (r == null || !(r instanceof EntityLiving)) {
				// ë∂ç›ÇµÇ»Ç¢MOB
				System.out.println(String.format("figua-lost:%s",
						nbttagcompound.getString("mobString")));
//				IFI_ItemFigure.checkCreateEntity(worldObj);
				r = (Entity) IFI_ItemFigure.entityIndexMap.values().toArray()[0];
			}
			setRenderEntity((EntityLiving) r);
			renderEntity.readFromNBT(nbttagcompound.getCompoundTag("Entity"));
			renderEntity.dataWatcher.updateObject(0, nbttagcompound.getByte("DataWatcher0"));
			renderEntity.prevRotationPitch = nbttagcompound.getFloat("prevPitch");
			renderEntity.prevRotationYaw = nbttagcompound.getFloat("prevYaw");
			renderEntity.prevRotationYawHead = renderEntity.rotationYawHead = renderEntity.prevRotationYaw;
			getGui().readEntityFromNBT(nbttagcompound);
			getGui().setRotation();
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		if (mobString == null)
			mobString = "";
		nbttagcompound.setString("mobString", mobString);
		nbttagcompound.setFloat("zoom", zoom);
		nbttagcompound.setShort("Health", (byte) health);
		nbttagcompound.setFloat("additionalYaw", additionalYaw);
		NBTTagCompound lnbt = new NBTTagCompound();
		if (renderEntity != null) {
			renderEntity.writeToNBT(lnbt);
			nbttagcompound.setByte("DataWatcher0", renderEntity.dataWatcher.getWatchableObjectByte(0));
			nbttagcompound.setFloat("prevPitch", renderEntity.prevRotationPitch);
			nbttagcompound.setFloat("prevYaw", renderEntity.prevRotationYaw);
			getGui().writeEntityToNBT(nbttagcompound);
		}
		nbttagcompound.setCompoundTag("Entity", lnbt);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entity) {
		return entity.boundingBox;
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	// protected void dealFireDamage(int i)
	// {
	// attackEntityFrom(null, i);
	// }

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		Entity entity = damagesource.getEntity();
		if (worldObj.isRemote || isDead) {
			return true;
		}
		setBeenAttacked();
		if (entity instanceof EntityPlayer) {
			if (riddenByEntity != null) {
				riddenByEntity.mountEntity(null);
			}
			entityDropItem(new ItemStack(mod_IFI_Figure.figure.itemID, 1, mobIndex), 0.0F);
			setDead();
		} else {
			health -= i;
			if (health <= 0) {
				setDead();
			}
		}
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		
		// óéâ∫ï™
		motionY -= 0.080000000000000002D;
		motionY *= 0.98000001907348633D;
		
		// ínè„îªíË
		if (onGround) {
			motionX *= 0.5D;
			motionY *= 0.5D;
			motionZ *= 0.5D;
		}
		pushOutOfBlocks(posX, (boundingBox.minY + boundingBox.maxY) / 2D, posZ);
		moveEntity(motionX, motionY, motionZ);
		
		// ë¨ìxå∏êä
		motionX *= 0.99000000953674316D;
		motionY *= 0.94999998807907104D;
		motionZ *= 0.99000000953674316D;
		
		// ìñÇËîªíË
		// List list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
		// boundingBox.expand(0.17D, 0.0D, 0.17D));
		List list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
				boundingBox.expand(0.02D, 0.0D, 0.02D));
		if (list != null && list.size() > 0) {
			for (int j1 = 0; j1 < list.size(); j1++) {
				Entity entity = (Entity) list.get(j1);
				// if(entity != riddenByEntity && entity.canBePushed() &&
				// (entity instanceof EntityZabuton))
				if (entity.canBePushed()) {
					entity.applyEntityCollision(this);
				}
			}
		}
		if (renderEntity != null) {
			setFlag(2, renderEntity.isRiding());
		}
	}

	@Override
	public boolean interact(EntityPlayer entityplayer) {
		if (worldObj.isRemote) {
			// Client
			ModLoader.openGUI(entityplayer, getGui());
		}
		return true;
	}

	public void setRenderEntity(EntityLiving entity) {
		renderEntity = entity;
		if (renderEntity != null) {
			mobString = EntityList.getEntityString(renderEntity);
			mobIndex = EntityList.getEntityID(renderEntity);
			setZoom(zoom);
		}
	}

	public boolean hasRenderEntity() {
		return renderEntity != null;
	}

	public void setZoom(float z) {
		if (z == 0) {
			z = 4F;
		}
		zoom = z;
		width = renderEntity.width / zoom;
		height = renderEntity.height / zoom;
		setPosition(posX, posY, posZ);
		renderEntity.renderDistanceWeight = zoom;
	}

	public IFI_GuiFigurePause getGui() {
		Class<IFI_GuiFigurePause> cl = mod_IFI_Figure.guiClassMap.get(mobString);
		IFI_GuiFigurePause g = null;
		afterrender = null;
		if (cl != null) {
			try {
				Constructor<IFI_GuiFigurePause> cn = cl
						.getConstructor(new Class[] { IFI_EntityFigure.class });
				g = cn.newInstance(new Object[] { this });

				afterrender = g.getClass().getMethod("afterRender",
						new Class[] { IFI_EntityFigure.class });
			} catch (Exception exception) {
				System.out.println("can't constract Gui.");
			}
		}
		if (g == null) {
			g = new IFI_GuiFigurePause(this);
		}
		
		return g;
	}

	public boolean callAfterRender() {
		if (afterrender == null)
			return false;
		
		try {
			afterrender.invoke(null, new Object[] { this });
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
