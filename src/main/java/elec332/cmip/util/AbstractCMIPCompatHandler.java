package elec332.cmip.util;

import com.google.common.collect.Lists;
import elec332.cmip.CMIP;
import elec332.core.util.AbstractCompatHandler;

import java.util.List;

/**
 * Created by Elec332 on 6-10-2015.
 */
public abstract class AbstractCMIPCompatHandler extends AbstractCompatHandler.ICompatHandler{

    public String getCategoryOverride(){
        return getName();
    }

    public List<Class> findClasses(String... s){
        List<Class> ret = Lists.newArrayList();
        for (String s1 : s) {
            try {
                ret.add(Class.forName(s1));
            } catch (Exception e) {
                CMIP.logger.info("Error finding class: " + s1);
            }
        }
        return ret;
    }

    protected static final String energy, maxEnergy, tier, progress, tpLoc, heat, maxHeat, laser, avgEnergy, name, access,
                                    range, active, specialData1, energyOut, specialData2, specialData3, specialData4, specialData5,
                                    pressure, maxPressure, temperature, maxTemperature, fluid;

    static {
        energy = "energy";
        maxEnergy = "maxEnergy";
        tier = "tier";
        progress = "progress";
        tpLoc = "tpLoc";
        heat = "heat";
        maxHeat = "maxHeat";
        laser = "laser";
        avgEnergy = "avgEnergy";
        name = "name";
        access = "access";
        range = "range";
        active = "active";
        specialData1 = "specialData1";
        energyOut = "energyOut";
        specialData2 = "specialData2";
        specialData3 = "specialData3";
        specialData4 = "specialData4";
        specialData5 = "specialData5";
        pressure = "pressure";
        maxPressure = "maxPressure";
        temperature = "temperature";
        maxTemperature = "maxTemperature";
        fluid = "fluid";
    }
}
