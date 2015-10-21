package elec332.cmip.mods.waila;

import elec332.cmip.util.AbstractCMIPCompatHandler;
import elec332.cmip.util.WrappedTaggedList;
import mcp.mobius.waila.api.*;
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
    public final List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (shouldHandle(currenttip)){
            getWailaHead(currenttip, itemStack, accessor, config);
        }
        return currenttip;
    }

    public void getWailaHead(List<String> currenttip, ItemStack itemStack, IWailaDataAccessor accessor, IWailaConfigHandler config) {
    }

    @Override
    public final List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (shouldHandle(currenttip)){
            getWailaBody(currenttip, itemStack, accessor, config);
        }
        return currenttip;
    }

    public void getWailaBody(List<String> currenttip, ItemStack itemStack, IWailaDataAccessor accessor, IWailaConfigHandler config) {
    }

    @Override
    public final List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (shouldHandle(currenttip)){
            getWailaTail(currenttip, itemStack, accessor, config);
        }
        return currenttip;
    }

    public void getWailaTail(List<String> currenttip, ItemStack itemStack, IWailaDataAccessor accessor, IWailaConfigHandler config) {
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
                    break;
                case HEAD:
                    getRegistrar().registerHeadProvider(obj, clazz);
                    break;
                case BODY:
                    getRegistrar().registerBodyProvider(obj, clazz);
                    break;
                case TAIL:
                    getRegistrar().registerTailProvider(obj, clazz);
                    break;
                case NBT:
                    getRegistrar().registerNBTProvider(obj, clazz);
                    break;
            }
        }
    }

    protected static IWailaRegistrar getRegistrar(){
        return ModuleRegistrar.instance();
    }

    public enum Type{
        STACK, HEAD, BODY, TAIL, NBT
    }

    private boolean shouldHandle(List<String> currenttip){
        if (currenttip instanceof WrappedTaggedList){
            WrappedTaggedList<String, String> list = (WrappedTaggedList<String, String>) currenttip;
            String s = list.getAdditionalData().get("done"+getName());
            if (s != null && s.equals("yes")){
                return false;
            }
            list.getAdditionalData().put("done"+getName(), "yes");
            return true;
        }
        return true;
    }

}
