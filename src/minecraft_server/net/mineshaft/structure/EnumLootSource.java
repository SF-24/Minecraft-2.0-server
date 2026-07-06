package net.mineshaft.structure;

import net.minecraft.enchantment.Enchantment;

import java.util.List;

public enum EnumLootSource {

    NETHER_FORTRESS(true, List.of(Enchantment.flame,Enchantment.fireAspect,Enchantment.fireProtection,Enchantment.smite)),
    NETHER_BASEMENT(true, List.of()), // TODO:
    JUNGLE_PYRAMID(false, List.of(Enchantment.baneOfArthropods,Enchantment.projectileProtection,Enchantment.punch)),
    DESERT_PYRAMID(false, List.of(Enchantment.smite, Enchantment.blastProtection,Enchantment.knockback)),
    STRONGHOLD(true, List.of(Enchantment.featherFalling, Enchantment.power, Enchantment.infinity)), // TODO:
    DUNGEON(false, List.of()),;

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
