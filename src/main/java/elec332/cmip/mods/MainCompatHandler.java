package elec332.cmip.mods;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.Loader;
import elec332.cmip.CMIP;
import elec332.cmip.util.CMIPCompatHandler;
import elec332.cmip.util.Config;
import elec332.core.util.AbstractCompatHandler;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by Elec332 on 6-10-2015.
 */
public class MainCompatHandler extends AbstractCompatHandler {

    public MainCompatHandler(Configuration config, Logger logger) {
        super(config, logger);
        handlers = Lists.newArrayList();
    }

    private List<CMIPCompatHandler> handlers;

    @Override
    public boolean addCategoryComment() {
        return false;
    }

    @Override
    public void loadList() {
    }

    public void addHandler(CMIPCompatHandler handler) {
        if (configuration.getBoolean("enabled", Config.mainHandlerCategory+"."+getCategory(handler).toLowerCase(), handler.enabledByDefault(), "When set to false, none of the handlers for "+handler.getModName()+" will be loaded.") && Loader.isModLoaded(handler.getModName())){
            handlers.add(handler);
            handler.registerModules();
        }
    }

    private String getCategory(CMIPCompatHandler handler){
        List<String> validNames = CMIP.configWrapper.getRegisteredCategories();
        String name = handler.getConfig().toLowerCase();
        if (validNames.contains(Config.mainHandlerCategory+"."+name))
            return name;
        for (String s : validNames){
            if (s.contains(Config.mainHandlerCategory+".")){
                s = s.replace(Config.mainHandlerCategory+".", "");
                if (s.contains(name) || name.contains(s))
                    return s;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void init() {
        super.init();
        initHandlers();
    }

    private void initHandlers(){
        for (CMIPCompatHandler handler : handlers){
            handler.init();
        }
    }

    /* Yes, im lazy */
    public CMIPCompatHandler createHandler(String modName){
        return new CMIPCompatHandler(modName);
    }

    public CMIPCompatHandler registerHandler(CMIPCompatHandler handler){
        addHandler(handler);
        return handler;
    }

    public CMIPCompatHandler createAndRegisterHandler(String modName){
        CMIPCompatHandler handler = createHandler(modName);
        return registerHandler(handler);
    }
    /* Approx end of lazyness */

    public static CMIPCompatHandler waila, nei;

    public void loadHandlers(){
        waila = createAndRegisterHandler(WAILA);
        nei = registerHandler(createHandler(NEI).setConfigName("NEI"));
    }

    public static final String APPLIEDENERGISTICS2 = "appliedenergistics2";
    public static final String ARSMAGICA2 = "arsmagica2";
    public static final String BIBLIOCRAFT = "BiblioCraft";
    public static final String BIOMESOPLENTY = "BiomesOPlenty";
    public static final String BUILDCRAFT = "BuildCraft|Core";
    public static final String BUILDCRAFT_BUILDERS = "BuildCraft|Builders";
    public static final String BUILDCRAFT_CORE = "BuildCraft|Core";
    public static final String BUILDCRAFT_ENERGY = "BuildCraft|Energy";
    public static final String BUILDCRAFT_FACTORY = "BuildCraft|Factory";
    public static final String BUILDCRAFT_SILICON = "BuildCraft|Factory";
    public static final String BUILDCRAFT_TRANSPORT = "BuildCraft|Transport";
    public static final String CHICKENCHUNKS = "ChickenChunks";
    public static final String COMPUTERCRAFT = "ComputerCraft";
    public static final String COMPUTERCRAFT_TURTLE = "CCTurtle";
    public static final String DRACONICEVOLUTION = "DraconicEvolution";
    public static final String ENDERSTORAGE = "EnderStorage";
    public static final String EXTRABEES = "ExtraBees";
    public static final String EXTRAUTILITIES = "ExtraUtilities";
    public static final String FORESTRY = "Forestry";
    public static final String FORGEMULTIPART = "ForgeMicroblock";
    public static final String GREGTECH = "gregtech";
    public static final String IC2 = "IC2";
    public static final String MAGICBEES = "MagicBees";
    public static final String MAGNETICRAFT = "Magneticraft";
    public static final String MEKANISM = "Mekanism";
    public static final String MPS = "powersuits";
    public static final String MFR = "MineFactoryReloaded";
    public static final String MYSTCRAFT = "Mystcraft";
    public static final String NEI = "NotEnoughItems";
    public static final String OPENBLOCKS = "OpenBlocks";
    public static final String OPENCOMPUTERS = "OpenComputers";
    public static final String RAILCRAFT = "Railcraft";
    public static final String STEVESCARTS = "StevesCarts";
    public static final String TINKERSCONSTRUCT = "TConstruct";
    public static final String TINKERSMECHWORKS = "TMechworks";
    public static final String TINKERSSTEELWORKS = "TSteelworks";
    public static final String THAUMCRAFT = "Thaumcraft";
    public static final String WAILA = "Waila";
    public static final String WIRELESSREDSTONECBE = "WR-CBE|Core";

}
