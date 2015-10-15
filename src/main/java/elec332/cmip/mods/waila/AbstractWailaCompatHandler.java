package elec332.cmip.mods.waila;

import elec332.cmip.util.AbstractCMIPCompatHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Elec332 on 6-10-2015.
 */
public abstract class AbstractWailaCompatHandler extends AbstractCMIPCompatHandler implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
        if (tile != null && tag != null)
            tile.writeToNBT(tag);
        return tag;
    }

    protected void registerHandler(Type type, Class... classes){
        registerHandler(type, this, classes);
    }

    protected void registerHandler(Type type, IWailaDataProvider obj, Class... classes){
        for (Class clazz : classes){
            switch (type){
                case STACK:
                    getRegistrar().registerStackProvider(obj, clazz);
                    return;
                case HEAD:
                    getRegistrar().registerHeadProvider(obj, clazz);
                    return;
                case BODY:
                    getRegistrar().registerBodyProvider(obj, clazz);
                    return;
                case TAIL:
                    getRegistrar().registerTailProvider(obj, clazz);
                    return;
                case NBT:
                    getRegistrar().registerNBTProvider(obj, clazz);
                    return;
            }
        }
    }

    protected static IWailaRegistrar getRegistrar(){
        return ModuleRegistrar.instance();
    }

    public enum Type{
        STACK, HEAD, BODY, TAIL, NBT
    }

}
