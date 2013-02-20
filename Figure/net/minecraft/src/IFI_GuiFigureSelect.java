package net.minecraft.src;

import static net.minecraft.src.IFI_Statics.*;

/**
 * ブランクのFigureの設置時選択用GUI。
 * GUIを閉じた時にサーバーへ情報を送信。
 */
public class IFI_GuiFigureSelect extends MMM_GuiMobSelect {

	protected IFI_EntityFigure targetFigure;


	public IFI_GuiFigureSelect(World pWorld, IFI_EntityFigure entityfigure) {
		super(pWorld, IFI_ItemFigure.entityStringMap);
		screenTitle = "Figure Select";
		targetFigure = entityfigure;
	}

	@Override
	public void initGui() {
		super.initGui();
		controlList.add(new GuiButton(300, width / 2 - 60, height - 44, 120, 20, "Select"));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 300) {
			mc.displayGuiScreen(null);
			return;
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		IFI_ServerFigure isf = mod_IFI_Figure.getServerFigure(targetFigure);
		byte ldata[];
		if (isf != null) {
			isf.setRotation(targetFigure);
			ldata = new byte[17 + targetFigure.mobString.length()];
			MMM_Helper.setStr(ldata, 17, targetFigure.mobString);
		} else {
			ldata = new byte[17];
		}
		// 設定されたEntityに適合するパケットセンダーを実行
		ldata[0] = IFI_Server_SpawnFigure;
		MMM_Helper.setFloat(ldata, 1, (float)targetFigure.posX);
		MMM_Helper.setFloat(ldata, 5, (float)targetFigure.posY);
		MMM_Helper.setFloat(ldata, 9, (float)targetFigure.posZ);
		MMM_Helper.setFloat(ldata, 13, targetFigure.rotationYaw);
		ModLoader.clientSendPacket(new Packet250CustomPayload("IFI|Upd", ldata));
	}

	@Override
	public void clickSlot(int pIndex, boolean pDoubleClick, String pName, EntityLiving pEntity) {
		targetFigure.setRenderEntity(pEntity);
	}

	@Override
	public void drawSlot(int pSlotindex, int pX, int pY, int pDrawheight,
			Tessellator pTessellator, String pName, Entity pEntity) {
		drawString(fontRenderer, pName,
				(width - fontRenderer.getStringWidth(pName)) / 2, pY + 10, 0xffffff);
	}

}
