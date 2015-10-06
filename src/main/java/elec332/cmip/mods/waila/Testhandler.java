package elec332.cmip.mods.waila;

import elec332.core.main.ElecCore;
import elec332.core.util.AbstractCompatHandler;

/**
 * Created by Elec332 on 6-10-2015.
 */
public class Testhandler extends AbstractCompatHandler.ICompatHandler {

    @Override
    public String getName() {
        return ElecCore.instance.modID();
    }

    @Override
    public void init() {
        System.out.println("Worked!");
    }

}
