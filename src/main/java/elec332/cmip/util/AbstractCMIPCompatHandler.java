package elec332.cmip.util;

import com.google.common.collect.Lists;
import elec332.cmip.CMIP;
import elec332.core.util.AbstractCompatHandler;

import java.util.List;

/**
 * Created by Elec332 on 6-10-2015.
 */
public abstract class AbstractCMIPCompatHandler extends AbstractCompatHandler.ICompatHandler{

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
}
