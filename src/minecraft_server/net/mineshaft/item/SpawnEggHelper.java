package net.mineshaft.item;

import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;

public class SpawnEggHelper {

    public static ItemStack getSpawnEgg(String nameString, Class<?> entityClass, String typeParam, int type) {
        ItemStack spawnEgg = new ItemStack(Items.spawn_egg, 1, EntityList.getEntityID(entityClass)); // 54 is typically Spider in 1.8.9
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound entityTag = new NBTTagCompound();
        NBTTagCompound display = new NBTTagCompound();
//        display.setString("Name", "item.spawnEgg." +nameTranslationString);
        entityTag.setString("id", EntityList.getEntityString(entityClass)); // Ensure the base entity is Spider
        entityTag.setByte(typeParam, (byte) type); // Your custom tag (use setInteger or setByte depending on your mod's expectation)

        // Lore: unused.
        NBTTagList loreList = new NBTTagList();
        // EnumChatFormatting.GRAY provides "§7"
        loreList.appendTag(new NBTTagString(EnumChatFormatting.GRAY + "Variant: " + nameString));
        display.setTag("Lore", loreList);

        tag.setTag("display", display);
        tag.setTag("EntityTag", entityTag);
        spawnEgg.setTagCompound(tag);
        return spawnEgg;
    }

}
