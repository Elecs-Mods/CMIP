package elec332.cmip.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by Elec332 on 11-10-2015.
 */
public class ContainerNull extends Container {

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }

}
