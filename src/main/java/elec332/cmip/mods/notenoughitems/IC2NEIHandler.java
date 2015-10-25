package elec332.cmip.mods.notenoughitems;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import elec332.cmip.client.ClientMessageHandler;
import elec332.cmip.mods.MainCompatHandler;
import elec332.cmip.util.Config;
import ic2.api.recipe.ICannerEnrichRecipeManager;
import ic2.api.recipe.Recipes;
import ic2.core.Ic2Items;
import ic2.core.block.machine.gui.GuiCanner;
import ic2.core.block.machine.gui.GuiReplicator;
import ic2.core.block.machine.gui.GuiScanner;
import ic2.core.item.ItemFluidCell;
import ic2.core.uu.UuGraph;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

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
                uuMap.put(new AbstractCMIPNEITemplateRecipeHandler.ItemWithMeta(entry.getKey()), entry.getValue() * 1.0E-5D);
            }
            registerRecipeHandler(new IC2ReplicatorHandler(), 0.9f);

            enrichRecipes = Maps.newHashMap(Recipes.cannerEnrich.getRecipes());

            registerUsageAndRecipeHandler(new FluidSolidCanningMachineHandler(), 0.9f);
        }
    }

    private static final Map<AbstractCMIPNEITemplateRecipeHandler.ItemWithMeta, Double> uuMap;
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
            return StatCollector.translateToLocal("ic2.Replicator.gui.name");
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
                this.stack = new PositionedStack(output.toStack(), 10, 12);
                double uu = uuMap.get(output);
                if (uu == Double.POSITIVE_INFINITY || uu <= 0) {
                    throw new IllegalArgumentException();
                }
                uu *= 1000;
                this.uu = uu;
                this.energy = (uu/0.1)*512;
                list = Lists.newArrayList();
            }

            private PositionedStack stack;
            private double uu, energy;
            private List<PositionedStack> list;

            @Override
            public PositionedStack getResult() {
                return stack;
            }

            @Override
            public List<PositionedStack> getIngredients() {
                return this.getCycledIngredients(cycleticks / 20, list);
            }
        }

    }

    public static class FluidSolidCanningMachineHandler extends AbstractCMIPNEITemplateRecipeHandler {

        private boolean b = true;
        private static final int cycleTime = 50;

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
        public int recipiesPerPage() {
            return 1;
        }

        @Override
        public void drawBackground(int recipe) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GuiDraw.changeTexture(this.getGuiTexture());
            GuiDraw.drawTexturedModalRect(0, 10, 5, 11, 166, 90);
            GuiDraw.drawTexturedModalRect(58, 80, 196, 65, 50, 14);
            GuiDraw.drawTexturedModalRect(69, 21, 233, 0, (int) (((this.cycleticks % cycleTime) / (float)cycleTime) * 23), 14);
        }

        @Override
        public void onUpdate() {
            super.onUpdate();
            if ((float)(this.cycleticks % cycleTime) / (float)cycleTime == 0){
                b = !b;
            }
        }

        @Override
        public String getGuiTexture() {
            return "ic2:textures/gui/GUICanner.png";
        }

        @Override
        public String getRecipeName() {
            return StatCollector.translateToLocal("ic2.Canner.gui.name");
        }

        @Override
        public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
            return handleTankTooltips(gui, currenttip, recipe);
        }

        public class CachedSFCannerRecipe extends AbstractCachedRecipe {

            private CachedSFCannerRecipe(ICannerEnrichRecipeManager.Input input, FluidStack output){
                PositionedStack inputStack = new PositionedStack(input.additive.getInputs(), 75, 43);
                this.listF = Lists.newArrayList(inputStack, new PositionedStack(getUniversalFluidCell(), 36, 16));
                this.listI = Lists.newArrayList(inputStack);
                addTank(new Rectangle(38, 47, 12, 45), input.fluid, 8000);
                addTank(new GuiFluidTank(new Rectangle(116, 47, 12, 45), output, 8000){
                    @Override
                    public void draw() {
                        if (!b) {
                            super.draw();
                        }
                    }

                    @Override
                    public List<String> getTooltip() {
                        if (!b) {
                            return super.getTooltip();
                        } else {
                            return Lists.newArrayList(ClientMessageHandler.getEmptyMessage());
                        }
                    }
                });
                ItemStack stack = getUniversalFluidCell();
                ((ItemFluidCell)stack.getItem()).fill(stack, output, true);
                result = new PositionedStack(stack, 114, 16);
            }

            private List<PositionedStack> listF;
            private List<PositionedStack> listI;
            private PositionedStack result;


            @Override
            public PositionedStack getResult() {
                return b?result:null;
            }

            @Override
            public List<PositionedStack> getIngredients() {
                return b?listF:listI;
            }

        }

        private static ItemStack getUniversalFluidCell(){
            return Ic2Items.FluidCell.copy();
        }

    }

    static {
        uuMap = Maps.newHashMap();
        enrichRecipes = Maps.newHashMap();
    }

}
