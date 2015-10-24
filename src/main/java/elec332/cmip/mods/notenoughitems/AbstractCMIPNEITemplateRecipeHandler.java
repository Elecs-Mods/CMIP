package elec332.cmip.mods.notenoughitems;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import elec332.core.client.render.RenderHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

/**
 * Created by Elec332 on 23-10-2015.
 */
public abstract class AbstractCMIPNEITemplateRecipeHandler extends TemplateRecipeHandler {

    @Override
    public final Class<? extends GuiContainer> getGuiClass() {
        return null;
    }

    @Override
    public final List<Class<? extends GuiContainer>> getRecipeTransferRectGuis() {
        return null;
    }

    @SuppressWarnings("unchecked")
    public void addTransferRect(Class<? extends GuiContainer> gui, Rectangle rectangle, String identifier, boolean addToRecipeGui, Object... args){
        addTransferRect(Lists.<Class<? extends GuiContainer>>newArrayList(gui), rectangle, identifier, addToRecipeGui, args);
    }

    @Override
    public void drawExtras(int i) {
        CachedRecipe recipe = arecipes.get(i);
        if (recipe instanceof AbstractCachedRecipe){
            for (AbstractCachedRecipe.GuiFluidTank tank : ((AbstractCachedRecipe) recipe).recipeTanks){
                tank.draw();
            }
        }
    }

    public void addTransferRect(List<Class<? extends GuiContainer>> gui, Rectangle rectangle, String identifier, boolean addToRecipeGui, Object... args){
        RecipeTransferRect transferRect = new RecipeTransferRect(rectangle, identifier, args);
        if (addToRecipeGui)
            this.transferRects.add(transferRect);
        TemplateRecipeHandler.RecipeTransferRectHandler.registerRectsToGuis(gui, Lists.newArrayList(transferRect));
    }

    public void drawProgressBar(Rectangle rectangle, int u, int v, int direction, boolean invert){
        this.drawProgressBar(rectangle.x, rectangle.y, u, v, rectangle.width, rectangle.height, 48, invert ? direction + 4 : direction);
    }

    @Override
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
        super.handleTooltip(gui, currenttip, recipe);
        return handleTankTooltips(gui, currenttip, recipe);
    }

    public List<String> handleTankTooltips(GuiRecipe gui, List<String> currenttip, int i){
        CachedRecipe recipe = arecipes.get(i);
        if (recipe instanceof AbstractCachedRecipe){
            AbstractCachedRecipe tank = (AbstractCachedRecipe) recipe;
            for (AbstractCachedRecipe.GuiFluidTank rTank : tank.recipeTanks){
                if (rTank.isMouseOver(gui, i)){
                    currenttip.addAll(rTank.getTooltip());
                }
            }
        }
        return currenttip;
    }

    public abstract class AbstractCachedRecipe extends CachedRecipe {

        public AbstractCachedRecipe(){
            this.recipeTanks = Lists.newArrayList();
        }

        public AbstractCachedRecipe addTank(Rectangle r, FluidStack fluid, int capacity){
            return addTank(new GuiFluidTank(r, fluid, capacity));
        }

        public AbstractCachedRecipe addTank(GuiFluidTank tank){
            recipeTanks.add(tank);
            return this;
        }

        private List<AbstractCachedRecipe.GuiFluidTank> recipeTanks;
        private int index = -1;

        public int getIndex(){
            if (index != -1){
                return index;
            }
            if (arecipes.contains(this)){
                index = arecipes.indexOf(this);
                return index;
            }
            throw new RuntimeException();
        }



        /* Most code was taken from elec332.core.inventory.widget.FluidTankWidget */
        public class GuiFluidTank{

            protected GuiFluidTank(Rectangle r, FluidStack fluid, int capacity){
                this.r = r;
                this.capacity = capacity;
                this.fluidStack = fluid;
            }

            private Rectangle r;
            private int capacity;
            private FluidStack fluidStack;

            public final boolean isMouseOver(GuiRecipe gui){
                return isMouseOver(gui, getIndex());
            }

            public final boolean isMouseOver(GuiRecipe gui, int recipe){
                Point pos = GuiDraw.getMousePosition();
                Point offset = gui.getRecipePosition(recipe);
                Point relMouse = new Point(pos.x - gui.guiLeft - offset.x, pos.y - gui.guiTop - offset.y);
                return r.contains(relMouse);
            }

            public List<String> getTooltip(){
                String fluid = (fluidStack == null || fluidStack.getFluid() == null)?null:fluidStack.getFluid().getName();
                int amount = fluidStack==null?0:fluidStack.amount;
                return Lists.newArrayList(EnumChatFormatting.GRAY+"Fluid: "+fluid+"  Amount: "+amount);
            }

            public void draw() {
                if (capacity == 0)
                    return;
                if (fluidStack == null || fluidStack.getFluid() == null || fluidStack.amount <= 0)
                    return;
                IIcon fluidIcon = RenderHelper.getFluidTexture(fluidStack.getFluid(), false);
                float scale = fluidStack.amount / (float) capacity;
                RenderHelper.bindTexture(RenderHelper.getBlocksResourceLocation());
                int height = (int) (r.height * scale);
                for (int col = 0; col < r.width; col += 16) {
                    for (int row = 0; row <= height; row += 16) {
                        int drawH = Math.min(height-row, 16);
                        int drawW = Math.min(r.width-col, 16);
                        GuiDraw.gui.drawTexturedModelRectFromIcon(r.x + col, r.y + r.height - row - drawH, fluidIcon, drawW, drawH);
                    }
                }
                GL11.glColor4f(1, 1, 1, 1);
            }
        }



    }

    protected final List<PositionedStack> emptyList(){
        return EMPTY_LIST;
    }

    private final List<PositionedStack> EMPTY_LIST = ImmutableList.copyOf(Lists.<PositionedStack>newArrayList());

}
