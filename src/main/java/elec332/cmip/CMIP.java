package elec332.cmip;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import elec332.cmip.mods.MainCompatHandler;
import elec332.cmip.proxies.CommonProxy;
import elec332.cmip.util.Config;
import elec332.cmip.util.WrappedTaggedList;
import elec332.core.config.ConfigWrapper;
import elec332.core.helper.FileHelper;
import elec332.core.helper.MCModInfo;
import elec332.core.modBaseUtils.ModInfo;
import elec332.core.network.NetworkHandler;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Created by Elec332 on 24-2-2015.
 */
@Mod(modid = CMIP.ModID, name = CMIP.ModName, dependencies = ModInfo.DEPENDENCIES+"@[#ELECCORE_VER#,);after:Waila;after:NotEnoughItems",
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
    public static NetworkHandler networkHandler;
    public static ConfigWrapper configWrapper;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        mainConfigFolder = FileHelper.getCustomConfigFolderElec(event, ModName);
        networkHandler = new NetworkHandler(ModID);
        mainConfig = new Configuration(new File(mainConfigFolder, "main.cfg"));
        mainConfig.load();
        configWrapper = new ConfigWrapper(mainConfig);
        compatHandler = new MainCompatHandler(/*mainConfig*/null, logger);
        compatHandler.loadHandlers();
        if (Loader.isModLoaded("Waila")){
            WrappedTaggedList.ListReplacer.registerAll();
        }
        configWrapper.registerConfigWithInnerClasses(new Config());
        configWrapper.refresh();
        //setting up mod stuff

        MCModInfo.CreateMCModInfo(event, "Created by Elec332",
                "Provides more cross-mod integration.",
                "website link", "logo",
                new String[]{"Elec332"});
        if (mainConfig.hasChanged())
            mainConfig.save();
        /*Class c = Config.WAILA.AE2.class;
        System.out.println(c.getName());
        System.out.println(c.getCanonicalName());
        System.out.println(c.toString());
        System.out.println(c.getSimpleName());
        System.out.println(c.getPackage().getName());
        System.exit(0);*/
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //NOPE
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        //NOPE
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event){
        compatHandler.init();
    }

}
