package elec332.cmip.mods.notenoughitems;

import codechicken.nei.PositionedStack;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import elec332.cmip.mods.MainCompatHandler;
import elec332.cmip.util.ItemWithMeta;
import elec332.core.util.AbstractCompatHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import toops.tsteelworks.api.highoven.IMixAgentRegistry;
import toops.tsteelworks.api.highoven.IMixerRegistry;
import toops.tsteelworks.api.highoven.ISmeltingRegistry;
import toops.tsteelworks.client.gui.HighOvenGui;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Elec332 on 25-10-2015.
 */
public class TinkersSteelworksNEIHandler extends AbstractNEICompatHandler {

    @Override
    public String getName() {
        return MainCompatHandler.TINKERSSTEELWORKS;
    }

    @Override
    public AbstractCompatHandler.CompatEnabled compatEnabled() {
        return AbstractCompatHandler.CompatEnabled.FALSE;
    }

    @Override
    public void init() {
        for (Map.Entry<String, IMixAgentRegistry.IMixAgent> entry : new Iterable<Map.Entry<String, IMixAgentRegistry.IMixAgent>>() {
            @Override
            public Iterator<Map.Entry<String, IMixAgentRegistry.IMixAgent>> iterator() {
                return IMixAgentRegistry.INSTANCE.iterator();
            }
        }){
            mixAgentRegistry.put(entry.getKey(), entry.getValue().getConsumeChance());
        }
        for (Map.Entry<IMixerRegistry.IMixHolder, IMixerRegistry.IMixOutput> entry : new Iterable<Map.Entry<IMixerRegistry.IMixHolder, IMixerRegistry.IMixOutput>>() {
            @Override
            public Iterator<Map.Entry<IMixerRegistry.IMixHolder, IMixerRegistry.IMixOutput>> iterator() {
                return IMixerRegistry.INSTANCE.iterator();
            }
        }){
            mixRegistry.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<ItemStack, ISmeltingRegistry.IMeltData> entry : new Iterable<Map.Entry<ItemStack, ISmeltingRegistry.IMeltData>>() {
            @Override
            public Iterator<Map.Entry<ItemStack, ISmeltingRegistry.IMeltData>> iterator() {
                return ISmeltingRegistry.INSTANCE.iterator();
            }
        }){
            smeltRegistry.put(new ItemWithMeta(entry.getKey()), entry.getValue());
        }

        registerUsageAndRecipeHandler(new TSteelWorksHighOvenHandler(), -1);
    }

    private static Map<String, Integer> mixAgentRegistry;
    private static Map<IMixerRegistry.IMixHolder, IMixerRegistry.IMixOutput> mixRegistry;
    private static Map<ItemWithMeta, ISmeltingRegistry.IMeltData> smeltRegistry;

    public static class TSteelWorksHighOvenHandler extends AbstractCMIPNEITemplateRecipeHandler {

        @Override
        public void loadTransferRects() {
            addTransferRect(HighOvenGui.class, new Rectangle(40, 5, 20, 15), "TSteelHighOven", true);
        }

        @Override
        public void loadCraftingRecipes(String outputId, Object... results) {
            if (outputId.equals("TSteelHighOven")){
                loadAllOvenRecipes();
            } else {
                super.loadCraftingRecipes(outputId, results);
            }
        }

        @Override
        public void loadUsageRecipes(String inputId, Object... ingredients) {
            if (inputId.equals("TSteelHighOven")){
                loadAllOvenRecipes();
            } else {
                super.loadUsageRecipes(inputId, ingredients);
            }
        }

