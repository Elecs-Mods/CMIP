package elec332.cmip.mods.notenoughitems;

import buildcraft.BuildCraftTransport;
import cpw.mods.fml.common.registry.GameRegistry;
import elec332.cmip.mods.MainCompatHandler;
import elec332.cmip.util.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

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
        //hideItem(new ItemStack(BuildCraftTransport.genericPipeBlock));
        if (Config.NEI.BuildCraft.hideBuildCraftFacades) {
            setItemListEntries(getFacade(), getReplacements());
        }
    }

    private ItemStack[] getReplacements(){
        return new ItemStack[]{
                buildcraft.transport.ItemFacade.allFacades.get(0), buildcraft.transport.ItemFacade.allHollowFacades.get(0)
        };
    }

    private Item getFacade(){
        return GameRegistry.findItem(MainCompatHandler.BUILDCRAFT_TRANSPORT, "pipeFacade");
    }
}
