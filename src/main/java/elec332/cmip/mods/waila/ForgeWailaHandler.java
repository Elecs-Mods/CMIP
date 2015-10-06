package elec332.cmip.mods.waila;

import com.google.common.collect.Lists;
import elec332.cmip.client.ClientMessageHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
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
        List<FluidTankInfo> data = filter(((IFluidHandler) accessor.getTileEntity()).getTankInfo(accessor.getSide()));
        if (data.isEmpty()) {
            currenttip.add(ClientMessageHandler.getEmptyMessage());
        } else if (data.size() == 1){
            FluidTankInfo info = data.get(0);
            if (info.fluid == null || info.fluid.amount == 0) {
                currenttip.add(ClientMessageHandler.getEmptyMessage());
            } else {
                currenttip.add(ClientMessageHandler.getLiquidMessage() + info.fluid.getLocalizedName());
                currenttip.add(ClientMessageHandler.getAmountMessage() + info.fluid.amount + "/" + info.capacity);
            }
        } else {
            for (FluidTankInfo info : data){
                currenttip.add((info.fluid == null ? 0 : info.fluid.amount) + "/" + info.capacity + (info.fluid == null ? "" : " " + info.fluid.getLocalizedName()));
            }
        }
        return currenttip;
    }

    private List<FluidTankInfo> filter(FluidTankInfo... data){
        List<FluidTankInfo> ret = Lists.newArrayList();
        if (data != null){
            for (FluidTankInfo fluidTankInfo : data){
                if (fluidTankInfo != null)
                    ret.add(fluidTankInfo);
            }
        }
        return ret;
    }
}
