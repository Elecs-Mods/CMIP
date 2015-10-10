package elec332.cmip.mods.waila;

import codechicken.chunkloader.ChunkLoaderManager;
import codechicken.chunkloader.TileChunkLoader;
import codechicken.chunkloader.TileChunkLoaderBase;
import codechicken.core.ServerUtils;
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
public class ChickenChunksWailaHandler extends AbstractWailaCompatHandler {

    @Override
    public String getName() {
        return MainCompatHandler.CHICKENCHUNKS;
    }

    @Override
    public void init() {
        registerHandler(Type.BODY, TileChunkLoaderBase.class, TileChunkLoader.class);
        registerHandler(Type.NBT, TileChunkLoaderBase.class, TileChunkLoader.class);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (tag != null){
            if (tag.hasKey(access)) {
                boolean access = tag.getBoolean(ChickenChunksWailaHandler.access);
                if (!access) {
                    currenttip.add(ClientMessageHandler.getNoAccessMessage());
                    return currenttip;
                }
                currenttip.add(ClientMessageHandler.getOwnerMessage()+tag.getString(name));
                currenttip.add(ClientMessageHandler.getActiveMessage()+tag.getBoolean(active));
                if (tag.hasKey(range)){
                    currenttip.add(ClientMessageHandler.getRangeMessage()+tag.getInteger(range));
                    currenttip.add(ClientMessageHandler.getLoadedChunksMessage()+tag.getInteger(specialData1));
                }
            }
        }
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
        if (tag != null && tile != null){
            if (tile instanceof TileChunkLoaderBase){
                boolean access = !(((TileChunkLoaderBase) tile).getOwner() != null && !((TileChunkLoaderBase) tile).getOwner().equals(player.getCommandSenderName()) && (!ChunkLoaderManager.opInteract() || !ServerUtils.isPlayerOP(player.getCommandSenderName())));
                tag.setBoolean(ChickenChunksWailaHandler.access, access);
                if (access){
                    tag.setString(name, ((TileChunkLoaderBase) tile).getOwner());
                    tag.setBoolean(active, ((TileChunkLoaderBase) tile).active);
                    if (tile instanceof TileChunkLoader){
                        tag.setInteger(range, ((TileChunkLoader) tile).radius);
                        tag.setInteger(specialData1, ((TileChunkLoader) tile).countLoadedChunks());
                    }
                }
            }
        }
        return tag;
    }
}
