package elec332.cmip.mods.notenoughitems;

import buildcraft.BuildCraftTransport;
import buildcraft.transport.ItemFacade;
import elec332.cmip.mods.MainCompatHandler;
import elec332.cmip.util.Config;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 10-10-2015.
 */
public class BuildCraftNEIHandler extends AbstractNEICompatHandler {

    @Override
    public String getName() {
        return MainCompatHandler.BUILDCRAFT;
    }

    @Override
    public void init() {
        hideItem(new ItemStack(BuildCraftTransport.genericPipeBlock));
        if (Config.NEI.BuildCraft.hideBuildCraftFacades) {
            setItemListEntries(BuildCraftTransport.facadeItem, ItemFacade.allFacades.get(0), ItemFacade.allHollowFacades.get(0));
        }
    }
}
