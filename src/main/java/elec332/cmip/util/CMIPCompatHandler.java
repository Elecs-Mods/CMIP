package elec332.cmip.util;

import com.google.common.reflect.ClassPath;
import elec332.cmip.CMIP;
import elec332.core.config.ConfigWrapper;
import elec332.core.util.AbstractCompatHandler;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Created by Elec332 on 6-10-2015.
 */
public class CMIPCompatHandler extends AbstractCompatHandler {

    public CMIPCompatHandler(String name){
        this(name, name.toLowerCase());
    }

    public CMIPCompatHandler(String name, String packageName) {
        super(/*ConfigWrapper.wrapCategoryAsConfig(CMIP.mainConfig, name)*/CMIP.mainConfig, CMIP.logger);
        if (!CMIP.configWrapper.hasBeenLoaded())
            throw new IllegalArgumentException();
        this.name = name;
        this.packageName = "elec332.cmip.mods."+packageName;
        this.enabledByDefault = true;
        this.config = name.toLowerCase();
    }

    private final String name, packageName;
    private String config;
    private boolean enabledByDefault;

    public String getModName(){
        return name;
    }

    public boolean enabledByDefault(){
        return enabledByDefault;
    }

    public CMIPCompatHandler setEnabledByDefault(boolean b) {
        this.enabledByDefault = b;
        return this;
    }

    public String getConfig() {
        return config;
    }

    public CMIPCompatHandler setConfigName(String config) {
        this.config = config.toLowerCase();
        return this;
    }

    @Override
    public boolean addCategoryComment() {
        return false;
    }

    public void registerModules(){
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(getClass().getClassLoader()).getTopLevelClasses(packageName)){
                Class clazz = classInfo.load();
                if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()) && AbstractCMIPCompatHandler.class.isAssignableFrom(clazz)){
                    addHandler((ICompatHandler) clazz.newInstance());
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getLoadingMessage(ICompatHandler handler) {
        return "Loading compat handler: " + getNameFor(handler);
    }

    @Override
    protected String getHandlerNotLoaded(ICompatHandler handler) {
        return "Skipping compat handler: " + getNameFor(handler);
    }

    @Override
    protected CompatEnabled getIsCompatEnabled(String mod, CompatEnabled defaultValue) {
        boolean b = defaultValue != CompatEnabled.FALSE;
        String start = Config.mainHandlerCategory+"."+getConfig().toLowerCase()+".";
        String category = checkModName(start, mod);
        b = configuration.getBoolean("enabled", start+category.toLowerCase(), b, "If set to false, the "+getModName()+"-"+mod+" handler will never load. (Handy when you are using versions that are not compatible)");
        return b ? CompatEnabled.AUTO : CompatEnabled.FALSE;
    }

    /* Check to make sure that this uses an already generated category*/
    private String checkModName(String start, String modName){
        /* All compat handlers should be based on this, if not, please crash....*/
        AbstractCMIPCompatHandler compatHandler = (AbstractCMIPCompatHandler) forMod(modName);
        List<String> validNames = CMIP.configWrapper.getRegisteredCategories();
        modName = modName.toLowerCase();
        start = start.toLowerCase();
        if (validNames.contains(start + modName))
            return modName;
        String attempt2 = compatHandler.getCategoryOverride().toLowerCase();
        if (validNames.contains(start + attempt2)){
            return attempt2;
        }
        for (String s : validNames){
            if (s.contains(start)){
                s = s.replace(start, "");
                if (s.contains(modName.toLowerCase()) || modName.toLowerCase().contains(s))
                    return s;
            }
        }
        /* Fix the category, NOW */
        throw new RuntimeException("Illegal category name: "+modName);
    }

    private String getNameFor(ICompatHandler handler){
        return handler.getName() + "-" + name;
    }

    @Override
    public void loadList() {
    }

}
