package elec332.cmip.mods.notenoughitems;

import appeng.api.AEApi;
import appeng.items.parts.ItemFacade;
import elec332.cmip.mods.MainCompatHandler;
import elec332.cmip.util.Config;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 10-10-2015.
 */
public class AE2NEIHandler extends AbstractNEICompatHandler{

    @Override
    public String getName() {
        return MainCompatHandler.APPLIEDENERGISTICS2;
    }

    @Override
    public void init() {
        if (Config.NEI.AE2.hideAE2Facades)
            setItemListEntries(AEApi.instance().items().itemFacade.item(), ((ItemFacade) AEApi.instance().items().itemFacade.item()).createFacadeForItem(new ItemStack(Blocks.stone), false));
    }

}
