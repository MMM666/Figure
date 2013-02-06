package net.minecraft.src;

import java.text.Format;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import net.minecraft.client.Minecraft;

public class GuiFigureSelectSlotSelect extends GuiSlot {

	protected Minecraft mc;
	public GuiFigureSelect ownerGui;
    private float xSize_lo;
    private float ySize_lo;
	private int selected;

	
	public GuiFigureSelectSlotSelect(Minecraft minecraft, GuiFigureSelect guifiguaselect)
    {
    	super(minecraft, guifiguaselect.width, guifiguaselect.height, 32, guifiguaselect.height - 52, 36);
    	ownerGui = guifiguaselect;
    	selected = 0;
    	mc = minecraft;
    	
    }
    
	@Override
	protected int getSize() {
		return IFI_ItemFigure.entityStringMap.size();
	}

	@Override
	protected int getContentHeight()
    {
        return getSize() * 36;
    }

	@Override
	protected void elementClicked(int i, boolean flag) {
//		if (flag)
			selected = i;
		System.out.println(String.format("index: %d, b: %b", i, flag));
	}

	@Override
	protected boolean isSelected(int i) {
		if (IFI_ItemFigure.entityStringMap.size() > i && i == selected) {
	        String s = IFI_ItemFigure.entityStringMap.keySet().toArray()[i].toString();
//			ownerGui.targetFigure.setRenderEntity(ItemFigure.entityStringMap.get(s));
	        Entity e = EntityList.createEntityByName(s, mc.theWorld);
	        if (e instanceof EntityLiving) {
		        e.setPositionAndRotation(0, 0, 0, ownerGui.targetFigure.rotationYaw, 0);
				ownerGui.targetFigure.setRenderEntity((EntityLiving)e);
                if (e instanceof EntityLiving) {
                    EntityLiving sel = (EntityLiving)e;
                	sel.prevRotationYawHead = sel.rotationYawHead = e.rotationYaw;
                }
//				System.out.println(s);
				return true;
	        } 
		}
		return false;
	}

	@Override
	protected void drawBackground() {
//        ownerGui.drawDefaultBackground();
	}

	@Override
    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
        xSize_lo = i;
        ySize_lo = j;
    }

	@Override
	protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
		if (IFI_ItemFigure.entityStringMap.size() <= i) return;
        String s = IFI_ItemFigure.entityStringMap.keySet().toArray()[i].toString();
        ownerGui.drawString(ownerGui.fontRenderer, s, j + 70, k + 12, 0xffffff);
        EntityLiving entityliving = (EntityLiving)IFI_ItemFigure.entityStringMap.get(s);
        
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
        GL11.glPushMatrix();
        float f1 = 15F;
        if (entityliving.height > 2F) {
            f1 = f1 * 2F / entityliving.height;
        }
        GL11.glTranslatef(j + 30, k + 30, 50F + f1);
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f5 = (float)(j + 30) - xSize_lo;
        float f6 = (float)((k + 30) - 10) - ySize_lo;
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(f6 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
        entityliving.renderYawOffset = (float)Math.atan(f5 / 40F) * 20F;
        entityliving.rotationYaw = (float)Math.atan(f5 / 40F) * 40F;
        entityliving.rotationPitch = -(float)Math.atan(f6 / 40F) * 20F;
        entityliving.prevRotationYawHead = entityliving.rotationYawHead;
        entityliving.rotationYawHead = entityliving.rotationYaw;
        GL11.glTranslatef(0.0F, entityliving.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180F;
        try {
            RenderManager.instance.renderEntityWithPosYaw(entityliving, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        }
        catch (Exception e) {
        	for (Entry<Integer, Entity> ek : IFI_ItemFigure.entityIndexMap.entrySet()) {
        		if (ek.getValue() == entityliving) {
        			IFI_ItemFigure.entityIndexMap.remove(ek.getKey());
        			break;
        		}
        	}
        	IFI_ItemFigure.entityStringMap.remove(s);
        }
        // 影だかバイオームだかの処理?
        GL13.glMultiTexCoord2f(33985 /*GL_TEXTURE1_ARB*/, 240.0F, 240.0F);
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
	}
}
