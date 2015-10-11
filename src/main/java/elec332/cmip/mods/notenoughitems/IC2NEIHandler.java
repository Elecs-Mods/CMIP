package elec332.cmip.mods.notenoughitems;

import elec332.cmip.mods.MainCompatHandler;
import ic2.core.Ic2Items;

/**
 * Created by Elec332 on 10-10-2015.
 */
public class IC2NEIHandler extends AbstractNEICompatHandler {

    @Override
    public String getName() {
        return MainCompatHandler.IC2;
    }

    @Override
    public void init() {
        removeFromSight(Ic2Items.reinforcedDoorBlock);
        removeFromSight(Ic2Items.copperCableBlock);
        removeFromSight(Ic2Items.miningPipeTip);
    }

}
