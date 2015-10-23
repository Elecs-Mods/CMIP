package elec332.cmip.mods.notenoughitems;

import codechicken.nei.recipe.TemplateRecipeHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.inventory.GuiContainer;

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

    public void addTransferRect(List<Class<? extends GuiContainer>> gui, Rectangle rectangle, String identifier, boolean addToRecipeGui, Object... args){
        RecipeTransferRect transferRect = new RecipeTransferRect(rectangle, identifier, args);
        if (addToRecipeGui)
            this.transferRects.add(transferRect);
        TemplateRecipeHandler.RecipeTransferRectHandler.registerRectsToGuis(gui, Lists.newArrayList(transferRect));
    }

    public void drawProgressBar(Rectangle rectangle, int u, int v, int direction, boolean invert){
        this.drawProgressBar(rectangle.x, rectangle.y, u, v, rectangle.width, rectangle.height, 48, invert ? direction + 4 : direction);
    }

}
