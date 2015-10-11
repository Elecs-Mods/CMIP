package elec332.cmip.mods.notenoughitems;

import codechicken.microblock.ItemMicroPart;
import codechicken.microblock.handler.MicroblockProxy;
import codechicken.nei.api.API;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.registry.GameData;
import elec332.cmip.mods.MainCompatHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 10-10-2015.
 */
public class FMPNEIHandler extends AbstractNEICompatHandler {

    @Override
    public String getName() {
        return MainCompatHandler.FORGEMULTIPART;
    }

    @Override
    public void init() {
        Item item = MicroblockProxy.itemMicro();//GameRegistry.findItem("ForgeMicroblock", "microblock");
        int[] allTypes = new int[]{1, 2, 4, 257, 258, 260, 513, 514, 516, 769, 770, 772};
        List<ItemStack> toAdd = Lists.newArrayList();
        for (int i : allTypes){
            toAdd.add(ItemMicroPart.create(i, GameData.getBlockRegistry().getNameForObject(Blocks.stone)));
        }
        if (item != null) {
            setItemListEntries(item, toAdd);
        }
        saws = toStackList(Lists.newArrayList(MicroblockProxy.sawStone(), MicroblockProxy.sawIron(), MicroblockProxy.sawDiamond()));
        API.registerRecipeHandler(new FMPCraftingRecipeHandler());
    }

    private static List<ItemStack> saws;

    public static class FMPCraftingRecipeHandler extends ShapelessRecipeHandler{

        @Override
        public void loadCraftingRecipes(ItemStack result) {
            if (result != null && result.getItem() == MicroblockProxy.itemMicro() && result.stackTagCompound != null){
                Block block = GameData.getBlockRegistry().getObject(result.stackTagCompound.getString("mat"));
                if (block == null)
                    block = Blocks.fire;
                ItemStack recipeBlock = new ItemStack(block);
                ItemStack outputCopy = result.copy();
                int i = result.getItemDamage();
                if (i <= 4){
                    switch (i){
                        case 1:
                            outputCopy.setItemDamage(2);
                            addRecipe(result, outputCopy);
                            break;
                        case 2:
                            outputCopy.setItemDamage(1);
                            addRecipeWithoutSaw(result, outputCopy.copy(), outputCopy.copy());
                            outputCopy.setItemDamage(4);
                            addRecipe(result, outputCopy);
                            break;
                        case 4:
                            outputCopy.setItemDamage(2);
                            addRecipeWithoutSaw(result, outputCopy.copy(), outputCopy.copy());
                            addRecipe(result, recipeBlock);
                            break;
                    }
                } else if (i <= 260){
                    final int q = 256;
                    i -= q;
                    switch (i){
                        case 1:
                            outputCopy.setItemDamage(2 + q);
                            addRecipe(result, outputCopy);
                            break;
                        case 2:
                            outputCopy.setItemDamage(1 + q);
                            addRecipeWithoutSaw(result, outputCopy.copy(), outputCopy.copy());
                            outputCopy.setItemDamage(4 + q);
                            addRecipe(result, outputCopy);
                            break;
                        case 4:
                            outputCopy.setItemDamage(2 + q);
                            addRecipeWithoutSaw(result, outputCopy.copy(), outputCopy.copy());
                            addRecipe(result, recipeBlock);
                            break;
                    }
                } else if (i <= 516){
                    final int q = 512;
                    i -= q;
                    switch (i){
                        case 1:
                            outputCopy.setItemDamage(2 + q);
                            addRecipe(result, outputCopy);
                            break;
                        case 2:
                            outputCopy.setItemDamage(1 + q);
                            addRecipeWithoutSaw(result, outputCopy.copy(), outputCopy.copy());
                            outputCopy.setItemDamage(4 + q);
                            addRecipe(result, outputCopy);
                            break;
                        case 4:
                            outputCopy.setItemDamage(2 + q);
                            addRecipeWithoutSaw(result, outputCopy.copy(), outputCopy.copy());
                            addRecipe(result, recipeBlock);
                            break;
                    }
                } else if (i <= 772){
                    final int q = 768;
                    i -= q;
                    switch (i){
                        case 1:
                            outputCopy.setItemDamage(2 + q);
                            addRecipe(result, outputCopy);
                            break;
                        case 2:
                            outputCopy.setItemDamage(1 + q);
                            addRecipeWithoutSaw(result, outputCopy.copy(), outputCopy.copy());
                            outputCopy.setItemDamage(4 + q);
                            addRecipe(result, outputCopy);
                            break;
                        case 4:
                            outputCopy.setItemDamage(2 + q);
                            addRecipeWithoutSaw(result, outputCopy.copy(), outputCopy.copy());
                            addRecipe(result, recipeBlock);
                            break;
                    }
                }
            }
        }

        private void addShapedCachedRecipe(int width, int height, ItemStack output, Object... ingredients){
            arecipes.add(new CachedShapedRecipe(width, height, ingredients, output));
        }

        private void addShapelessRecipe(ItemStack output, Object... ingredients){
            arecipes.add(new CachedShapelessRecipe(ingredients, output));
        }

        private void addRecipe(ItemStack out, ItemStack... stacks){
            if (stacks.length > 8)
                throw new IllegalArgumentException();
            List ingredients = Lists.newArrayList(saws, stacks);
            arecipes.add(new CachedShapelessRecipe(ingredients, out));
        }

        private void addRecipeWithoutSaw(ItemStack out, ItemStack... stacks){
            if (stacks.length > 9)
                throw new IllegalArgumentException();
            arecipes.add(new CachedShapelessRecipe(stacks, out));
        }

    }



}
