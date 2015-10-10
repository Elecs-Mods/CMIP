package elec332.cmip.util;

import com.google.common.reflect.ClassPath;
import elec332.cmip.CMIP;
import elec332.core.util.AbstractCompatHandler;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Modifier;

/**
 * Created by Elec332 on 6-10-2015.
 */
public class CMIPCompatHandler extends AbstractCompatHandler {

    public CMIPCompatHandler(String name){
        this(name, name.toLowerCase());
    }

    public CMIPCompatHandler(String name, String packageName) {
        super(new Configuration(CMIP.mainConfigFolder, name+".cfg"), CMIP.logger);
        this.name = name;
        this.packageName = "elec332.cmip.mods."+packageName;
    }

    private final String name, packageName;

    public String getModName(){
        return name;
    }

    public void registerModules(){
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(getClass().getClassLoader()).getTopLevelClasses(packageName)){
                Class clazz = classInfo.load();
                if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())){
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

    private String getNameFor(ICompatHandler handler){
        return handler.getName() + "-" + name;
    }

    @Override
    public void loadList() {
    }

}
