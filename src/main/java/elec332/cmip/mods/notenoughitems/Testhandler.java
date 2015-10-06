package elec332.cmip.mods.notenoughitems;

import elec332.core.util.AbstractCompatHandler;

/**
 * Created by Elec332 on 6-10-2015.
 */
public class Testhandler extends AbstractCompatHandler.ICompatHandler {
    @Override
    public String getName() {
        return "NotEnoughItems";
    }

    @Override
    public void init() {
        System.out.println("NEI loaded!");
    }
}
