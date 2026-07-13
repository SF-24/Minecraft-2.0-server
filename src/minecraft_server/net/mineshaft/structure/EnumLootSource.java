package net.mineshaft.structure;

import net.minecraft.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnumLootSource {

    NETHER_FORTRESS(true, Arrays.asList(Enchantment.flame, Enchantment.fireAspect, Enchantment.fireProtection, Enchantment.smite)),
    NETHER_BASEMENT(true, Arrays.asList()), // TODO:
    JUNGLE_PYRAMID(false, Arrays.asList(Enchantment.baneOfArthropods,Enchantment.projectileProtection,Enchantment.punch)),
    DESERT_PYRAMID(false, Arrays.asList(Enchantment.smite, Enchantment.blastProtection,Enchantment.knockback)),
    STRONGHOLD(true, Arrays.asList(Enchantment.featherFalling, Enchantment.power, Enchantment.infinity)), // TODO:
    DUNGEON(false, Arrays.asList()),;

    private final List<Enchantment> nativeEnchantments;
    private final boolean allowSuperEnchants;

    EnumLootSource(boolean allowsSuperEnchants, List<Enchantment> nativeEnchantments) {
        this.nativeEnchantments = nativeEnchantments;
        this.allowSuperEnchants = allowsSuperEnchants;
    }

    public List<Enchantment> getNativeEnchantments() {
        return this.nativeEnchantments;
    }

    public boolean hasSuperEnchants() {
        return allowSuperEnchants;
    }
}
