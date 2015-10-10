package elec332.cmip.mods.waila;

import elec332.cmip.client.ClientMessageHandler;
import elec332.cmip.mods.MainCompatHandler;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeHousingInventory;
import forestry.api.apiculture.IBeekeepingLogic;
import forestry.core.tiles.IPowerHandler;
import forestry.core.tiles.IRestrictedAccessTile;
import forestry.core.tiles.TileEngine;
import forestry.mail.tiles.IMailContainer;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Elec332 on 10-10-2015.
 */
public class ForestryWailaHandler extends AbstractWailaCompatHandler {

    @Override
    public String getName() {
        return MainCompatHandler.FORESTRY;
    }

    @Override
    public void init() {
        registerHandler(Type.BODY, IRestrictedAccessTile.class, IPowerHandler.class, TileEngine.class, IBeeHousing.class, IMailContainer.class);
        registerHandler(Type.NBT, IRestrictedAccessTile.class, IPowerHandler.class, TileEngine.class, IBeeHousing.class, IMailContainer.class);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (tag != null){
            if (tag.hasKey(access)){
                currenttip.add(ClientMessageHandler.getAccessMessage()+StatCollector.translateToLocal("for."+tag.getString(access)));
            }
            if (tag.hasKey(energy)){
                currenttip.add(ClientMessageHandler.getEnergyMessage()+tag.getInteger(energy)+"/"+tag.getInteger(maxEnergy)+" RF");
            }
            if (tag.hasKey(energyOut)){
                currenttip.add(ClientMessageHandler.getOutputMessage()+tag.getInteger(energyOut)+" RF/t");
                currenttip.add(ClientMessageHandler.getHeatMessage()+tag.getString(heat));
            }
            if (tag.hasKey(specialData4)){
                boolean q = false;
                if (tag.hasKey(specialData1)){
                    currenttip.add(ClientMessageHandler.getDroneMessage()+tag.getString(specialData1));
                }
                if (tag.hasKey(specialData2)){
                    q = true;
                    currenttip.add(ClientMessageHandler.getQueenMessage()+tag.getString(specialData2));
                }
                if (tag.hasKey(progress) && q){
                    currenttip.add(ClientMessageHandler.getLifeSpanMessage()+ClientMessageHandler.format(tag.getInteger(progress))+"%");
                }
            }
            if (tag.hasKey(specialData3)){
                currenttip.add(ClientMessageHandler.getMailMessage(tag.getBoolean(specialData3)));
            }
        }
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
        if (tile != null && tag != null){
            if (tile instanceof IRestrictedAccessTile){
                tag.setString(access, ((IRestrictedAccessTile) tile).getAccessHandler().getAccessType().getName());
            }
            if (tile instanceof IPowerHandler){
                tag.setInteger(energy, ((IPowerHandler) tile).getEnergyManager().getTotalEnergyStored());
                tag.setInteger(maxEnergy, ((IPowerHandler) tile).getEnergyManager().getMaxEnergyStored());
            }
            if (tile instanceof TileEngine){
                tag.setString(heat, ((TileEngine) tile).getTemperatureState().toString());
                tag.setInteger(energyOut, ((TileEngine) tile).getCurrentOutput());
            }
            if (tile instanceof IBeeHousing){
                tag.setBoolean(specialData4, true);
                IBeeHousingInventory inv = ((IBeeHousing) tile).getBeeInventory();
                if (inv != null) {
                    if (inv.getDrone() != null)
                        tag.setString(specialData1, inv.getDrone().getItem().getItemStackDisplayName(inv.getDrone()));
                    if (inv.getQueen() != null)
                        tag.setString(specialData2, inv.getQueen().getItem().getItemStackDisplayName(inv.getQueen()));
                }
                IBeekeepingLogic beekeepingLogic = ((IBeeHousing) tile).getBeekeepingLogic();
                if (beekeepingLogic != null) {
                    tag.setInteger(progress, beekeepingLogic.getBeeProgressPercent());
                }
            }
            if (tile instanceof IMailContainer){
                tag.setBoolean(specialData3, ((IMailContainer) tile).hasMail());
            }
        }
        return tag;
    }
}
