package fr.neyuux.refont.lg.config;

import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ConfigItem extends CustomItemStack {

    public ConfigItem(Material material, String name) {
        super(material, name);
    }

    public ConfigItem(Material material, String name, String... lore) {
        super(material, name, lore);
    }

    public ConfigItem(Material material, int amount, String name) {
        super(material, amount, name);
    }

    public ConfigItem(Material material, int amount, String name, String... lore) {
        super(material, amount, name, lore);
    }

    public ConfigItem(Material material, int amount, short durabilite, String name) {
        super(material, amount, durabilite, name);
    }

    public ConfigItem(Material material, short durabilite, String name) {
        super(material, durabilite, name);
    }

    public ConfigItem(Material material, short durabilite) {
        super(material, durabilite);
    }

    public ConfigItem(Material material, short durabilite, String name, String... lore) {
        super(material, durabilite, name, lore);
    }

    public ConfigItem(Material material) {
        super(material);
    }

    public ConfigItem(ItemStack item) {
        super(item);
    }


    public void onClick(InventoryClickEvent ev) {
        ev.setCancelled(true);
    }
}
