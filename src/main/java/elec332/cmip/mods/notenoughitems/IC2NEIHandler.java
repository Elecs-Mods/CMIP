package elec332.cmip.mods.notenoughitems;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.google.common.collect.Maps;
import elec332.cmip.client.ClientMessageHandler;
import elec332.cmip.mods.MainCompatHandler;
import elec332.cmip.util.Config;
import ic2.api.recipe.ICannerEnrichRecipeManager;
import ic2.core.Ic2Items;
import ic2.core.block.machine.gui.GuiCanner;
import ic2.core.block.machine.gui.GuiReplicator;
import ic2.core.block.machine.gui.GuiScanner;
import ic2.core.item.ItemFluidCell;
import ic2.core.uu.UuGraph;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Elec332 on 10-10-2015.
 */
public class IC2NEIHandler extends AbstractNEICompatHandler {

    @Override
    public String getName() {
        return MainCompatHandler.IC2;
    }

    @Override
    public void init() {
        if (Config.NEI.IC2.hideUnplacableBlocks) {
            removeFromSight(Ic2Items.reinforcedDoorBlock);
            removeFromSight(Ic2Items.copperCableBlock);
            removeFromSight(Ic2Items.miningPipeTip);
        }
        if (Config.NEI.IC2.addUUMatterRecipes){
            for (Map.Entry<ItemStack, Double> entry : new Iterable<Map.Entry<ItemStack, Double>>() {
                @Override
                public Iterator<Map.Entry<ItemStack, Double>> iterator() {
                    return UuGraph.iterator();
                }
            }){
                uuMap.put(new ItemWithMeta(entry.getKey()), entry.getValue() * 1.0E-5D);
            }
            registerRecipeHandler(new IC2ReplicatorHandler());

            //enrichRecipes = Maps.newHashMap(Recipes.cannerEnrich.getRecipes());

            //registerUsageAndRecipeHandler(new FluidSolidCanningMachineHandler());
        }
    }

    private static final Map<ItemWithMeta, Double> uuMap;
    private static Map<ICannerEnrichRecipeManager.Input, FluidStack> enrichRecipes;

    public static class IC2ReplicatorHandler extends AbstractCMIPNEITemplateRecipeHandler {

        @Override
        public void loadTransferRects() {
            addTransferRect(GuiReplicator.class, new Rectangle(40, 25, 80, 15), "uuRecipes", false);
            addTransferRect(GuiScanner.class, new Rectangle(90, 12, 75, 50), "uuRecipes", false);
        }

        @Override
        public String getGuiTexture() {
            return "nei:textures/gui/recipebg.png";
        }

        @Override
        public String getRecipeName() {
            return "ic2.Replicator.gui.name";
        }

        @Override
        public int numRecipes() {
            return 1;
        }

        @Override
        public void drawExtras(int i) {
            CachedUURecipe recipe = (CachedUURecipe) arecipes.get(i);
            GuiDraw.drawString("Energy: " + ClientMessageHandler.format(recipe.energy), 1, 33, Color.GRAY.getRGB());
            GuiDraw.drawString("UU: "+ClientMessageHandler.format(recipe.uu)+"mB", 1, 45, Color.GRAY.getRGB());
        }

        @Override
        public void loadCraftingRecipes(String outputId, Object... results) {
            if (outputId.equals("uuRecipes")){
                for (ItemWithMeta iwm : uuMap.keySet()){
                    tryAddRecipe(iwm);
                }
            } else {
                super.loadCraftingRecipes(outputId, results);
            }
        }

        @Override
        public void loadCraftingRecipes(final ItemStack result) {
            ItemWithMeta check = new ItemWithMeta(result);
            tryAddRecipe(check);
        }

        @Override
        public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
            return currenttip;
        }

        private void tryAddRecipe(final ItemWithMeta check){
            if (uuMap.keySet().contains(check)) {
                double uu = uuMap.get(check);
                if (uu != Double.POSITIVE_INFINITY && uu > 0) {
                    arecipes.add(new CachedUURecipe(check));
                }
            }
        }

