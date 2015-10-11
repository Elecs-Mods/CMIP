package elec332.cmip.network;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import elec332.cmip.mods.notenoughitems.RFToolsNEIHandler;
import elec332.cmip.util.ContainerNull;
import elec332.core.network.AbstractPacket;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

/**
 * Created by Elec332 on 7-10-2015.
 */
public class PacketRFToolsCrafterNEIRecipe extends AbstractPacket {

    public PacketRFToolsCrafterNEIRecipe(){
    }

    public PacketRFToolsCrafterNEIRecipe(NBTTagCompound tag){
        super(tag);
    }

    @Override
    public IMessage onMessage(AbstractPacket abstractPacket, MessageContext messageContext) {
        if (messageContext.getServerHandler().playerEntity.openContainer != null){
            NBTTagList list = abstractPacket.networkPackageObject.getTagList("items", 10);
            InventoryCrafting craftingInventory = new InventoryCrafting(new ContainerNull(), 3, 3);
            List<Integer> done = Lists.newArrayList();
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                int j = tag.getInteger("slot");
                ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
                craftingInventory.setInventorySlotContents(j, stack);
                done.add(j);
            }
            /*for (int i = 0; i < 9; i++) {
                if (!done.contains(i)){
                    craftingInventory.setInventorySlotContents(i, null);
                    //messageContext.getServerHandler().playerEntity.openContainer.getSlot(i).putStack(null);
                    //messageContext.getServerHandler().playerEntity.openContainer.getSlot(i).onSlotChanged();
                }
            }*/
            ItemStack out = CraftingManager.getInstance().findMatchingRecipe(craftingInventory, messageContext.getServerHandler().playerEntity.getServerForPlayer());
            if (out == null) {
                System.out.println("Invalid recipe!");
                return null;
            }
            TileEntity tile = RFToolsNEIHandler.getTileFromCrafter(messageContext.getServerHandler().playerEntity.openContainer);
            NBTTagCompound currentData = new NBTTagCompound();
            tile.writeToNBT(currentData);
            boolean success = false;
            NBTTagList recipeList = currentData.getTagList("Recipes", 10);
            for (int i = 0; i < recipeList.tagCount(); i++) {
                NBTTagCompound tag = recipeList.getCompoundTagAt(i);
                if (tag.hasKey("Result")){
                    ItemStack stack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Result"));
                    if (stack != null && stack.getItem() != null)
                        continue;
                }
                writeRecipeToNBT(craftingInventory, out, tag);
                success = true;
                break;
            }
            if (success) {
                tile.readFromNBT(currentData);
                tile.markDirty();
                tile.getWorldObj().markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
            }
            messageContext.getServerHandler().playerEntity.openContainer.detectAndSendChanges();
            /*TileEntity tile = RFToolsNEIHandler.getTileFromCrafter(messageContext.getServerHandler().playerEntity.openContainer);
            tile.markDirty();
            tile.getWorldObj().markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);*/
        }
        return null;
    }

    private void writeRecipeToNBT(InventoryCrafting craftingInventory, ItemStack out, NBTTagCompound tag){
        if (out == null || out.getItem() == null){
            throw new IllegalArgumentException();
        }
        NBTTagList list = new NBTTagList();
        for(int i = 0; i < craftingInventory.getSizeInventory(); ++i) {
            NBTTagCompound tag1 = new NBTTagCompound();
            ItemStack stack = craftingInventory.getStackInSlot(i);
            if (stack != null){
                stack.writeToNBT(tag1);
            }
            list.appendTag(tag1);
        }
        NBTTagCompound tag2 = new NBTTagCompound();
        out.writeToNBT(tag2);
        tag.setTag("Result", tag2);
        tag.setTag("Items", list);
    }

}
