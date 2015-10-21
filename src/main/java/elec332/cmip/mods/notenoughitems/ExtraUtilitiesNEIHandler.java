package elec332.cmip.mods.notenoughitems;

import com.google.common.collect.Lists;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.multipart.microblock.ItemMicroBlock;
import cpw.mods.fml.common.registry.GameRegistry;
import elec332.cmip.mods.MainCompatHandler;
import elec332.cmip.util.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 11-10-2015.
 */
public class ExtraUtilitiesNEIHandler extends AbstractNEICompatHandler {

    @Override
    public String getName() {
        return MainCompatHandler.EXTRAUTILITIES;
    }

    @Override
    public void init() {
        if (Config.NEI.ExtraUtilities.hideExtraUtilitiesMultiParts) {
            int[] types = new int[]{0, 1, 2, 3};
            ItemStack microPart = ItemMicroBlock.getStack(new ItemStack(ExtraUtils.microBlocks), getNameOfRepresentative());
            List<ItemStack> replace = Lists.newArrayList();
            for (int i : types) {
                ItemStack stack = microPart.copy();
                stack.setItemDamage(i);
                replace.add(stack);
            }
            setItemListEntries(microPart.getItem(), replace);
        }
        if (Config.NEI.ExtraUtilities.hideFilledDrums) {
            setItemListEntries(Item.getItemFromBlock(ExtraUtils.drum), new ItemStack(ExtraUtils.drum, 1, 0), new ItemStack(ExtraUtils.drum, 1, 1));
        }
    }

}
