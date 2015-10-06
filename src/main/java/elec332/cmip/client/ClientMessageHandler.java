package elec332.cmip.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.StatCollector;

import static elec332.cmip.client.ClientMessageHandler.WailaSpecialChars.*;

/**
 * Created by Elec332 on 6-10-2015.
 */
@SideOnly(Side.CLIENT)
public class ClientMessageHandler {

    public static String getEmptyMessage(){
        return ITALIC + localise("cmip.message.empty");
    }

    public static String getLiquidMessage(){
        return localise("cmip.message.liquid") + ": ";
    }

    public static String getAmountMessage(){
        return localise("cmip.message.amount") + ": ";
    }

    public static String getDimensionMessage(){
        return localise("cmip.message.dimension") + ": ";
    }

    public static String getNameMessage(){
        return localise("cmip.message.name") + ": ";
    }

    private static String localise(String s){
        return StatCollector.translateToLocal(s);
    }

    /**
     * Copied from WAILA-API to avoid crashes when WAILA is not loaded
     */
    public static class WailaSpecialChars {

        public static String MCStyle  = "\u00A7";

        public static String BLACK    = MCStyle + "0";
        public static String DBLUE    = MCStyle + "1";
        public static String DGREEN   = MCStyle + "2";
        public static String DAQUA    = MCStyle + "3";
        public static String DRED     = MCStyle + "4";
        public static String DPURPLE  = MCStyle + "5";
        public static String GOLD     = MCStyle + "6";
        public static String GRAY     = MCStyle + "7";
        public static String DGRAY    = MCStyle + "8";
        public static String BLUE     = MCStyle + "9";
        public static String GREEN    = MCStyle + "a";
        public static String AQUA     = MCStyle + "b";
        public static String RED      = MCStyle + "c";
        public static String LPURPLE  = MCStyle + "d";
        public static String YELLOW   = MCStyle + "e";
        public static String WHITE    = MCStyle + "f";

        public static String OBF      = MCStyle + "k";
        public static String BOLD     = MCStyle + "l";
        public static String STRIKE   = MCStyle + "m";
        public static String UNDER    = MCStyle + "n";
        public static String ITALIC   = MCStyle + "o";
        public static String RESET    = MCStyle + "r";

        public static String WailaStyle     = "\u00A4";
        public static String WailaIcon      = "\u00A5";
        public static String WailaRenderer  = "\u00A6";
        public static String TAB         = WailaStyle + WailaStyle +"a";
        public static String ALIGNRIGHT  = WailaStyle + WailaStyle +"b";
        public static String ALIGNCENTER = WailaStyle + WailaStyle +"c";
        public static String HEART       = WailaStyle + WailaIcon  +"a";
        public static String HHEART      = WailaStyle + WailaIcon  +"b";
        public static String EHEART      = WailaStyle + WailaIcon  +"c";
        public static String RENDER      = WailaStyle + WailaRenderer +"a";

    }
}
