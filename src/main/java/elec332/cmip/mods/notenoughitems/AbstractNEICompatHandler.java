package elec332.cmip.mods.notenoughitems;

import codechicken.nei.api.API;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameData;
import elec332.cmip.util.AbstractCMIPCompatHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Elec332 on 6-10-2015.
 */
public abstract class AbstractNEICompatHandler extends AbstractCMIPCompatHandler {

    public static final String CRAFTING = "crafting";

    private static Block representative;
    private static String nameOfRepresentative;

    static Map<AbstractCMIPNEITemplateRecipeHandler, Float> recipeHandlers;
    static Map<AbstractCMIPNEITemplateRecipeHandler, Float> usageHandlers;
    static List<Class<? extends TemplateRecipeHandler>> handlersToRemove;

    public void removeHandler(Class<? extends TemplateRecipeHandler> clazz){
        handlersToRemove.add(clazz);
    }

    public void registerUsageAndRecipeHandler(AbstractCMIPNEITemplateRecipeHandler handler, float position){
        registerRecipeHandler(handler, position);
        registerUsageHandler(handler, position);
    }

    public void registerRecipeHandler(AbstractCMIPNEITemplateRecipeHandler handler, float position){
        recipeHandlers.put(handler, position);
    }

    public void registerUsageHandler(AbstractCMIPNEITemplateRecipeHandler handler, float position){
        usageHandlers.put(handler, position);
        //GuiUsageRecipe.usagehandlers = registerHandler(handler, GuiUsageRecipe.usagehandlers, position);
    }

    /* Why do you make me return an ArrayList, NEI? :( */
    static <A> ArrayList<A> registerHandler(A obj, ArrayList<A> list, float position){
        /* Mimic NEI checking */
        for (Object o : list){
            if (o.getClass() == obj.getClass())
                return list;
        }

        if (position <= 0){
            list.add(obj);
            return list;
        }
        ArrayList<A> ret = Lists.newArrayList();
        if (position >= 1){
            ret.add(obj);
            ret.addAll(list);
            return ret;
        }
        int i = 0;
        int approxNewIndex = (int) (list.size() * position);
        System.out.println(approxNewIndex);
        for (A a : list){
            ret.add(a);
            if (i == approxNewIndex){
                ret.add(obj);
            }
            i++;
        }
        return ret;
    }

    public List<Item> toItemList(List<Block> blocks){
        List<Item> ret = Lists.newArrayList();
        for (Block block : blocks){
            ret.add(Item.getItemFromBlock(block));
        }
        return ret;
    }

    public List<ItemStack> toStackList(List<Item> items){
        List<ItemStack> ret = Lists.newArrayList();
        for (Item item : items){
            ret.add(new ItemStack(item));
        }
        return ret;
    }

    public void hideItems(List<ItemStack> stacks){
        for (ItemStack stack : stacks){
            hideItem(stack);
        }
    }

    public void hideItem(ItemStack stack){
        if (stack.getItem() == null)
            return;
        API.hideItem(stack);
    }

    public void removeFromSight(ItemStack stack){
        if (stack.getItem() == null)
            return;
        API.hideItem(stack);
        stack.getItem().setCreativeTab(null);
    }

    public void setItemListEntries(Item item, ItemStack... stacks){
        setItemListEntries(item, Lists.newArrayList(stacks));
    }

    public void setItemListEntries(Item item, List<ItemStack> stacks){
        API.setItemListEntries(item, stacks);
    }

    public static Block getRepresentative(){
        return representative;
    }

    public static String getNameOfRepresentative(){
        return nameOfRepresentative;
    }

    static {
        representative = Blocks.stone;
        nameOfRepresentative = GameData.getBlockRegistry().getNameForObject(representative);
        recipeHandlers = Maps.newHashMap();
        usageHandlers = Maps.newHashMap();
        handlersToRemove = Lists.newArrayList();
    }

}