        public class CachedUURecipe extends TemplateRecipeHandler.CachedRecipe{

            public CachedUURecipe(ItemWithMeta output){
                if (!uuMap.keySet().contains(output))
                    throw new IllegalArgumentException();
                this.stack = output;
                double uu = uuMap.get(output);
                if (uu == Double.POSITIVE_INFINITY || uu <= 0) {
                    throw new IllegalArgumentException();
                }
                uu *= 1000;
                this.uu = uu;
                this.energy = (uu/0.1)*512;
            }

            private ItemWithMeta stack;
            private double uu, energy;

            public double getNeededUU() {
                return uu;
            }

            public double getNeededEnergy() {
                return energy;
            }

            @Override
            public PositionedStack getResult() {
                return new PositionedStack(stack.toStack(), 10, 12);
            }
        }

    }

    public static class FluidSolidCanningMachineHandler extends AbstractCMIPNEITemplateRecipeHandler{

        @Override
        public void loadCraftingRecipes(String outputId, Object... results) {
            if (outputId.equals("sfCanner")){
                for (Map.Entry<ICannerEnrichRecipeManager.Input, FluidStack> entry : enrichRecipes.entrySet()){
                    arecipes.add(new CachedSFCannerRecipe(entry.getKey(), entry.getValue()));
                }
            } else {
                super.loadCraftingRecipes(outputId, results);
            }
        }

        @Override
        public void loadUsageRecipes(ItemStack ingredient) {
            for (Map.Entry<ICannerEnrichRecipeManager.Input, FluidStack> entry : enrichRecipes.entrySet()){
                if (entry.getKey().additive.matches(ingredient)){
                    arecipes.add(new CachedSFCannerRecipe(entry.getKey(), entry.getValue()));
                }
            }
        }

        @Override
        public void loadTransferRects() {
            addTransferRect(GuiCanner.class, new Rectangle(65, 10, 22, 15), "sfCanner", false);
        }

        @Override
        public String getGuiTexture() {
            return "ic2:textures/gui/GUICanner.png";
        }

        @Override
        public String getRecipeName() {
            return "ic2.FluidBottler.gui.name";
        }

        @Override
        public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
            return currenttip;
        }

        public class CachedSFCannerRecipe extends CachedRecipe{

            private CachedSFCannerRecipe(ICannerEnrichRecipeManager.Input input, FluidStack output){
                this.input = input;
                this.output = output;
            }

            private final ICannerEnrichRecipeManager.Input input;
            private final FluidStack output;

            @Override
            public PositionedStack getResult() {
                ItemStack stack = Ic2Items.cell;
                ((ItemFluidCell)stack.getItem()).fill(stack, output, true);
                return new PositionedStack(stack, 17, 152);
            }

            @Override
            public PositionedStack getIngredient() {
                return new PositionedStack(input.additive.getInputs() , 80, 44);
            }

            @Override
            public PositionedStack getOtherStack() {
                return new PositionedStack(new ItemStack(Items.cooked_chicken), 10, 10);
            }
        }
    }

    private static final class ItemWithMeta {

        private ItemWithMeta(ItemStack stack){
            if (stack == null || stack.getItem() == null)
                throw new IllegalArgumentException();
            this.item = stack.getItem();
            this.meta = stack.getItemDamage();
        }

        private final Item item;
        private final int meta;

        public ItemStack toStack(){
            return new ItemStack(item, 1, meta);
        }

        @Override
        public int hashCode() {
            return super.hashCode()*meta*5+item.hashCode()*39;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ItemWithMeta && ((ItemWithMeta) obj).item == item && ((ItemWithMeta) obj).meta == meta;
        }

        public boolean stackEqual(ItemStack stack){
            return stack != null && stack.getItem() != null && stack.getItem() == item && stack.getItemDamage() == meta;
        }
    }

    static {
        uuMap = Maps.newHashMap();
        enrichRecipes = Maps.newHashMap();
    }

}