        private void loadAllOvenRecipes(){
            for (Map.Entry<ItemWithMeta, ISmeltingRegistry.IMeltData> entry : smeltRegistry.entrySet()){
                FluidStack input = entry.getValue().getResult();
                arecipes.add(new CachedHighOvenRecipe(entry.getKey(), entry.getValue().isOre(), entry.getValue().getMeltingPoint(), (String)null, null, null, input));
                if (input != null && input.getFluid() != null){
                    Map<Fluid, List<Map.Entry<IMixerRegistry.IMixHolder, IMixerRegistry.IMixOutput>>> map = Maps.newHashMap();
                    for (Map.Entry<IMixerRegistry.IMixHolder, IMixerRegistry.IMixOutput> e : mixRegistry.entrySet()){
                        if (e.getKey().getInputFluid().equals(input.getFluid())){
                            if (e.getValue().getFluidOutput() != null) {
                                Fluid fluid = e.getValue().getFluidOutput().getFluid();
                                if (!map.containsKey(fluid)) {
                                    map.put(fluid, Lists.<Map.Entry<IMixerRegistry.IMixHolder, IMixerRegistry.IMixOutput>>newArrayList());
                                }
                                map.get(fluid).add(e);
                                //arecipes.add(new CachedHighOvenRecipe(entry.getKey(), entry.getValue().isOre(), entry.getValue().getMeltingPoint(), e.getKey().getOxidizer(), e.getKey().getReducer(), e.getKey().getPurifier(), e.getValue().getFluidOutput()));
                            }
                        }
                    }
                    for (Map.Entry<Fluid, List<Map.Entry<IMixerRegistry.IMixHolder, IMixerRegistry.IMixOutput>>> eF : map.entrySet()){
                        List<ItemStack> ox = Lists.newArrayList();
                        List<ItemStack> red = Lists.newArrayList();
                        List<ItemStack> p = Lists.newArrayList();
                        FluidStack output = new FluidStack(eF.getKey(), entry.getValue().getResult().amount);
                        for (Map.Entry<IMixerRegistry.IMixHolder, IMixerRegistry.IMixOutput> e : eF.getValue()){
                            IMixerRegistry.IMixHolder m = e.getKey();
                            ox.addAll(getList(m.getOxidizer()));
                            red.addAll(getList(m.getReducer()));
                            p.addAll(getList(m.getPurifier()));
                        }
                        arecipes.add(new CachedHighOvenRecipe(entry.getKey(), entry.getValue().isOre(), entry.getValue().getMeltingPoint(), ox, red, p, output));
                    }
                }
            }
        }

        @Override
        public String getGuiTexture() {
            return "tsteelworks:textures/gui/highoven.png";
        }

        @Override
        public String getRecipeName() {
            return StatCollector.translateToLocal("crafters.HighOven");
        }

        public class CachedHighOvenRecipe extends AbstractCachedRecipe {

            public CachedHighOvenRecipe(ItemWithMeta in, boolean ore, int temp, String ox, String red, String p, FluidStack out){
                this(in, ore, temp, getList(ox), getList(red), getList(p), out);
            }

            @SuppressWarnings("unchecked")
            public CachedHighOvenRecipe(ItemWithMeta in, boolean ore, int temp, List<ItemStack> ox, List<ItemStack> red, List<ItemStack> p, FluidStack out){
                Object obj;
                if (!ore) {
                    obj = in.toStack();
                } else {
                    List ret = Lists.newArrayList();
                    for (int i : OreDictionary.getOreIDs(in.toStack())){
                        ret.addAll(OreDictionary.getOres(OreDictionary.getOreName(i)));
                    }
                    obj = ret;
                }
                this.mainIn = new PositionedStack(obj, 12, 5);
                this.temp = temp;
                this.otherStacks = Lists.newArrayList();
                if (ox != null && !ox.isEmpty()){
                    otherStacks.add(new PositionedStack(ox, 25, 3));
                }
                if (red != null && !red.isEmpty()){
                    otherStacks.add(new PositionedStack(red, 25, 25));
                }
                if (p != null && !p.isEmpty()){
                    otherStacks.add(new PositionedStack(p, 25, 38));
                }
                addTank(new Rectangle(100, 10, 20, 40), out, 10000);
            }

            private PositionedStack mainIn;
            private int temp;
            private List<PositionedStack> otherStacks;

            @Override /* using for input */
            public PositionedStack getResult() {
                return mainIn;
            }

            @Override
            public List<PositionedStack> getOtherStacks() {
                return otherStacks;
            }
        }

        private static List<ItemStack> getList(String s){
            if (s == null || !OreDictionary.doesOreNameExist(s)){
                return Lists.newArrayList();
            }
            return OreDictionary.getOres(s);
        }

    }

    static {
        mixAgentRegistry = Maps.newHashMap();
        mixRegistry = Maps.newHashMap();
        smeltRegistry = Maps.newHashMap();
    }

}
