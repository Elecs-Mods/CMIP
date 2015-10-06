package elec332.cmip;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import elec332.cmip.mods.MainCompatHandler;
import elec332.core.helper.FileHelper;
import elec332.core.helper.MCModInfo;
import elec332.core.modBaseUtils.ModInfo;
import elec332.cmip.proxies.CommonProxy;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by Elec332 on 24-2-2015.
 */
@Mod(modid = CMIP.ModID, name = CMIP.ModName, dependencies = ModInfo.DEPENDENCIES+"@[#ELECCORE_VER#,)",
        acceptedMinecraftVersions = ModInfo.ACCEPTEDMCVERSIONS, useMetadata = true, canBeDeactivated = true)
public class CMIP {

    public static final String ModName = "CMIP";
    public static final String ModID = "CMIP";

    @SidedProxy(clientSide = "elec332.cmip.proxies.ClientProxy", serverSide = "elec332.cmip.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(ModID)
    public static CMIP instance;
    public static Logger logger;
    public static File mainConfigFolder;
    public static Configuration mainConfig;
    public static MainCompatHandler compatHandler;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        mainConfigFolder = FileHelper.getCustomConfigFolderElec(event, ModName);
        mainConfig = new Configuration(new File(mainConfigFolder, "main.cfg"));
        mainConfig.load();
        compatHandler = new MainCompatHandler(mainConfig, logger);
        compatHandler.loadHandlers();
        //setting up mod stuff

        MCModInfo.CreateMCModInfo(event, "Created by Elec332",
                "Provides more cross-mod integration.",
                "website link", "logo",
                new String[]{"Elec332"});
        if (mainConfig.hasChanged())
            mainConfig.save();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //NOPE
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        compatHandler.init();
    }

}
