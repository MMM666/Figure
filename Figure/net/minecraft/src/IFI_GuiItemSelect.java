package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class IFI_GuiItemSelect extends GuiContainer {

	public static InventoryBasic inventory = new InventoryBasic("tmp", false, 45);
	public static InventoryBasic inventoryItem = new InventoryBasic("sel", false, 9);

	private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();

	private float currentScroll = 0.0F;

	private boolean isScrolling = false;

	private boolean wasClicking;
	private GuiTextField searchField;

	private List backupContainerSlots;
	private Slot field_74235_v = null;
	private boolean field_74234_w = false;
	private CreativeCrafting field_82324_x;
	protected GuiScreen ownerScreen;
	protected EntityLiving target;
	public static boolean isChange = false;


	public IFI_GuiItemSelect(GuiScreen pOwner, EntityLiving pTarget, EntityPlayer pPlayer) {
		super(new IFI_ContainerItemSelect(pPlayer));
		this.allowUserInput = true;
		this.ySize = 136;
		this.xSize = 195;
		ownerScreen = pOwner;
		target = pTarget;
	}

	@Override
	protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4) {
		this.field_74234_w = true;
		boolean var5 = par4 == 1; // Shift
		InventoryPlayer var6 = this.mc.thePlayer.inventory;
		ItemStack var7 = var6.getItemStack();
		mod_IFI_Figure.Debug("handolMouse.(%d, %d, %d)", par2, par3, par4);
		
		if (par1Slot != null) {
			if (var5) {
				// Shiftが押されている
				if (var7 != null) {
					var6.setItemStack(par1Slot.getStack());
					par1Slot.putStack(var7);
				} else {
					var7 = par1Slot.getStack();
					if (var7 != null) {
						var7 = var7.splitStack(1);
					}
				}
			} else if (par1Slot.inventory == inventory) {
				// Creativeエリアの処理
				ItemStack var8 = par1Slot.getStack();
				ItemStack var9;
				
				if (par4 == 2) {
					return;
				}
				
				if (par4 == 3) {
					if (var6.getItemStack() == null && par1Slot.getHasStack()) {
						var9 = par1Slot.getStack().copy();
						var9.stackSize = var9.getMaxStackSize();
						var6.setItemStack(var9);
					}
					
					return;
				}
				
				if (var7 != null && var8 != null && var7.isItemEqual(var8)) {
					if (par3 == 0) {
						if (var5) {
							var7.stackSize = var7.getMaxStackSize();
						} else if (var7.stackSize < var7.getMaxStackSize()) {
							++var7.stackSize;
						}
					} else if (var7.stackSize <= 1) {
						var6.setItemStack((ItemStack) null);
					} else {
						--var7.stackSize;
					}
				} else if (var8 != null && var7 == null) {
					var6.setItemStack(ItemStack.copyItemStack(var8));
					var7 = var6.getItemStack();
					
					if (var5) {
						var7.stackSize = var7.getMaxStackSize();
					}
				} else {
					var6.setItemStack((ItemStack) null);
				}
			} else {
				// 装備品欄
				this.inventorySlots.slotClick(par1Slot == null ? par2 : par1Slot.slotNumber, par3, par4, this.mc.thePlayer);
			}
		} else {
			// スロットをクリックしなかった
			mod_IFI_Figure.Debug("Drop.(%d, %d, %d)", par2, par3, par4);
			this.mc.thePlayer.dropPlayerItem(var6.getItemStack());
			this.mc.playerController.func_78752_a(var6.getItemStack());
			
			var6.setItemStack((ItemStack) null);
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		this.searchField = new GuiTextField(this.fontRenderer,
				this.guiLeft + 82, this.guiTop + 6, 89, this.fontRenderer.FONT_HEIGHT);
		this.searchField.setMaxStringLength(15);
		this.searchField.setEnableBackgroundDrawing(false);
		this.searchField.setVisible(false);
		this.searchField.setTextColor(16777215);
		int var1 = selectedTabIndex;
		selectedTabIndex = -1;
		this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[var1]);
		this.field_82324_x = new CreativeCrafting(this.mc);
		this.mc.thePlayer.inventoryContainer.addCraftingToCrafters(this.field_82324_x);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();

		if (this.mc.thePlayer != null && this.mc.thePlayer.inventory != null) {
			this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_82324_x);
		}
		isChange = true;
		target.setCurrentItemOrArmor(0, inventoryItem.getStackInSlot(4));
		target.setCurrentItemOrArmor(1, inventoryItem.getStackInSlot(3));
		target.setCurrentItemOrArmor(2, inventoryItem.getStackInSlot(2));
		target.setCurrentItemOrArmor(3, inventoryItem.getStackInSlot(1));
		target.setCurrentItemOrArmor(4, inventoryItem.getStackInSlot(0));
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex()) {
			if (Keyboard.isKeyDown(this.mc.gameSettings.keyBindChat.keyCode)) {
				this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
			} else {
				if (par2 == 1) {
					mc.displayGuiScreen(ownerScreen);
				}
			}
		} else {
			if (this.field_74234_w) {
				this.field_74234_w = false;
				this.searchField.setText("");
			}
			
			if (!this.checkHotbarKeys(par2)) {
				if (this.searchField.textboxKeyTyped(par1, par2)) {
					this.updateCreativeSearch();
				} else {
					super.keyTyped(par1, par2);
				}
			}
		}
	}

	private void updateCreativeSearch() {
		IFI_ContainerItemSelect lcontainer = (IFI_ContainerItemSelect)inventorySlots;
		lcontainer.itemList.clear();
		Item[] var2 = Item.itemsList;
		int var3 = var2.length;
		int var4;

		for (var4 = 0; var4 < var3; ++var4) {
			Item var5 = var2[var4];

			if (var5 != null && var5.getCreativeTab() != null) {
				var5.getSubItems(var5.itemID, (CreativeTabs) null, lcontainer.itemList);
			}
		}

		Enchantment[] var8 = Enchantment.enchantmentsList;
		var3 = var8.length;

		for (var4 = 0; var4 < var3; ++var4) {
			Enchantment var12 = var8[var4];

			if (var12 != null && var12.type != null) {
				Item.enchantedBook.func_92113_a(var12, lcontainer.itemList);
			}
		}

		Iterator var9 = lcontainer.itemList.iterator();
		String var10 = this.searchField.getText().toLowerCase();

		while (var9.hasNext()) {
			ItemStack var11 = (ItemStack) var9.next();
			boolean var13 = false;
			Iterator var6 = var11.getTooltip(this.mc.thePlayer,
					this.mc.gameSettings.advancedItemTooltips).iterator();

			while (true) {
				if (var6.hasNext()) {
					String var7 = (String) var6.next();

					if (!var7.toLowerCase().contains(var10)) {
						continue;
					}

					var13 = true;
				}

				if (!var13) {
					var9.remove();
				}

				break;
			}
		}

		this.currentScroll = 0.0F;
		lcontainer.scrollTo(0.0F);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		CreativeTabs lct = CreativeTabs.creativeTabArray[selectedTabIndex];
		
		if (lct.drawInForegroundOfTab()) {
			this.fontRenderer.drawString(lct.getTranslatedTabLabel(), 8, 6, 4210752);
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		if (par3 == 0) {
			int var4 = par1 - this.guiLeft;
			int var5 = par2 - this.guiTop;
			CreativeTabs[] var6 = CreativeTabs.creativeTabArray;
			int var7 = var6.length;
			
			for (int var8 = 0; var8 < var7; ++var8) {
				CreativeTabs var9 = var6[var8];
				if (var9 == CreativeTabs.tabInventory) {
					continue;
				}
				if (this.func_74232_a(var9, var4, var5)) {
					this.setCurrentCreativeTab(var9);
					return;
				}
			}
		}
		
		super.mouseClicked(par1, par2, par3);
	}

	private boolean needsScrollBars() {
		return selectedTabIndex != CreativeTabs.tabInventory.getTabIndex()
				&& CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory()
				&& ((IFI_ContainerItemSelect) this.inventorySlots).hasMoreThan1PageOfItemsInList();
	}

	private void setCurrentCreativeTab(CreativeTabs par1CreativeTabs) {
		int var2 = selectedTabIndex;
		selectedTabIndex = par1CreativeTabs.getTabIndex();
		IFI_ContainerItemSelect var3 = (IFI_ContainerItemSelect) this.inventorySlots;
		var3.itemList.clear();
		par1CreativeTabs.displayAllReleventItems(var3.itemList);
/*
		if (par1CreativeTabs == CreativeTabs.tabInventory) {
			Container var4 = this.mc.thePlayer.inventoryContainer;

			if (this.backupContainerSlots == null) {
				this.backupContainerSlots = var3.inventorySlots;
			}

			var3.inventorySlots = new ArrayList();

			for (int var5 = 0; var5 < var4.inventorySlots.size(); ++var5) {
				SlotCreativeInventory var6 = new SlotCreativeInventory(null,
						(Slot) var4.inventorySlots.get(var5), var5);
				var3.inventorySlots.add(var6);
				int var7;
				int var8;
				int var9;

				if (var5 >= 5 && var5 < 9) {
					var7 = var5 - 5;
					var8 = var7 / 2;
					var9 = var7 % 2;
					var6.xDisplayPosition = 9 + var8 * 54;
					var6.yDisplayPosition = 6 + var9 * 27;
				} else if (var5 >= 0 && var5 < 5) {
					var6.yDisplayPosition = -2000;
					var6.xDisplayPosition = -2000;
				} else if (var5 < var4.inventorySlots.size()) {
					var7 = var5 - 9;
					var8 = var7 % 9;
					var9 = var7 / 9;
					var6.xDisplayPosition = 9 + var8 * 18;

					if (var5 >= 36) {
						var6.yDisplayPosition = 112;
					} else {
						var6.yDisplayPosition = 54 + var9 * 18;
					}
				}
			}

			this.field_74235_v = new Slot(inventory, 0, 173, 112);
			var3.inventorySlots.add(this.field_74235_v);
		} else if (var2 == CreativeTabs.tabInventory.getTabIndex()) {
			var3.inventorySlots = this.backupContainerSlots;
			this.backupContainerSlots = null;
		}
*/
		if (this.searchField != null) {
			if (par1CreativeTabs == CreativeTabs.tabAllSearch) {
				this.searchField.setVisible(true);
				this.searchField.setCanLoseFocus(false);
				this.searchField.setFocused(true);
				this.searchField.setText("");
				this.updateCreativeSearch();
			} else {
				this.searchField.setVisible(false);
				this.searchField.setCanLoseFocus(true);
				this.searchField.setFocused(false);
			}
		}

		this.currentScroll = 0.0F;
		var3.scrollTo(0.0F);
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int var1 = Mouse.getEventDWheel();

		if (var1 != 0 && this.needsScrollBars()) {
			int var2 = ((IFI_ContainerItemSelect) this.inventorySlots).itemList
					.size() / 9 - 5 + 1;

			if (var1 > 0) {
				var1 = 1;
			}

			if (var1 < 0) {
				var1 = -1;
			}

			this.currentScroll = (float) ((double) this.currentScroll - (double) var1
					/ (double) var2);

			if (this.currentScroll < 0.0F) {
				this.currentScroll = 0.0F;
			}

			if (this.currentScroll > 1.0F) {
				this.currentScroll = 1.0F;
			}

			((IFI_ContainerItemSelect) this.inventorySlots).scrollTo(this.currentScroll);
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		boolean var4 = Mouse.isButtonDown(0);
		int var5 = this.guiLeft;
		int var6 = this.guiTop;
		int var7 = var5 + 175;
		int var8 = var6 + 18;
		int var9 = var7 + 14;
		int var10 = var8 + 112;

		if (!this.wasClicking && var4 && par1 >= var7 && par2 >= var8
				&& par1 < var9 && par2 < var10) {
			this.isScrolling = this.needsScrollBars();
		}

		if (!var4) {
			this.isScrolling = false;
		}

		this.wasClicking = var4;

		if (this.isScrolling) {
			this.currentScroll = ((float) (par2 - var8) - 7.5F)
					/ ((float) (var10 - var8) - 15.0F);

			if (this.currentScroll < 0.0F) {
				this.currentScroll = 0.0F;
			}

			if (this.currentScroll > 1.0F) {
				this.currentScroll = 1.0F;
			}

			((IFI_ContainerItemSelect) this.inventorySlots).scrollTo(this.currentScroll);
		}

		super.drawScreen(par1, par2, par3);
		CreativeTabs[] var11 = CreativeTabs.creativeTabArray;
		int var12 = var11.length;

		for (int var13 = 0; var13 < var12; ++var13) {
			CreativeTabs lct = var11[var13];
			if (lct == CreativeTabs.tabInventory) {
				continue;
			}
			
			if (this.renderCreativeInventoryHoveringText(lct, par1, par2)) {
				break;
			}
		}
/*
		if (this.field_74235_v != null
				&& selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()
				&& this.isPointInRegion(this.field_74235_v.xDisplayPosition,
						this.field_74235_v.yDisplayPosition, 16, 16, par1, par2)) {
			this.drawCreativeTabHoveringText(StringTranslate.getInstance()
					.translateKey("inventory.binSlot"), par1, par2);
		}
*/
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		CreativeTabs var5 = CreativeTabs.creativeTabArray[selectedTabIndex];
		CreativeTabs[] var7 = CreativeTabs.creativeTabArray;
		int var8 = var7.length;
		int var9;

		for (var9 = 0; var9 < var8; ++var9) {
			CreativeTabs var10 = var7[var9];
			if (var10 == CreativeTabs.tabInventory) {
				continue;
			}
			this.mc.renderEngine.bindTexture("/gui/allitems.png");

			if (var10.getTabIndex() != selectedTabIndex) {
				this.renderCreativeTab(var10);
			}
		}

		this.mc.renderEngine.bindTexture("/gui/creative_inv/" + var5.getBackgroundImageName());
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize,
				this.ySize);
		this.searchField.drawTextBox();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var11 = this.guiLeft + 175;
		var8 = this.guiTop + 18;
		var9 = var8 + 112;
		this.mc.renderEngine.bindTexture("/gui/allitems.png");

		if (var5.shouldHidePlayerInventory()) {
			this.drawTexturedModalRect(var11, var8
					+ (int) ((float) (var9 - var8 - 17) * this.currentScroll),
					232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
		}

		this.renderCreativeTab(var5);
/*
		if (var5 == CreativeTabs.tabInventory) {
			GuiInventory.drawPlayerOnGui(this.mc, this.guiLeft + 43,
					this.guiTop + 45, 20, (float) (this.guiLeft + 43 - par2),
					(float) (this.guiTop + 45 - 30 - par3));
		}
*/
	}

	protected boolean func_74232_a(CreativeTabs par1CreativeTabs, int par2, int par3) {
		int var4 = par1CreativeTabs.getTabColumn();
		int var5 = 28 * var4;
		byte var6 = 0;

		if (var4 == 5) {
			var5 = this.xSize - 28 + 2;
		} else if (var4 > 0) {
			var5 += var4;
		}

		int var7;

		if (par1CreativeTabs.isTabInFirstRow()) {
			var7 = var6 - 32;
		} else {
			var7 = var6 + this.ySize;
		}

		return par2 >= var5 && par2 <= var5 + 28 && par3 >= var7
				&& par3 <= var7 + 32;
	}

	protected boolean renderCreativeInventoryHoveringText(
			CreativeTabs par1CreativeTabs, int par2, int par3) {
		int var4 = par1CreativeTabs.getTabColumn();
		int var5 = 28 * var4;
		byte var6 = 0;

		if (var4 == 5) {
			var5 = this.xSize - 28 + 2;
		} else if (var4 > 0) {
			var5 += var4;
		}

		int var7;

		if (par1CreativeTabs.isTabInFirstRow()) {
			var7 = var6 - 32;
		} else {
			var7 = var6 + this.ySize;
		}

		if (this.isPointInRegion(var5 + 3, var7 + 3, 23, 27, par2, par3)) {
			this.drawCreativeTabHoveringText(
					par1CreativeTabs.getTranslatedTabLabel(), par2, par3);
			return true;
		} else {
			return false;
		}
	}

	protected void renderCreativeTab(CreativeTabs par1CreativeTabs) {
		boolean var2 = par1CreativeTabs.getTabIndex() == selectedTabIndex;
		boolean var3 = par1CreativeTabs.isTabInFirstRow();
		int var4 = par1CreativeTabs.getTabColumn();
		int var5 = var4 * 28;
		int var6 = 0;
		int var7 = this.guiLeft + 28 * var4;
		int var8 = this.guiTop;
		byte var9 = 32;

		if (var2) {
			var6 += 32;
		}

		if (var4 == 5) {
			var7 = this.guiLeft + this.xSize - 28;
		} else if (var4 > 0) {
			var7 += var4;
		}

		if (var3) {
			var8 -= 28;
		} else {
			var6 += 64;
			var8 += this.ySize - 4;
		}

		GL11.glDisable(GL11.GL_LIGHTING);
		this.drawTexturedModalRect(var7, var8, var5, var6, 28, var9);
		this.zLevel = 100.0F;
		itemRenderer.zLevel = 100.0F;
		var7 += 6;
		var8 += 8 + (var3 ? 1 : -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		ItemStack var10 = new ItemStack(par1CreativeTabs.getTabIconItem());
		itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer,
				this.mc.renderEngine, var10, var7, var8);
		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer,
				this.mc.renderEngine, var10, var7, var8);
		GL11.glDisable(GL11.GL_LIGHTING);
		itemRenderer.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 0) {
			this.mc.displayGuiScreen(new GuiAchievements(this.mc.statFileWriter));
		}

		if (par1GuiButton.id == 1) {
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.statFileWriter));
		}
	}

	public int func_74230_h() {
		return selectedTabIndex;
	}

	/**
	 * 選択用のインベントリをクリア
	 */
	public static void clearInventory() {
		for (int li = 0; li < inventoryItem.getSizeInventory(); li++) {
			inventoryItem.setInventorySlotContents(li, null);
		}
	}

}
