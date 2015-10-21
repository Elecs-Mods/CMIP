package elec332.cmip.mods.notenoughitems;

import codechicken.nei.api.API;
import elec332.cmip.CMIP;
import elec332.cmip.client.nei.RFToolsCrafterOverlayHandler;
import elec332.cmip.network.PacketRFToolsCrafterNEIRecipe;
import elec332.cmip.util.Config;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

import java.lang.reflect.Field;

/**
 * Created by Elec332 on 6-10-2015.
 */
public class RFToolsNEIHandler extends AbstractNEICompatHandler {

    @Override
    public String getName() {
        return "rftools";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init() {
        if (Config.NEI.RFTools.addNEICrafterSupport) {
            try {
                guiCrafterClass = (Class<? extends GuiContainer>) Class.forName("mcjty.rftools.blocks.crafter.GuiCrafter");
                containerCrafterClass = Class.forName("mcjty.rftools.blocks.crafter.CrafterContainer");
                tile = containerCrafterClass.getDeclaredField("crafterBaseTE");
                tile.setAccessible(true);
                //API.registerGuiOverlay(guiCrafterClass, CRAFTING, new RFToolsCrafterStackPositioner());
                API.registerGuiOverlayHandler(guiCrafterClass, new RFToolsCrafterOverlayHandler(), CRAFTING);
                CMIP.networkHandler.registerServerPacket(PacketRFToolsCrafterNEIRecipe.class);
            } catch (Exception e) {
                CMIP.logger.info("Error loading NEI handler for RFTools", e);
            }
        }
    }

    public static Class<? extends GuiContainer> guiCrafterClass;
    public static Class containerCrafterClass;
    private static Field tile;

    public static TileEntity getTileFromCrafter(Container crafterContainer){
        try {
            if (crafterContainer.getClass().equals(containerCrafterClass)){
                    return (TileEntity) tile.get(crafterContainer);
            }
        } catch (Exception e){
            //
        }
        throw new IllegalArgumentException();
    }

}
