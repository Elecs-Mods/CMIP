package elec332.cmip.mods.waila;

import com.brandon3055.draconicevolution.common.tileentities.energynet.TileRemoteEnergyBase;
import elec332.cmip.client.ClientMessageHandler;
import elec332.cmip.mods.MainCompatHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Elec332 on 13-10-2015.
 */
public class DraconicEvolutionWailaHandler extends AbstractWailaCompatHandler {

    @Override
    public String getName() {
        return MainCompatHandler.DRACONICEVOLUTION;
    }

    @Override
    public void init() {
        registerHandler(Type.BODY, TileRemoteEnergyBase.class);
        registerHandler(Type.NBT, TileRemoteEnergyBase.class);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (tag != null){
            if (tag.hasKey(specialData1)){
                currenttip.add(ClientMessageHandler.getConnectedMachinesMessage()+tag.getInteger(specialData1));
            }
        }
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
        if (tile != null && tag != null){
            if (tile instanceof TileRemoteEnergyBase){
                tag.setInteger(specialData1, ((TileRemoteEnergyBase) tile).linkedDevices.size());
            }
        }
        return tag;
    }
}
