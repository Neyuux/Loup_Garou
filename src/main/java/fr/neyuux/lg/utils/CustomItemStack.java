package fr.neyuux.lg.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class CustomItemStack extends ItemStack {

    public CustomItemStack(Material m){
        super(m);
    }

    public CustomItemStack(Material m, int amount){
        super(m, amount);
    }

    public CustomItemStack(Material m, int amount, byte data) {
        super(m, amount, data);
    }

    public CustomItemStack(Material m, int amount,String string){
        super(m, amount);
        this.setDisplayName(string);
    }

    public CustomItemStack(ItemStack i){
        super(i);
    }

    public void use(HumanEntity player, Event event) {
        if (event instanceof InventoryClickEvent)
            ((InventoryClickEvent) event).setCancelled(true);
    }

    public CustomItemStack putAmount(int amount){
        this.setAmount(amount);
        return this;
    }

    public CustomItemStack setDamage(int damage){
        this.setDurability((short) damage);
        return this;
    }

    public CustomItemStack setDisplayName(String displayname){
        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(displayname);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack setSkullOwner(String owner){
        String display = getDisplayName();
        SkullMeta im = (SkullMeta)this.getItemMeta();
        im.setDisplayName(display);
        im.setOwner(owner);
        this.setItemMeta(im);
        return this;
    }

    public CustomItemStack setLore(List<String> lore){
        ItemMeta meta = this.getItemMeta();
        meta.setLore(lore);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack setLoreLine(int line, String text) {
        ItemMeta meta = this.getItemMeta();
        List<String> lore = meta.getLore();
        lore.set(line, text);
        meta.setLore(lore);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack addLore(String text) {
        ItemMeta meta = this.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        lore.add(text);
        meta.setLore(lore);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack setTypeV(Material type){
        this.setType(type);
        return this;
    }

    public CustomItemStack addItemFlags(ItemFlag... flags) {
        ItemMeta meta = this.getItemMeta();
        meta.addItemFlags(flags);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack removeItemFlags(ItemFlag... flags) {
        ItemMeta meta = this.getItemMeta();
        meta.removeItemFlags(flags);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack setEnchantments(Map<Enchantment, Integer> enchantments){
        this.addUnsafeEnchantments(enchantments);
        return this;
    }

    public CustomItemStack addEnchantmentV(Enchantment ench, int lvl){
        this.addUnsafeEnchantment(ench, lvl);
        return this;
    }

    public CustomItemStack addGlowEffect() {
        return this.addEnchantmentV(Enchantment.DURABILITY, 1).addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public CustomItemStack setUnbreakable(boolean unbreakable){
        ItemMeta meta = this.getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        this.setItemMeta(meta);
        return this;
    }

    /**
     * Set the head texture. Works on skulls only.
     *
     * @author MaygoDev
     * @param texture The hash of the texture.
     * @return Le {@link CustomItemStack} actuel
     */
    public CustomItemStack setTexture(String texture) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        propertyMap.put("textures", new Property("textures", texture));
        SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
        Class<?> c_skullMeta = skullMeta.getClass();
        try {
            Field f_profile = c_skullMeta.getDeclaredField("profile");
            f_profile.setAccessible(true);
            f_profile.set(skullMeta, profile);
            f_profile.setAccessible(false);
            this.setItemMeta(skullMeta);
            return this;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean isUnbreakable(){
        return this.getItemMeta().spigot().isUnbreakable();
    }

    public String getDisplayName() {
        return this.getItemMeta().getDisplayName();
    }

    public List<String> getLore() {
        return this.getItemMeta().getLore();
    }

    public CustomItemStack setLore(String... lore){
        return this.setLore(Arrays.asList(lore));
    }

    public CustomItemStack clone(){
        return new CustomItemStack(this);
    }


    @SuppressWarnings("deprecation")
    public boolean isCustomSimilar(ItemStack stack) {
        if (stack == null) return false;
        else if (stack == this) return true;
        else {
            return this.getTypeId() == stack.getTypeId() && this.getDurability() == stack.getDurability() && this.hasItemMeta() == stack.hasItemMeta() && (!this.hasItemMeta() || !this.getItemMeta().hasLore() || !this.getItemMeta().hasDisplayName() || (this.getItemMeta().getDisplayName().equals(stack.getItemMeta().getDisplayName()) && this.getItemMeta().getLore().equals(stack.getItemMeta().getLore())));
        }
    }

    public static int getSlot(Inventory inv, CustomItemStack customItemStack) {
        int slot = -1;
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null) continue;
            if (customItemStack.isCustomSimilar(item))
                slot = i;
        }
        return slot;
    }
}