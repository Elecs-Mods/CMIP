package elec332.cmip.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 25-10-2015.
 */
public final class ItemWithMeta {

    public ItemWithMeta(ItemStack stack){
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
