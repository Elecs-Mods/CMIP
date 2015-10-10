package elec332.cmip.mods.waila;

import buildcraft.api.boards.RedstoneBoardRegistry;
import buildcraft.api.boards.RedstoneBoardRobotNBT;
import buildcraft.api.robots.EntityRobotBase;
import buildcraft.api.robots.RobotManager;
import buildcraft.core.lib.block.TileBuildCraft;
import buildcraft.core.lib.engines.TileEngineBase;
import buildcraft.robotics.EntityRobot;
import buildcraft.robotics.boards.BCBoardNBT;
import buildcraft.silicon.TileLaserTableBase;
import com.google.common.collect.Lists;
import elec332.cmip.client.ClientMessageHandler;
import elec332.cmip.mods.MainCompatHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Elec332 on 10-10-2015.
 */
public class BuildCraftWailaHandler extends AbstractWailaCompatHandler implements IWailaEntityProvider{

    @Override
    public String getName() {
        return MainCompatHandler.BUILDCRAFT;
    }

    @Override
    public void init() {
        registerHandler(Type.BODY, TileBuildCraft.class, TileEngineBase.class, TileLaserTableBase.class);
        registerHandler(Type.NBT, TileBuildCraft.class, TileEngineBase.class, TileLaserTableBase.class);
        getRegistrar().registerBodyProvider((IWailaEntityProvider)this, EntityRobotBase.class);
        getRegistrar().registerNBTProvider((IWailaEntityProvider) this, EntityRobotBase.class);
        getRegistrar().registerNBTProvider((IWailaEntityProvider) this, EntityRobot.class);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (tag != null){
            if (tag.hasKey(energy)){
                int energy = tag.getInteger(BuildCraftWailaHandler.energy);
                if (tag.hasKey(maxEnergy)){
                    currenttip.add(ClientMessageHandler.getEnergyMessage()+energy+"/"+tag.getInteger(maxEnergy)+" RF");
                }
                currenttip.add(ClientMessageHandler.getEnergyMessage()+energy+" RF");
            }
            if (tag.hasKey(avgEnergy)){
                currenttip.add(ClientMessageHandler.getAverageMessage()+ClientMessageHandler.getHeatMessage()+ClientMessageHandler.format(tag.getFloat(avgEnergy)));
            }
            if (tag.hasKey(heat)){
                currenttip.add(ClientMessageHandler.getHeatMessage()+tag.getInteger(heat));
            }
        }
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
        if (tile != null && tag != null){

            if (tile instanceof TileBuildCraft){
                tag.setInteger(energy, ((TileBuildCraft) tile).getBattery().getEnergyStored());
                tag.setInteger(maxEnergy, ((TileBuildCraft) tile).getBattery().getMaxEnergyStored());
            }
            if (tile instanceof TileEngineBase){
                tag.removeTag(maxEnergy);
                tag.setInteger(energy, ((TileEngineBase) tile).getEnergyStored());
                tag.setFloat(heat, (float)((TileEngineBase) tile).getCurrentHeatValue());
            }
            if (tile instanceof TileLaserTableBase){
                tag.removeTag(maxEnergy);
                tag.setInteger(energy, ((TileLaserTableBase) tile).getEnergy());
                tag.setInteger(avgEnergy, ((TileLaserTableBase) tile).getRecentEnergyAverage());
            }
        }
        return tag;
    }

    @Override
    public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config){
        currenttip.clear();
        NBTTagCompound tag = accessor.getNBTData();
        if (tag != null){
            if (tag.hasKey(energy)){
                currenttip.add(ClientMessageHandler.getEnergyMessage()+tag.getInteger(energy)+"/"+tag.getInteger(maxEnergy)+" RF");
            }
            if (tag.hasKey(name)){
                String id = tag.getString(name);
                List<String> dummy = Lists.newArrayList();
                RedstoneBoardRegistry.instance.getRedstoneBoard(id).addInformation(null, Minecraft.getMinecraft().thePlayer, dummy, true);
                if (!dummy.isEmpty()) {
                    String desc = dummy.get(0);
                    currenttip.add(ClientMessageHandler.WailaSpecialChars.ALIGNCENTER + EnumChatFormatting.getTextWithoutFormattingCodes(desc));
                }
            }
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config){
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world){
        if (ent != null && tag != null){
            if (ent instanceof EntityRobotBase){
                tag.setInteger(energy, ((EntityRobotBase) ent).getEnergy());
                tag.setInteger(maxEnergy, ((EntityRobotBase) ent).getBattery().getMaxEnergyStored());
            }
            if (ent instanceof EntityRobot){
                RedstoneBoardRobotNBT robotNBT = ((EntityRobot) ent).board.getNBTHandler();
                if (robotNBT != RedstoneBoardRegistry.instance.getEmptyRobotBoard()){
                    tag.setString(name, robotNBT.getID());
                }
            }
        }
        return tag;
    }

}
