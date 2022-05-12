package fr.norrion.daily_quests.utils.enumeration;

import org.bukkit.entity.*;

public enum ListProjectile {
    ARROW,
    FIREBALL,
    ENDER_PEARL,
    FIREWORK,
    FISHHOOK,
    LLAMA_SPLIT,
    SHULKER_BULLET,
    SNOWBALL,
    THROWN_POTION,
    TRIDENT,
    WITHER_SKULL,
    EGG;

    public boolean isIntance(Projectile projectile) {

        return switch (this) {
            case ARROW -> projectile instanceof Arrow;
            case EGG    -> projectile instanceof Egg;
            case FIREBALL    -> projectile instanceof Fireball;
            case ENDER_PEARL    -> projectile instanceof EnderPearl;
            case FIREWORK    -> projectile instanceof Firework;
            case FISHHOOK    -> projectile instanceof FishHook;
            case LLAMA_SPLIT    -> projectile instanceof LlamaSpit;
            case SHULKER_BULLET    -> projectile instanceof ShulkerBullet;
            case SNOWBALL    -> projectile instanceof Snowball;
            case THROWN_POTION    -> projectile instanceof ThrownPotion;
            case TRIDENT    -> projectile instanceof Trident;
            case WITHER_SKULL    -> projectile instanceof WitherSkull;
        };
    }
}
