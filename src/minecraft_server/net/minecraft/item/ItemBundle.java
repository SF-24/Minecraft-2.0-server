package net.minecraft.item;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemBundle extends Item {
    // TODO: Add leather-style dye

    public ItemBundle() {
        this.setMaxStackSize(1);
    }

    public static int getBundleLimit() {
        return 64;
    }

    public static boolean isBundleable(ItemStack stack) {
        if(stack==null||stack.isEmpty()||stack.stackSize<0||stack.getItem()==Item.getItemFromBlock(Blocks.air)) return false;
        return stack.getItem() != Items.bundle && stack.getItem() != Items.ender_pouch;
    }

    public static boolean isFull(ItemStack bundle) {
        return getItemAmount(bundle) == 64;
    }

    private static boolean isDyed(ItemStack stack) {
        return false;
    }

    public static int getItemAmount(ItemStack stack) {
        if(getItems(stack)==null) return 0;
        int amount = 0;
        for(ItemStack itemStack : getItems(stack)) {
            amount += getItemstackAmount(itemStack) * itemStack.stackSize;
        }
        return amount;
    }

    private static int getEmptyAmount(ItemStack bundle) {
        return 64-getItemAmount(bundle);
    }

    private static int getItemstackAmount(ItemStack stack) {
        int size = 1;
        if (stack.getMaxStackSize() < 64) size = 64 / (stack.getItem().getBundledItemStackLimit()==1?4:stack.getItem().getBundledItemStackLimit());
        return size;
    }


    public static ItemStack[] getItems(ItemStack bundle) {
        NBTTagList list = getItemList(bundle);
        if (list == null) return null;

        int count = list.tagCount();

        if (count <= 0) return null;

        ItemStack[] array = new ItemStack[count];

        for (int i = 0; i < count; i++) {
            ItemStack stack = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
            array[i] = stack;
        }

        return array;
    }

    public static ItemStack getItem(ItemStack bundle, int slot) {
        NBTTagList list = getItemList(bundle);
        if (list == null) return null;
        int count = list.tagCount();
        if (count <= 0) return null;

        slot = checkSlot(bundle, slot);
        return ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(slot));
    }

    private static int checkSlot(ItemStack bundle, int slot) {
        NBTTagList list = getItemList(bundle);
        if (list == null || list.tagCount() == 0) return -1;
        if (slot >= list.tagCount()) slot = list.tagCount() - 1;
        else if (slot < 0) slot = 0;
        return slot;
    }

    private static NBTTagList getItemList(ItemStack bundle) {
        if(bundle==null || bundle.isEmpty()) return null;
        NBTTagCompound nbtTagCompound = bundle.getTagCompound();
        if(nbtTagCompound==null) return null;
        if (!nbtTagCompound.hasKey("StoredItems", 9)) return null;
//        MineshaftLogger.logDebug("Getter: " + nbtTagCompound.getTagList("StoredItems", 10).toString());
        return nbtTagCompound.getTagList("StoredItems", 10);
    }

    // NBT Utilities
    public static void addItem(InventoryPlayer inventoryPlayer, Slot inventorySlot, ItemStack bundle, ItemStack stack, boolean isBundleCursor) {
        NBTTagList list = getItemList(bundle);
        if (list == null) {
            if(bundle.getTagCompound()==null) {
                bundle.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound compound = bundle.getTagCompound();
            list = new NBTTagList();
            compound.setTag("StoredItems", list);
        }
        int empty = getEmptyAmount(bundle);
        int itemSize = getItemstackAmount(stack);
        int itemCount = stack.stackSize;
        int countToAdd = Math.min(itemCount, empty / itemSize);
        if (countToAdd == 0) return;
        int amountToAdd = Math.min(itemSize * itemCount, itemSize * countToAdd);
//        addAmount(bundle, amountToAdd);
        NBTTagCompound compound = stack.writeToNBT(new NBTTagCompound());
        if (compound.hasKey("Count", 1)) {
            compound.setByte("Count", (byte) countToAdd);
        }
        else compound.setInteger("Count", countToAdd);
        list.appendTag(compound);
        stack.stackSize=(stack.stackSize - countToAdd);
        if (stack.stackSize <= 0) {
            if(!isBundleCursor) {
                inventoryPlayer.setItemStack(null);
            } else {
                inventorySlot.putStack(null);
            }
        }
//        if(stack.stackSize<=0) stack.setItem(Item.getItemFromBlock(Blocks.air));
    }

    public static ItemStack removeItem(InventoryPlayer inventoryPlayer, Slot inventorySlot, ItemStack bundle, int slot, int amount, boolean isBundleCursor) {
        NBTTagList list = getItemList(bundle);
        if (list == null) {
            if(bundle.getTagCompound()==null) {
                bundle.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound compound = bundle.getTagCompound();
            list = new NBTTagList();
            compound.setTag("StoredItems", list);
            return null;
        }

        slot = checkSlot(bundle, slot);

        NBTTagCompound stackTag = list.getCompoundTagAt(slot);
        ItemStack stack = ItemStack.loadItemStackFromNBT(stackTag);
        if (stack.isEmpty()) return stack;

        amount = Math.min(stack.stackSize, amount);
        if (amount == stack.stackSize) {
            list.removeTag(slot);
        } else {
            int count = stack.stackSize - amount;

            if (stackTag.hasKey("Count", 1)) {
                stackTag.setByte("Count", (byte) count);
            }
            else stackTag.setInteger("Count", count);

            stack.stackSize=(amount);
            if(amount<=0) {
                if(isBundleCursor) {
                    inventorySlot.putStack(null);
                }
            }
//            if(stack.stackSize<=0) stack.setItem(Item.getItemFromBlock(Blocks.air));
        }
//        addAmount(bundle, -getItemstackAmount(stack) * stack.getCount());

        return stack;
    }

    public static int getSlotCount(ItemStack bundle) {
        NBTTagList list = getItemList(bundle);
        if (list == null) return 0;
        return list.tagCount();
    }
}
