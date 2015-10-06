package elec332.cmip.mods.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.List;

/**
 * Created by Elec332 on 6-10-2015.
 */
public class ForgeWailaHandler extends AbstractWailaCompatHandler {

    @Override
    public String getName() {
        return "Forge";
    }

    @Override
    public void init() {
        registerHandler(Type.BODY, IFluidHandler.class);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        FluidTankInfo[] data = ((IFluidHandler) accessor.getTileEntity()).getTankInfo(accessor.getSide());
        if (data != null) {
            for (FluidTankInfo info : data) {
                if (info != null) {
                    currenttip.add((info.fluid == null ? 0 : info.fluid.amount) + " / " + info.capacity + (info.fluid == null ? "" : " " + StatCollector.translateToLocal(info.fluid.getUnlocalizedName())));
                }
            }
        }
        return currenttip;
    }
}
