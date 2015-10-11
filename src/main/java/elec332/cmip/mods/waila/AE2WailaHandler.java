package elec332.cmip.mods.waila;

import appeng.tile.qnb.TileQuantumBridge;
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
 * Created by Elec332 on 10-10-2015.
 */
public class AE2WailaHandler extends AbstractWailaCompatHandler {

    @Override
    public String getName() {
        return MainCompatHandler.APPLIEDENERGISTICS2;
    }

    @Override
    public void init() {
        registerHandler(Type.BODY, TileQuantumBridge.class);
        registerHandler(Type.NBT, TileQuantumBridge.class);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (tag != null){
            if (tag.hasKey(specialData1)){
                currenttip.add(ClientMessageHandler.getFrequencyMessage()+tag.getLong(specialData1));
            }
        }
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
        if (tile != null && tag != null){
            if (tile instanceof TileQuantumBridge){
                tag.setLong(specialData1, ((TileQuantumBridge) tile).getQEFrequency());
            }
        }
        return tag;
    }
}
