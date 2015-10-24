package elec332.cmip.mods.notenoughitems;

import codechicken.nei.api.API;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.registry.GameData;
import elec332.cmip.util.AbstractCMIPCompatHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 6-10-2015.
 */
public abstract class AbstractNEICompatHandler extends AbstractCMIPCompatHandler {

    public static final String CRAFTING = "crafting";

    private static Block representative;
    private static String nameOfRepresentative;

    public void registerUsageAndRecipeHandler(AbstractCMIPNEITemplateRecipeHandler handler){
        registerRecipeHandler(handler);
        registerUsageHandler(handler);
    }

    public void registerRecipeHandler(AbstractCMIPNEITemplateRecipeHandler handler){
        API.registerRecipeHandler(handler);
    }

    public void registerUsageHandler(AbstractCMIPNEITemplateRecipeHandler handler){
        API.registerUsageHandler(handler);
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
    }

}