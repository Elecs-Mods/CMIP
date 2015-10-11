package elec332.cmip.mods.waila;

import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
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
 * Created by Elec332 on 11-10-2015.
 */
public class ExtraUtilitiesWailaHandler extends AbstractWailaCompatHandler {

    @Override
    public String getName() {
        return MainCompatHandler.EXTRAUTILITIES;
    }

    @Override
    public void init() {
        registerHandler(Type.BODY, TileEntityTransferNode.class);
        registerHandler(Type.NBT, TileEntityTransferNode.class);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (tag != null){
            if (tag.hasKey(specialData1)){
                currenttip.add(ClientMessageHandler.getSearchLocationMessage()+tag.getInteger(specialData1)+", "+tag.getInteger(specialData2)+", "+tag.getInteger(specialData3));
            }
        }
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
        if (tile != null && tag != null){
            if (tile instanceof TileEntityTransferNode) {
                tag.setInteger(specialData1, ((TileEntityTransferNode) tile).pipe_x);
                tag.setInteger(specialData2, ((TileEntityTransferNode) tile).pipe_y);
                tag.setInteger(specialData3, ((TileEntityTransferNode) tile).pipe_z);
            }
        }
        return tag;
    }
}
