package elec332.cmip.mods.notenoughitems;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.registry.GameData;
import elec332.cmip.CMIP;
import elec332.cmip.mods.MainCompatHandler;
import jds.bibliocraft.blocks.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Elec332 on 10-10-2015.
 */
public class BiblioCraftNEIHandler extends AbstractNEICompatHandler {

    @Override
    public String getName() {
        return MainCompatHandler.BIBLIOCRAFT;
    }

    private List<Block> biblioBlocks;
    private List<Class<? extends Block>> classes;

    @Override
    public void init() {
        getBlocks();
        List<ItemStack> toRemove = Lists.newArrayList();
        for (Block block : GameData.getBlockRegistry().typeSafeIterable()){
            if (shouldBeRemoved(block))
                toRemove.add(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
        }

        for (Block block : biblioBlocks){
            setItemListEntries(Item.getItemFromBlock(block), new ItemStack(block));
        }
        hideItems(toRemove);
    }

    private boolean shouldBeRemoved(Block block){
        if (biblioBlocks.contains(block))
            return false;
        for (Class<? extends Block> clazz : classes){
            if (clazz.isInstance(block.getClass()))
                return true;
        }
        return false;
    }

    public void getBlocks(){
        biblioBlocks = Lists.newArrayList();
        classes = Lists.newArrayList();
        boolean b = false;
        for (Field field : BlockLoader.class.getDeclaredFields()){
            if (Block.class.isAssignableFrom(field.getType())){
                try {
                    biblioBlocks.add((Block) field.get(null));
                } catch (IllegalAccessException e) {
                    if (!b) {
                        CMIP.logger.error("Error reflecting BiblioCraft.");
                        b = true;
                    }
                }
            }
        }
        for (Block block : biblioBlocks){
            classes.add(block.getClass());
        }
    }

}
