package elec332.cmip.mods.notenoughitems;

import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.google.common.collect.Lists;
import elec332.core.main.ElecCore;

import java.util.List;
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
            @SuppressWarnings("all")
            public void run() {
                for (Map.Entry<AbstractCMIPNEITemplateRecipeHandler, Float> entry : AbstractNEICompatHandler.recipeHandlers.entrySet()){
                    GuiCraftingRecipe.craftinghandlers = AbstractNEICompatHandler.registerHandler(entry.getKey(), GuiCraftingRecipe.craftinghandlers, entry.getValue());
                }
                for (Map.Entry<AbstractCMIPNEITemplateRecipeHandler, Float> entry : AbstractNEICompatHandler.usageHandlers.entrySet()){
                    GuiUsageRecipe.usagehandlers = AbstractNEICompatHandler.registerHandler(entry.getKey(), GuiUsageRecipe.usagehandlers, entry.getValue());
                }
                for (Class<? extends TemplateRecipeHandler> clazz : AbstractNEICompatHandler.handlersToRemove){
                    List list = Lists.newArrayList();
                    for (Object obj : GuiCraftingRecipe.craftinghandlers){
                        if (obj.getClass() == clazz) {
                            if (!list.contains(obj))
                                list.add(obj);
                        }
                    }
                    for (Object obj : GuiUsageRecipe.usagehandlers){
                        if (obj.getClass() == clazz) {
                            if (!list.contains(obj))
                                list.add(obj);
                        }
                    }
                    for (Object toRemove : list) {
                        GuiCraftingRecipe.craftinghandlers.remove(toRemove);
                        GuiUsageRecipe.usagehandlers.remove(toRemove);
                    }
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
