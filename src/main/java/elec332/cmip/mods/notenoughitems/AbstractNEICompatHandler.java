package elec332.cmip.mods.notenoughitems;

import codechicken.nei.api.API;
import elec332.cmip.util.AbstractCMIPCompatHandler;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 6-10-2015.
 */
public abstract class AbstractNEICompatHandler extends AbstractCMIPCompatHandler {

    public static final String CRAFTING = "crafting";

    public void hideItem(ItemStack stack){
        if (stack.getItem() == null)
            return;
        API.hideItem(stack);
        stack.getItem().setCreativeTab(null);
    }

}
