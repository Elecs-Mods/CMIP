package elec332.cmip.mods.waila;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.integration.modules.waila.PartWailaDataProvider;
import appeng.integration.modules.waila.part.BasePartWailaDataProvider;
import appeng.integration.modules.waila.part.IPartWailaDataProvider;
import appeng.parts.p2p.PartP2PTunnel;
import appeng.tile.qnb.TileQuantumBridge;
import elec332.cmip.CMIP;
import elec332.cmip.client.ClientMessageHandler;
import elec332.cmip.mods.MainCompatHandler;
import elec332.core.java.ReflectionHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.reflect.Field;
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
        try {
            PartWailaDataProvider aePartHandler = new PartWailaDataProvider();
            Field ae2Providers = PartWailaDataProvider.class.getDeclaredField("providers");
            @SuppressWarnings("unchecked")
            List<IPartWailaDataProvider> list = (List<IPartWailaDataProvider>) ReflectionHelper.makeFinalFieldModifiable(ae2Providers).get(aePartHandler);
            list.clear();
            list.add(new P2PHandler());
            for (Type type : Type.values()){
                registerHandler(type, aePartHandler, IPartHost.class);
            }
        } catch (Exception e){
            CMIP.logger.error("Error registering AE2 part handler.");
        }

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

    public static class P2PHandler extends BasePartWailaDataProvider{

        @Override
        public List<String> getWailaBody(IPart part, List<String> currentToolTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            NBTTagCompound tag = accessor.getNBTData();
            if (tag != null) {
                if (tag.hasKey(specialData1)) {
                    long l = tag.getLong(specialData1);
                    if (l != 0L) {
                        currentToolTip.add(ClientMessageHandler.getFrequencyMessage() + tag.getLong(specialData1));
                    } else {
                        currentToolTip.add(ClientMessageHandler.getNoConnectionMessage());
                    }
                    currentToolTip.add(ClientMessageHandler.getNiceInputModeMessage(tag.getBoolean(specialData2)));
                }
            }
            return currentToolTip;
        }

        @Override
        public NBTTagCompound getNBTData(EntityPlayerMP player, IPart part, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
            if (tile != null && tag != null){
                if (part instanceof PartP2PTunnel){
                    tag.setLong(specialData1, ((PartP2PTunnel) part).freq);
                    tag.setBoolean(specialData2, ((PartP2PTunnel) part).output);
                }
            }
            return tag;
        }
    }
}
