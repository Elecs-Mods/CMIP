package elec332.cmip.util;

import com.google.common.collect.Maps;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.*;

/**
 * Created by Elec332 on 19-10-2015.
 */
@SuppressWarnings("all")
public class WrappedTaggedList<W, T> implements ITaggedList<W, T> {

    public static <W, T> WrappedTaggedList<W, T> from(ITaggedList<W, T> list){
        return new WrappedTaggedList(list);
    }

    private WrappedTaggedList(ITaggedList<W, T> list){
        this.list = list;
        this.additionalData = Maps.newHashMap();
    }

    private final ITaggedList<W, T> list;
    private final Map<W, T> additionalData;

    public Map<W, T> getAdditionalData() {
        return additionalData;
    }

    @Override
    public boolean add(W w, T tag) {
        return list.add(w, tag);
    }

    @Override
    public boolean add(W w, Collection<? extends T> taglst) {
        return list.add(w, taglst);
    }

    @Override
    public Set<T> getTags(W w) {
        return list.getTags(w);
    }

    @Override
    public Set<T> getTags(int index) {
        return list.getTags(index);
    }

    @Override
    public void addTag(W w, T tag) {
        list.addTag(w, tag);
    }

    @Override
    public void addTag(int index, T tag) {
        list.addTag(index, tag);
    }

    @Override
    public void removeTag(W w, T tag) {
        list.removeTag(w, tag);
    }

    @Override
    public void removeTag(int index, T tag) {
        list.removeTag(index, tag);
    }

    @Override
    public Set<W> getEntries(T tag) {
        return list.getEntries(tag);
    }

    @Override
    public void removeEntries(T tag) {
        list.removeEntries(tag);
    }

    @Override
    public String getTagsAsString(W w) {
        return list.getTagsAsString(w);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<W> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(W w) {
        return list.add(w);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends W> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends W> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public W get(int index) {
        return list.get(index);
    }

    @Override
    public W set(int index, W element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, W element) {
        list.add(index, element);
    }

    @Override
    public W remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<W> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<W> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<W> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    public static class ListReplacer implements IWailaDataProvider, IWailaEntityProvider, IWailaFMPProvider{

        private static final ListReplacer instance;

        private ListReplacer(){
        }

        public static ListReplacer getInstance() {
            return instance;
        }

        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
            throw new IllegalAccessError();
        }

        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return fromCurrentTip(currenttip);
        }

        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return fromCurrentTip(currenttip);
        }

        @Override
        public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
            return fromCurrentTip(currenttip);
        }

        @Override
        public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
            throw new IllegalAccessError();
        }

        @Override
        public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config){
            throw new IllegalAccessError();
        }

        @Override
        public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config){
            return fromCurrentTip(currenttip);
        }

        @Override
        public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config){
            return fromCurrentTip(currenttip);
        }

        @Override
        public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config){
            return fromCurrentTip(currenttip);
        }

        @Override
        public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world){
            throw new IllegalAccessError();
        }

        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaFMPAccessor accessor, IWailaConfigHandler config){
            return fromCurrentTip(currenttip);
        }

        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaFMPAccessor accessor, IWailaConfigHandler config){
            return fromCurrentTip(currenttip);
        }

        @Override
        public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaFMPAccessor accessor, IWailaConfigHandler config){
            return fromCurrentTip(currenttip);
        }


        private static WrappedTaggedList<String, String> fromCurrentTip(List<String> currenttip){
            return WrappedTaggedList.from((ITaggedList<String, String>) currenttip);
        }

        public static void registerAll(){
            IWailaRegistrar registrar = ModuleRegistrar.instance();
            registerForBlocks(registrar);
            registerForEntities(registrar);
            registerForFMP(registrar);
        }

        public static void registerForBlocks(IWailaRegistrar registrar){
            registrar.registerHeadProvider((IWailaDataProvider) instance, Block.class);
            registrar.registerBodyProvider((IWailaDataProvider) instance, Block.class);
            registrar.registerTailProvider((IWailaDataProvider) instance, Block.class);
        }

        public static void registerForEntities(IWailaRegistrar registrar){
            registrar.registerHeadProvider((IWailaEntityProvider) instance, Entity.class);
            registrar.registerBodyProvider((IWailaEntityProvider) instance, Entity.class);
            registrar.registerTailProvider((IWailaEntityProvider) instance, Entity.class);
        }

        /* This probably won't work */
        public static void registerForFMP(IWailaRegistrar registrar){
            registrar.registerHeadProvider((IWailaFMPProvider) instance, "");
            registrar.registerBodyProvider((IWailaFMPProvider) instance, "");
            registrar.registerTailProvider((IWailaFMPProvider) instance, "");
        }

        static {
            instance = new ListReplacer();
        }
    }

}
