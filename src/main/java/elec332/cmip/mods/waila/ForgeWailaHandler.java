package elec332.cmip.mods.waila;

import com.google.common.collect.Lists;
import elec332.cmip.client.ClientMessageHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.List;

/**
 * Created by Elec332 on 6-10-2015.
 */
public class ForgeWailaHandler extends AbstractWailaCompatHandler {

    public ForgeWailaHandler(){
        ignored = Lists.newArrayList();
    }

    @Override
    public String getName() {
        return "Forge";
    }

    @Override
    public void init() {
        List<Class> toRemove = Lists.newArrayList();
        for (Class clazz : ModuleRegistrar.instance().bodyBlockProviders.keySet()){
            if (IFluidHandler.class.isAssignableFrom(clazz)){
                toRemove.add(clazz);
            } /*else if (Block.class.isAssignableFrom(clazz)){
                for (Block block : GameData.getBlockRegistry().typeSafeIterable()){
                    if (ITileEntityProvider.class.isAssignableFrom(clazz)){
                        try {
                            TileEntity tile = ((ITileEntityProvider) block).createNewTileEntity(null, 0);
                            if (IFluidHandler.class.isAssignableFrom(tile.getClass()))
                                toRemove.add(clazz);
                        } catch (Exception e){
                            //Ignore
                        }
                    } else if (block.hasTileEntity(0)){
                        try {
                            TileEntity tile = block.createTileEntity(null, 0);
                            if (IFluidHandler.class.isAssignableFrom(tile.getClass()))
                                toRemove.add(clazz);
                        } catch (Exception e){
                            //Ignore
                        }
                    }
                }
            }*/
        }
        for (Class clazz : toRemove){
            ModuleRegistrar.instance().bodyBlockProviders.remove(clazz);
        }
        registerHandler(Type.BODY, IFluidHandler.class);

        ignored.add("tconstruct.smeltery.logic.LavaTankLogic");
    }

    public List<String> ignored;

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (isIgnored(accessor))
            return currenttip;
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

    private boolean isIgnored(IWailaDataAccessor accessor){
        return ignored.contains(accessor.getTileEntity().getClass().getCanonicalName());
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
