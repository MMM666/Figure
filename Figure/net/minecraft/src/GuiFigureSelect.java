package net.minecraft.src;

import org.lwjgl.input.Mouse;

public class GuiFigureSelect extends GuiScreen
{
    protected String screenTitle;
    protected GuiSlot selectPanel;
    protected EntityFigure targetFigure;
    private GuiButton localScroll;


	public GuiFigureSelect(EntityFigure entityfigure) {
		screenTitle = "Figure Select";
		targetFigure = entityfigure;
    }

	@Override
    public void initGui() {
        controlList.clear();
        controlList.add(new GuiButton(300, width / 2 - 60, height - 44, 120, 20, "Select"));
        
        selectPanel = new GuiFigureSelectSlotSelect(mc, this);
        selectPanel.registerScrollButtons(controlList, 3, 4);

        localScroll = new GuiButton(3, 0, 0, "");
    }
    
	@Override
    protected void actionPerformed(GuiButton guibutton) {
        if(!guibutton.enabled) {
            return;
        }
        if(guibutton.id == 300) {
            mc.displayGuiScreen(null);
        	return;
        } else {
            selectPanel.actionPerformed(guibutton);
        }
    }

	@Override
    public void drawScreen(int i, int j, float f) {
        selectPanel.drawScreen(i, j, f);
        drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);
        super.drawScreen(i, j, f);
    }

	@Override
    public void handleMouseInput() {
    	// ホイールスクロール用
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if(i != 0) {
        	localScroll.id = i > 0 ? 3 : 4;
        	selectPanel.actionPerformed(localScroll);
        }
    }

    @Override
    public void onGuiClosed() {
    	super.onGuiClosed();
    	targetFigure.getGui().setRotation();    
    }
    
}
