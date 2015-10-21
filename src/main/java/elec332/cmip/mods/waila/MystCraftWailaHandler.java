package elec332.cmip.mods.waila;

import elec332.cmip.client.ClientMessageHandler;
import elec332.cmip.mods.MainCompatHandler;
import elec332.cmip.util.Config;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Elec332 on 6-10-2015.
 */
public class MystCraftWailaHandler extends AbstractWailaCompatHandler {

    @Override
    public String getName() {
        return MainCompatHandler.MYSTCRAFT;
    }

    @Override
    public void init() {
        if (Config.WAILA.MystCraft.showDimData) {
            for (Class clazz : findClasses(mystcraftBookStands)) {
                registerHandler(Type.BODY, clazz);
                registerHandler(Type.NBT, clazz);
            }
        }
    }

    private static String[] mystcraftBookStands = new String[]{"com.xcompwiz.mystcraft.tileentity.TileEntityLectern",
            "com.xcompwiz.mystcraft.tileentity.TileEntityBookstand", "com.xcompwiz.mystcraft.tileentity.TileEntityBookReceptacle"};

    @Override
    public void getWailaBody(List<String> currenttip, ItemStack itemStack, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        ItemStack inv = ItemStack.loadItemStackFromNBT(accessor.getNBTData().getTagList("Items", 10).getCompoundTagAt(0));
        boolean valid = false;
        int dimension = 0;
        String name = "???";
        if (inv != null && inv.stackTagCompound != null){
            NBTTagCompound tag = inv.stackTagCompound;
            if (tag.hasKey("Dimension")){
                dimension = tag.getInteger("Dimension");
                valid = true;
            } else if (tag.hasKey("AgeUID")){
                dimension = tag.getInteger("AgeUID");
                valid = true;
            }
            if (tag.hasKey("DisplayName")){
                name = tag.getString("DisplayName");
            } else if (tag.hasKey("agename")){
                name = tag.getString("agename");
            }
        }
        if (valid){
            currenttip.add(ClientMessageHandler.getDimensionMessage() + dimension);
            currenttip.add(ClientMessageHandler.getNameMessage() + name);
        }
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        if (te != null){
            te.writeToNBT(tag);
        }
        return tag;
    }
}
