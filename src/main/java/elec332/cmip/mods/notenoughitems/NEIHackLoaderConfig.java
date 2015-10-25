package elec332.cmip.mods.notenoughitems;

import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import elec332.core.main.ElecCore;

import java.util.Map;

/**
 * Created by Elec332 on 25-10-2015.
 *
 * Yes, the class name MUST begin with "NEI" and end with "Config"......
 *
 * The only reason I don't register it directly, is that I do not want my
 * handlers to pop up first every single time....
 *
 * (IConfigureNEI handlers get registered on the first world tick -_-)
 */
public final class NEIHackLoaderConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        ElecCore.tickHandler.registerCall(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<AbstractCMIPNEITemplateRecipeHandler, Float> entry : AbstractNEICompatHandler.recipeHandlers.entrySet()){
                    GuiCraftingRecipe.craftinghandlers = AbstractNEICompatHandler.registerHandler(entry.getKey(), GuiCraftingRecipe.craftinghandlers, entry.getValue());
                }
                for (Map.Entry<AbstractCMIPNEITemplateRecipeHandler, Float> entry : AbstractNEICompatHandler.usageHandlers.entrySet()){
                    GuiUsageRecipe.usagehandlers = AbstractNEICompatHandler.registerHandler(entry.getKey(), GuiUsageRecipe.usagehandlers, entry.getValue());
                }
            }
        });
    }

    @Override
    public String getName() {
        return "Bew";
    }

    @Override
    public String getVersion() {
        return "None";
    }
}
