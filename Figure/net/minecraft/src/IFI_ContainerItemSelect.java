package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

/**
 * 装備選択用、丸パクリ。
 */
public class IFI_ContainerItemSelect extends Container {

	public List itemList = new ArrayList();
	public EntityPlayer openPlayer;

	public IFI_ContainerItemSelect(EntityPlayer pPlayer) {
		int var3;
		openPlayer = pPlayer;
		
		for (var3 = 0; var3 < 5; ++var3) {
			for (int var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(IFI_GuiItemSelect.inventory,
						var3 * 9 + var4, 9 + var4 * 18, 18 + var3 * 18));
			}
		}
		this.addSlotToContainer(new MMM_SlotArmor(this, IFI_GuiItemSelect.inventoryItem, 0, 9 + 0 * 18, 112, 0));
		this.addSlotToContainer(new MMM_SlotArmor(this, IFI_GuiItemSelect.inventoryItem, 1, 9 + 1 * 18, 112, 1));
		this.addSlotToContainer(new MMM_SlotArmor(this, IFI_GuiItemSelect.inventoryItem, 2, 9 + 2 * 18, 112, 2));
		this.addSlotToContainer(new MMM_SlotArmor(this, IFI_GuiItemSelect.inventoryItem, 3, 9 + 3 * 18, 112, 3));
		for (var3 = 4; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(IFI_GuiItemSelect.inventoryItem, var3, 9 + var3 * 18, 112));
		}
		this.scrollTo(0.0F);
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return true;
	}

	public void scrollTo(float par1) {
		int var2 = this.itemList.size() / 9 - 5 + 1;
		int var3 = (int) ((double) (par1 * (float) var2) + 0.5D);
		
		if (var3 < 0) {
			var3 = 0;
		}
		
		for (int var4 = 0; var4 < 5; ++var4) {
			for (int var5 = 0; var5 < 9; ++var5) {
				int var6 = var5 + (var4 + var3) * 9;
				
				if (var6 >= 0 && var6 < this.itemList.size()) {
					IFI_GuiItemSelect.inventory.setInventorySlotContents(
							var5 + var4 * 9, (ItemStack) this.itemList.get(var6));
				} else {
					IFI_GuiItemSelect.inventory.setInventorySlotContents(
							var5 + var4 * 9, (ItemStack) null);
				}
			}
		}
	}

	public boolean hasMoreThan1PageOfItemsInList() {
		return this.itemList.size() > 45;
	}

	@Override
	protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		return null;
	}

	@Override
	public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot) {
		return par2Slot.yDisplayPosition > 90;
	}

	@Override
	public boolean canDragIntoSlot(Slot par1Slot) {
		return false;
//		return par1Slot.inventory instanceof InventoryPlayer || par1Slot.yDisplayPosition > 90 && par1Slot.xDisplayPosition <= 162;
	}

	@Override
	public void putStackInSlot(int par1, ItemStack par2ItemStack) {
		// GUIを開いている時にアイテムを回収した時の処理
		openPlayer.inventoryContainer.getSlot(par1).putStack(par2ItemStack);
	}

}
