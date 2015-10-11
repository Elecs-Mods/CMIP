package elec332.cmip.client.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.recipe.IRecipeHandler;
import elec332.cmip.CMIP;
import elec332.cmip.mods.notenoughitems.RFToolsNEIHandler;
import elec332.cmip.network.PacketRFToolsCrafterNEIRecipe;
import elec332.cmip.util.ContainerNull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Elec332 on 6-10-2015.
 */
public class RFToolsCrafterOverlayHandler implements IOverlayHandler {

    @Override
    public void overlayRecipe(GuiContainer guiContainer, IRecipeHandler iRecipeHandler, int i, boolean b) {
        if (Minecraft.getMinecraft().currentScreen == guiContainer && guiContainer.getClass().equals(RFToolsNEIHandler.guiCrafterClass)) {
            List<PositionedStack> items = iRecipeHandler.getIngredientStacks(i);
            ItemStack[] recipe = new ItemStack[9];
            for (PositionedStack stack : items){
                stack.setPermutationToRender(0);
                switch (stack.relx){
                    case 25:
                        recipe[slotY(stack.rely, 0)] = stack.item;
                        break;
                    case 43:
                        recipe[slotY(stack.rely, 3)] = stack.item;
                        break;
                    case 61:
                        recipe[slotY(stack.rely, 6)] = stack.item;
                }
            }

            NBTTagList send = new NBTTagList();
            for (int j = 0; j < recipe.length; j++) {
                ItemStack stack = recipe[j];
                if (stack != null){
                    NBTTagCompound tag = new NBTTagCompound();
                    stack.writeToNBT(tag).setInteger("slot", j);
                    send.appendTag(tag);
                }
            }
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("items", send);

            CMIP.networkHandler.getNetworkWrapper().sendToServer(new PacketRFToolsCrafterNEIRecipe(tag));
        }
    }

    private int slotY(int y, int i){
        switch (y){
            case 6:
                return i;
            case 24:
                return i+1;
            case 42:
                return i+2;
            default:
                throw new RuntimeException();
        }
    }
            /*List<DistributedIngred> distributedIngredients = this.getPermutationIngredients(items);
            for (DistributedIngred ingred : distributedIngredients){
                ingred.invAmount = 64;
            }
            List<IngredientDistribution> ingredients = assignIngredients(items, distributedIngredients);
            ItemStack[] toSend = new ItemStack[10];
            PositionedStack out = iRecipeHandler.getResultStack(i);
            out.setPermutationToRender(0);
            if (ingredients.size() > 9)
                return;
            for (int j = 0; j < 9; j++) {
                try {
                    IngredientDistribution stack = ingredients.get(i);
                    if (stack != null){
                        System.out.println(MineTweakerHelper.getItemRegistryName(stack.permutation));
                        toSend[j] = stack.permutation;
                    }
                } catch (Throwable t){
                    //
                }
            }
            toSend[9] = out.item;
            NBTTagList send = new NBTTagList();
            for (int j = 0; j < 10; j++) {
                ItemStack stack = toSend[j];
                if (stack != null){
                    NBTTagCompound tag = new NBTTagCompound();
                    stack.writeToNBT(tag).setInteger("slot", j);
                    send.appendTag(tag);
                }
            }
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("items", send);
            CMIP.networkHandler.getNetworkWrapper().sendToServer(new PacketRFToolsCrafterNEIRecipe(tag));
        }
    }

    private List<DefaultOverlayHandler.DistributedIngred> getPermutationIngredients(List<PositionedStack> ingredients) {
        ArrayList ingredStacks = new ArrayList();
        Iterator i$ = ingredients.iterator();

        while(i$.hasNext()) {
            PositionedStack posstack = (PositionedStack)i$.next();
            ItemStack[] arr$ = posstack.items;
            int len$ = arr$.length;

            for(int i$1 = 0; i$1 < len$; ++i$1) {
                ItemStack pstack = arr$[i$1];
                DefaultOverlayHandler.DistributedIngred istack = this.findIngred(ingredStacks, pstack);
                if(istack == null) {
                    ingredStacks.add(istack = new DefaultOverlayHandler.DistributedIngred(pstack));
                }

                istack.recipeAmount += pstack.stackSize;
            }
        }

        return ingredStacks;
    }

    private List<DefaultOverlayHandler.IngredientDistribution> assignIngredients(List<PositionedStack> ingredients, List<DefaultOverlayHandler.DistributedIngred> ingredStacks) {
        ArrayList assignedIngredients = new ArrayList();
        Iterator i$ = ingredients.iterator();

        while(i$.hasNext()) {
            PositionedStack posstack = (PositionedStack)i$.next();
            DefaultOverlayHandler.DistributedIngred biggestIngred = null;
            ItemStack permutation = null;
            int biggestSize = 0;
            ItemStack[] arr$ = posstack.items;
            int len$ = arr$.length;
            int i$1 = 0;

            while(i$1 < len$) {
                ItemStack pstack = arr$[i$1];
                int j = 0;

                while(true) {
                    if(j < ingredStacks.size()) {
                        label52: {
                            DefaultOverlayHandler.DistributedIngred istack = (DefaultOverlayHandler.DistributedIngred)ingredStacks.get(j);
                            if(InventoryUtils.canStack(pstack, istack.stack) && istack.invAmount - istack.distributed >= pstack.stackSize) {
                                int relsize = (istack.invAmount - istack.invAmount / istack.recipeAmount * istack.distributed) / pstack.stackSize;
                                if(relsize > biggestSize) {
                                    biggestSize = relsize;
                                    biggestIngred = istack;
                                    permutation = pstack;
                                    break label52;
                                }
                            }

                            ++j;
                            continue;
                        }
                    }

                    ++i$1;
                    break;
                }
            }

            if(biggestIngred == null) {
                return Lists.newArrayList();
            }

            biggestIngred.distributed += permutation.stackSize;
            assignedIngredients.add(new DefaultOverlayHandler.IngredientDistribution(biggestIngred, permutation));
        }

        return assignedIngredients;
    }*/
}
