package elec332.basemod.main;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import elec332.basemod.init.BlockRegister;
import elec332.basemod.init.CommandRegister;
import elec332.basemod.init.ItemRegister;
import elec332.core.helper.FileHelper;
import elec332.core.helper.MCModInfo;
import elec332.core.modBaseUtils.ModBase;
import elec332.core.modBaseUtils.ModInfo;
import elec332.basemod.proxies.CommonProxy;

import java.io.File;

/**
 * Created by Elec332 on 24-2-2015.
 */
@Mod(modid = ModName.ModNID, name = ModName.ModName, dependencies = ModInfo.DEPENDENCIES+"@[#ELECCORE_VER#,)",
        acceptedMinecraftVersions = ModInfo.ACCEPTEDMCVERSIONS, useMetadata = true, canBeDeactivated = true)
public class ModName extends ModBase {

    public static final String ModName = "YourModName"; //Human readable name
    public static final String ModID = "yourmodid";  //modid (usually lowercase)

    @SidedProxy(clientSide = "elec332.basemod.proxies.ClientProxy", serverSide = "elec332.basemod.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(ModID)
    public static ModName instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.cfg = FileHelper.getConfigFileElec(event);
        loadConfiguration();
        ItemRegister.instance.preInit(event);
        BlockRegister.instance.preInit(event);
        //setting up mod stuff

        loadConfiguration();
        MCModInfo.CreateMCModInfo(event, "Created by ....",
                "mod description",
                "website link", "logo",
                new String[]{"authorList"});
        notifyEvent(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        loadConfiguration();
        ItemRegister.instance.init(event);
        BlockRegister.instance.init(event);
        //register items/blocks

        notifyEvent(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        loadConfiguration();
        //Mod compat stuff

        notifyEvent(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        CommandRegister.instance.init(event);
    }

    File cfg;

    @Override
    public File configFile() {
        return cfg;
    }

    @Override
    public String modID(){
        return ModID;
    }
}
